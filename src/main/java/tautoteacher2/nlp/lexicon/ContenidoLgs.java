package tautoteacher2.nlp.lexicon;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Resultado de parsear un archivo {@code .lgs}: lemas léxicos y patrones semánticos, en orden de aparición.
 */
public final class ContenidoLgs {

    private final Map<String, String> lemas;
    private final List<PatronSemanticoLgs> patronesSemanticos;

    public ContenidoLgs(Map<String, String> lemas, List<PatronSemanticoLgs> patronesSemanticos) {
        this.lemas = lemas == null || lemas.isEmpty()
                ? Map.of()
                : Collections.unmodifiableMap(Map.copyOf(lemas));
        this.patronesSemanticos = patronesSemanticos == null || patronesSemanticos.isEmpty()
                ? List.of()
                : List.copyOf(patronesSemanticos);
    }

    public Map<String, String> lemas() {
        return lemas;
    }

    public List<PatronSemanticoLgs> patronesSemanticos() {
        return patronesSemanticos;
    }

    public static ContenidoLgs vacio() {
        return new ContenidoLgs(Map.of(), List.of());
    }
}
