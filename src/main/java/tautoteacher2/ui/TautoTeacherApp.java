package main.java.tautoteacher2.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TautoTeacherApp {

    private VentanaPrincipal ventana;
    private PanelEntradaNatural panelEntrada;
    private PanelResultadoLogico panelResultado;

    public void iniciar() {
        ventana = new VentanaPrincipal();

        // Obtener referencias a los paneles
        panelEntrada = ventana.getPanelEntradaNatural();
        panelResultado = ventana.getPanelResultadoLogico();

        // Agregar listener al botón procesar
        panelEntrada.setProcesarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String texto = panelEntrada.getTexto();
                // Simulación de procesamiento
                String resultadoSimulado = "Procesado: " + texto.toUpperCase();
                panelResultado.setResultado(resultadoSimulado);
            }
        });

        ventana.setVisible(true);
        System.out.println("Ventana principal mostrada.");
    }
}