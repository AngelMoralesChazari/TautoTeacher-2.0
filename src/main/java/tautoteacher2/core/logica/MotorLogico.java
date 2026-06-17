package tautoteacher2.core.logica;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MotorLogico {
    public static boolean esTautologia(String formula) {
        validarSintaxis(formula);

        // Reemplazar símbolos
        String expr = formula.replace("∧", "&&")
                             .replace("∨", "||")
                             .replace("¬", "!")
                             .replace("→", "->")
                             .replace("↔", "<->");

        // Extraer variables
        Set<String> variables = new TreeSet<>();
        Matcher matcher = Pattern.compile("[a-zA-Z]").matcher(expr);

        while (matcher.find()) {
            variables.add(matcher.group());
        }

        if (variables.isEmpty()) {
            throw new IllegalArgumentException("La expresión no contiene variables proposicionales");
        }

        String vars[] = variables.toArray(new String[0]);
        int n = vars.length;
        int totalCombinations = 1 << n; // 2^n combinaciones

        for (int i = 0; i < totalCombinations; i++) {
            Map<String, Boolean> valores = new HashMap<>();

            for (int j = 0; j < n; j++) {
                valores.put(vars[j], (i & (1 << j)) != 0);
            }

            String evaluable = expr;

            // Reemplazo variables con sus valores
            for (String var : vars) {
                evaluable = evaluable.replaceAll("(?<![a-zA-Z0-9_])" + Pattern.quote(var) + "(?![a-zA-Z0-9_])",
                        valores.get(var).toString());
            }

            try {
                if (!evaluaExpresion(evaluable)) {
                    return false;
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Error al evaluar la expresión con valores: " + valores, e);
            }
        }
        return true;
    }

    public static String tipoFormula(String formula) {
        validarSintaxis(formula);

        String expr = formula.replace("∧", "&&")
                             .replace("∨", "||")
                             .replace("¬", "!")
                             .replace("→", "->")
                             .replace("↔", "<->");

        Set<String> variables = extraerVariables(expr);

        if (variables.isEmpty()) {
            throw new IllegalArgumentException("La expresión no contiene variables proposicionales");
        }

        String vars[] = variables.toArray(new String[0]);
        int n = vars.length;
        int totalCombinaciones = 1 << n;

        boolean algunaTrue = false;
        boolean algunaFalse = false;

        for (int i = 0; i < totalCombinaciones; i++) {
            Map<String, Boolean> valores = new HashMap<>();
            for (int j = 0; j < n; j++) {
                valores.put(vars[j], (i & (1 << j)) != 0);
            }

            String evaluable = expr;
            for (String var : vars) {
                evaluable = evaluable.replaceAll("(?<![a-zA-Z0-9_])" + Pattern.quote(var) + "(?![a-zA-Z0-9_])",
                        valores.get(var).toString());
            }

            boolean res = evaluaExpresion(evaluable);

            if (res)
                algunaTrue = true;
            else
                algunaFalse = true;

            if (algunaTrue && algunaFalse)
                break; // Contingencia
        }

        if (algunaTrue && !algunaFalse)
            return "TAUTOLOGÍA";
        if (!algunaTrue && algunaFalse)
            return "CONTRADICCIÓN";
        return "CONTINGENCIA";
    }

    public static String generarExplicacionEducativa(String formula, boolean esTautologia) {
        StringBuilder sb = new StringBuilder();
        sb.append("DEMOSTRACIÓN POR REFUTACION\n");
        sb.append("=============================\n\n");

        // 1. Presentar la fórmula general
        sb.append("1. FÓRMULA INGRESADA:\n   ").append(formula).append("\n\n");

        // 2. Mostrar variables que la componen
        Set<String> variables = extraerVariables(formula);
        sb.append("2. VARIABLES PROPOSICIONALES:\n   ");
        int idx = 0;
        for (String var : variables) {
            sb.append(var);
            if (++idx < variables.size())
                sb.append(", ");
        }
        sb.append("\n\n");

        // 3. Intentar desglosar como implicación o equivalencia principal
        String antecedente = null, consecuente = null;
        String tipoPrincipal = null;
        int nivel = 0, pos = -1;
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == '(')
                nivel++;
            if (c == ')')
                nivel--;
            if (nivel == 0) {
                if (formula.startsWith("→", i)) {
                    pos = i;
                    tipoPrincipal = "implicacion";
                    break;
                }
                if (formula.startsWith("↔", i)) {
                    pos = i;
                    tipoPrincipal = "equivalencia";
                    break;
                }
            }
        }
        if (pos != -1) {
            antecedente = formula.substring(0, pos).trim();
            consecuente = formula.substring(pos + 1).trim();
        }

        if (antecedente != null && consecuente != null) {
            sb.append("3. ESTRUCTURA DE LA FÓRMULA:\n");
            if ("implicacion".equals(tipoPrincipal)) {
                sb.append("   La fórmula es una implicación donde:\n");
                sb.append("   - Antecedente: ").append(antecedente).append("\n");
                sb.append("   - Consecuente: ").append(consecuente).append("\n\n");
                sb.append("4. MÉTODO DE DEMOSTRACIÓN:\n");
                sb.append("   Para demostrar que es una tautología, asumiremos que:\n");
                sb.append("   1. El antecedente es VERDADERO\n");
                sb.append("   2. El consecuente es FALSO\n");
                sb.append("   Si llegamos a una contradicción, la fórmula es tautología.\n\n");

                // Paso a paso para implicaciones del tipo (P→Q)∧(Q→R)→(P→R)
                sb.append("5. PASO A PASO:\n");

                // a) Asumir que el consecuente = F
                sb.append("   a) Asumir ").append(consecuente).append(" = F\n");
                if (consecuente.contains("→")) {
                    String partesCons[] = consecuente.split("→");
                    if (partesCons.length == 2) {
                        String P = partesCons[0].replaceAll("[()]", "").trim();
                        String R = partesCons[1].replaceAll("[()]", "").trim();
                        sb.append("      - ").append(consecuente).append(" es FALSO solo cuando:\n");
                        sb.append("        ").append(P).append(" = V y ").append(R).append(" = F\n\n");
                    } else {
                        sb.append("      - Error al analizar el consecuente como implicación.\n\n");
                    }
                } else {
                    sb.append("      - ").append(consecuente).append(" es FALSO solo cuando:\n");
                    sb.append("        ").append(consecuente).append(" = F\n\n");
                }

                // b) Sustituir estos valores en el antecedente
                sb.append("   b) Sustituir estos valores en el antecedente ").append(antecedente).append(":\n");
                // Buscar subpreposiciones
                List<String> preposiciones = new ArrayList<>();
                String ant = antecedente;
                nivel = 0;
                int last = 0;
                for (int i = 0; i < ant.length(); i++) {
                    char c = ant.charAt(i);
                    if (c == '(')
                        nivel++;
                    if (c == ')')
                        nivel--;
                    if (nivel == 0 && i < ant.length() - 1 && ant.charAt(i) == '∧') {
                        preposiciones.add(ant.substring(last, i).trim());
                        last = i + 1;
                    }
                }
                preposiciones.add(ant.substring(last).trim());

                sb.append("      - El antecedente es una conjunción de:\n");
                int pidx = 1;
                for (String prep : preposiciones) {
                    sb.append("        ").append(pidx++).append(". ").append(prep).append("\n");
                }
                sb.append("\n");

                // Declarar P y R fuera del ciclo para que sean accesibles
                String P = null, R = null;
                if (consecuente.contains("→")) {
                    String partesCons[] = consecuente.split("→");
                    if (partesCons.length == 2) {
                        P = partesCons[0].replaceAll("[()]", "").trim();
                        R = partesCons[1].replaceAll("[()]", "").trim();
                    }
                }

                // c) Evaluar cada preposición con los valores asignados
                for (String prep : preposiciones) {
                    if (prep.contains("→")) {
                        String partes[] = prep.split("→");
                        String H = partes[0].replaceAll("[()]", "").trim();
                        String C = partes[1].replaceAll("[()]", "").trim();
                        sb.append("   c) Evaluar ").append(prep).append(" con ");
                        if (P != null && H.equals(P)) {
                            sb.append(P).append("=V");
                        }
                        if (R != null && C.equals(R)) {
                            if (P != null && H.equals(P)) {
                                sb.append(", ");
                            }
                            sb.append(R).append("=F");
                        }
                        sb.append(":\n");
                        if (P != null && R != null && H.equals(P) && C.equals(R)) {
                            sb.append("      - ").append(H).append(" = V, ").append(C).append(" = F\n");
                            sb.append("      - ").append(prep).append(" = V→F = F\n");
                        } else if (P != null && H.equals(P)) {
                            sb.append("      - ").append(H).append(" = V\n");
                            sb.append("      - Para que ").append(prep).append(" sea VERDADERA, ");
                            sb.append(C).append(" debe ser V (V→V = V)\n");
                            sb.append("      - Por lo tanto, ").append(C).append(" = V\n");
                        } else if (R != null && C.equals(R)) {
                            sb.append("      - ").append(C).append(" = F\n");
                            sb.append("      - Para que ").append(prep).append(" sea VERDADERA, ");
                            sb.append(H).append(" debe ser F (F→F = V)\n");
                            sb.append("      - Por lo tanto, ").append(H).append(" = F\n");
                        } else {
                            sb.append("      - No se puede determinar directamente con los valores asignados.\n");
                        }
                        sb.append("\n");
                    }
                }

                // d) Evaluar la conjunción
                sb.append("   d) Evaluar la conjunción completa:\n");
                sb.append("      - Si alguna de las preposiciones es FALSA, toda la conjunción es FALSA.\n");
                sb.append("      - En este caso, al sustituir los valores, una de las preposiciones resulta FALSA.\n");
                sb.append("      - ¡CONTRADICCIÓN! El antecedente debería ser VERDADERO, pero resulta FALSO.\n\n");

                // 6. Conclusión
                sb.append("6. CONCLUSIÓN:\n");
                if (esTautologia) {
                    sb.append("   Hemos llegado a una contradicción al asumir el consecuente falso.\n");
                    sb.append("   Por lo tanto, la fórmula ES una TAUTOLOGÍA.\n");
                } else {
                    sb.append("   No se llegó a contradicción en todos los casos posibles.\n");
                    sb.append("   Por lo tanto, la fórmula NO es una tautología.\n");
                }
            } else if ("equivalencia".equals(tipoPrincipal)) {
                sb.append("   La fórmula es una equivalencia lógica donde:\n");
                sb.append("   - Primer miembro: ").append(antecedente).append("\n");
                sb.append("   - Segundo miembro: ").append(consecuente).append("\n\n");
                sb.append(
                        "   Una equivalencia es verdadera si ambos miembros tienen el mismo valor de verdad en todas las interpretaciones.\n\n");
                sb.append("   Para demostrar que es una tautología, analizamos todos los casos posibles:\n");
                sb.append("   - Si ambos miembros son verdaderos, la equivalencia es verdadera.\n");
                sb.append("   - Si ambos miembros son falsos, la equivalencia es verdadera.\n");
                sb.append("   - Si uno es verdadero y el otro falso, la equivalencia es falsa.\n\n");
                sb.append(
                        "   Por lo tanto, para que la fórmula sea una tautología, ambos miembros deben coincidir en valor de verdad en todas las interpretaciones.\n");
            }
        } else {
            sb.append(
                    "No se detectó una implicación o equivalencia principal o la fórmula no es del tipo esperado para este análisis detallado.\n");
        }

        // Explicación adicional según el resultado
        sb.append("\nEXPLICACIÓN FINAL:\n");
        if (esTautologia) {
            sb.append(
                    "   Una TAUTOLOGÍA es una fórmula que es verdadera bajo cualquier interpretación posible de sus variables.\n");
            sb.append("   En este caso, la fórmula es SIEMPRE VERDADERA.\n");
        } else {

            // Determinar si es contradicción o contingencia
            String tipo = tipoFormula(formula);
            if ("CONTRADICCIÓN".equals(tipo)) {
                sb.append(
                        "   Una CONTRADICCIÓN es una fórmula que es falsa bajo cualquier interpretación posible de sus variables.\n");
                sb.append("   En este caso, la fórmula es SIEMPRE FALSA.\n");
            } else if ("CONTINGENCIA".equals(tipo)) {
                sb.append(
                        "   Una CONTINGENCIA es una fórmula que es verdadera en algunas interpretaciones y falsa en otras.\n");
                sb.append("   En este caso, la fórmula es VERDADERA para algunos valores y FALSA para otros.\n");
            }
        }

        return sb.toString();
    }

    private static void validarSintaxis(String formula) {
        int balance = 0;
        for (char c : formula.toCharArray()) {
            if (c == '(')
                balance++;
            if (c == ')')
                balance--;
            if (balance < 0)
                throw new IllegalArgumentException("Paréntesis no balanceados");
        }
        if (balance != 0)
            throw new IllegalArgumentException("Paréntesis no balanceados");

        if (!formula.matches("^[a-zA-Z∧∨¬→↔()\\s]+$")) {
            throw new IllegalArgumentException("Caracteres no válidos en la expresión");
        }
    }

    private static Set<String> extraerVariables(String expr) {
        Set<String> variables = new TreeSet<>();
        Matcher matcher = Pattern.compile("[a-zA-Z]").matcher(expr);
        while (matcher.find()) {
            variables.add(matcher.group());
        }
        return variables;
    }

    private static boolean evaluaExpresion(String expr) {
        expr = expr.trim();
        if (expr.equals("true") || expr.equals("(true)"))
            return true;
        if (expr.equals("false") || expr.equals("(false)"))
            return false;

        // Evaluar paréntesis primero
        int parentAbiertos = expr.lastIndexOf('(');
        if (parentAbiertos != -1) {
            int parentCerrados = expr.indexOf(')', parentAbiertos);
            if (parentCerrados == -1) {
                throw new IllegalArgumentException("Paréntesis no balanceados");
            }
            String subExpr = expr.substring(parentAbiertos + 1, parentCerrados);
            boolean subResult = evaluaExpresion(subExpr);
            String newExpr = expr.substring(0, parentAbiertos) + subResult + expr.substring(parentCerrados + 1);
            return evaluaExpresion(newExpr);
        }

        // Negación
        if (expr.startsWith("!")) {
            return !evaluaExpresion(expr.substring(1).trim());
        }

        // Equivalencia
        int equivIndex = buscarOperadorMasBajo(expr, "<->");
        if (equivIndex != -1) {
            boolean left = evaluaExpresion(expr.substring(0, equivIndex));
            boolean right = evaluaExpresion(expr.substring(equivIndex + 3));
            return left == right;
        }

        // Implicación
        int implIndex = buscarOperadorMasBajo(expr, "->");
        if (implIndex != -1) {
            boolean left = evaluaExpresion(expr.substring(0, implIndex));
            boolean right = evaluaExpresion(expr.substring(implIndex + 2));
            return !left || right;
        }

        // Or
        int orIndex = buscarOperadorMasBajo(expr, "||");
        if (orIndex != -1) {
            boolean left = evaluaExpresion(expr.substring(0, orIndex));
            boolean right = evaluaExpresion(expr.substring(orIndex + 2));
            return left || right;
        }

        // And
        int andIndex = buscarOperadorMasBajo(expr, "&&");
        if (andIndex != -1) {
            boolean left = evaluaExpresion(expr.substring(0, andIndex));
            boolean right = evaluaExpresion(expr.substring(andIndex + 2));
            return left && right;
        }

        // Valores booleanos
        if (expr.equals("true"))
            return true;
        if (expr.equals("false"))
            return false;

        throw new IllegalArgumentException("Expresión no válida: " + expr);
    }

    private static int buscarOperadorMasBajo(String expr, String operator) {
        int nivelDeParent = 0;
        int index = -1;

        while ((index = expr.indexOf(operator, index + 1)) != -1) {
            if (index == -1)
                break;

            String before = expr.substring(0, index);
            nivelDeParent = contarParentesis(before);

            if (nivelDeParent == 0) {
                return index;
            }
        }
        return -1;
    }

    private static int contarParentesis(String str) {
        int contador = 0;
        for (char c : str.toCharArray()) {
            if (c == '(')
                contador++;
            if (c == ')')
                contador--;
        }
        return contador;
    }
}