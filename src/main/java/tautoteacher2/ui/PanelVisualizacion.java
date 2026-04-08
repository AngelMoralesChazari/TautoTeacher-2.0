package tautoteacher2.ui;

import javax.swing.*;
import java.awt.*;

public class PanelVisualizacion extends JPanel {

    private final JTextArea areaVisualizacion;

    public PanelVisualizacion() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Visualización"));
        setOpaque(false);

        areaVisualizacion = new JTextArea();
        areaVisualizacion.setEditable(false);
        areaVisualizacion.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        areaVisualizacion.setText("Aquí se mostrará la visualización");

        add(new JScrollPane(areaVisualizacion), BorderLayout.CENTER);
    }

    public void setVisualizacion(String texto) {
        areaVisualizacion.setText(texto);
    }
}