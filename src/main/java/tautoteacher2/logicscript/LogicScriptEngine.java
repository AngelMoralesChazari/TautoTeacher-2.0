package tautoteacher2.logicscript;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicScriptEngine {
    private static final Pattern PATRON_CONDICIONAL =
            Pattern.compile("^si\\s+(.+?),?\\s*entonces\\s+(.+)$", Pattern.CASE_INSENSITIVE);

    private static final Pattern PATRON_CONDICIONAL_INVERSO =
            Pattern.compile("^(.+?)\\s+si\\s+(.+)$", Pattern.CASE_INSENSITIVE);

    private static final char SIMBOLOS[] = "pqrstuvwxyzabcdefghijklmno".toCharArray();

    public LogicScriptResult traducir(String textoOriginal) {
        List<String> pasosDeAnalisis = new ArrayList<>();
        Map<String, String> proposiciones = new LinkedHashMap<>();

        String texto = normalizar(textoOriginal);
        if (texto.isEmpty()) {
            return LogicScriptResult.error("No se ingresó texto en lenguaje natural.", pasosDeAnalisis);
        }
        pasosDeAnalisis.add("Entrada normalizada: " + texto);

        String partes[] = texto.split("\\s*,\\s*en caso de que\\s+", 2);
        String formulaPrincipal = traducirBloque(partes[0], proposiciones, pasosDeAnalisis);
        if (formulaPrincipal == null) {
            return LogicScriptResult.error(
                    "No pude mapear el enunciado a una forma lógica con las reglas actuales.", pasosDeAnalisis);
        }

        String formulaFinal = formulaPrincipal;
        if (partes.length == 2) {
            String formulaSecundaria = traducirBloque("en caso de que " + partes[1], proposiciones, pasosDeAnalisis);
            if (formulaSecundaria != null) {
                formulaFinal = "(" + formulaPrincipal + ") ∧ (" + formulaSecundaria + ")";
                pasosDeAnalisis.add("Composición por conjunción de dos bloques.");
            }
        }

        return LogicScriptResult.exito(
                formulaFinal,
                "Traducción LogicScript completada.",
                pasosDeAnalisis,
                proposiciones
        );
    }

    private String traducirBloque(String bloque, Map<String, String> props, List<String> trazas) {
        String limpio = bloque.trim();
        if (limpio.isEmpty()) {
            return null;
        }

        if (limpio.toLowerCase().startsWith("en caso de que ")) {
            String resto = limpio.substring("en caso de que ".length()).trim();
            String[] dosPartes = resto.split("\\s*,\\s*", 2);

            if (dosPartes.length == 2) {
                String antecedente = dosPartes[0];
                String consecuente = dosPartes[1];
                String izquierda = proposicion(antecedente, props, trazas);
                String derecha = proposicion(consecuente, props, trazas);
                trazas.add("Patrón detectado: EN_CASO_DE_QUE.");
                return izquierda + " → " + derecha;
            }
        }

        Matcher m = PATRON_CONDICIONAL.matcher(limpio);
        if (m.matches()) {
            String antecedente = m.group(1).trim();
            String consecuente = m.group(2).trim();
            String izquierda = proposicion(antecedente, props, trazas);
            String derecha = proposicion(consecuente, props, trazas);
            trazas.add("Patrón detectado: SI_ENTONCES.");
            return izquierda + " → " + derecha;
        }

        Matcher inv = PATRON_CONDICIONAL_INVERSO.matcher(limpio);
        if (inv.matches() && limpio.toLowerCase().contains(" si ")) {
            String consecuente = inv.group(1).trim();
            String antecedente = inv.group(2).trim();
            String izquierda = proposicion(antecedente, props, trazas);
            String derecha = proposicion(consecuente, props, trazas);
            trazas.add("Patrón detectado: CONSECUENTE_SI_ANTECEDENTE.");
            return izquierda + " → " + derecha;
        }

        if (limpio.contains(" y ")) {
            String[] segs = limpio.split("\\s+y\\s+", 2);
            String izquierda = proposicion(segs[0], props, trazas);
            String derecha = proposicion(segs[1], props, trazas);
            trazas.add("Patrón detectado: CONJUNCION.");
            return izquierda + " ∧ " + derecha;
        }

        return proposicion(limpio, props, trazas);
    }

    private String proposicion(String fragmento, Map<String, String> props, List<String> trazas) {
        String limpio = fragmento.trim();
        boolean negado = false;

        if (limpio.toLowerCase().startsWith("no ")) {
            negado = true;
            limpio = limpio.substring(3).trim();
        }

        String simbolo = props.get(limpio);
        if (simbolo == null) {
            simbolo = siguienteSimbolo(props.size());
            props.put(limpio, simbolo);
            trazas.add("Nueva proposición: " + simbolo + " = \"" + limpio + "\"");
        }
        return negado ? "¬" + simbolo : simbolo;
    }

    private String siguienteSimbolo(int indice) {
        if (indice < SIMBOLOS.length) {
            return String.valueOf(SIMBOLOS[indice]);
        }
        return "p" + indice;
    }

    private String normalizar(String textoOriginal) {
        if (textoOriginal == null) {
            return "";
        }
        String t = textoOriginal.trim().toLowerCase();
        if (t.isEmpty()) {
            return "";
        }

        t = Normalizer.normalize(t, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        t = t.replaceAll("[;:]+", ",");
        t = t.replaceAll("\\s+", " ");
        return t;
    }
}
