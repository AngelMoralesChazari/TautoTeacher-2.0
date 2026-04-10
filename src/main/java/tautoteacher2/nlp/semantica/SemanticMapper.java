package tautoteacher2.nlp.semantica;

import java.util.List;
import tautoteacher2.logicscript.ir.AndExpr;
import tautoteacher2.logicscript.ir.AtomExpr;
import tautoteacher2.logicscript.ir.ImpExpr;
import tautoteacher2.logicscript.ir.LogicExpr;
import tautoteacher2.logicscript.ir.OrExpr;
import tautoteacher2.nlp.lexer.TipoTokenNatural;
import tautoteacher2.nlp.lexer.TokenNatural;
import tautoteacher2.nlp.lexicon.BaseConocimiento;
import tautoteacher2.nlp.lexicon.PatronSemanticoLgs;
import tautoteacher2.nlp.lexicon.TipoSalidaIrPatron;

/**
 * Traduce secuencias léxicas de lenguaje natural a la IR de LogicScript.
 * Los patrones se cargan desde {@code logicscript/core.lgs} (directiva {@code pattern});
 * si el archivo no declara ninguno, se usan los mismos cinco patrones MVP embebidos.
 */
public class SemanticMapper {
    private final BaseConocimiento baseConocimiento;
    private final List<PatronSemanticoLgs> patrones;

    public SemanticMapper(BaseConocimiento baseConocimiento) {
        this(baseConocimiento, null);
    }

    /**
     * @param patronesDesdeArchivo lista desde {@link tautoteacher2.nlp.lexicon.ContenidoLgs#patronesSemanticos()};
     *                             si es null o vacía, se aplican patrones predeterminados en código.
     */
    public SemanticMapper(BaseConocimiento baseConocimiento, List<PatronSemanticoLgs> patronesDesdeArchivo) {
        this.baseConocimiento = baseConocimiento;
        if (patronesDesdeArchivo == null || patronesDesdeArchivo.isEmpty()) {
            this.patrones = patronesPredeterminados();
        } else {
            this.patrones = List.copyOf(patronesDesdeArchivo);
        }
    }

    public LogicExpr mapearBloque(String textoBloque, List<TokenNatural> tokens, List<String> pasosDeAnalisis) {
        if (tokens == null || tokens.isEmpty()) {
            return null;
        }

        for (PatronSemanticoLgs p : patrones) {
            if (coincideForma(tokens, p.forma())) {
                pasosDeAnalisis.add("SemanticMapper: patrón " + p.nombre() + " detectado.");
                return construirDesdePatron(p, tokens);
            }
        }

        String literal = primerLiteral(tokens);
        if (literal != null) {
            pasosDeAnalisis.add("SemanticMapper: fallback a átomo simple.");
            return atomoDesde(literal);
        }

        if (textoBloque == null || textoBloque.isBlank()) {
            return null;
        }
        pasosDeAnalisis.add("SemanticMapper: fallback por texto completo.");
        return atomoDesde(textoBloque);
    }

    private LogicExpr construirDesdePatron(PatronSemanticoLgs p, List<TokenNatural> tokens) {
        String lexIzq = tokens.get(p.indiceIzq()).getLexema();
        String lexDer = tokens.get(p.indiceDer()).getLexema();
        AtomExpr a = atomoDesde(lexIzq);
        AtomExpr b = atomoDesde(lexDer);
        return switch (p.tipoIr()) {
            case IMP -> new ImpExpr(a, b);
            case AND -> new AndExpr(a, b);
            case OR -> new OrExpr(a, b);
        };
    }

    private static List<PatronSemanticoLgs> patronesPredeterminados() {
        return List.of(
                new PatronSemanticoLgs(
                        "SI_ENTONCES",
                        List.of(TipoTokenNatural.SI, TipoTokenNatural.LITERAL, TipoTokenNatural.ENTONCES, TipoTokenNatural.LITERAL),
                        TipoSalidaIrPatron.IMP,
                        1,
                        3),
                new PatronSemanticoLgs(
                        "CONSECUENTE_SI_ANTECEDENTE",
                        List.of(TipoTokenNatural.LITERAL, TipoTokenNatural.SI, TipoTokenNatural.LITERAL),
                        TipoSalidaIrPatron.IMP,
                        2,
                        0),
                new PatronSemanticoLgs(
                        "EN_CASO_DE_QUE",
                        List.of(TipoTokenNatural.EN_CASO_DE_QUE, TipoTokenNatural.LITERAL, TipoTokenNatural.LITERAL),
                        TipoSalidaIrPatron.IMP,
                        1,
                        2),
                new PatronSemanticoLgs(
                        "CONJUNCION",
                        List.of(TipoTokenNatural.LITERAL, TipoTokenNatural.Y, TipoTokenNatural.LITERAL),
                        TipoSalidaIrPatron.AND,
                        0,
                        2),
                new PatronSemanticoLgs(
                        "DISYUNCION",
                        List.of(TipoTokenNatural.LITERAL, TipoTokenNatural.O, TipoTokenNatural.LITERAL),
                        TipoSalidaIrPatron.OR,
                        0,
                        2));
    }

    private AtomExpr atomoDesde(String fragmentoOriginal) {
        String limpio = fragmentoOriginal == null ? "" : fragmentoOriginal.trim();
        boolean negada = false;
        if (limpio.startsWith("no ")) {
            negada = true;
            limpio = limpio.substring(3).trim();
        }
        String canonico = baseConocimiento.canonicalizarFragmento(limpio);
        return new AtomExpr(canonico.isBlank() ? limpio : canonico, negada);
    }

    private static boolean coincideForma(List<TokenNatural> tokens, List<TipoTokenNatural> forma) {
        if (tokens.size() != forma.size()) {
            return false;
        }
        for (int i = 0; i < forma.size(); i++) {
            if (tokens.get(i).getTipo() != forma.get(i)) {
                return false;
            }
        }
        return true;
    }

    private static String primerLiteral(List<TokenNatural> tokens) {
        for (TokenNatural t : tokens) {
            if (t.getTipo() == TipoTokenNatural.LITERAL && !t.getLexema().isBlank()) {
                return t.getLexema();
            }
        }
        return null;
    }
}
