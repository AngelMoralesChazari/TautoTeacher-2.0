package main.java.tautoteacher2.ui;

import javax.swing.*;
import java.awt.*;

public class PanelResultadoLogico extends JPanel {

    private JTextArea areaResultado;

    public PanelResultadoLogico() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Resultado lógico"));

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaResultado);

        add(scroll, BorderLayout.CENTER);
    }

    public void setResultado(String texto) {
        areaResultado.setText(texto);
    }
}