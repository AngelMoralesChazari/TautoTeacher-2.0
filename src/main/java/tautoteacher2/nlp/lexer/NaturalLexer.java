package tautoteacher2.nlp.lexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Lexer incremental sobre cadena normalizada: reconoce conectores de uso frecuente
 * y agrupa el resto como literales. Orden de palabras clave: más largas primero.
 */
public final class NaturalLexer {

    private static final String PALABRAS_CLAVE[] = {
            "en caso de que ",
            "entonces ",
            "si ",
            " y ",
            " o ",
    };

    private static final TipoTokenNatural TIPOS[] = {
            TipoTokenNatural.EN_CASO_DE_QUE,
            TipoTokenNatural.ENTONCES,
            TipoTokenNatural.SI,
            TipoTokenNatural.Y,
            TipoTokenNatural.O,
    };

    public List<TokenNatural> tokenizar(String normalizado) {
        List<TokenNatural> salida = new ArrayList<>();
        if (normalizado == null || normalizado.isEmpty()) {
            return salida;
        }
        String s = normalizado;
        int i = 0;
        int n = s.length();
        while (i < n) {
            // Palabra clave antes de consumir espacios: patrones como " y " empiezan en espacio;
            // si saltamos espacios primero, nunca coinciden tras un literal (p. ej. "a y b").
            int[] kw = encontrarPalabraClave(s, i);
            if (kw != null) {
                int fin = kw[0];
                int indiceTipo = kw[1];
                salida.add(new TokenNatural(TIPOS[indiceTipo], PALABRAS_CLAVE[indiceTipo].trim()));
                i = fin;
                continue;
            }
            char c = s.charAt(i);
            if (c == ' ' || c == ',') {
                i++;
                continue;
            }
            int j = finLiteral(s, i);
            String lit = s.substring(i, j).trim();
            if (!lit.isEmpty()) {
                salida.add(new TokenNatural(TipoTokenNatural.LITERAL, lit));
            }
            i = j;
        }
        return salida;
    }

    private static int[] encontrarPalabraClave(String s, int i) {
        for (int k = 0; k < PALABRAS_CLAVE.length; k++) {
            String p = PALABRAS_CLAVE[k];
            if (s.regionMatches(i, p, 0, p.length())) {
                return new int[] { i + p.length(), k };
            }
        }
        return null;
    }

    /** Avanza hasta el inicio de la siguiente palabra clave o el final. */
    private static int finLiteral(String s, int i) {
        int n = s.length();
        int j = i;
        while (j < n) {
            if (s.charAt(j) == ',') {
                break;
            }
            if (encontrarPalabraClave(s, j) != null) {
                break;
            }
            j++;
        }
        return j;
    }
}
