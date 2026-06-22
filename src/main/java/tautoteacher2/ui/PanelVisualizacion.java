package tautoteacher2.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import tautoteacher2.core.logica.TablaVerdad;
import tautoteacher2.core.logica.TablaVerdad.Fila;
import tautoteacher2.core.visualizacion.ConstructorArbolEvaluacion;
import tautoteacher2.core.visualizacion.RenderizadorArbolEvaluacion;

/**
 * Panel «Visualización Clara»: pestañas Tabla de verdad y Árbol de evaluación.
 */
public class PanelVisualizacion extends JPanel {

    private static final Color COLOR_VERDADERO = new Color(212, 237, 218);
    private static final Color COLOR_FALSO = new Color(248, 215, 218);
    private static final Color COLOR_VERDADERO_TEXTO = new Color(21, 87, 36);
    private static final Color COLOR_FALSO_TEXTO = new Color(114, 28, 36);

    private final JLabel etiquetaFormula;
    private final JLabel etiquetaLeyenda;
    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final JTextArea areaArbol;
    private final JComboBox<String> selectorFilaArbol;
    private final JLabel etiquetaInterpretacionArbol;

    private TablaVerdad.Resultado resultadoActual;
    private String formulaActual = "";
    private Map<String, String> proposicionesActuales = Map.of();
    private boolean actualizandoSeleccion;

