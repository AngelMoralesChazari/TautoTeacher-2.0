package tautoteacher2.core.logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Datos estructurados de una tabla de verdad para la UI (Visualización Clara).
 */
public final class TablaVerdad {

    public record Fila(boolean[] valoresVariables, boolean resultadoFormula) {
    }

    public record Resultado(
            String formula,
            List<String> variables,
            List<Fila> filas,
            String mensajeInformativo) {

        public Resultado {
            variables = List.copyOf(variables);
            filas = List.copyOf(filas);
        }

        public boolean tieneDatos() {
            return !variables.isEmpty() && !filas.isEmpty();
        }
    }

    private static final int MAX_VARIABLES = 4;

    private TablaVerdad() {
    }

    public static Resultado construir(String formula) {
        if (formula == null || formula.isBlank()) {
            return vacio(formula, "No hay fórmula para mostrar.");
        }
        try {
            MotorLogico.tipoFormula(formula);
        } catch (IllegalArgumentException ex) {
            return vacio(formula, ex.getMessage());
        }

        String expr = exprInterna(formula);
        Set<String> variables = extraerVariables(expr);
        if (variables.isEmpty()) {
            return vacio(formula, "La fórmula no contiene variables proposicionales.");
        }
        if (variables.size() > MAX_VARIABLES) {
            return new Resultado(
                    formula,
                    List.of(),
                    List.of(),
                    "La fórmula tiene más de " + MAX_VARIABLES + " variables; no se muestra la tabla.");
        }

        String[] vars = variables.toArray(new String[0]);
        int n = vars.length;
        int total = 1 << n;
        List<Fila> filas = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            Map<String, Boolean> valores = new HashMap<>();
            boolean[] filaValores = new boolean[n];
            for (int j = 0; j < n; j++) {
                boolean v = (i & (1 << j)) != 0;
                valores.put(vars[j], v);
                filaValores[j] = v;
            }
            String evaluable = expr;
            for (String v : vars) {
                evaluable = evaluable.replaceAll(
                        "(?<![a-zA-Z0-9_])" + Pattern.quote(v) + "(?![a-zA-Z0-9_])",
                        valores.get(v).toString());
            }
            filas.add(new Fila(filaValores, MotorLogico.evaluaParaTablaVerdad(evaluable)));
        }

        return new Resultado(formula, List.of(vars), filas, null);
    }

    /** Mapa variable → valor de verdad para una fila de la tabla. */
    public static Map<String, Boolean> valoresDeFila(List<String> variables, Fila fila) {
        Map<String, Boolean> valores = new LinkedHashMap<>();
        for (int i = 0; i < variables.size(); i++) {
            valores.put(variables.get(i), fila.valoresVariables()[i]);
        }
        return valores;
    }

    private static Resultado vacio(String formula, String mensaje) {
        return new Resultado(formula != null ? formula : "", List.of(), List.of(), mensaje);
    }

    private static String exprInterna(String formula) {
        return formula.replace("∧", "&&")
                .replace("∨", "||")
                .replace("¬", "!")
                .replace("→", "->")
                .replace("↔", "<->");
    }

    private static Set<String> extraerVariables(String expr) {
        Set<String> variables = new TreeSet<>();
        Matcher matcher = Pattern.compile("[a-zA-Z]").matcher(expr);
        while (matcher.find()) {
            variables.add(matcher.group());
        }
        return variables;
    }
}
