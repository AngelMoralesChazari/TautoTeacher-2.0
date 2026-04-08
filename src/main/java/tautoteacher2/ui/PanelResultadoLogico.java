package tautoteacher2.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Resultado al estilo TautoTeacher original: icono centrado arriba, texto debajo; bloque más compacto que el área de fórmula.
 */
public class PanelResultadoLogico extends JPanel {

    /** Menos filas que la entrada: el panel Resultado debe ocupar menos altura vertical. */
    private static final int FILAS_AREA_TEXTO = 4;
    private static final int ALTURA_PREFERIDA_SCROLL = 110;

    private final JLabel etiquetaIcono;
    private final JTextArea areaResultado;
    private final JScrollPane scrollResultado;

    public PanelResultadoLogico() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Resultado"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setOpaque(false);

        etiquetaIcono = new JLabel();
        etiquetaIcono.setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
        etiquetaIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel filaIcono = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filaIcono.setOpaque(false);
        filaIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        filaIcono.add(etiquetaIcono);

        areaResultado = new JTextArea(FILAS_AREA_TEXTO, 40);
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        areaResultado.setBackground(Color.WHITE);
        Border bordeArea = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );
        areaResultado.setBorder(bordeArea);

        scrollResultado = new JScrollPane(areaResultado);
        scrollResultado.setBorder(BorderFactory.createEmptyBorder());
        scrollResultado.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollResultado.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollResultado.setPreferredSize(new Dimension(400, ALTURA_PREFERIDA_SCROLL));
        scrollResultado.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(filaIcono);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(scrollResultado);
    }

    public void setResultado(String texto) {
        setResultado(texto, Color.BLACK);
    }

    public void setResultado(String texto, Color color) {
        areaResultado.setText(texto);
        areaResultado.setForeground(color != null ? color : Color.BLACK);
        areaResultado.setCaretPosition(0);
        scrollResultado.getVerticalScrollBar().setValue(0);
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
