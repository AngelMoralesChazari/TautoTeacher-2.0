package tautoteacher2.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class PanelEntradaNatural extends JPanel {
    public enum ModoEntrada {
        FORMULA,
        LENGUAJE_NATURAL
    }

    private final JTextArea areaFormula;
    private final JButton botonProcesar;

    public PanelEntradaNatural() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Ingrese Su Expresión Lógica"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setOpaque(false);

        // Fuente del título del borde
        TitledBorder tbExterno = (TitledBorder) ((javax.swing.border.CompoundBorder) getBorder()).getOutsideBorder();
        tbExterno.setTitleFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Panel de símbolos 
        JPanel panelSimbolos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelSimbolos.setBorder(BorderFactory.createTitledBorder("Símbolos Lógicos"));
        panelSimbolos.setOpaque(false);
        panelSimbolos.setAlignmentX(Component.LEFT_ALIGNMENT);

        areaFormula = new JTextArea();
        areaFormula.setLineWrap(true);
        areaFormula.setWrapStyleWord(true);
        areaFormula.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        areaFormula.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        String[] simbolos = {"¬", "∧", "∨", "→", "↔", "(", ")"};
        for (String simbolo : simbolos) {
            JButton botonSimbolo = new JButton(simbolo);
            botonSimbolo.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            botonSimbolo.setFocusable(false);
            botonSimbolo.addActionListener(e -> {
                int pos = areaFormula.getCaretPosition();
                areaFormula.insert(simbolo, pos);
                areaFormula.requestFocus();
                areaFormula.setCaretPosition(pos + simbolo.length());
            });
            panelSimbolos.add(botonSimbolo);
        }

        // Scroll del área de texto 
        JScrollPane scroll = new JScrollPane(areaFormula);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setPreferredSize(new Dimension(700, 150));

        // Botón verificar
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

    public ModoEntrada getModoEntrada() {
        return ModoEntrada.FORMULA;
    }

    public String getTexto() {
        return areaFormula.getText();
    }

    public String getTextoFormula() {
        return areaFormula.getText();
    }
    
    public String getTextoLenguajeNatural() {
        return "";
    }

    public void setProcesarListener(ActionListener listener) {
        botonProcesar.addActionListener(listener);
    }
}