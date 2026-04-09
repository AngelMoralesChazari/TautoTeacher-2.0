package tautoteacher2.logicscript;

import tautoteacher2.core.logica.MotorLogico;

public final class LogicScriptCli {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Uso: java -cp <out> tautoteacher2.logicscript.LogicScriptCli \"<enunciado en lenguaje natural>\"");
            System.exit(1);
        }
        String enunciado = String.join(" ", args).trim();
        if (enunciado.isEmpty()) {
            System.err.println("Enunciado vacío.");
            System.exit(1);
        }

        LogicScriptService servicio = new LogicScriptService();
        LogicScriptResult resultado = servicio.traducir(enunciado);

        System.out.println("=== LogicScript (demo consola) ===");
        System.out.println("Entrada: " + enunciado);
        System.out.println();

        if (!resultado.isExito()) {
            System.out.println("Estado: ERROR");
            System.out.println(resultado.getMensaje());
            for (String paso : resultado.getPasosDeAnalisis()) {
                System.out.println("  - " + paso);
            }
            System.exit(2);
        }

        String formula = resultado.getFormula();
        System.out.println("Estado: OK");
        System.out.println("Fórmula: " + formula);
        System.out.println("Proposiciones: " + resultado.getProposiciones());
        System.out.println("Pasos de análisis:");
        for (String paso : resultado.getPasosDeAnalisis()) {
            System.out.println("  - " + paso);
        }
        System.out.println();

        try {
            boolean taut = MotorLogico.esTautologia(formula);
            String tipo = MotorLogico.tipoFormula(formula);
            System.out.println("Motor lógico:");
            System.out.println("  Tautología: " + taut);
            System.out.println("  Clasificación: " + tipo);
        } catch (Exception ex) {
            System.out.println("Motor lógico: error al evaluar — " + ex.getMessage());
            System.exit(3);
        }
    }
}
