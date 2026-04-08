package tautoteacher2.ui;

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
        panelEntrada.setProcesarListener(e -> {
            String texto = panelEntrada.getTexto();
            String modo = panelEntrada.getModoEntrada() == PanelEntradaNatural.ModoEntrada.FORMULA
                    ? "Fórmula lógica"
                    : "Lenguaje Natural";
            String resultadoSimulado = "[" + modo + "] Procesado: " + texto.toUpperCase();
            panelResultado.setResultado(resultadoSimulado);
        });

        ventana.setVisible(true);
        System.out.println("Ventana principal mostrada.");
    }
}