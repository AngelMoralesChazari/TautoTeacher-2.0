package tautoteacher2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelEntradaNatural extends JPanel {

    private final JTextArea areaTexto;
    private final JButton botonProcesar;

    public PanelEntradaNatural() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Ingrese Su Expresión Lógica"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setOpaque(false);

        areaTexto = new JTextArea();
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        areaTexto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JPanel panelSimbolos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelSimbolos.setBorder(BorderFactory.createTitledBorder("Símbolos Lógicos"));
        panelSimbolos.setOpaque(false);

        String[] simbolos = {"¬", "∧", "∨", "→", "↔", "(", ")"};
        for (String simbolo : simbolos) {
            JButton botonSimbolo = new JButton(simbolo);
            botonSimbolo.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            botonSimbolo.setFocusable(false);
            botonSimbolo.addActionListener(e -> {
                int pos = areaTexto.getCaretPosition();
                areaTexto.insert(simbolo, pos);
                areaTexto.requestFocus();
                areaTexto.setCaretPosition(pos + simbolo.length());
            });
            panelSimbolos.add(botonSimbolo);
        }

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setPreferredSize(new Dimension(700, 150));

        botonProcesar = new JButton("✔ Verificar Tautología");
        botonProcesar.setBackground(new Color(74, 111, 165));
        botonProcesar.setForeground(Color.BLACK);
        botonProcesar.setFont(new Font("Dialog", Font.BOLD, 14));
        botonProcesar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        botonProcesar.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(panelSimbolos);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scroll);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(botonProcesar);
    }

    public String getTexto() {
        return areaTexto.getText();
    }

    public void setProcesarListener(ActionListener listener) {
        botonProcesar.addActionListener(listener);
    }
}