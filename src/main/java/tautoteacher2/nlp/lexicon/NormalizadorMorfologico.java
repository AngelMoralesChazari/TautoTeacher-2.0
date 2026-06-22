package tautoteacher2.nlp.lexicon;

import java.util.Locale;
import java.util.Set;

/**
 * Normalización morfológica heurística del español (Fase A del léxico LogicScript).
 * <p>
 * Complementa los {@code lemma} explícitos en {@code .lgs}: si no hay entrada manual,
 * intenta obtener un infinitivo aproximado; si no aplica, devuelve la palabra tal cual.
 * <p>
 * Documentación: {@code docs/LogicScript/NormalizadorMorfologico.md}
 */
public final class NormalizadorMorfologico {

    public NormalizadorMorfologico() {
    }

    /**
     * Sustantivos, locuciones y formas que no deben pasar por reglas verbales
     * (evita {@code gorra → gorrar}, {@code calor → calorar}, etc.).
     */
    private static final Set<String> NO_VERBO = Set.of(
            "gorra",
            "sombrero",
            "paraguas",
            "calor",
            "frio",
            "sol",
            "cielo",
            "nube",
            "nubes",
            "lluvia",
            "examen",
            "clase");

    /**
     * @param palabra token en minúsculas sin acentos (salida de {@link tautoteacher2.nlp.normalizacion.NormalizadorTexto})
     * @return infinitivo aproximado o la palabra original si no hay regla aplicable
     */
    public String normalizar(String palabra) {
        if (palabra == null || palabra.isBlank()) {
            return "";
        }
        String w = palabra.trim().toLowerCase(Locale.ROOT);
        if (w.length() < 3) {
            return w;
        }
        if (esInfinitivo(w)) {
            return w;
        }
        if (NO_VERBO.contains(w)) {
            return w;
        }

        String porSufijo = aplicarReglasSufijo(w);
        if (porSufijo != null) {
            return porSufijo;
        }
        return w;
    }

    private static boolean esInfinitivo(String w) {
        if ("ir".equals(w)) {
            return true;
        }
        return w.length() >= 4 && (w.endsWith("ar") || w.endsWith("er") || w.endsWith("ir"));
    }

    /**
     * Reglas de sufijo ordenadas de más largas a más cortas (mayor especificidad primero).
     * Cada entrada: {@code {sufijo_flexionado, vocal_infinitivo "a"|"e"|"i"} }.
     */
    private static String aplicarReglasSufijo(String w) {
        String[][] reglas = {
                {"iaria", "a"}, {"aria", "a"}, {"iaba", "a"}, {"abas", "a"}, {"aban", "a"},
                {"amos", "a"}, {"ais", "a"}, {"aron", "a"}, {"aran", "a"}, {"are", "a"},
                {"aba", "a"}, {"ado", "a"}, {"ada", "a"},
                {"emos", "e"}, {"eis", "e"},
                {"imos", "i"},
                {"an", "a"}, {"as", "a"},
                {"a", "a"},
                {"o", "?"}
        };

        for (String[] regla : reglas) {
            String sufijo = regla[0];
            if (!w.endsWith(sufijo) || w.length() <= sufijo.length() + 1) {
                continue;
            }
            String raiz = w.substring(0, w.length() - sufijo.length());
            if (raiz.length() < 2) {
                continue;
            }
            String vocal = regla[1];
            if ("?".equals(vocal)) {
                String inf = infinitivoDesdePrimeraPersona(raiz);
                if (inf != null) {
                    return inf;
                }
            } else {
                return raiz + vocal + "r";
            }
        }
        return null;
    }

    /**
     * Presente 1.ª persona {@code -o}: elige infinitivo según la raíz.
     * - Raíz acaba en vocal → prioriza {@code -ar} (estudio → estudiar).
     * - Raíz acaba en consonante → prueba {@code -ar}, luego {@code -ir} (llego → llegar; duermo → dormir).
     */
    private static String infinitivoDesdePrimeraPersona(String raiz) {
        if (raiz.isEmpty()) {
            return null;
        }
        char ultima = raiz.charAt(raiz.length() - 1);
        if (esVocal(ultima)) {
            return raiz + "ar";
        }
        if (raiz.endsWith("rm") || raiz.endsWith("mm") || raiz.endsWith("rc") || raiz.endsWith("rt")) {
            return raiz + "ir";
        }
        String candidatoAr = raiz + "ar";
        if (candidatoAr.length() >= 4) {
            return candidatoAr;
        }
        return raiz + "ir";
    }

    private static boolean esVocal(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }
}
