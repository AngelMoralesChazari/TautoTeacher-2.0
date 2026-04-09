package tautoteacher2.logicscript;

import java.util.ArrayList;
import java.util.List;
import tautoteacher2.logicscript.ir.AndExpr;
import tautoteacher2.logicscript.ir.AtomExpr;
import tautoteacher2.logicscript.ir.EmitidorFormula;
import tautoteacher2.logicscript.ir.LogicExpr;
import tautoteacher2.nlp.lexer.NaturalLexer;
import tautoteacher2.nlp.lexer.TokenNatural;
import tautoteacher2.nlp.lexicon.BaseConocimiento;
import tautoteacher2.nlp.normalizacion.NormalizadorTexto;
import tautoteacher2.nlp.semantica.SemanticMapper;

/**
 * Orquesta normalización → lexemas (diagnóstico) → patrones → IR → emisión de fórmula.
 * Los patrones de alto nivel siguen en regex hasta migrar a parser sobre tokens.
 */
public class LogicScriptEngine {
    private final NormalizadorTexto normalizador = new NormalizadorTexto();
    private final NaturalLexer lexer = new NaturalLexer();
    private final BaseConocimiento baseConocimiento = new BaseConocimiento();
    private final SemanticMapper semanticMapper = new SemanticMapper(baseConocimiento);

    public LogicScriptResult traducir(String textoOriginal) {
        List<String> pasosDeAnalisis = new ArrayList<>();
        RegistroProposiciones registro = new RegistroProposiciones();

        String texto = normalizador.normalizar(textoOriginal);
        if (texto.isEmpty()) {
            return LogicScriptResult.error("No se ingresó texto en lenguaje natural.", pasosDeAnalisis);
        }
        pasosDeAnalisis.add("Entrada normalizada: " + texto);
        List<TokenNatural> lexemas = lexer.tokenizar(texto);
        pasosDeAnalisis.add("Lexemas LN: " + lexemas);

        String[] partes = texto.split("\\s*,\\s*en caso de que\\s+", 2);
        LogicExpr principal = traducirBloque(partes[0], pasosDeAnalisis);
        if (principal == null) {
            return LogicScriptResult.error(
                    "No pude mapear el enunciado a una forma lógica con las reglas actuales.",
                    pasosDeAnalisis);
        }

        LogicExpr expresionFinal = principal;
        if (partes.length == 2) {
            LogicExpr secundaria = traducirBloque("en caso de que " + partes[1], pasosDeAnalisis);
            if (secundaria != null) {
                expresionFinal = new AndExpr(principal, secundaria);
                pasosDeAnalisis.add("Composición por conjunción de dos bloques.");
            }
        }

        String formula = EmitidorFormula.emitir(expresionFinal, registro, pasosDeAnalisis);
        return LogicScriptResult.exito(
                formula,
                "Traducción LogicScript completada.",
                pasosDeAnalisis,
                registro.mapaParaResultado());
    }

    private LogicExpr traducirBloque(String bloque, List<String> pasosDeAnalisis) {
        String limpio = bloque.trim();
        if (limpio.isEmpty()) {
            return null;
        }
        List<TokenNatural> tokensBloque = lexer.tokenizar(limpio);
        pasosDeAnalisis.add("Tokens bloque: " + tokensBloque);
        LogicExpr expr = semanticMapper.mapearBloque(limpio, tokensBloque, pasosDeAnalisis);
        if (expr != null) {
            return expr;
        }
        pasosDeAnalisis.add("Fallback local: átomo simple.");
        return new AtomExpr(baseConocimiento.canonicalizarFragmento(limpio), false);
    }
}
