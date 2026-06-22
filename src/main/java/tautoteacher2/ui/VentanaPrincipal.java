package tautoteacher2.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    private final PanelEntradaNatural panelEntrada;
    private final PanelResultadoLogico panelResultado;
    private final PanelVisualizacion panelVisualizacion;
    private JTextArea areaExplicacionEducativa;
    private final CardLayout layoutSecciones;
    private final JPanel contenedorSecciones;

    public VentanaPrincipal() {
        super("TautoTeacher - Logica Proposicional");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(650, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        panelEntrada = new PanelEntradaNatural();
        panelResultado = new PanelResultadoLogico();
        panelVisualizacion = new PanelVisualizacion();
        layoutSecciones = new CardLayout();
        contenedorSecciones = new JPanel(layoutSecciones);
        contenedorSecciones.setOpaque(false);

        Color colorPrimario = new Color(74, 111, 165);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(16, 8, 0, 16));
        panelPrincipal.setBackground(new Color(248, 249, 250));

        JPanel panelNorte = construirEncabezadoYTarjetas(colorPrimario);
        JPanel panelAnalisis = construirPanelAnalisis();
        JPanel panelEducativo = construirPanelEducativo();

        contenedorSecciones.add(panelAnalisis, "analisis");
        contenedorSecciones.add(panelVisualizacion, "visualizacion");
        contenedorSecciones.add(panelEducativo, "educativo");

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);
        panelPrincipal.add(contenedorSecciones, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
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

    public void mostrarSeccion(String idSeccion) {
        layoutSecciones.show(contenedorSecciones, idSeccion);
    }

    /** Actualiza el panel «Explicación» (pasos de traducción LogicScript). */
    public void setContenidoEducativo(String texto) {
        areaExplicacionEducativa.setText(texto != null ? texto : "");
        areaExplicacionEducativa.setCaretPosition(0);
    }

    public void limpiarContenidoEducativo() {
        setContenidoEducativo("Procese un enunciado para ver la traducción y la demostración educativa.");
        panelVisualizacion.limpiar();
    }

    private JPanel construirEncabezadoYTarjetas(Color colorPrimario) {
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setOpaque(false);

        JPanel filaTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        filaTitulo.setOpaque(false);

        JLabel titulo = new JLabel("TautoTeacher");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(colorPrimario);
        filaTitulo.add(titulo);

        JLabel subtitulo = new JLabel("Herramienta para la verificación de tautologías lógicas");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setOpaque(false);

        encabezado.add(filaTitulo);
        encabezado.add(Box.createRigidArea(new Dimension(0, 10)));
        encabezado.add(subtitulo);

        // Tarjetas de navegación
        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 15, 0));
        tarjetas.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        tarjetas.setPreferredSize(new Dimension(0, 120));
        tarjetas.setOpaque(false);
        tarjetas.add(crearTarjeta("Análisis Rápido", "Verifica expresiones lógicas en tiempo real", "analisis"));
        tarjetas.add(crearTarjeta("Visualización Clara", "Pestañas Tabla de verdad y Árbol de evaluación", "visualizacion"));
        tarjetas.add(crearTarjeta("Explicación", "Traducción y demostración educativa", "educativo"));

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.setOpaque(false);
        norte.add(encabezado);
        norte.add(Box.createRigidArea(new Dimension(0, 10)));
        norte.add(tarjetas);
        return norte;
    }

    private JPanel crearTarjeta(String titulo, String descripcion, String seccion) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        tarjeta.setBackground(Color.WHITE);

        JLabel etiquetaTitulo = new JLabel(titulo);
        etiquetaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        etiquetaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel etiquetaDescripcion = new JLabel(
                "<html><div style='text-align:center;width:220px;'>" + descripcion + "</div></html>",
                SwingConstants.CENTER
        );
        etiquetaDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        etiquetaDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(etiquetaTitulo);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 6)));
        tarjeta.add(etiquetaDescripcion);

        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                layoutSecciones.show(contenedorSecciones, seccion);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBackground(new Color(230, 240, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBackground(Color.WHITE);
            }
        });

        return tarjeta;
    }

    private JPanel construirPanelAnalisis() {
        JPanel panelInstrucciones = new JPanel();
        panelInstrucciones.setLayout(new BoxLayout(panelInstrucciones, BoxLayout.Y_AXIS));
        panelInstrucciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Instrucciones"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panelInstrucciones.setOpaque(false);

        String[] instrucciones = {
            "Fórmula lógica: símbolos ∧ ∨ ¬ → ↔ y paréntesis; ejemplos: \"p → q\", \"¬(p ∧ ¬q)\".",
            "Lenguaje natural: escriba su enunciado en español.",
            "Resultado: explicación breve y dictamen.",
            "Visualización Clara: pestaña Tabla (V/F con colores) y pestaña Árbol (por interpretación).",
            "Explicación: traducción, pasos LogicScript y demostración educativa."
        };
        for (String instruccion : instrucciones) {
            JLabel etiqueta = new JLabel("• " + instruccion);
            etiqueta.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            panelInstrucciones.add(etiqueta);
            panelInstrucciones.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelEntrada.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelResultado.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelInstrucciones.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelCentral.add(panelEntrada);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        panelCentral.add(panelResultado);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        panelCentral.add(panelInstrucciones);

        JPanel panelAnalisis = new JPanel(new BorderLayout());
        panelAnalisis.setOpaque(false);
        panelAnalisis.add(panelCentral, BorderLayout.CENTER);
        return panelAnalisis;
    }

    private JPanel construirPanelEducativo() {
        JPanel panelEducativo = new JPanel(new BorderLayout());
        panelEducativo.setOpaque(false);
        panelEducativo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        areaExplicacionEducativa = new JTextArea();
        areaExplicacionEducativa.setEditable(false);
        areaExplicacionEducativa.setLineWrap(true);
        areaExplicacionEducativa.setWrapStyleWord(true);
        areaExplicacionEducativa.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 15));
        areaExplicacionEducativa.setText(
                "Procese un enunciado en lenguaje natural para ver los pasos de traducción.");

        JScrollPane scroll = new JScrollPane(areaExplicacionEducativa);
        scroll.setBorder(BorderFactory.createTitledBorder("Explicación educativa"));

        panelEducativo.add(scroll, BorderLayout.CENTER);
        return panelEducativo;
    }
}