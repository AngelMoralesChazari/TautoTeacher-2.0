package tautoteacher2.core.visualizacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import tautoteacher2.core.logica.MotorLogico;

/**
 * Construye un árbol de evaluación sustituyendo variables por V/F y replicando la semántica del motor.
 */
public final class ConstructorArbolEvaluacion {

    private static final String[] OPERADORES = { "<->", "->", "||", "&&" };

    private ConstructorArbolEvaluacion() {
    }

    public static NodoArbolEvaluacion construir(String formula, Map<String, Boolean> valores) {
        String expr = normalizar(formula);
        for (Map.Entry<String, Boolean> e : valores.entrySet()) {
            expr = expr.replaceAll(
                    "(?<![a-zA-Z0-9_])" + Pattern.quote(e.getKey()) + "(?![a-zA-Z0-9_])",
                    e.getValue().toString());
        }
        return construirDesdeExpr(expr.trim());
    }

    private static String normalizar(String formula) {
        return formula.replace("∧", "&&")
                .replace("∨", "||")
                .replace("¬", "!")
                .replace("→", "->")
                .replace("↔", "<->");
    }

    private static NodoArbolEvaluacion construirDesdeExpr(String expr) {
        expr = expr.trim();
        if (expr.startsWith("(") && expr.endsWith(")") && parentesisBalanceados(expr.substring(1, expr.length() - 1))) {
            return construirDesdeExpr(expr.substring(1, expr.length() - 1).trim());
        }

        for (String op : OPERADORES) {
            int idx = buscarOperador(expr, op);
            if (idx != -1) {
                NodoArbolEvaluacion izq = construirDesdeExpr(expr.substring(0, idx).trim());
                NodoArbolEvaluacion der = construirDesdeExpr(expr.substring(idx + op.length()).trim());
                boolean valor = switch (op) {
                    case "<->" -> izq.valor() == der.valor();
                    case "->" -> !izq.valor() || der.valor();
                    case "||" -> izq.valor() || der.valor();
                    case "&&" -> izq.valor() && der.valor();
                    default -> false;
                };
                return new NodoArbolEvaluacion(simboloLegible(op), valor, List.of(izq, der));
            }
        }

        if (expr.startsWith("!")) {
            NodoArbolEvaluacion hijo = construirDesdeExpr(expr.substring(1).trim());
            return new NodoArbolEvaluacion("¬", !hijo.valor(), List.of(hijo));
        }

        if ("true".equals(expr)) {
            return new NodoArbolEvaluacion("V", true);
        }
        if ("false".equals(expr)) {
            return new NodoArbolEvaluacion("F", false);
        }

        boolean valor = MotorLogico.evaluarExpresionSustituida(expr);
        return new NodoArbolEvaluacion(expr, valor);
    }

    private static String simboloLegible(String op) {
        return switch (op) {
            case "&&" -> "∧";
            case "||" -> "∨";
            case "->" -> "→";
            case "<->" -> "↔";
            default -> op;
        };
    }

    private static int buscarOperador(String expr, String op) {
        int par = 0;
        for (int i = 0; i <= expr.length() - op.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') {
                par++;
            } else if (c == ')') {
                par--;
            } else if (par == 0 && expr.startsWith(op, i)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean parentesisBalanceados(String expr) {
        int par = 0;
        for (char c : expr.toCharArray()) {
            if (c == '(') {
                par++;
            } else if (c == ')') {
                par--;
            }
            if (par < 0) {
                return false;
            }
        }
        return par == 0;
    }
}
