package tautoteacher2.ui;

import javax.swing.*;
import java.awt.*;

public class PanelResultadoLogico extends JPanel {

    private final JLabel etiquetaIcono;
    private final JTextArea areaResultado;

    public PanelResultadoLogico() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Resultado"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setOpaque(false);

        etiquetaIcono = new JLabel();
        etiquetaIcono.setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
        etiquetaIcono.setAlignmentX(Component.LEFT_ALIGNMENT);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        areaResultado.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(etiquetaIcono);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(scroll);
    }

    public void setResultado(String texto) {
        areaResultado.setText(texto);
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