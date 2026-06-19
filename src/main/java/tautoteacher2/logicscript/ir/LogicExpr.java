package tautoteacher2.logicscript.ir;

/**
 * Representación intermedia (IR) de LogicScript: expresión proposicional antes de emitir
 * la cadena para {@code MotorLogico}. Evolucionará con más conectivas y nodos.
 */
public sealed interface LogicExpr permits AtomExpr, NegExpr, AndExpr, OrExpr, ImpExpr, EquivExpr {
}
