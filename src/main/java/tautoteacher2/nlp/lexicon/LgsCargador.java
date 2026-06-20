package tautoteacher2.nlp.lexicon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import tautoteacher2.nlp.lexer.TipoTokenNatural;

/**
 * Carga archivos {@code .lgs} (v0.2): lemas, patrones semánticos y metadatos ignorables.
 * Formato: {@code docs/LogicScript/FormatoLGS.md} y {@code docs/LogicScript/Pattern.md}.
 */
public final class LgsCargador {

    private LgsCargador() {
    }

    /**
     * Carga lemas y patrones desde el classpath con diagnóstico explícito.
     * Preferir este método frente a {@link #cargarDesdeClasspath(String)} en el motor LogicScript.
     */
    public static ResultadoCargaLgs cargarConDiagnostico(String rutaRecurso) {
        Objects.requireNonNull(rutaRecurso, "rutaRecurso");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = LgsCargador.class.getClassLoader();
        }
        try (InputStream in = cl.getResourceAsStream(rutaRecurso)) {
            if (in == null) {
                return new ResultadoCargaLgs(
                        ContenidoLgs.vacio(),
                        EstadoCargaLgs.RECURSO_NO_ENCONTRADO,
                        "No se encontró el recurso en el classpath. Copie src/main/resources a out al compilar.",
                        rutaRecurso);
            }
            return cargarConDiagnostico(in, rutaRecurso);
        } catch (IOException e) {
            return new ResultadoCargaLgs(
                    ContenidoLgs.vacio(),
                    EstadoCargaLgs.ERROR_LECTURA,
                    e.getMessage(),
                    rutaRecurso);
        }
    }

    /**
     * Parsea un stream ya abierto (útil en pruebas y herramientas).
     */
    public static ResultadoCargaLgs cargarConDiagnostico(InputStream entrada, String rutaEtiqueta) {
        Objects.requireNonNull(entrada, "entrada");
        try {
            ContenidoLgs contenido = cargar(entrada);
            return new ResultadoCargaLgs(contenido, EstadoCargaLgs.EXITO, "", rutaEtiqueta);
        } catch (IOException e) {
            EstadoCargaLgs estado = mensajePareceSintaxis(e)
                    ? EstadoCargaLgs.ERROR_SINTAXIS
                    : EstadoCargaLgs.ERROR_LECTURA;
            return new ResultadoCargaLgs(ContenidoLgs.vacio(), estado, e.getMessage(), rutaEtiqueta);
        }
    }

    /**
     * Carga lemas y patrones desde el classpath, p. ej. {@code logicscript/core.lgs}.
     * Si el recurso no existe o hay error, devuelve {@link ContenidoLgs#vacio()} sin mensaje
     * (uso legacy; el motor usa {@link #cargarConDiagnostico(String)}).
     */
    public static ContenidoLgs cargarDesdeClasspath(String rutaRecurso) {
        ResultadoCargaLgs r = cargarConDiagnostico(rutaRecurso);
        if (r.bloqueaTraduccion() || r.estado() == EstadoCargaLgs.RECURSO_NO_ENCONTRADO) {
            return ContenidoLgs.vacio();
        }
        return r.contenido();
    }

    private static boolean mensajePareceSintaxis(IOException e) {
        String m = e.getMessage();
        return m != null && m.contains("Línea ");
    }

    /**
     * Compatibilidad: solo el mapa de lemas del recurso indicado.
     */
    public static Map<String, String> cargarLemasDesdeClasspath(String rutaRecurso) {
        return cargarDesdeClasspath(rutaRecurso).lemas();
    }

    public static ContenidoLgs cargar(InputStream entrada) throws IOException {
        Map<String, String> lemas = new LinkedHashMap<>();
        List<PatronSemanticoLgs> patrones = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8))) {
            String linea;
            int num = 0;
            while ((linea = br.readLine()) != null) {
                num++;
                String t = linea.trim();
                if (t.isEmpty() || t.startsWith("#")) {
                    continue;
                }
                String tl = t.toLowerCase(Locale.ROOT);
                if (tl.startsWith("version")) {
                    continue;
                }
                if (tl.startsWith("lemma ")) {
                    parsearLemma(num, t, lemas);
                    continue;
                }
                if (tl.startsWith("pattern ")) {
                    patrones.add(parsearPattern(num, t));
                    continue;
                }
                throw new IOException("Línea " + num + ": directiva no reconocida: " + linea);
            }
        }
        return new ContenidoLgs(lemas, patrones);
    }

    public static Map<String, String> cargarLemas(InputStream entrada) throws IOException {
        return cargar(entrada).lemas();
    }

    private static void parsearLemma(int numLinea, String linea, Map<String, String> lemas) throws IOException {
        String resto = linea.substring(6).trim();
        int flecha = resto.indexOf("->");
        if (flecha < 0) {
            throw new IOException("Línea " + numLinea + ": falta '->' en lemma: " + linea);
        }
        String forma = resto.substring(0, flecha).trim().toLowerCase(Locale.ROOT);
        String lema = resto.substring(flecha + 2).trim().toLowerCase(Locale.ROOT);
        if (forma.isEmpty() || lema.isEmpty()) {
            throw new IOException("Línea " + numLinea + ": forma o lema vacío: " + linea);
        }
        lemas.put(forma, lema);
    }

    private static PatronSemanticoLgs parsearPattern(int numLinea, String linea) throws IOException {
        int flechaDoble = linea.indexOf("=>");
        if (flechaDoble < 0) {
            throw new IOException("Línea " + numLinea + ": falta '=>' en pattern: " + linea);
        }
        String izquierda = linea.substring(0, flechaDoble).trim();
        String derecha = linea.substring(flechaDoble + 2).trim();
        if (!izquierda.toLowerCase(Locale.ROOT).startsWith("pattern ")) {
            throw new IOException("Línea " + numLinea + ": se esperaba 'pattern': " + linea);
        }
        String tokensPatron = izquierda.substring(8).trim();
        String[] partesForma = tokensPatron.split("\\s+");
        if (partesForma.length < 2) {
            throw new IOException("Línea " + numLinea + ": pattern necesita nombre y al menos un token: " + linea);
        }
        String nombre = partesForma[0];
        List<TipoTokenNatural> forma = new ArrayList<>();
        for (int i = 1; i < partesForma.length; i++) {
            forma.add(parsearTipoToken(numLinea, partesForma[i]));
        }
        String[] partesSalida = derecha.split("\\s+");
        if (partesSalida.length < 3) {
            throw new IOException("Línea " + numLinea + ": tras '=>' se espera p. ej. 'imp left=0 right=2': " + linea);
        }
        TipoSalidaIrPatron tipoIr = parsearTipoIr(numLinea, partesSalida[0]);
        int left = -1;
        int right = -1;
        int mid = -1;
        for (int i = 1; i < partesSalida.length; i++) {
            String p = partesSalida[i].toLowerCase(Locale.ROOT);
            if (p.startsWith("left=")) {
                left = parsearIndice(numLinea, p.substring(5));
            } else if (p.startsWith("right=")) {
                right = parsearIndice(numLinea, p.substring(6));
            } else if (p.startsWith("mid=")) {
                mid = parsearIndice(numLinea, p.substring(4));
            }
        }
        if (left < 0 || right < 0) {
            throw new IOException("Línea " + numLinea + ": faltan left= e right= en pattern: " + linea);
        }
        if ((tipoIr == TipoSalidaIrPatron.IMP_AND || tipoIr == TipoSalidaIrPatron.IMP_OR) && mid < 0) {
            throw new IOException("Línea " + numLinea + ": imp_and/imp_or requieren mid=: " + linea);
        }
        try {
            return new PatronSemanticoLgs(nombre, forma, tipoIr, left, right, mid);
        } catch (IllegalArgumentException ex) {
            throw new IOException("Línea " + numLinea + ": " + ex.getMessage() + " — " + linea, ex);
        }
    }

    private static int parsearIndice(int numLinea, String s) throws IOException {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IOException("Línea " + numLinea + ": índice inválido: " + s);
        }
    }

    private static TipoSalidaIrPatron parsearTipoIr(int numLinea, String token) throws IOException {
        String t = token.trim().toLowerCase(Locale.ROOT);
        return switch (t) {
            case "imp", "implica", "implies" -> TipoSalidaIrPatron.IMP;
            case "and" -> TipoSalidaIrPatron.AND;
            case "or" -> TipoSalidaIrPatron.OR;
            case "equiv", "equivalente", "iff", "bicondicional" -> TipoSalidaIrPatron.EQUIV;
            case "imp_and" -> TipoSalidaIrPatron.IMP_AND;
            case "imp_or" -> TipoSalidaIrPatron.IMP_OR;
            default -> throw new IOException("Línea " + numLinea + ": tipo IR desconocido (use imp, and, or, equiv, imp_and, imp_or): " + token);
        };
    }

    private static TipoTokenNatural parsearTipoToken(int numLinea, String token) throws IOException {
        String t = token.trim().toLowerCase(Locale.ROOT).replace('-', '_');
        return switch (t) {
            case "si" -> TipoTokenNatural.SI;
            case "entonces" -> TipoTokenNatural.ENTONCES;
            case "en_caso_de_que" -> TipoTokenNatural.EN_CASO_DE_QUE;
            case "si_y_solo_si" -> TipoTokenNatural.SI_Y_SOLO_SI;
            case "siempre_que" -> TipoTokenNatural.SIEMPRE_QUE;
            case "y" -> TipoTokenNatural.Y;
            case "o" -> TipoTokenNatural.O;
            case "literal" -> TipoTokenNatural.LITERAL;
            default -> throw new IOException("Línea " + numLinea + ": token de forma desconocido: " + token);
        };
    }
}
