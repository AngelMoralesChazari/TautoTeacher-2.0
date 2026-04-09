package tautoteacher2.ui;

import java.awt.Color;
import tautoteacher2.core.logica.MotorLogico;
import tautoteacher2.logicscript.LogicScriptResult;
import tautoteacher2.logicscript.LogicScriptService;

public class TautoTeacherApp {

    private static final Color COLOR_AFIRMACION = new Color(40, 167, 69);
    private static final Color COLOR_ERROR = new Color(220, 53, 69);

    private VentanaPrincipal ventana;
    private PanelEntradaNatural panelEntrada;
    private PanelResultadoLogico panelResultado;
    private final LogicScriptService logicScriptService = new LogicScriptService();

    public void iniciar() {
        ventana = new VentanaPrincipal();

        panelEntrada = ventana.getPanelEntradaNatural();
        panelResultado = ventana.getPanelResultadoLogico();

        panelEntrada.setProcesarListener(e -> procesarEntrada());

        ventana.setVisible(true);
        System.out.println("Ventana principal mostrada.");
    }

    private void procesarEntrada() {
        if (panelEntrada.getModoEntrada() == PanelEntradaNatural.ModoEntrada.LENGUAJE_NATURAL) {
            procesarLenguajeNatural();
            return;
        }

        String formula = normalizarSimbolosTeclado(panelEntrada.getTextoFormula().trim());
        if (formula.isEmpty()) {
            panelResultado.limpiarIcono();
            panelResultado.setResultado("Por favor ingrese una expresión lógica para verificar.", COLOR_ERROR);
            return;
        }

        try {
            boolean esTautologia = MotorLogico.esTautologia(formula);
            String tipo = MotorLogico.tipoFormula(formula);

            if (esTautologia) {
                panelResultado.setEstado(true);
                panelResultado.setResultado(
                        "\"" + formula + "\" es una tautología.\n"
                                + "La expresión es siempre verdadera bajo todas las interpretaciones posibles.",
                        COLOR_AFIRMACION
                );
            } else {
                panelResultado.setEstado(false);
                panelResultado.setResultado(
                        "\"" + formula + "\" no es una tautología.\n"
                                + "Clasificación: " + tipo + ".\n"
                                + "Existen interpretaciones donde la expresión es falsa.",
                        COLOR_ERROR
                );
            }
        } catch (Exception ex) {
            panelResultado.limpiarIcono();
            panelResultado.setResultado("Error al analizar la expresión: " + ex.getMessage(), COLOR_ERROR);
        }
    }

    private void procesarLenguajeNatural() {
        String enunciado = panelEntrada.getTextoLenguajeNatural().trim();
        if (enunciado.isEmpty()) {
            panelResultado.limpiarIcono();
            panelResultado.setResultado("Por favor ingrese un enunciado en lenguaje natural.", COLOR_ERROR);
            return;
        }

        try {
            LogicScriptResult traduccion = logicScriptService.traducir(enunciado);
            if (!traduccion.isExito()) {
                panelResultado.limpiarIcono();
                panelResultado.setResultado(
                        "No pude interpretar el enunciado con las reglas actuales de LogicScript.\n"
                                + traduccion.getMensaje(),
                        COLOR_ERROR
                );
                return;
            }

            String formula = traduccion.getFormula();
            boolean esTautologia = MotorLogico.esTautologia(formula);
            String tipo = MotorLogico.tipoFormula(formula);

            if (esTautologia) {
                panelResultado.setEstado(true);
                panelResultado.setResultado(
                        "Tu enunciado es una tautología.\n"
                                + "Clasificación: " + tipo + ".\n"
                                + "La expresión es verdadera en todas las interpretaciones.\n"
                                + "Forma lógica interna: " + formula,
                        COLOR_AFIRMACION
                );
            } else {
                panelResultado.setEstado(false);
                panelResultado.setResultado(
                        "Tu enunciado NO es una tautología.\n"
                                + "Clasificación: " + tipo + ".\n"
                                + "Existen interpretaciones donde la expresión es falsa.\n"
                                + "Forma lógica interna: " + formula,
                        COLOR_ERROR
                );
            }
        } catch (Exception ex) {
            panelResultado.limpiarIcono();
            panelResultado.setResultado("Error al analizar el enunciado: " + ex.getMessage(), COLOR_ERROR);
        }
    }

    private static String normalizarSimbolosTeclado(String f) {
        if (f == null || f.isEmpty()) {
            return "";
        }
        String s = f.trim();
        s = s.replace("<->", "↔");
        s = s.replace("<=>", "↔");
        s = s.replace("->", "→");
        s = s.replace("&&", "∧");
        s = s.replace("||", "∨");
        s = s.replace("^", "∧");
        s = s.replace("~", "¬");
        s = s.replace("!", "¬");
        return s.replaceAll("\\s+", " ").trim();
    }
}