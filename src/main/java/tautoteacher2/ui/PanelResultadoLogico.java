package tautoteacher2.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class PanelResultadoLogico extends JPanel {

    private static final int ALTURA_MINIMA_RESPUESTA = 200;
    private static final int FILAS_AREA_TEXTO = 8;

    private final JLabel etiquetaIcono;
    private final JTextArea areaResultado;
    private final JScrollPane scrollResultado;

    public PanelResultadoLogico() {
        super(new BorderLayout(12, 0));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Resultado"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setOpaque(false);

        etiquetaIcono = new JLabel();
        etiquetaIcono.setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
        etiquetaIcono.setVerticalAlignment(SwingConstants.TOP);
        etiquetaIcono.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaIcono.setPreferredSize(new Dimension(44, 44));
        etiquetaIcono.setMinimumSize(new Dimension(44, 44));

        JPanel columnaIcono = new JPanel(new BorderLayout());
        columnaIcono.setOpaque(false);
        columnaIcono.add(etiquetaIcono, BorderLayout.NORTH);
        add(columnaIcono, BorderLayout.WEST);

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
        scrollResultado.setPreferredSize(new Dimension(400, ALTURA_MINIMA_RESPUESTA));
        scrollResultado.setMinimumSize(new Dimension(200, ALTURA_MINIMA_RESPUESTA));
        add(scrollResultado, BorderLayout.CENTER);
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
