package tautoteacher2.core.logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

        sb.append("\n\n");
        sb.append("═══════════════════════════════════\n");
        sb.append("EXPLICACIÓN EDUCATIVA\n");
        sb.append("═══════════════════════════════════\n\n");

        if (!simboloALema.isEmpty()) {
            sb.append("Lectura en lenguaje natural\n");
            sb.append("────────────────────────────\n\n");
            sb.append("  ").append(aplicarLeyendas(formula, simboloALema)).append("\n\n");
        }

        String formulaParaAnalisis = quitarParentesisExternos(formula);
        String demostracion = MotorLogico.generarExplicacionEducativa(formulaParaAnalisis, esTautologia);
        sb.append(aplicarLeyendas(demostracion, simboloALema));

        String tabla = MotorLogico.generarTablaVerdad(formula);
        if (tabla != null && !tabla.isBlank()) {
            sb.append("\n\n");
            sb.append(aplicarLeyendas(tabla, simboloALema));
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
