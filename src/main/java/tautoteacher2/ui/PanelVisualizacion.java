package main.java.tautoteacher2.ui;

import javax.swing.*;
import java.awt.*;

public class PanelVisualizacion extends JPanel {

    private JLabel etiqueta;

    public PanelVisualizacion() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Visualización"));

        etiqueta = new JLabel("Aquí se mostrará la visualización");
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);

        add(etiqueta, BorderLayout.CENTER);
    }
}