package tautoteacher2.ui;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import tautoteacher2.core.logica.ExplicacionEducativaBuilder;
import tautoteacher2.core.logica.MotorLogico;
import tautoteacher2.core.logica.TablaVerdad;
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
    private PanelVisualizacion panelVisualizacion;
    private final LogicScriptService logicScriptService = new LogicScriptService();

    public void iniciar() {
        ventana = new VentanaPrincipal();

        panelEntrada = ventana.getPanelEntradaNatural();
        panelResultado = ventana.getPanelResultadoLogico();
        panelVisualizacion = ventana.getPanelVisualizacion();

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
            ventana.limpiarContenidoEducativo();
            panelVisualizacion.limpiar();
            return;
        }

        try {
            String tipo = MotorLogico.tipoFormula(formula);
            mostrarDictamen(tipo);
            ventana.setContenidoEducativo(ExplicacionEducativaBuilder.construir(
                    formatearSeccionTecnicaFormula(formula, tipo),
                    formula,
                    tipo,
                    Map.of()));
            actualizarVisualizacion(formula, Map.of());
        } catch (Exception ex) {
            panelResultado.limpiarIcono();
            panelResultado.setResultado("Error al analizar la expresión: " + ex.getMessage(), COLOR_ERROR);
            ventana.limpiarContenidoEducativo();
            panelVisualizacion.limpiar();
        }
    }

    private void procesarLenguajeNatural() {
        String enunciado = panelEntrada.getTextoLenguajeNatural().trim();
        if (enunciado.isEmpty()) {
            panelResultado.limpiarIcono();
            panelResultado.setResultado("Por favor ingrese un enunciado.", COLOR_ERROR);
            ventana.limpiarContenidoEducativo();
            panelVisualizacion.limpiar();
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
                ventana.setContenidoEducativo(formatearExplicacionError(traduccion, enunciado));
                panelVisualizacion.limpiar();
                return;
            }

            String formula = traduccion.getFormula();
            String tipo = MotorLogico.tipoFormula(formula);
            boolean traduccionDudosa = usoFallback(traduccion.getPasosDeAnalisis());

            mostrarDictamen(tipo);
            ventana.setContenidoEducativo(ExplicacionEducativaBuilder.construir(
                    formatearSeccionTecnicaLenguajeNatural(traduccion, enunciado, tipo, traduccionDudosa),
                    formula,
                    tipo,
                    traduccion.getProposiciones()));
            actualizarVisualizacion(formula, traduccion.getProposiciones());
        } catch (Exception ex) {
            panelResultado.limpiarIcono();
            panelResultado.setResultado("Error al analizar el enunciado: " + ex.getMessage(), COLOR_ERROR);
            ventana.limpiarContenidoEducativo();
            panelVisualizacion.limpiar();
        }
    }

    private void actualizarVisualizacion(String formula, Map<String, String> proposiciones) {
        panelVisualizacion.mostrarTabla(TablaVerdad.construir(formula), proposiciones);
    }

    private void mostrarDictamen(String tipo) {
        panelResultado.setDictamen(tipo);
        panelResultado.setResultado(formatearDictamenBreve(tipo), colorDictamen(tipo));
    }

    private static String formatearDictamenBreve(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return "";
        }
        return descripcionClasificacion(tipo)
                + "\n\nPor lo tanto, se clasifica como:\n\n"
                + tipo.toUpperCase();
    }

    private static Color colorDictamen(String tipo) {
        if (tipo == null) {
            return COLOR_TEXTO;
        }
        return switch (tipo.toUpperCase()) {
            case "TAUTOLOGÍA" -> COLOR_AFIRMACION;
            case "CONTRADICCIÓN" -> COLOR_ERROR;
            default -> COLOR_TEXTO;
        };
    }

    private static String formatearSeccionTecnicaFormula(String formula, String tipo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Fórmula ingresada\n");
        sb.append("─────────────────\n\n");
        sb.append(formula).append("\n\n");
        sb.append("Verificación lógica\n");
        sb.append("─────────────────\n\n");
        sb.append("Clasificación: ").append(tipo).append("\n\n");
        sb.append(descripcionClasificacion(tipo));
        return sb.toString();
    }

    private static String formatearSeccionTecnicaLenguajeNatural(
            LogicScriptResult traduccion,
            String enunciadoOriginal,
            String tipo,
            boolean traduccionDudosa
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("Enunciado\n");
        sb.append("─────────\n\n");
        sb.append(enunciadoOriginal).append("\n\n");

        sb.append("Traducción LogicScript\n");
        sb.append("──────────────────────\n\n");
        sb.append("Fórmula: ").append(traduccion.getFormula()).append("\n\n");
        sb.append(formatearMapaProposiciones(traduccion.getProposiciones()));

        sb.append("Verificación lógica\n");
        sb.append("─────────────────\n\n");
        sb.append("Clasificación: ").append(tipo).append("\n\n");
        sb.append(descripcionClasificacion(tipo));

        if (traduccionDudosa) {
            sb.append("\nAdvertencia\n");
            sb.append("───────────\n\n");
            sb.append("Se usó un fallback (átomo simple). ");
            sb.append("La estructura del enunciado puede no haberse capturado por completo.\n");
        }

        sb.append("\nPasos de análisis\n");
        sb.append("─────────────────\n\n");
        sb.append(formatearListaPasos(traduccion.getPasosDeAnalisis()));
        return sb.toString();
    }

    private static String formatearExplicacionError(LogicScriptResult traduccion, String enunciadoOriginal) {
        StringBuilder sb = new StringBuilder();
        sb.append("Enunciado\n");
        sb.append("─────────\n\n");
        sb.append(enunciadoOriginal).append("\n\n");
        sb.append("Error de traducción\n");
        sb.append("───────────────────\n\n");
        sb.append(traduccion.getMensaje()).append("\n\n");
        sb.append("Pasos de análisis\n");
        sb.append("─────────────────\n\n");
        sb.append(formatearListaPasos(traduccion.getPasosDeAnalisis()));
        return sb.toString();
    }

    private static String descripcionClasificacion(String tipo) {
        if (tipo == null) {
            return "";
        }
        return switch (tipo.toUpperCase()) {
            case "TAUTOLOGÍA" ->
                    "La fórmula es verdadera en todas las interpretaciones posibles de sus variables.";
            case "CONTRADICCIÓN" ->
                    "La fórmula es falsa en todas las interpretaciones posibles de sus variables.";
            default ->
                    "La fórmula es verdadera en algunas interpretaciones y falsa en otras.";
        };
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

    private static String formatearListaPasos(List<String> pasos) {
        if (pasos == null || pasos.isEmpty()) {
            return "  (sin pasos registrados)\n";
        }
        StringBuilder sb = new StringBuilder();
        for (String paso : pasos) {
            sb.append("  • ").append(paso).append("\n");
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
