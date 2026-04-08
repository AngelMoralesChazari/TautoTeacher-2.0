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
    private final JTextArea areaLenguajeNatural;
    private final JRadioButton radioFormula;
    private final JRadioButton radioLn;
    private final CardLayout layoutTarjetasEntrada;
    private final JPanel panelContenidoPorModo;
    private final JButton botonProcesar;

    public PanelEntradaNatural() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Entrada"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setOpaque(false);

        TitledBorder tbExterno = (TitledBorder) ((javax.swing.border.CompoundBorder) getBorder()).getOutsideBorder();
        tbExterno.setTitleFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel panelSelector = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelSelector.setOpaque(false);
        panelSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
        radioFormula = new JRadioButton("Fórmula Lógica", true);
        radioLn = new JRadioButton("Lenguaje Natural");
        radioFormula.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radioLn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radioFormula.setOpaque(false);
        radioLn.setOpaque(false);
        ButtonGroup grupoModo = new ButtonGroup();
        grupoModo.add(radioFormula);
        grupoModo.add(radioLn);
        panelSelector.add(radioFormula);
        panelSelector.add(radioLn);

        layoutTarjetasEntrada = new CardLayout();
        panelContenidoPorModo = new JPanel(layoutTarjetasEntrada);
        panelContenidoPorModo.setOpaque(false);
        panelContenidoPorModo.setAlignmentX(Component.LEFT_ALIGNMENT);

        areaFormula = new JTextArea();
        configurarAreaTexto(areaFormula);
        JPanel capaFormula = construirCapaFormula();

        areaLenguajeNatural = new JTextArea();
        configurarAreaTexto(areaLenguajeNatural);
        areaLenguajeNatural.setToolTipText("Escriba aquí su enunciado");
        JPanel capaLn = construirCapaLenguajeNatural();

        panelContenidoPorModo.add(capaFormula, "formula");
        panelContenidoPorModo.add(capaLn, "ln");

        radioFormula.addActionListener(e -> {
            if (radioFormula.isSelected()) {
                layoutTarjetasEntrada.show(panelContenidoPorModo, "formula");
            }
        });
        radioLn.addActionListener(e -> {
            if (radioLn.isSelected()) {
                layoutTarjetasEntrada.show(panelContenidoPorModo, "ln");
            }
        });

        botonProcesar = new JButton("✔ Verificar Tautología");
        botonProcesar.setBackground(new Color(74, 111, 165));
        botonProcesar.setForeground(Color.BLACK);
        botonProcesar.setFont(new Font("Dialog", Font.BOLD, 14));
        botonProcesar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        botonProcesar.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(panelSelector);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(panelContenidoPorModo);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(botonProcesar);
    }

    private void configurarAreaTexto(JTextArea area) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private JPanel construirCapaFormula() {
        JPanel capa = new JPanel();
        capa.setLayout(new BoxLayout(capa, BoxLayout.Y_AXIS));
        capa.setAlignmentX(Component.LEFT_ALIGNMENT);
        capa.setOpaque(false);
        capa.setBorder(BorderFactory.createTitledBorder("Expresión en lógica proposicional"));

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

        areaFormula.setRows(5);
        areaFormula.setColumns(40);

        JScrollPane scroll = new JScrollPane(areaFormula);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setPreferredSize(new Dimension(700, 150));

        capa.add(panelSimbolos);
        capa.add(Box.createRigidArea(new Dimension(0, 5)));
        capa.add(scroll);
        return capa;
    }

    private JPanel construirCapaLenguajeNatural() {
        JPanel capa = new JPanel(new BorderLayout(0, 8));
        capa.setAlignmentX(Component.LEFT_ALIGNMENT);
        capa.setOpaque(false);
        capa.setBorder(BorderFactory.createTitledBorder("Enunciado"));

        JLabel ayuda = new JLabel(
                "<html><body style='width: 640px;'>Escriba su enunciado.</body></html>"
        );
        ayuda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ayuda.setForeground(new Color(80, 80, 80));

        areaLenguajeNatural.setRows(5);
        JScrollPane scroll = new JScrollPane(areaLenguajeNatural);
        scroll.setPreferredSize(new Dimension(700, 150));

        capa.add(ayuda, BorderLayout.NORTH);
        capa.add(scroll, BorderLayout.CENTER);
        return capa;
    }

    public ModoEntrada getModoEntrada() {
        return radioLn.isSelected() ? ModoEntrada.LENGUAJE_NATURAL : ModoEntrada.FORMULA;
    }

    /**
     * Texto activo según el modo: fórmula o enunciado LN.
     */
    public String getTexto() {
        return getModoEntrada() == ModoEntrada.FORMULA
                ? areaFormula.getText()
                : areaLenguajeNatural.getText();
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
