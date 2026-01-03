package main.java.tautoteacher2.ui;

import javax.swing.*;
import java.awt.*;

public class PanelEntradaNatural extends JPanel {

    private JTextArea areaTexto;

    public PanelEntradaNatural() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Entrada en lenguaje natural"));

        areaTexto = new JTextArea(5, 80);
        JScrollPane scroll = new JScrollPane(areaTexto);

        add(scroll, BorderLayout.CENTER);
    }

    public String getTexto() {
        return areaTexto.getText();
    }
}