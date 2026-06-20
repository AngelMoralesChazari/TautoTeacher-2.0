package tautoteacher2.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import tautoteacher2.core.logica.TablaVerdad;

/**
 * Panel «Visualización Clara»: tabla de verdad con columnas alineadas y colores V/F.
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
        tabla.setDefaultRenderer(Object.class, new CeldaValorRenderer());

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Tabla de verdad"));

        add(encabezado, java.awt.BorderLayout.NORTH);
        add(scroll, java.awt.BorderLayout.CENTER);
    }

    public void mostrarTabla(TablaVerdad.Resultado resultado, Map<String, String> proposiciones) {
        if (resultado == null) {
            limpiar();
            return;
        }

        etiquetaFormula.setText("<html><b>Fórmula:</b> " + escaparHtml(resultado.formula()) + "</html>");

        Map<String, String> simboloALema = invertirProposiciones(proposiciones);
        if (resultado.mensajeInformativo() != null && !resultado.mensajeInformativo().isBlank()) {
            etiquetaLeyenda.setText(resultado.mensajeInformativo());
            modelo.setRowCount(0);
            modelo.setColumnCount(0);
            return;
        }

        if (!resultado.tieneDatos()) {
            etiquetaLeyenda.setText("No hay datos para la tabla.");
            modelo.setRowCount(0);
            modelo.setColumnCount(0);
            return;
        }

        etiquetaLeyenda.setText(formatearLeyenda(resultado.variables(), simboloALema));

        String[] columnas = new String[resultado.variables().size() + 1];
        for (int i = 0; i < resultado.variables().size(); i++) {
            String var = resultado.variables().get(i);
            columnas[i] = encabezadoVariable(var, simboloALema.get(var));
        }
        columnas[columnas.length - 1] = "Resultado";

        modelo.setColumnIdentifiers(columnas);
        modelo.setRowCount(0);

        for (TablaVerdad.Fila fila : resultado.filas()) {
            Object[] celdas = new Object[columnas.length];
            for (int i = 0; i < fila.valoresVariables().length; i++) {
                celdas[i] = fila.valoresVariables()[i];
            }
            celdas[columnas.length - 1] = fila.resultadoFormula();
            modelo.addRow(celdas);
        }

        for (int i = 0; i < columnas.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(i == columnas.length - 1 ? 120 : 90);
        }
    }

    public void limpiar() {
        etiquetaFormula.setText("Procese un enunciado para ver la tabla de verdad.");
        etiquetaLeyenda.setText(" ");
        modelo.setRowCount(0);
        modelo.setColumnCount(0);
    }

    private static String encabezadoVariable(String simbolo, String lema) {
        if (lema == null || lema.isBlank()) {
            return simbolo;
        }
        return simbolo + " (" + lema.replace('_', ' ') + ")";
    }

    private static String formatearLeyenda(java.util.List<String> variables, Map<String, String> simboloALema) {
        if (simboloALema.isEmpty()) {
            return "Filas: todas las combinaciones de valores V (verdadero) y F (falso).";
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

    private static final class CeldaValorRenderer extends DefaultTableCellRenderer {
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
