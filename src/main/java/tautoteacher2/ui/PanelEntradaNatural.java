package main.java.tautoteacher2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelEntradaNatural extends JPanel {

    private JTextArea areaTexto;
    private JButton botonProcesar;

    public PanelEntradaNatural() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Entrada en lenguaje natural"));

        areaTexto = new JTextArea(5, 80);
        JScrollPane scroll = new JScrollPane(areaTexto);

        botonProcesar = new JButton("Procesar");

        // Panel para el botón
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(botonProcesar);

        add(scroll, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    public String getTexto() {
        return areaTexto.getText();
    }

    public void setProcesarListener(ActionListener listener) {
        botonProcesar.addActionListener(listener);
    }
}