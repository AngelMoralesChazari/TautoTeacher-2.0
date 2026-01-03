package main.java.tautoteacher2.ui;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private PanelEntradaNatural panelEntrada;
    private PanelResultadoLogico panelResultado;
    private PanelVisualizacion panelVisualizacion;

    public VentanaPrincipal() {
        super("TautoTeacher 2.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        panelEntrada = new PanelEntradaNatural();
        panelResultado = new PanelResultadoLogico();
        panelVisualizacion = new PanelVisualizacion();

        setLayout(new BorderLayout());

        add(panelEntrada, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(1, 2));
        panelCentro.add(panelResultado);
        panelCentro.add(panelVisualizacion);

        add(panelCentro, BorderLayout.CENTER);
    }

    public PanelEntradaNatural getPanelEntradaNatural() {
        return panelEntrada;
    }

    public PanelResultadoLogico getPanelResultadoLogico() {
        return panelResultado;
    }

    public PanelVisualizacion getPanelVisualizacion() {
        return panelVisualizacion;
    }
}