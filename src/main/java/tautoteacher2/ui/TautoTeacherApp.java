package tautoteacher2.ui;

import java.awt.Color;
import tautoteacher2.core.logica.MotorLogico;

public class TautoTeacherApp {

    private static final Color COLOR_AFIRMACION = new Color(40, 167, 69);
    private static final Color COLOR_ERROR = new Color(220, 53, 69);

    private VentanaPrincipal ventana;
    private PanelEntradaNatural panelEntrada;
    private PanelResultadoLogico panelResultado;

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
            panelResultado.limpiarIcono();
            panelResultado.setResultado(
                    "Modo LN (lenguaje natural): el análisis con el motor lógico se activará cuando el "
                            + "pipeline NLP traduzca el texto a fórmula.",
                    Color.DARK_GRAY
            );
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