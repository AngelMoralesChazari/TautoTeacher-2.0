package tautoteacher2.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

public class PanelEntradaNatural extends JPanel {
    public enum ModoEntrada {
        FORMULA,
        LENGUAJE_NATURAL
    }

    private static final int IDX_FORMULA = 0;
    private static final int IDX_LN = 1;

    private final JTabbedPane pestanas;
    private final JTextArea areaFormula;
    private JTextArea areaLenguajeNatural;
    private final JButton botonProcesar;

    public PanelEntradaNatural() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Entrada"),
                BorderFactory.createEmptyBorder(10, 8, 10, 10)
        ));
        setOpaque(false);

        TitledBorder tbExterno = (TitledBorder) ((CompoundBorder) getBorder()).getOutsideBorder();
        tbExterno.setTitleFont(new Font("Segoe UI", Font.PLAIN, 14));

        pestanas = new JTabbedPane();
        pestanas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pestanas.setAlignmentX(Component.LEFT_ALIGNMENT);

        areaFormula = new JTextArea();
        configurarAreaTexto(areaFormula, new Font("Segoe UI Symbol", Font.PLAIN, 14));

        JPanel panelFormula = construirPanelFormula();
        JPanel panelLn = construirPanelLenguajeNatural();

        pestanas.addTab("Fórmula lógica", panelFormula);
        pestanas.addTab("Lenguaje natural", panelLn);

        botonProcesar = new JButton("✔ Verificar Tautología");
        botonProcesar.setBackground(new Color(74, 111, 165));
        botonProcesar.setForeground(Color.BLACK);
        botonProcesar.setFont(new Font("Dialog", Font.BOLD, 14));
        botonProcesar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filaBoton.setOpaque(false);
        filaBoton.setAlignmentX(Component.LEFT_ALIGNMENT);
        filaBoton.add(botonProcesar);

        add(pestanas);
        add(Box.createRigidArea(new Dimension(0, 12)));
        add(filaBoton);
    }

    private JPanel construirPanelFormula() {
        JPanel raiz = new JPanel();
        raiz.setLayout(new BoxLayout(raiz, BoxLayout.Y_AXIS));
        raiz.setOpaque(false);
        raiz.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JPanel panelSimbolos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelSimbolos.setBorder(BorderFactory.createTitledBorder("Símbolos lógicos"));
        panelSimbolos.setOpaque(false);
        panelSimbolos.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        JScrollPane scroll = new JScrollPane(areaFormula);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setPreferredSize(new Dimension(700, 150));

        raiz.add(panelSimbolos);
        raiz.add(Box.createRigidArea(new Dimension(0, 10)));
        raiz.add(scroll);
        return raiz;
    }

    private JPanel construirPanelLenguajeNatural() {
        JPanel raiz = new JPanel();
        raiz.setLayout(new BoxLayout(raiz, BoxLayout.Y_AXIS));
        raiz.setOpaque(false);
        raiz.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JLabel ayuda = new JLabel("<html><body style='width:680px'>"
                + "Escriba su enunciado en español. Más adelante se traducirá a fórmula y se "
                + "evaluará si corresponde a una tautología."
                + "</body></html>");
        ayuda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ayuda.setForeground(new Color(80, 80, 80));
        ayuda.setAlignmentX(Component.LEFT_ALIGNMENT);

        areaLenguajeNatural = new JTextArea();
        configurarAreaTexto(areaLenguajeNatural, new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollLn = new JScrollPane(areaLenguajeNatural);
        scrollLn.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollLn.setPreferredSize(new Dimension(700, 150));

        raiz.add(ayuda);
        raiz.add(Box.createRigidArea(new Dimension(0, 8)));
        raiz.add(scrollLn);
        return raiz;
    }

    private static void configurarAreaTexto(JTextArea area, Font fuente) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(fuente);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    public ModoEntrada getModoEntrada() {
        return pestanas.getSelectedIndex() == IDX_LN ? ModoEntrada.LENGUAJE_NATURAL : ModoEntrada.FORMULA;
    }

    public String getTexto() {
        return getModoEntrada() == ModoEntrada.FORMULA ? areaFormula.getText() : areaLenguajeNatural.getText();
    }

    public String getTextoFormula() {
        return areaFormula.getText();
    }

    public String getTextoLenguajeNatural() {
        return areaLenguajeNatural.getText();
    }

    public void setProcesarListener(ActionListener listener) {
        botonProcesar.addActionListener(listener);
    }
}