    public PanelVisualizacion() {
        setLayout(new java.awt.BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setOpaque(false);

        JPanel encabezado = new JPanel();
        encabezado.setLayout(new javax.swing.BoxLayout(encabezado, javax.swing.BoxLayout.Y_AXIS));
        encabezado.setOpaque(false);

        etiquetaFormula = new JLabel("Procese un enunciado para ver la tabla de verdad.");
        etiquetaFormula.setFont(new Font("Segoe UI Symbol", Font.BOLD, 16));
        etiquetaFormula.setAlignmentX(Component.LEFT_ALIGNMENT);

        etiquetaLeyenda = new JLabel(" ");
        etiquetaLeyenda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        etiquetaLeyenda.setForeground(new Color(80, 80, 80));
        etiquetaLeyenda.setAlignmentX(Component.LEFT_ALIGNMENT);

        encabezado.add(etiquetaFormula);
        encabezado.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 6)));
        encabezado.add(etiquetaLeyenda);

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabla.setRowHeight(32);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setDefaultRenderer(Object.class, new CeldaValorRenderer());

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        areaArbol = new JTextArea();
        areaArbol.setEditable(false);
        areaArbol.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaArbol.setLineWrap(false);
        areaArbol.setText("Procese un enunciado y elija una interpretación para ver el árbol.");

        selectorFilaArbol = new JComboBox<>();
        selectorFilaArbol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        selectorFilaArbol.setEnabled(false);
        selectorFilaArbol.addActionListener(e -> {
            if (!actualizandoSeleccion && selectorFilaArbol.getSelectedIndex() >= 0) {
                int fila = selectorFilaArbol.getSelectedIndex();
                sincronizarSeleccionTabla(fila);
                actualizarArbolFila(fila);
            }
        });

        etiquetaInterpretacionArbol = new JLabel(" ");
        etiquetaInterpretacionArbol.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        etiquetaInterpretacionArbol.setForeground(new Color(100, 100, 100));

        JPanel panelArbol = new JPanel(new java.awt.BorderLayout(0, 8));
        panelArbol.setOpaque(false);

        JPanel barraArbol = new JPanel();
        barraArbol.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        barraArbol.setOpaque(false);
        barraArbol.add(new JLabel("Interpretación:"));
        barraArbol.add(selectorFilaArbol);

        JPanel norteArbol = new JPanel();
        norteArbol.setLayout(new javax.swing.BoxLayout(norteArbol, javax.swing.BoxLayout.Y_AXIS));
        norteArbol.setOpaque(false);
        norteArbol.add(barraArbol);
        norteArbol.add(etiquetaInterpretacionArbol);

        panelArbol.add(norteArbol, java.awt.BorderLayout.NORTH);
        panelArbol.add(new JScrollPane(areaArbol), java.awt.BorderLayout.CENTER);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pestanas.addTab("Tabla", scrollTabla);
        pestanas.addTab("Árbol", panelArbol);

        tabla.getSelectionModel().addListSelectionListener(eventoSeleccionFila());

        add(encabezado, java.awt.BorderLayout.NORTH);
        add(pestanas, java.awt.BorderLayout.CENTER);
    }

    public void mostrarTabla(TablaVerdad.Resultado resultado, Map<String, String> proposiciones) {
        resultadoActual = resultado;
        formulaActual = resultado != null ? resultado.formula() : "";

        if (resultado == null) {
            limpiar();
            return;
        }

        etiquetaFormula.setText("<html><b>Fórmula:</b> " + escaparHtml(resultado.formula()) + "</html>");

        if (resultado.mensajeInformativo() != null && !resultado.mensajeInformativo().isBlank()) {
            etiquetaLeyenda.setText(resultado.mensajeInformativo());
            vaciarTablaYArbol(resultado.mensajeInformativo());
            return;
        }

        if (!resultado.tieneDatos()) {
            etiquetaLeyenda.setText("No hay datos para la tabla.");
            vaciarTablaYArbol("No hay datos para el árbol de evaluación.");
            return;
        }

        Map<String, String> simboloALema = invertirProposiciones(proposiciones);
        proposicionesActuales = simboloALema;
        etiquetaLeyenda.setText(formatearLeyenda(resultado.variables(), simboloALema));

        String[] columnas = new String[resultado.variables().size() + 1];
        for (int i = 0; i < resultado.variables().size(); i++) {
            String var = resultado.variables().get(i);
            columnas[i] = encabezadoVariable(var, simboloALema.get(var));
        }
        columnas[columnas.length - 1] = "Resultado";

        modelo.setColumnIdentifiers(columnas);
        modelo.setRowCount(0);

        actualizandoSeleccion = true;
        selectorFilaArbol.removeAllItems();
        int indice = 0;
        for (Fila fila : resultado.filas()) {
            Object[] celdas = new Object[columnas.length];
            for (int i = 0; i < fila.valoresVariables().length; i++) {
                celdas[i] = fila.valoresVariables()[i];
            }
            celdas[columnas.length - 1] = fila.resultadoFormula();
            modelo.addRow(celdas);
            selectorFilaArbol.addItem(etiquetaFilaCombo(indice, resultado.variables(), fila, simboloALema));
            indice++;
        }
        selectorFilaArbol.setEnabled(true);
        actualizandoSeleccion = false;

        for (int i = 0; i < columnas.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(i == columnas.length - 1 ? 120 : 90);
        }

        if (tabla.getRowCount() > 0) {
            int filaInicial = filaInicialSugerida(resultado);
            seleccionarFila(filaInicial);
        }
    }

    public void limpiar() {
        resultadoActual = null;
        formulaActual = "";
        proposicionesActuales = Map.of();
        etiquetaFormula.setText("Procese un enunciado para ver la tabla de verdad.");
        etiquetaLeyenda.setText(" ");
        etiquetaInterpretacionArbol.setText(" ");
        modelo.setRowCount(0);
        modelo.setColumnCount(0);
        actualizandoSeleccion = true;
        selectorFilaArbol.removeAllItems();
        selectorFilaArbol.setEnabled(false);
        actualizandoSeleccion = false;
        areaArbol.setText("Procese un enunciado y elija una interpretación para ver el árbol.");
    }

    private void vaciarTablaYArbol(String mensajeArbol) {
        etiquetaInterpretacionArbol.setText(" ");
        modelo.setRowCount(0);
        modelo.setColumnCount(0);
        actualizandoSeleccion = true;
        selectorFilaArbol.removeAllItems();
        selectorFilaArbol.setEnabled(false);
        actualizandoSeleccion = false;
        areaArbol.setText(mensajeArbol);
    }

    private ListSelectionListener eventoSeleccionFila() {
        return e -> {
            if (e.getValueIsAdjusting() || resultadoActual == null || !resultadoActual.tieneDatos()) {
                return;
            }
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                actualizandoSeleccion = true;
                selectorFilaArbol.setSelectedIndex(fila);
                actualizandoSeleccion = false;
                actualizarArbolFila(fila);
            }
        };
    }

    private void seleccionarFila(int indiceFila) {
        if (indiceFila < 0 || indiceFila >= tabla.getRowCount()) {
            return;
        }
        actualizandoSeleccion = true;
        tabla.setRowSelectionInterval(indiceFila, indiceFila);
        selectorFilaArbol.setSelectedIndex(indiceFila);
        actualizandoSeleccion = false;
        actualizarArbolFila(indiceFila);
    }

    private void sincronizarSeleccionTabla(int indiceFila) {
        if (indiceFila < 0 || indiceFila >= tabla.getRowCount()) {
            return;
        }
        actualizandoSeleccion = true;
        tabla.setRowSelectionInterval(indiceFila, indiceFila);
        actualizandoSeleccion = false;
    }

    private void actualizarArbolFila(int indiceFila) {
        if (resultadoActual == null || formulaActual.isBlank() || indiceFila < 0) {
            return;
        }
        List<Fila> filas = resultadoActual.filas();
        if (indiceFila >= filas.size()) {
            return;
        }

        Fila fila = filas.get(indiceFila);
        Map<String, Boolean> valores = TablaVerdad.valoresDeFila(resultadoActual.variables(), fila);
        var raiz = ConstructorArbolEvaluacion.construir(formulaActual, valores);
        areaArbol.setText(RenderizadorArbolEvaluacion.renderizar(raiz));
        areaArbol.setCaretPosition(0);

        etiquetaInterpretacionArbol.setText(
                describirValores(resultadoActual.variables(), fila, proposicionesActuales)
                        + " → resultado " + (fila.resultadoFormula() ? "V" : "F"));
    }

    private static String etiquetaFilaCombo(
            int numero,
            List<String> variables,
            Fila fila,
            Map<String, String> simboloALema
    ) {
        return "Fila " + (numero + 1) + " — " + describirValores(variables, fila, simboloALema)
                + " → " + (fila.resultadoFormula() ? "V" : "F");
    }

    private static int filaInicialSugerida(TablaVerdad.Resultado resultado) {
        List<Fila> filas = resultado.filas();
        for (int i = 0; i < filas.size(); i++) {
            if (filas.get(i).resultadoFormula()) {
                return i;
            }
        }
        return 0;
    }

    private static String describirValores(List<String> variables, Fila fila, Map<String, String> simboloALema) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < variables.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            String var = variables.get(i);
            String leyenda = simboloALema.getOrDefault(var, var).replace('_', ' ');
            sb.append(var).append(" (").append(leyenda).append(")=");
            sb.append(fila.valoresVariables()[i] ? "V" : "F");
        }
        return sb.toString();
    }

    private static String encabezadoVariable(String simbolo, String lema) {
        if (lema == null || lema.isBlank()) {
            return simbolo;
        }
        return simbolo + " (" + lema.replace('_', ' ') + ")";
    }

    private static String formatearLeyenda(List<String> variables, Map<String, String> simboloALema) {
        if (simboloALema.isEmpty()) {
            return "Pestaña Tabla: combinaciones V/F. Pestaña Árbol: elija la interpretación a evaluar.";
        }
        StringBuilder sb = new StringBuilder("Leyenda: ");
        for (int i = 0; i < variables.size(); i++) {
            if (i > 0) {
                sb.append(" · ");
            }
            String var = variables.get(i);
            String lema = simboloALema.getOrDefault(var, var);
            sb.append(var).append(" = ").append(lema.replace('_', ' '));
        }
        return sb.toString();
    }

    private static Map<String, String> invertirProposiciones(Map<String, String> proposiciones) {
        if (proposiciones == null || proposiciones.isEmpty()) {
            return Map.of();
        }
        Map<String, String> simboloALema = new LinkedHashMap<>();
        for (Map.Entry<String, String> e : proposiciones.entrySet()) {
            simboloALema.put(e.getValue(), e.getKey());
        }
        return simboloALema;
    }

    private static String escaparHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private final class CeldaValorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);

            if (value instanceof Boolean b) {
                setText(b ? "V" : "F");
                if (isSelected) {
                    return c;
                }
                if (b) {
                    setBackground(COLOR_VERDADERO);
                    setForeground(COLOR_VERDADERO_TEXTO);
                } else {
                    setBackground(COLOR_FALSO);
                    setForeground(COLOR_FALSO_TEXTO);
                }
                setOpaque(true);
            } else {
                setText(value != null ? value.toString() : "");
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            return c;
        }
    }
}
