package tautoteacher2.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class PanelResultadoLogico extends JPanel {

    private static final Color EXITO = new Color(40, 167, 69);
    private static final Color EXITO_FONDO = new Color(220, 252, 231);
    private static final Color ERROR = new Color(220, 53, 69);
    private static final Color ERROR_FONDO = new Color(254, 226, 226);
    private static final Color ADVERTENCIA = new Color(200, 120, 0);
    private static final Color ADVERTENCIA_FONDO = new Color(254, 243, 199);

    private final InsigniaDictamen insignia;
    private final JTextArea areaResultado;

    public PanelResultadoLogico() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Resultado"),
                BorderFactory.createEmptyBorder(
                        UiEscalado.escalar(8),
                        UiEscalado.escalar(8),
                        UiEscalado.escalar(8),
                        UiEscalado.escalar(8))));
        setOpaque(false);

        if (getBorder() instanceof javax.swing.border.CompoundBorder cb
                && cb.getOutsideBorder() instanceof TitledBorder tb) {
            tb.setTitleFont(UiEscalado.fuente("Segoe UI", java.awt.Font.PLAIN, 14));
        }

        insignia = new InsigniaDictamen();
        insignia.setAlignmentX(Component.CENTER_ALIGNMENT);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(UiEscalado.fuente("Segoe UI Symbol", java.awt.Font.PLAIN, 16));
        areaResultado.setBackground(Color.WHITE);
        areaResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        areaResultado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(
                        UiEscalado.escalar(5),
                        UiEscalado.escalar(5),
                        UiEscalado.escalar(5),
                        UiEscalado.escalar(5))));

        add(insignia);
        add(Box.createRigidArea(new Dimension(0, UiEscalado.escalar(4))));
        add(areaResultado);
    }

    public void setResultado(String texto) {
        setResultado(texto, Color.BLACK);
    }

    public void setResultado(String texto, Color color) {
        areaResultado.setText(texto);
        areaResultado.setForeground(color != null ? color : Color.BLACK);
        areaResultado.setCaretPosition(0);
        revalidate();
        repaint();
    }

    public void limpiarIcono() {
        insignia.limpiar();
    }

    public void setEstado(boolean exito) {
        if (exito) {
            insignia.aplicar("\u2714", EXITO_FONDO, EXITO);
        } else {
            insignia.aplicar("\u2718", ERROR_FONDO, ERROR);
        }
    }

    public void setDictamen(String tipo) {
        if (tipo == null) {
            limpiarIcono();
            return;
        }
        switch (tipo.toUpperCase()) {
            case "TAUTOLOGÍA" -> insignia.aplicar("\u2714", EXITO_FONDO, EXITO);
            case "CONTRADICCIÓN" -> insignia.aplicar("\u2718", ERROR_FONDO, ERROR);
            default -> insignia.aplicar("\u25CB", ADVERTENCIA_FONDO, ADVERTENCIA);
        }
    }
}
