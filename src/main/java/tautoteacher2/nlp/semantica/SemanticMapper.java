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

/**
 * Traduce secuencias léxicas de lenguaje natural a la IR de LogicScript.
 * Implementa patrones MVP para condicional, conjunción, disyunción y negación.
 */
public class SemanticMapper {
    private final BaseConocimiento baseConocimiento;

    public SemanticMapper(BaseConocimiento baseConocimiento) {
        this.baseConocimiento = baseConocimiento;
    }

    public LogicExpr mapearBloque(String textoBloque, List<TokenNatural> tokens, List<String> pasosDeAnalisis) {
        if (tokens == null || tokens.isEmpty()) {
            return null;
        }

        // si X entonces Y
        if (coincideForma(tokens, TipoTokenNatural.SI, TipoTokenNatural.LITERAL, TipoTokenNatural.ENTONCES, TipoTokenNatural.LITERAL)) {
            pasosDeAnalisis.add("SemanticMapper: patrón SI_ENTONCES detectado.");
            return new ImpExpr(atomoDesde(tokens.get(1).getLexema()), atomoDesde(tokens.get(3).getLexema()));
        }

        // Y si X
        if (coincideForma(tokens, TipoTokenNatural.LITERAL, TipoTokenNatural.SI, TipoTokenNatural.LITERAL)) {
            pasosDeAnalisis.add("SemanticMapper: patrón CONSECUENTE_SI_ANTECEDENTE detectado.");
            return new ImpExpr(atomoDesde(tokens.get(2).getLexema()), atomoDesde(tokens.get(0).getLexema()));
        }

        // en caso de que X, Y
        if (coincideForma(tokens, TipoTokenNatural.EN_CASO_DE_QUE, TipoTokenNatural.LITERAL, TipoTokenNatural.LITERAL)) {
            pasosDeAnalisis.add("SemanticMapper: patrón EN_CASO_DE_QUE detectado.");
            return new ImpExpr(atomoDesde(tokens.get(1).getLexema()), atomoDesde(tokens.get(2).getLexema()));
        }

        // X y Y
        if (coincideForma(tokens, TipoTokenNatural.LITERAL, TipoTokenNatural.Y, TipoTokenNatural.LITERAL)) {
            pasosDeAnalisis.add("SemanticMapper: patrón CONJUNCION detectado.");
            return new AndExpr(atomoDesde(tokens.get(0).getLexema()), atomoDesde(tokens.get(2).getLexema()));
        }

        // X o Y
        if (coincideForma(tokens, TipoTokenNatural.LITERAL, TipoTokenNatural.O, TipoTokenNatural.LITERAL)) {
            pasosDeAnalisis.add("SemanticMapper: patrón DISYUNCION detectado.");
            return new OrExpr(atomoDesde(tokens.get(0).getLexema()), atomoDesde(tokens.get(2).getLexema()));
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

    private static boolean coincideForma(List<TokenNatural> tokens, TipoTokenNatural... forma) {
        if (tokens.size() != forma.length) {
            return false;
        }
        for (int i = 0; i < forma.length; i++) {
            if (tokens.get(i).getTipo() != forma[i]) {
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
