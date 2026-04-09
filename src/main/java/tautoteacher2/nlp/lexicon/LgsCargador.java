package tautoteacher2.nlp.lexicon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Carga archivos {@code .lgs} con sintaxis mínima (v0.1): lemas y metadatos ignorables.
 * Formato descrito en {@code docs/LogicScript/FormatoLGS.md}.
 */
public final class LgsCargador {

    private LgsCargador() {
    }

    /**
     * Carga lemas desde el classpath, p. ej. {@code logicscript/core.lgs}.
     * Si el recurso no existe o hay error de lectura, devuelve mapa vacío.
     */
    public static Map<String, String> cargarLemasDesdeClasspath(String rutaRecurso) {
        Objects.requireNonNull(rutaRecurso, "rutaRecurso");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = LgsCargador.class.getClassLoader();
        }
        try (InputStream in = cl.getResourceAsStream(rutaRecurso)) {
            if (in == null) {
                return Collections.emptyMap();
            }
            return cargarLemas(in);
        } catch (IOException e) {
            return Collections.emptyMap();
        }
    }

    public static Map<String, String> cargarLemas(InputStream entrada) throws IOException {
        Map<String, String> lemas = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8))) {
            String linea;
            int num = 0;
            while ((linea = br.readLine()) != null) {
                num++;
                String t = linea.trim();
                if (t.isEmpty() || t.startsWith("#")) {
                    continue;
                }
                if (t.toLowerCase().startsWith("version")) {
                    continue;
                }
                if (t.toLowerCase().startsWith("lemma ")) {
                    String resto = t.substring(6).trim();
                    int flecha = resto.indexOf("->");
                    if (flecha < 0) {
                        throw new IOException("Línea " + num + ": falta '->' en lemma: " + linea);
                    }
                    String forma = resto.substring(0, flecha).trim().toLowerCase();
                    String lema = resto.substring(flecha + 2).trim().toLowerCase();
                    if (forma.isEmpty() || lema.isEmpty()) {
                        throw new IOException("Línea " + num + ": forma o lema vacío: " + linea);
                    }
                    lemas.put(forma, lema);
                    continue;
                }
                throw new IOException("Línea " + num + ": directiva no reconocida: " + linea);
            }
        }
        return Collections.unmodifiableMap(lemas);
    }
}
