package tautoteacher2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaPrincipal extends JFrame {

    private final PanelEntradaNatural panelEntrada;
    private final PanelResultadoLogico panelResultado;
    private final PanelVisualizacion panelVisualizacion;
    private final CardLayout layoutSecciones;
    private final JPanel contenedorSecciones;

    public VentanaPrincipal() {
        super("TautoTeacher - Logica Proposicional");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 650));
        setSize(1200, 800);
        setLocationRelativeTo(null);

        panelEntrada = new PanelEntradaNatural();
        panelResultado = new PanelResultadoLogico();
        panelVisualizacion = new PanelVisualizacion();
        layoutSecciones = new CardLayout();
        contenedorSecciones = new JPanel(layoutSecciones);
        contenedorSecciones.setOpaque(false);

        Color colorPrimario = new Color(74, 111, 165);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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

    private JPanel construirEncabezadoYTarjetas(Color colorPrimario) {
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setOpaque(false);

        JLabel titulo = new JLabel("TautoTeacher");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titulo.setForeground(colorPrimario);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Herramienta para la verificación de tautologías lógicas");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 15, 0));
        tarjetas.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        tarjetas.setOpaque(false);
        tarjetas.add(crearTarjeta("Análisis Rápido", "Verifica expresiones lógicas en tiempo real", "analisis"));
        tarjetas.add(crearTarjeta("Visualización Clara", "Resultados presentados de forma intuitiva", "visualizacion"));
        tarjetas.add(crearTarjeta("Educativo", "Perfecto para estudiantes de lógica", "educativo"));

        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.setOpaque(false);
        norte.add(titulo);
        norte.add(Box.createRigidArea(new Dimension(0, 8)));
        norte.add(subtitulo);
        norte.add(Box.createRigidArea(new Dimension(0, 12)));
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
                "Modo Fórmula: use ∧, ∨, ¬, →, ↔ y paréntesis; los botones insertan símbolos en el área de texto.",
                "Modo LN: escriba enunciados en español; el paso de traducción a fórmula se integrará con el motor en el siguiente desarrollo.",
                "Ejemplos de fórmula: \"p → q\", \"¬(p ∧ ¬q)\", \"(p ∨ q) ↔ (q ∨ p)\""
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
        panelCentral.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEntrada.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInstrucciones.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        JTextArea areaEducativa = new JTextArea();
        areaEducativa.setEditable(false);
        areaEducativa.setLineWrap(true);
        areaEducativa.setWrapStyleWord(true);
        areaEducativa.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        areaEducativa.setText("Aquí se mostrará la explicación educativa.");
        panelEducativo.add(new JScrollPane(areaEducativa), BorderLayout.CENTER);
        return panelEducativo;
    }
}