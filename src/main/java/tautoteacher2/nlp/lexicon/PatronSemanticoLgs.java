package tautoteacher2.nlp.lexicon;

import java.util.List;
import tautoteacher2.nlp.lexer.TipoTokenNatural;

/**
 * Patrón declarativo: secuencia de tipos de token y qué slots {@link TipoTokenNatural#LITERAL}
 * alimentan cada lado de la conectiva IR.
 *
 * @param nombre        identificador para trazas (p. ej. {@code SI_ENTONCES})
 * @param forma         longitud fija; debe coincidir exactamente con la tokenización del bloque
 * @param tipoIr        conectiva a instanciar
 * @param indiceIzq     índice 0-based del literal izquierdo / antecedente según {@link #tipoIr}
 * @param indiceDer     índice 0-based del literal derecho / consecuente
 */
public record PatronSemanticoLgs(
        String nombre,
        List<TipoTokenNatural> forma,
        TipoSalidaIrPatron tipoIr,
        int indiceIzq,
        int indiceDer) {

    public PatronSemanticoLgs {
        forma = List.copyOf(forma);
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("nombre vacío");
        }
        if (indiceIzq < 0 || indiceIzq >= forma.size() || indiceDer < 0 || indiceDer >= forma.size()) {
            throw new IllegalArgumentException("índices fuera de rango para forma: " + nombre);
        }
        if (forma.get(indiceIzq) != TipoTokenNatural.LITERAL || forma.get(indiceDer) != TipoTokenNatural.LITERAL) {
            throw new IllegalArgumentException("slots left/right deben ser literal en: " + nombre);
        }
    }
}
