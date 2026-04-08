package tautoteacher2.ui;

import java.awt.*;
import javax.swing.*;

public class PanelResultadoLogico extends JPanel {

    private final JLabel etiquetaIcono;
    private final JTextArea areaResultado;

    public PanelResultadoLogico() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Resultado"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setOpaque(false);

        etiquetaIcono = new JLabel();
        etiquetaIcono.setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
        etiquetaIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        // Segoe UI Symbol dibuja ∧∨→↔¬; Segoe UI plano suele mostrar □ en esos caracteres.
        areaResultado.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        areaResultado.setBackground(Color.WHITE);
        areaResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        areaResultado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        add(etiquetaIcono);
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
        etiquetaIcono.setText("");
    }

    public void setEstado(boolean exito) {
        if (exito) {
            etiquetaIcono.setText("✔");
            etiquetaIcono.setForeground(new Color(40, 167, 69));
        } else {
            etiquetaIcono.setText("✘");
            etiquetaIcono.setForeground(new Color(220, 53, 69));
        }
    }
}
