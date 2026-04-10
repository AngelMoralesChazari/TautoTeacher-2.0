package tautoteacher2.logicscript;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Pruebas de regresión del pipeline LogicScript (LN → fórmula).
 * Ejecutar tras compilar y copiar recursos:
 * {@code java -cp out tautoteacher2.logicscript.LogicScriptRegressionHarness}
 * <p>
 * Documentación: {@code docs/LogicScript/PruebasRegresion.md}
 */
public final class LogicScriptRegressionHarness {

    private static final String IMP = "\u2192";
    private static final String AND = "\u2227";
    private static final String OR = "\u2228";

    private record Caso(String id, String entrada, boolean esperaExito, String formulaEsperada) {
        Caso {
            Objects.requireNonNull(id);
            Objects.requireNonNull(entrada);
            formulaEsperada = formulaEsperada != null ? formulaEsperada : "";
        }
    }

    public static void main(String[] args) {
        List<Caso> casos = new ArrayList<>();
        casos.add(new Caso("si_entonces", "si llueve entonces llevo paraguas", true, "(p " + IMP + " q)"));
        casos.add(new Caso("consecuente_si", "llevo paraguas si llueve", true, "(p " + IMP + " q)"));
        casos.add(new Caso("en_caso_de_que", "en caso de que llueva, llevo paraguas", true, "(p " + IMP + " q)"));
        casos.add(new Caso("conjuncion", "llueve y estudio", true, "(p " + AND + " q)"));
        casos.add(new Caso("disyuncion", "llueve o estudio", true, "(p " + OR + " q)"));
        casos.add(new Caso("lemmas_si", "si estudio entonces apruebo", true, "(p " + IMP + " q)"));
        casos.add(new Caso(
                "dos_bloques",
                "si llueve entonces llevo paraguas, en caso de que estudio, apruebo",
                true,
                "((p " + IMP + " q) " + AND + " (r " + IMP + " s))"));
        casos.add(new Caso("vacio", "", false, ""));
        casos.add(new Caso("solo_espacios", "   \t  ", false, ""));

        LogicScriptService servicio = new LogicScriptService();
        int fallos = 0;
        for (Caso c : casos) {
            LogicScriptResult r = servicio.traducir(c.entrada());
            boolean okExito = r.isExito() == c.esperaExito();
            boolean okFormula = !c.esperaExito() || r.getFormula().equals(c.formulaEsperada());
            if (!okExito || !okFormula) {
                fallos++;
                System.err.println("FALLO [" + c.id() + "] entrada=" + repr(c.entrada()));
                System.err.println("  esperaba exito=" + c.esperaExito() + ", obtuvo=" + r.isExito());
                if (c.esperaExito()) {
                    System.err.println("  formula esperada: " + c.formulaEsperada());
                    System.err.println("  formula obtenida: " + r.getFormula());
                }
                if (!r.isExito()) {
                    System.err.println("  mensaje: " + r.getMensaje());
                }
            }
        }
        if (fallos == 0) {
            System.out.println("LogicScriptRegressionHarness: OK (" + casos.size() + " casos).");
        } else {
            System.err.println("LogicScriptRegressionHarness: " + fallos + " fallo(s) de " + casos.size() + ".");
            System.exit(1);
        }
    }

    private static String repr(String s) {
        if (s == null) {
            return "null";
        }
        return "\"" + s.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t") + "\"";
    }
}
