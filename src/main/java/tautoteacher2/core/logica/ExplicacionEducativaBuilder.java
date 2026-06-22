package tautoteacher2.core.logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import tautoteacher2.core.logica.TablaVerdad.Fila;

/**
 * Arma el texto del panel «Explicación»: sección técnica + demostración educativa
 * con símbolos sustituidos por lemas en español cuando hay mapa LogicScript.
 */
public final class ExplicacionEducativaBuilder {

    private ExplicacionEducativaBuilder() {
    }

    public static String construir(String seccionTecnica, String formula, String tipo, Map<String, String> proposiciones) {
        StringBuilder sb = new StringBuilder();
        if (seccionTecnica != null && !seccionTecnica.isBlank()) {
            sb.append(seccionTecnica.trim());
        }

        Map<String, String> simboloALema = invertirMapaProposiciones(proposiciones);
        boolean esTautologia = "TAUTOLOGÍA".equals(tipo);
        String tipoNorm = tipo != null ? tipo.toUpperCase() : "";

        sb.append("\n\n");
        sb.append("═══════════════════════════════════\n");
        sb.append("EXPLICACIÓN EDUCATIVA\n");
        sb.append("═══════════════════════════════════\n\n");

        if (!simboloALema.isEmpty()) {
            sb.append("Lectura en lenguaje natural\n");
            sb.append("────────────────────────────\n\n");
            sb.append("  ").append(aplicarLeyendas(formula, simboloALema)).append("\n\n");
        }

        if ("CONTINGENCIA".equals(tipoNorm) || "CONTRADICCIÓN".equals(tipoNorm)) {
            sb.append(seccionInterpretaciones(formula, tipoNorm, simboloALema));
            sb.append("\n");
        }

        if (esTautologia || "CONTINGENCIA".equals(tipoNorm)) {
            String formulaParaAnalisis = quitarParentesisExternos(formula);
            String demostracion = MotorLogico.generarExplicacionEducativa(formulaParaAnalisis, esTautologia);
            sb.append(aplicarLeyendas(demostracion, simboloALema));
        } else if ("CONTRADICCIÓN".equals(tipoNorm)) {
            sb.append("Demostración\n");
            sb.append("────────────\n\n");
            sb.append("  La fórmula es falsa en todas las filas de la tabla de verdad.\n");
            sb.append("  No existe interpretación que la haga verdadera.\n");
        }

        return sb.toString();
    }

    private static String seccionInterpretaciones(String formula, String tipo, Map<String, String> simboloALema) {
        TablaVerdad.Resultado tabla = TablaVerdad.construir(formula);
        if (!tabla.tieneDatos()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Por qué es ").append(tipo).append("\n");
        sb.append("────────────────────\n\n");
        sb.append("  Consulte «Visualización Clara» (pestaña Tabla o Árbol).\n\n");

        if ("CONTRADICCIÓN".equals(tipo)) {
            Fila ejemplo = tabla.filas().get(0);
            sb.append("  Ejemplo (fila 1): ");
            sb.append(describirFila(tabla.variables(), ejemplo, simboloALema));
            sb.append(" → la fórmula es F en todas las interpretaciones.\n");
            return sb.toString();
        }

        Fila filaVerdadera = null;
        Fila filaFalsa = null;
        for (Fila f : tabla.filas()) {
            if (f.resultadoFormula() && filaVerdadera == null) {
                filaVerdadera = f;
            }
            if (!f.resultadoFormula() && filaFalsa == null) {
                filaFalsa = f;
            }
            if (filaVerdadera != null && filaFalsa != null) {
                break;
            }
        }

        if (filaVerdadera != null) {
            sb.append("  Caso verdadero: ");
            sb.append(describirFila(tabla.variables(), filaVerdadera, simboloALema));
            sb.append(" → la fórmula es V.\n");
        }
        if (filaFalsa != null) {
            sb.append("  Caso falso:      ");
            sb.append(describirFila(tabla.variables(), filaFalsa, simboloALema));
            sb.append(" → la fórmula es F.\n");
        }
        sb.append("\n  Al existir interpretaciones donde es V y otras donde es F, la fórmula es contingente.\n");
        return sb.toString();
    }

    private static String describirFila(List<String> variables, Fila fila, Map<String, String> simboloALema) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < variables.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            String var = variables.get(i);
            String leyenda = leyendaLegible(simboloALema.getOrDefault(var, var));
            sb.append("«").append(leyenda).append("»=");
            sb.append(fila.valoresVariables()[i] ? "V" : "F");
        }
        return sb.toString();
    }

    static Map<String, String> invertirMapaProposiciones(Map<String, String> proposiciones) {
        if (proposiciones == null || proposiciones.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> simboloALema = new LinkedHashMap<>();
        for (Map.Entry<String, String> e : proposiciones.entrySet()) {
            simboloALema.put(e.getValue(), e.getKey());
        }
        return simboloALema;
    }

    static String aplicarLeyendas(String texto, Map<String, String> simboloALema) {
        if (texto == null || texto.isBlank() || simboloALema.isEmpty()) {
            return texto;
        }
        List<String> simbolos = new ArrayList<>(simboloALema.keySet());
        simbolos.sort((a, b) -> Integer.compare(b.length(), a.length()));
        String out = texto;
        for (String simbolo : simbolos) {
            String leyenda = leyendaLegible(simboloALema.get(simbolo));
            out = out.replaceAll("¬" + Pattern.quote(simbolo), "no " + leyenda);
            out = out.replaceAll("(?<![a-zA-Z])" + Pattern.quote(simbolo) + "(?![a-zA-Z])", "«" + leyenda + "»");
        }
        return out;
    }

    private static String leyendaLegible(String lema) {
        if (lema == null || lema.isBlank()) {
            return lema;
        }
        return lema.replace('_', ' ');
    }

    static String quitarParentesisExternos(String formula) {
        if (formula == null) {
            return "";
        }
        String s = formula.trim();
        while (s.startsWith("(") && s.endsWith(")")) {
            int nivel = 0;
            boolean envuelveTodo = true;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '(') {
                    nivel++;
                } else if (c == ')') {
                    nivel--;
                }
                if (nivel == 0 && i < s.length() - 1) {
                    envuelveTodo = false;
                    break;
                }
            }
            if (!envuelveTodo) {
                break;
            }
            s = s.substring(1, s.length() - 1).trim();
        }
        return s;
    }
}
