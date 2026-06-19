package tautoteacher2.ui;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import tautoteacher2.core.logica.MotorLogico;
import tautoteacher2.logicscript.LogicScriptResult;
import tautoteacher2.logicscript.LogicScriptService;

public class TautoTeacherApp {

    private static final Color COLOR_AFIRMACION = new Color(40, 167, 69);
    private static final Color COLOR_ADVERTENCIA = new Color(200, 120, 0);
    private static final Color COLOR_ERROR = new Color(220, 53, 69);
    private static final Color COLOR_TEXTO = new Color(33, 37, 41);

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

        ventana.limpiarContenidoEducativo();

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
                        "Fórmula: " + formula + "\n\n"
                                + "Clasificación: " + tipo + ".\n"
                                + "La expresión es siempre verdadera bajo todas las interpretaciones posibles.",
                        COLOR_AFIRMACION
                );
            } else {
                panelResultado.setEstado(false);
                panelResultado.setResultado(
                        "Fórmula: " + formula + "\n\n"
                                + "Clasificación: " + tipo + ".\n"
                                + "Existen interpretaciones donde la expresión es falsa.",
                        COLOR_TEXTO
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
            panelResultado.setResultado("Por favor ingrese un enunciado.", COLOR_ERROR);
            ventana.limpiarContenidoEducativo();
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
                ventana.setContenidoEducativo(formatearPasosEducativos(traduccion, enunciado));
                return;
            }

            ventana.setContenidoEducativo(formatearPasosEducativos(traduccion, enunciado));

            String formula = traduccion.getFormula();
            boolean esTautologia = MotorLogico.esTautologia(formula);
            String tipo = MotorLogico.tipoFormula(formula);
            boolean traduccionDudosa = usoFallback(traduccion.getPasosDeAnalisis());

            panelResultado.setEstado(esTautologia);
            panelResultado.setResultado(
                    construirResumenLenguajeNatural(traduccion, esTautologia, tipo, traduccionDudosa),
                    colorResumen(esTautologia, traduccionDudosa)
            );
        } catch (Exception ex) {
            panelResultado.limpiarIcono();
            panelResultado.setResultado("Error al analizar el enunciado: " + ex.getMessage(), COLOR_ERROR);
            ventana.limpiarContenidoEducativo();
        }
    }

    private static String construirResumenLenguajeNatural(
            LogicScriptResult traduccion,
            boolean esTautologia,
            String tipo,
            boolean traduccionDudosa
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("Traducción LogicScript\n");
        sb.append("─────────────────────\n\n");
        sb.append("Fórmula: ").append(traduccion.getFormula()).append("\n\n");
        sb.append(formatearMapaProposiciones(traduccion.getProposiciones()));
        sb.append("\nVerificación lógica\n");
        sb.append("─────────────────────\n\n");
        sb.append("Clasificación: ").append(tipo).append("\n");
        if (esTautologia) {
            sb.append("Es tautología: la fórmula es verdadera en todas las interpretaciones.\n");
        } else {
            sb.append("No es tautología: hay interpretaciones donde la fórmula es falsa.\n");
        }
        if (traduccionDudosa) {
            sb.append("\nAdvertencia: se usó un fallback (átomo simple). ");
            sb.append("La estructura del enunciado puede no haberse capturado por completo.\n");
        }
        sb.append("\nVea la tarjeta «Explicación» para el detalle de lexemas y patrones.");
        return sb.toString();
    }

    private static String formatearMapaProposiciones(Map<String, String> proposiciones) {
        if (proposiciones == null || proposiciones.isEmpty()) {
            return "Proposiciones: (ninguna asignada)\n\n";
        }
        StringBuilder sb = new StringBuilder("Proposiciones:\n");
        for (Map.Entry<String, String> e : proposiciones.entrySet()) {
            sb.append("  ").append(e.getValue()).append(" = ").append(e.getKey()).append("\n");
        }
        return sb.append("\n").toString();
    }

    private static String formatearPasosEducativos(LogicScriptResult traduccion, String enunciadoOriginal) {
        StringBuilder sb = new StringBuilder();
        sb.append("Enunciado: ").append(enunciadoOriginal).append("\n\n");
        if (traduccion.isExito()) {
            sb.append("Fórmula emitida: ").append(traduccion.getFormula()).append("\n\n");
            sb.append(formatearMapaProposiciones(traduccion.getProposiciones()));
        } else {
            sb.append("Estado: error de traducción\n");
            sb.append("Mensaje: ").append(traduccion.getMensaje()).append("\n\n");
        }
        sb.append("Pasos de análisis:\n");
        List<String> pasos = traduccion.getPasosDeAnalisis();
        if (pasos == null || pasos.isEmpty()) {
            sb.append("  (sin pasos registrados)\n");
        } else {
            for (String paso : pasos) {
                sb.append("  • ").append(paso).append("\n");
            }
        }
        return sb.toString();
    }

    private static boolean usoFallback(List<String> pasos) {
        if (pasos == null) {
            return false;
        }
        for (String paso : pasos) {
            if (paso != null && paso.toLowerCase().contains("fallback")) {
                return true;
            }
        }
        return false;
    }

    private static Color colorResumen(boolean esTautologia, boolean traduccionDudosa) {
        if (traduccionDudosa) {
            return COLOR_ADVERTENCIA;
        }
        return esTautologia ? COLOR_AFIRMACION : COLOR_TEXTO;
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
