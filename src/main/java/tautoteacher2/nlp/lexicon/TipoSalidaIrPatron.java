package tautoteacher2.nlp.lexicon;

/**
 * Conectiva de la IR que produce un patrón semántico cargado desde {@code .lgs}.
 */
public enum TipoSalidaIrPatron {
    /** Implicación ({@code ImpExpr}): antecedente → consecuente. */
    IMP,
    /** Conjunción ({@code AndExpr}). */
    AND,
    /** Disyunción ({@code OrExpr}). */
    OR,
    /** Equivalencia ({@code EquivExpr}). */
    EQUIV
}
