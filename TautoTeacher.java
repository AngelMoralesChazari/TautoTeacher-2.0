import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class TautoTeacher {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        class FondoPanel extends JPanel {
            private Image imagen;

            public FondoPanel(String rutaImagen) {
                this.imagen = new ImageIcon(rutaImagen).getImage();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagen != null) {
                    g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
                }
            }
        }

        // Ventana principal
        JFrame frame = new JFrame("TautoTeacher - Logica Proposicional");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);
        frame.setMinimumSize(new java.awt.Dimension(650, 600));

        // Colores
        java.awt.Color colorPrimario = new java.awt.Color(74, 111, 165);
        Color color = new Color(248, 249, 250);
        Color colorDeAfirmacion = new Color(40, 167, 69);
        Color colorDeError = new Color(220, 53, 69);

        // Panel principal
        FondoPanel panelPrincipal = new FondoPanel("C:\\Users\\angel\\Documents\\TautoTeacher\\TautoTeacher\\recursos\\Fondo.jpg");
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        // Encabezado
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setOpaque(false);

        JPanel titulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        titulo.setOpaque(false);

        String rutaIconoTautoTeacher = "C:\\Users\\angel\\Documents\\TautoTeacher\\TautoTeacher\\recursos\\logo3.png";
        JLabel etiquetaTitulo = new JLabel("TautoTeacher");
        etiquetaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        etiquetaTitulo.setForeground(colorPrimario);

        titulo.add(rutaIconoTautoTeacher.isEmpty() ? new JLabel() : new JLabel(new ImageIcon(new ImageIcon(rutaIconoTautoTeacher).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH))));
        titulo.add(etiquetaTitulo);

        JLabel subetiquetaTitulo = new JLabel("Herramienta para la verificación de tautologías lógicas");
        subetiquetaTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subetiquetaTitulo.setForeground(Color.GRAY);
        subetiquetaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subetiquetaTitulo.setOpaque(false);

        encabezado.add(titulo);
        encabezado.add(Box.createRigidArea(new java.awt.Dimension(0, 10)));
        encabezado.add(subetiquetaTitulo);

        // Panel para los logos de ingenieria
        JPanel panelImagenesEsquinas = new JPanel(new BorderLayout());
        panelImagenesEsquinas.setOpaque(false);

        // Logos 
        ImageIcon iconoIzq = new ImageIcon(new ImageIcon("C:\\Users\\angel\\Documents\\TautoTeacher\\TautoTeacher\\recursos\\FondoIngenieria.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        JLabel labelIzq = new JLabel(iconoIzq);
        labelIzq.setOpaque(false);
        panelImagenesEsquinas.add(labelIzq, BorderLayout.WEST);

        ImageIcon iconoDer = new ImageIcon(new ImageIcon("C:\\Users\\angel\\Documents\\TautoTeacher\\TautoTeacher\\recursos\\FondoIngenieria.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        JLabel labelDer = new JLabel(iconoDer);
        labelDer.setOpaque(false);
        panelImagenesEsquinas.add(labelDer, BorderLayout.EAST);

        encabezado.add(panelImagenesEsquinas);

        panelPrincipal.add(encabezado, BorderLayout.NORTH);

        // Panel de Características
        JPanel contenedorTarjetas = new JPanel(new java.awt.GridLayout(1, 3, 15, 0));
        contenedorTarjetas.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));
        contenedorTarjetas.setPreferredSize(new java.awt.Dimension(0, 120));
        contenedorTarjetas.setOpaque(false);

        // Panel principal de contenido con CardLayout
        JPanel contenedorSecciones = new JPanel(new CardLayout());
        contenedorSecciones.setOpaque(false);

        // Panel de entrada principal
        JPanel panelEntrada = new JPanel();
        panelEntrada.setLayout(new BoxLayout(panelEntrada, BoxLayout.Y_AXIS));
        panelEntrada.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Ingrese Su Expresión Lógica"), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        panelEntrada.setOpaque(false);

        javax.swing.border.Border borderEntrada = panelEntrada.getBorder();
        if (borderEntrada instanceof javax.swing.border.CompoundBorder cb) {

            javax.swing.border.Border outside = cb.getOutsideBorder();

            if (outside instanceof javax.swing.border.TitledBorder tb) {
                tb.setTitleFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
        }

        JTextArea areaDeExpresion = new JTextArea();
        areaDeExpresion.setLineWrap(true);
        areaDeExpresion.setWrapStyleWord(true);
        areaDeExpresion.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        areaDeExpresion.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JScrollPane panelDeDesplazamiento = new JScrollPane(areaDeExpresion);
        panelDeDesplazamiento.setPreferredSize(new java.awt.Dimension(700, 150));

        // Panel de Símbolos
        JPanel panelDeSimbolos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelDeSimbolos.setBorder(BorderFactory.createTitledBorder("Símbolos Lógicos"));
        panelDeSimbolos.setOpaque(false);

        // Agregar botones de símbolos
        String[] simbolos = { "¬", "∧", "∨", "→", "↔", "(", ")" };
        for (String simbolo : simbolos) {
            JButton botonDeSimbolos = new JButton(simbolo);
            botonDeSimbolos.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            botonDeSimbolos.addActionListener(e -> {
                int pos = areaDeExpresion.getCaretPosition();
                areaDeExpresion.insert(simbolo, pos);
                areaDeExpresion.requestFocus();
                areaDeExpresion.setCaretPosition(pos + simbolo.length());
            });
            panelDeSimbolos.add(botonDeSimbolos);
        }

        panelEntrada.add(panelDeSimbolos);
        panelEntrada.add(Box.createRigidArea(new java.awt.Dimension(0, 10)));
        panelEntrada.add(panelDeDesplazamiento);

        // Botón de verificación
        JButton botonDeVerificacion = new JButton("✔ Verificar Tautología");
        botonDeVerificacion.setBackground(new java.awt.Color(74, 111, 165));
        botonDeVerificacion.setForeground(Color.BLACK);
        botonDeVerificacion.setFont(new Font("Dialog", Font.BOLD, 14));
        botonDeVerificacion.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        botonDeVerificacion.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelEntrada.add(Box.createRigidArea(new java.awt.Dimension(0, 15)));
        panelEntrada.add(botonDeVerificacion);

        // Panel de resultados
        JPanel panelDeResultado = new JPanel();
        panelDeResultado.setLayout(new BoxLayout(panelDeResultado, BoxLayout.Y_AXIS));
        panelDeResultado.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Resultado"), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        panelDeResultado.setOpaque(false);

        JLabel resultadoEtiquetaIcono = new JLabel();
        JTextArea areaDeResultado = new JTextArea();
        areaDeResultado.setEditable(false);
        areaDeResultado.setLineWrap(true);
        areaDeResultado.setWrapStyleWord(true);
        areaDeResultado.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        areaDeResultado.setBackground(Color.WHITE);

        panelDeResultado.add(resultadoEtiquetaIcono);
        panelDeResultado.add(areaDeResultado);

        // Panel de instrucciones
        JPanel panelInstrucciones = new JPanel();
        panelInstrucciones.setLayout(new BoxLayout(panelInstrucciones, BoxLayout.Y_AXIS));
        panelInstrucciones.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Instrucciones"), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        panelInstrucciones.setOpaque(false);

        String instrucciones[] = {
                "Utilice los símbolos estándar: ∧ (AND), ∨ (OR), ¬ (NOT), → (implicación), ↔ (equivalencia)",
                "Puede usar paréntesis para agrupar expresiones",
                "Ejemplos válidos: \"p → q\", \"¬(p ∧ ¬q)\", \"(p ∨ q) ↔ (q ∨ p)\""
        };

        for (String instruccion : instrucciones) {
            JLabel instructionLabel = new JLabel("• " + instruccion);
            instructionLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            panelInstrucciones.add(instructionLabel);
            panelInstrucciones.add(Box.createRigidArea(new java.awt.Dimension(0, 5)));
        }

        // Entrada y resultados
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);

        panelCentral.add(panelEntrada);
        panelCentral.add(Box.createRigidArea(new java.awt.Dimension(0, 15)));

        panelCentral.add(panelDeResultado);
        panelCentral.add(Box.createRigidArea(new java.awt.Dimension(0, 15)));

        panelCentral.add(panelInstrucciones);

        // Panel de análisis
        JPanel panelAnalisis = new JPanel(new BorderLayout());
        panelAnalisis.setOpaque(false);
        panelAnalisis.add(panelCentral, BorderLayout.CENTER);

        JPanel panelVisualizacion = new JPanel(new BorderLayout());
        panelVisualizacion.setOpaque(false);
        JTextArea areaVisualizacion = new JTextArea();
        areaVisualizacion.setEditable(false);
        areaVisualizacion.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        panelVisualizacion.add(new JScrollPane(areaVisualizacion), BorderLayout.CENTER);

        // Panel Educativo
        JPanel panelEducativo = new JPanel(new BorderLayout());
        panelEducativo.setOpaque(false);
        JTextArea areaEducativa = new JTextArea();
        areaEducativa.setEditable(false);
        areaEducativa.setLineWrap(true);
        areaEducativa.setWrapStyleWord(true);
        areaEducativa.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        JScrollPane scrollEducativo = new JScrollPane(areaEducativa);
        panelEducativo.add(scrollEducativo, BorderLayout.CENTER);

        // Paneles Al CardLayout
        contenedorSecciones.add(panelAnalisis, "analisis");
        contenedorSecciones.add(panelVisualizacion, "visualizacion");
        contenedorSecciones.add(panelEducativo, "educativo");

        // CardLayout para cambiar de sección
        CardLayout layoutTarjetas = (CardLayout) contenedorSecciones.getLayout();

        // Tarjetas con acción para cambiar de sección
        String rutaIconoVerificacion = "C:\\Users\\angel\\Documents\\TautoTeacher\\TautoTeacher\\recursos\\busquedaRapida.png";
        agregarTarjeta(contenedorTarjetas, "Análisis Rápido", rutaIconoVerificacion,
                "Verifica expresiones lógicas en tiempo real",
                e -> layoutTarjetas.show(contenedorSecciones, "analisis"));

        String rutaIconoVisualizacion = "C:\\Users\\angel\\Documents\\TautoTeacher\\TautoTeacher\\recursos\\visualizacionRapida.png";
        agregarTarjeta(contenedorTarjetas, "Visualización Clara", rutaIconoVisualizacion,
                "Resultados presentados de forma intuitiva",
                e -> layoutTarjetas.show(contenedorSecciones, "visualizacion"));

        String rutaIconoEducativo = "C:\\Users\\angel\\Documents\\TautoTeacher\\TautoTeacher\\recursos\\educativo.png";
        agregarTarjeta(contenedorTarjetas, "Educativo", rutaIconoEducativo, "Perfecto para estudiantes de lógica",
                e -> layoutTarjetas.show(contenedorSecciones, "educativo"));

        // Panel superior para las tarjetas
        JPanel panelTarjetasArriba = new JPanel(new BorderLayout());
        panelTarjetasArriba.setOpaque(false);
        panelTarjetasArriba.add(contenedorTarjetas, BorderLayout.CENTER);

        // Nuevo panel contenedor vertical para encabezado y tarjetas
        JPanel panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));
        panelNorte.add(encabezado);
        panelNorte.add(Box.createRigidArea(new Dimension(0, 10)));
        panelNorte.add(panelTarjetasArriba);

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);
        panelPrincipal.add(contenedorSecciones, BorderLayout.CENTER);

        JPanel barraDeEstado = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        barraDeEstado.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(200, 200, 200)), BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        barraDeEstado.setOpaque(false);

        
        // Botón de verificación
        botonDeVerificacion.addActionListener(e -> {
            String expresion = areaDeExpresion.getText().trim();

            if (expresion.isEmpty()) {
                showResult(panelDeResultado, resultadoEtiquetaIcono, areaDeResultado,
                        "Por favor ingrese una expresión lógica para verificar.",
                        colorDeError, UIManager.getIcon("OptionPane.errorIcon"));
                areaEducativa.setText("");
                return;
            }

            try {
                // Verificar si es tautología
                boolean esTautologia = esTautologia(expresion);

                // Generar explicación educativa
                String explicacion = generarExplicacionEducativa(expresion, esTautologia);
                areaEducativa.setText(explicacion);

                // Mostrar resultado normal con palomita o equis
                if (esTautologia) {
                    resultadoEtiquetaIcono.setText("✔"); // Palomita
                    resultadoEtiquetaIcono.setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
                    resultadoEtiquetaIcono.setForeground(colorDeAfirmacion);
                    showResult(panelDeResultado, resultadoEtiquetaIcono, areaDeResultado, "\"" + expresion + "\" es una tautología.\nLa expresión es siempre verdadera bajo todas las interpretaciones posibles.", colorDeAfirmacion, null);

                } else {
                    resultadoEtiquetaIcono.setText("✘"); // Equis
                    resultadoEtiquetaIcono.setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
                    resultadoEtiquetaIcono.setForeground(colorDeError);
                    showResult(panelDeResultado, resultadoEtiquetaIcono, areaDeResultado,
                            "\"" + expresion
                                    + "\" no es una tautología.\nExisten interpretaciones donde la expresión es falsa.",
                            colorDeError, null);
                }

                // Generar visualización clara
                try {
                    // Extraer variables de la expresión
                    Set<String> variables = obtenerVariables(expresion);

                    // Crear un mapa de asignación de valores 
                    Map<String, Boolean> valores = new HashMap<>();
                    boolean toggle = true;
                    for (String var : variables) {
                        valores.put(var, toggle);
                        toggle = !toggle;
                    }

                    // Generar visualización ASCII
                    String visualizacion = generarVisualizacionVertical(expresion, valores);

                    
                    // String exprNormalizada = expresion
                    visualizacion = visualizacion
                            .replace("∧", "^")
                            .replace("∨", "v")
                            .replace("→", "->")
                            .replace("↔", "<->");

                    areaVisualizacion.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
                    areaVisualizacion.setText(visualizacion);

                } catch (Exception ex) {
                    areaVisualizacion.setText("Error al generar visualización: " + ex.getMessage());
                }

            } catch (Exception ex) {
                resultadoEtiquetaIcono.setText(""); // Limpia el texto si hay error
                showResult(panelDeResultado, resultadoEtiquetaIcono, areaDeResultado, "Error al analizar la expresión: " + ex.getMessage(), colorDeError, UIManager.getIcon("OptionPane.errorIcon"));
                areaEducativa.setText("Error: " + ex.getMessage());
            }
        });

        frame.setContentPane(panelPrincipal);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void agregarTarjeta(JPanel panel, String titulo, String rutaIcono, String descripcion, ActionListener listener) {

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)), BorderFactory.createEmptyBorder(7, 15, 15, 15)));
        tarjeta.setBackground(Color.WHITE);

        ImageIcon iconoOriginal = new ImageIcon(rutaIcono);
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JLabel etiquetaIcono = new JLabel(new ImageIcon(imagenEscalada));
        etiquetaIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel etiquetaTitulo = new JLabel(titulo);
        etiquetaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        etiquetaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel etiquetaDescripcion = new JLabel("<html><div style='text-align:center;width:180px;'>" + descripcion + "</div></html>", SwingConstants.CENTER);
        etiquetaDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        etiquetaDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listener.actionPerformed(null);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(new Color(230, 240, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(Color.WHITE);
            }
        });

        tarjeta.add(etiquetaIcono);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 10)));

        tarjeta.add(etiquetaTitulo);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 5)));

        tarjeta.add(etiquetaDescripcion);

        panel.add(tarjeta);
    }

    private static void showResult(JPanel panelDeResultado, JLabel iconLabel, JTextArea resultArea, String message, Color color, Icon icono) {
        resultArea.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        iconLabel.setIcon(icono);
        resultArea.setText(message);
        resultArea.setForeground(color);
        resultArea.setBackground(Color.WHITE);
        panelDeResultado.setVisible(true);
    }


    // === LOGICA ===

    private static boolean esTautologia(String formula) {
        validarSintaxis(formula);

        // Reemplazar símbolos
        String expr = formula.replace("∧", "&&")
                             .replace("∨", "||")
                             .replace("¬", "!")
                             .replace("→", "->")
                             .replace("↔", "<->");

        // Extraer variables
        Set<String> variables = new TreeSet<>();
        Matcher matcher = Pattern.compile("[a-zA-Z]").matcher(expr);

        while (matcher.find()) {
            variables.add(matcher.group());
        }

        if (variables.isEmpty()) {
            throw new IllegalArgumentException("La expresión no contiene variables proposicionales");
        }

        String vars[] = variables.toArray(new String[0]);
        int n = vars.length;
        int totalCombinations = 1 << n; // 2^n combinaciones

        for (int i = 0; i < totalCombinations; i++) {
            Map<String, Boolean> valores = new HashMap<>();

            for (int j = 0; j < n; j++) {
                valores.put(vars[j], (i & (1 << j)) != 0);
            }

            String evaluable = expr;

            // Reemplazo variables con sus valores
            for (String var : vars) {
                evaluable = evaluable.replaceAll("(?<![a-zA-Z0-9_])" + Pattern.quote(var) + "(?![a-zA-Z0-9_])",
                        valores.get(var).toString());
            }

            try {
                if (!evaluaExpresion(evaluable)) {
                    return false;
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Error al evaluar la expresión con valores: " + valores, e);
            }
        }
        return true;
    }

    private static void validarSintaxis(String formula) {
        // Verificar paréntesis balanceados
        int balance = 0;
        for (char c : formula.toCharArray()) {

            if (c == '(')
                balance++;
            if (c == ')')
                balance--;
            if (balance < 0)
                throw new IllegalArgumentException("Paréntesis no balanceados");
        }

        if (balance != 0)
            throw new IllegalArgumentException("Paréntesis no balanceados");

        // Verificar operadores válidos
        if (!formula.matches("^[a-zA-Z∧∨¬→↔()\\s]+$")) {
            throw new IllegalArgumentException("Caracteres no válidos en la expresión");
        }
    }

    private static boolean evaluarExpresion(String expr) {
        expr = expr.trim();
        if (expr.equals("true") || expr.equals("(true)"))
            return true;

        if (expr.equals("false") || expr.equals("(false)"))
            return false;

        // Evaluar paréntesis primero
        int parentAbiertos = expr.lastIndexOf('(');
        if (parentAbiertos != -1) {
            int parentCerrados = expr.indexOf(')', parentAbiertos);

            if (parentCerrados == -1) {
                throw new IllegalArgumentException("Paréntesis no balanceados");
            }

            String subExpr = expr.substring(parentAbiertos + 1, parentCerrados);
            boolean subResult = evaluaExpresion(subExpr);
            String newExpr = expr.substring(0, parentAbiertos) + subResult + expr.substring(parentCerrados + 1);
            return evaluaExpresion(newExpr);
        }

        // === EVALUADORES ===

        // negaciones
        if (expr.startsWith("!")) {
            return !evaluarExpresion(expr.substring(1).trim());
        }

        // Equivalencia
        int equivIndex = buscarOperadorMasBajo(expr, "<->");
        if (equivIndex != -1) {
            boolean left = evaluarExpresion(expr.substring(0, equivIndex));
            boolean right = evaluarExpresion(expr.substring(equivIndex + 3));
            return left == right;
        }

        // Implicación
        int implIndex = buscarOperadorMasBajo(expr, "->");
        if (implIndex != -1) {
            boolean left = evaluarExpresion(expr.substring(0, implIndex));
            boolean right = evaluarExpresion(expr.substring(implIndex + 2));
            return !left || right;
        }

        // Or
        int orIndex = buscarOperadorMasBajo(expr, "||");
        if (orIndex != -1) {
            boolean left = evaluarExpresion(expr.substring(0, orIndex));
            boolean right = evaluarExpresion(expr.substring(orIndex + 2));
            return left || right;
        }

        // And
        int andIndex = buscarOperadorMasBajo(expr, "&&");
        if (andIndex != -1) {
            boolean left = evaluarExpresion(expr.substring(0, andIndex));
            boolean right = evaluarExpresion(expr.substring(andIndex + 2));
            return left && right;
        }

        // Valores booleanos
        if (expr.equals("true"))
            return true;

        if (expr.equals("false"))
            return false;

        throw new IllegalArgumentException("Expresión no válida: " + expr);
    }

    private static int buscarOperadorMasBajo(String expr, String operator) {
        int nivelDeParent = 0;
        int index = -1;

        while ((index = expr.indexOf(operator, index + 1)) != -1) {
            if (index == -1)
                break;

            // Verificar que no esté dentro de paréntesis
            String before = expr.substring(0, index);
            nivelDeParent = contarParentesis(before);

            if (nivelDeParent == 0) {
                return index;
            }
        }

        return -1;
    }

    private static int contarParentesis(String str) {
        int contador = 0;

        for (char c : str.toCharArray()) {
            if (c == '(')
                contador++;
            if (c == ')')
                contador--;
        }
        return contador;
    }

    // Evaluador simple para expresiones
    private static boolean evaluaExpresion(String expr) {
        expr = expr.replaceAll("\\s+", ""); // Eliminar espacios
        return evalEquiv(expr);
    }

    private static boolean evalEquiv(String expr) {
        int par = 0;

        for (int i = 0; i < expr.length() - 2; i++) {
            char c = expr.charAt(i);
            if (c == '(')
                par++;
            if (c == ')')
                par--;
            if (par == 0 && expr.startsWith("<->", i)) {
                boolean left = evalImpl(expr.substring(0, i));
                boolean right = evalImpl(expr.substring(i + 3));
                return left == right;
            }
        }
        return evalImpl(expr);
    }

    private static boolean evalImpl(String expr) {
        int par = 0;

        for (int i = 0; i < expr.length() - 1; i++) {
            char c = expr.charAt(i);
            if (c == '(')
                par++;
            if (c == ')')
                par--;
            if (par == 0 && expr.startsWith("->", i)) {
                boolean left = evalOr(expr.substring(0, i));
                boolean right = evalOr(expr.substring(i + 2));
                return !left || right;
            }
        }
        return evalOr(expr);
    }

    private static boolean evalOr(String expr) {
        int par = 0;

        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')')
                par++;
            if (c == '(')
                par--;
            if (par == 0 && i >= 1 && expr.startsWith("||", i - 1)) {
                boolean left = evalOr(expr.substring(0, i - 1));
                boolean right = evalAnd(expr.substring(i + 1));
                return left || right;
            }
        }
        return evalAnd(expr);
    }

    private static boolean evalAnd(String expr) {
        int par = 0;

        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')')
                par++;
            if (c == '(')
                par--;
            if (par == 0 && i >= 1 && expr.startsWith("&&", i - 1)) {
                boolean left = evalAnd(expr.substring(0, i - 1));
                boolean right = evalNot(expr.substring(i + 1));
                return left && right;
            }
        }
        return evalNot(expr);
    }

    private static boolean evalNot(String expr) {
        expr = expr.trim();
        if (expr.startsWith("!")) {
            return !evalNot(expr.substring(1));
        }
        if (expr.startsWith("(") && expr.endsWith(")")) {
            return evaluaExpresion(expr.substring(1, expr.length() - 1));
        }
        if (expr.equals("true"))
            return true;
        if (expr.equals("false"))
            return false;
        throw new IllegalArgumentException("Expresión no válida: " + expr);
    }

    // Nodo para el árbol de evaluación visual
    private static class NodoVisualEval {
        String texto;
        boolean valor;
        List<NodoVisualEval> hijos = new ArrayList<>();
        int ancho = 1;
        int etiquetaAncho;

        NodoVisualEval(String texto, boolean valor) {
            this.texto = texto;
            this.valor = valor;
            this.etiquetaAncho = texto.length();
        }
    }

    private static NodoVisualEval construirArbolVisualEval(String expr) {
        expr = expr.trim();
        if (expr.startsWith("(") && expr.endsWith(")") && parenBalance(expr.substring(1, expr.length() - 1))) {
            expr = expr.substring(1, expr.length() - 1);
        }
        String[] ops = { "<->", "->", "||", "&&" };
        for (String op : ops) {
            int idx = buscarOperadorVisual(expr, op);
            if (idx != -1) {
                NodoVisualEval izq = construirArbolVisualEval(expr.substring(0, idx));
                NodoVisualEval der = construirArbolVisualEval(expr.substring(idx + op.length()));
                boolean valor = switch (op) {
                    case "<->" -> izq.valor == der.valor;
                    case "->" -> !izq.valor || der.valor;
                    case "||" -> izq.valor || der.valor;
                    case "&&" -> izq.valor && der.valor;
                    default -> false;
                };
                NodoVisualEval nodo = new NodoVisualEval(op, valor);
                nodo.hijos.add(izq);
                nodo.hijos.add(der);
                return nodo;
            }
        }
        if (expr.startsWith("!")) {
            NodoVisualEval hijo = construirArbolVisualEval(expr.substring(1));
            NodoVisualEval nodo = new NodoVisualEval("!", !hijo.valor);
            nodo.hijos.add(hijo);
            return nodo;
        }
        // Es hoja
        boolean valor = expr.equals("true");
        if (!valor && !expr.equals("false")) {
            valor = evaluaExpresion(expr);
        }
        return new NodoVisualEval(expr, valor);
    }

    // Visualización vertical del árbol
    private static String generarVisualizacionVertical(String formula, Map<String, Boolean> valores) {
        String expr = formula.replace("∧", "&&")
                             .replace("∨", "||")
                             .replace("¬", "!")
                             .replace("→", "->")
                             .replace("↔", "<->");

        for (Map.Entry<String, Boolean> entry : valores.entrySet()) {
            expr = expr.replaceAll("\\b" + Pattern.quote(entry.getKey()) + "\\b", entry.getValue().toString());
        }

        NodoVisualEval raiz = construirArbolVisualEval(expr);

        StringBuilder sb = new StringBuilder();
        sb.append("ÁRBOL DE EVALUACIÓN LÓGICA\n");
        sb.append("================================\n\n");
        sb.append("Expresión: ").append(formula).append("\n\n");

        sb.append(generarVisualizacionImagen2(raiz));

        sb.append("\nRESULTADO:\n");
        sb.append(raiz.valor ? "ES UNA TAUTOLOGÍA" : "NO ES UNA TAUTOLOGÍA");

        return sb.toString();
    }

    private static String generarVisualizacionTablaSemantica(NodoVisualEval nodo) {
        StringBuilder sb = new StringBuilder();
        dibujarTablaSemantica(nodo, sb, "", true);
        return sb.toString();
    }

    private static void dibujarTablaSemantica(NodoVisualEval nodo, StringBuilder sb, String prefijo, boolean esUltimo) {
        sb.append(prefijo);
        if (!prefijo.isEmpty()) {
            sb.append(esUltimo ? "└── " : "├── "); // Rama hacia el hijo
        }

        // Imprimir el valor y el texto
        sb.append((nodo.valor ? "V" : "F"))
                .append(" : ")
                .append(nodo.texto)
                .append("\n");

        // Dibujar hijos
        for (int i = 0; i < nodo.hijos.size(); i++) {
            NodoVisualEval hijo = nodo.hijos.get(i);
            dibujarTablaSemantica(hijo, sb,
                    prefijo + (esUltimo ? "    " : "│   "), // Prefijo para alinear ramas
                    i == nodo.hijos.size() - 1); // El último hijo dibuja └──
        }
    }

    private static String rtrim(String s) {
        return s.replaceAll("\\s+$", "");
    }

    private static int buscarOperadorVisual(String expr, String op) {
        int par = 0;
        for (int i = 0; i <= expr.length() - op.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(')
                par++;
            if (c == ')')
                par--;
            if (par == 0 && expr.startsWith(op, i)) {
                return i;
            }
        }
        return -1;
    }

    // Método auxiliar para verificar balance de paréntesis
    private static boolean parenBalance(String expr) {
        int par = 0;
        for (char c : expr.toCharArray()) {
            if (c == '(')
                par++;
            if (c == ')')
                par--;
            if (par < 0)
                return false;
        }
        return par == 0;
    }

    // Método auxiliar para centrar texto
    private static String centro(String s, int width) {
        if (s.length() >= width) {
            return s;
        }
        int left = (width - s.length()) / 2;
        int right = width - s.length() - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }

    // Método auxiliar para recolectar nodos por niveles en el árbol visual
    private static int calcularAnchoSubarbol(NodoVisualEval nodo) {
        if (nodo == null)
            return 0;
        if (nodo.hijos.isEmpty()) {
            return Math.max(nodo.texto.length() + 4, 6);
        }
        int ancho = 0;
        for (NodoVisualEval hijo : nodo.hijos) {
            ancho += calcularAnchoSubarbol(hijo);
        }
        return Math.max(ancho, nodo.texto.length() + 4);
    }

    // Calcula la altura del árbol
    private static int calcularAltura(NodoVisualEval nodo) {
        if (nodo == null)
            return 0;
        int maxHijo = 0;
        for (NodoVisualEval hijo : nodo.hijos) {
            maxHijo = Math.max(maxHijo, calcularAltura(hijo));
        }
        return 1 + maxHijo;
    }

    private static String generarVisualizacionImagen2(NodoVisualEval raiz) {
        int ancho = calcularAnchoSubarbol(raiz);
        int alto = calcularAltura(raiz) * 3; 
        char[][] canvas = new char[alto][ancho];
        for (char[] fila : canvas)
            java.util.Arrays.fill(fila, ' ');

        dibujarNodoTabla(raiz, canvas, 0, 0, ancho);

        StringBuilder sb = new StringBuilder();
        for (char[] fila : canvas) {
            sb.append(new String(fila)).append("\n");
        }
        return sb.toString();
    }

    private static void dibujarNodoTabla(NodoVisualEval nodo, char[][] canvas,
            int fila, int ini, int ancho) {
        if (nodo == null)
            return;
        int mid = ini + ancho / 2;

        // Valor en primera fila
        String val = nodo.valor ? "V" : "F";
        escribir(canvas, fila, mid, val);

        // Texto de la formula debajo
        escribir(canvas, fila + 1, mid - nodo.texto.length() / 2, nodo.texto);

        // Dibujar ramas a los hijos
        if (!nodo.hijos.isEmpty()) {
            int num = nodo.hijos.size();
            int espacioHijo = ancho / num;
            int pos = ini;
            for (NodoVisualEval h : nodo.hijos) {
                int hijoMid = pos + espacioHijo / 2;
                // dibujar linea horizontal
                for (int x = Math.min(mid, hijoMid); x <= Math.max(mid, hijoMid); x++) {
                    canvas[fila + 2][x] = '-';
                }
                // dibujar vertical al hijo
                canvas[fila + 2][hijoMid] = '|';
                dibujarNodoTabla(h, canvas, fila + 3, pos, espacioHijo);
                pos += espacioHijo;
            }
        }
    }

    private static void escribir(char[][] canvas, int fila, int col, String texto) {
        for (int i = 0; i < texto.length(); i++) {
            if (col + i < canvas[fila].length) {
                canvas[fila][col + i] = texto.charAt(i);
            }
        }
    }

    private static void recolectarNiveles(NodoVisualEval nodo, int nivel, List<List<NodoVisualEval>> niveles) {
        if (nodo == null)
            return;
        while (niveles.size() <= nivel)
            niveles.add(new ArrayList<>());
        niveles.get(nivel).add(nodo);
        for (NodoVisualEval hijo : nodo.hijos) {
            recolectarNiveles(hijo, nivel + 1, niveles);
        }
    }

    private static String centrar(String s, int ancho) {
        if (s.length() >= ancho)
            return s.substring(0, ancho);
        int izq = (ancho - s.length()) / 2;
        int der = ancho - s.length() - izq;
        return " ".repeat(izq) + s + " ".repeat(der);
    }

    // Calcula ancho mínimo necesario para cada subárbol
    private static int calcularAncho(NodoVisualEval nodo) {
        if (nodo.hijos.isEmpty()) {
            return Math.max(raizTexto(nodo).length(), 3);
        }
        int suma = 0;
        for (NodoVisualEval h : nodo.hijos) {
            suma += calcularAncho(h) + 3;
        }
        return Math.max(suma, raizTexto(nodo).length());
    }

    // Obtiene el texto que ocupa el nodo
    private static String raizTexto(NodoVisualEval nodo) {
        return (nodo.valor ? "V " : "F ") + nodo.texto;
    }

    // Dibuja nodo en el StringBuilder
    private static void dibujarNodo(NodoVisualEval nodo, StringBuilder sb, int nivel, int inicio, int ancho) {
        if (nodo == null)
            return;

        String texto = raizTexto(nodo);

        int padding = inicio + (ancho - texto.length()) / 2;

        appendLine(sb, texto, padding);

        if (!nodo.hijos.isEmpty()) {
            sb.append("\n");

            int childInicio = inicio;
            for (NodoVisualEval hijo : nodo.hijos) {
                int subAncho = calcularAncho(hijo);
                dibujarNodo(hijo, sb, nivel + 2, childInicio, subAncho);
                childInicio += subAncho + 3;
            }
        }
    }

    private static void appendLine(StringBuilder sb, String texto, int padding) {
        sb.append(" ".repeat(Math.max(0, padding))).append(texto).append("\n");
    }

    private static String generarVisualizacionArbol(NodoVisual nodo) {
        StringBuilder sb = new StringBuilder();
        generarVisualizacionArbolHelper(nodo, sb, 0, "");
        return sb.toString();
    }

    private static void generarVisualizacionArbolHelper(NodoVisual nodo, StringBuilder sb, int nivel, String prefijo) {
        if (nodo == null)
            return;
        for (int i = 0; i < nivel; i++) {
            sb.append("   ");
        }
        sb.append(prefijo);
        sb.append(nodo.texto);
        sb.append(" [").append(nodo.valor ? "V" : "F").append("]");
        sb.append("\n");

        for (int i = 0; i < nodo.hijos.size(); i++) {
            generarVisualizacionArbolHelper(nodo.hijos.get(i), sb, nivel + 1,
                    (i == nodo.hijos.size() - 1) ? "└─ " : "├─ ");
        }
    }

    private static class NodoVisual {
        String texto;
        boolean valor;
        List<NodoVisual> hijos = new ArrayList<>();

        NodoVisual(String texto, boolean valor) {
            this.texto = texto;
            this.valor = valor;
        }
    }

    private static Set<String> obtenerVariables(String formula) {

        Set<String> variables = new TreeSet<>();
        Matcher matcher = Pattern.compile("\\b[a-zA-Z]\\b").matcher(formula);

        while (matcher.find()) {
            variables.add(matcher.group());
        }

        return variables;
    }

    /**
     * Determina el tipo de fórmula
     * @param formula 
     * @return "TAUTOLOGÍA", "CONTRADICCIÓN" o "CONTINGENCIA"
     */
    private static String tipoFormula(String formula) {
        validarSintaxis(formula);

        String expr = formula.replace("∧", "&&")
                             .replace("∨", "||")
                             .replace("¬", "!")
                             .replace("→", "->")
                             .replace("↔", "<->");

        Set<String> variables = new TreeSet<>();
        Matcher matcher = Pattern.compile("[a-zA-Z]").matcher(expr);

        while (matcher.find()) {
            variables.add(matcher.group());
        }

        if (variables.isEmpty()) {
            throw new IllegalArgumentException("La expresión no contiene variables proposicionales");
        }

        String vars[] = variables.toArray(new String[0]);
        int n = vars.length;
        int totalCombinations = 1 << n;

        boolean algunaTrue = false;
        boolean algunaFalse = false;

        for (int i = 0; i < totalCombinations; i++) {

            Map<String, Boolean> valores = new HashMap<>();

            for (int j = 0; j < n; j++) {
                valores.put(vars[j], (i & (1 << j)) != 0);
            }

            String evaluable = expr;

            for (String var : vars) {
                evaluable = evaluable.replaceAll("(?<![a-zA-Z0-9_])" + Pattern.quote(var) + "(?![a-zA-Z0-9_])",
                        valores.get(var).toString());
            }

            try {
                boolean res = evaluaExpresion(evaluable);

                if (res)
                    algunaTrue = true;
                else
                    algunaFalse = true;

            } catch (Exception e) {
                throw new IllegalArgumentException("Error al evaluar la expresión con valores: " + valores, e);
            }

            if (algunaTrue && algunaFalse)
                break;
        }

        if (algunaTrue && !algunaFalse)
            return "TAUTOLOGÍA";

        if (!algunaTrue && algunaFalse)
            return "CONTRADICCIÓN";

        return "CONTINGENCIA";
    }

    // Generar la explicación educativa
    private static String generarExplicacionEducativa(String formula, boolean esTautologia) {
        StringBuilder sb = new StringBuilder();
        sb.append("DEMOSTRACIÓN POR REFUTACION\n");
        sb.append("=============================\n\n");

        // 1. Presentar la fórmula general
        sb.append("1. FÓRMULA INGRESADA:\n   ").append(formula).append("\n\n");

        // 2. Mostrar variables que la componen
        Set<String> variables = obtenerVariables(formula);
        sb.append("2. VARIABLES PROPOSICIONALES:\n   ");
        int idx = 0;
        for (String var : variables) {
            sb.append(var);
            if (++idx < variables.size())
                sb.append(", ");
        }
        sb.append("\n\n");

        // 3. Intentar desglosar como implicación o equivalencia principal
        String antecedente = null, consecuente = null;
        String tipoPrincipal = null;
        int nivel = 0, pos = -1;
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (c == '(')
                nivel++;
            if (c == ')')
                nivel--;
            if (nivel == 0) {
                if (formula.startsWith("→", i)) {
                    pos = i;
                    tipoPrincipal = "implicacion";
                    break;
                }
                if (formula.startsWith("↔", i)) {
                    pos = i;
                    tipoPrincipal = "equivalencia";
                    break;
                }
            }
        }
        if (pos != -1) {
            antecedente = formula.substring(0, pos).trim();
            consecuente = formula.substring(pos + 1).trim();
        }

        if (antecedente != null && consecuente != null) {
            sb.append("3. ESTRUCTURA DE LA FÓRMULA:\n");
            if ("implicacion".equals(tipoPrincipal)) {
                sb.append("   La fórmula es una implicación donde:\n");
                sb.append("   - Antecedente: ").append(antecedente).append("\n");
                sb.append("   - Consecuente: ").append(consecuente).append("\n\n");
                sb.append("4. MÉTODO DE DEMOSTRACIÓN:\n");
                sb.append("   Para demostrar que es una tautología, asumiremos que:\n");
                sb.append("   1. El antecedente es VERDADERO\n");
                sb.append("   2. El consecuente es FALSO\n");
                sb.append("   Si llegamos a una contradicción, la fórmula es tautología.\n\n");

                // Paso a paso para implicaciones 
                sb.append("5. PASO A PASO:\n");

                // a) Asumir el consecuente = F
                sb.append("   a) Asumir ").append(consecuente).append(" = F\n");
                if (consecuente.contains("→")) {
                    String[] partesCons = consecuente.split("→");
                    if (partesCons.length == 2) {
                        String P = partesCons[0].replaceAll("[()]", "").trim();
                        String R = partesCons[1].replaceAll("[()]", "").trim();
                        sb.append("      - ").append(consecuente).append(" es FALSO solo cuando:\n");
                        sb.append("        ").append(P).append(" = V y ").append(R).append(" = F\n\n");
                    } else {
                        sb.append("      - Error al analizar el consecuente como implicación.\n\n");
                    }
                } else {
                    sb.append("      - ").append(consecuente).append(" es FALSO solo cuando:\n");
                    sb.append("        ").append(consecuente).append(" = F\n\n");
                }

                // b) Sustituir estos valores en el antecedente
                sb.append("   b) Sustituir estos valores en el antecedente ").append(antecedente).append(":\n");
                // Buscar subpreposiciones
                List<String> preposiciones = new ArrayList<>();
                String ant = antecedente;
                nivel = 0;
                int last = 0;
                for (int i = 0; i < ant.length(); i++) {
                    char c = ant.charAt(i);
                    if (c == '(')
                        nivel++;
                    if (c == ')')
                        nivel--;
                    if (nivel == 0 && i < ant.length() - 1 && ant.charAt(i) == '∧') {
                        preposiciones.add(ant.substring(last, i).trim());
                        last = i + 1;
                    }
                }
                preposiciones.add(ant.substring(last).trim());

                sb.append("      - El antecedente es una conjunción de:\n");
                int pidx = 1;
                for (String prep : preposiciones) {
                    sb.append("        ").append(pidx++).append(". ").append(prep).append("\n");
                }
                sb.append("\n");

                // Declarar P y R fuera del ciclo para que sean accesibles
                String P = null, R = null;
                if (consecuente.contains("→")) {
                    String[] partesCons = consecuente.split("→");
                    if (partesCons.length == 2) {
                        P = partesCons[0].replaceAll("[()]", "").trim();
                        R = partesCons[1].replaceAll("[()]", "").trim();
                    }
                }

                // c) Evaluar cada preposición con los valores asignados
                for (String prep : preposiciones) {
                    if (prep.contains("→")) {
                        String[] partes = prep.split("→");
                        String H = partes[0].replaceAll("[()]", "").trim();
                        String C = partes[1].replaceAll("[()]", "").trim();
                        sb.append("   c) Evaluar ").append(prep).append(" con ");
                        if (P != null && H.equals(P)) {
                            sb.append(P).append("=V");
                        }
                        if (R != null && C.equals(R)) {
                            if (P != null && H.equals(P)) {
                                sb.append(", ");
                            }
                            sb.append(R).append("=F");
                        }
                        sb.append(":\n");
                        if (P != null && R != null && H.equals(P) && C.equals(R)) {
                            sb.append("      - ").append(H).append(" = V, ").append(C).append(" = F\n");
                            sb.append("      - ").append(prep).append(" = V→F = F\n");
                        } else if (P != null && H.equals(P)) {
                            sb.append("      - ").append(H).append(" = V\n");
                            sb.append("      - Para que ").append(prep).append(" sea VERDADERA, ");
                            sb.append(C).append(" debe ser V (V→V = V)\n");
                            sb.append("      - Por lo tanto, ").append(C).append(" = V\n");
                        } else if (R != null && C.equals(R)) {
                            sb.append("      - ").append(C).append(" = F\n");
                            sb.append("      - Para que ").append(prep).append(" sea VERDADERA, ");
                            sb.append(H).append(" debe ser F (F→F = V)\n");
                            sb.append("      - Por lo tanto, ").append(H).append(" = F\n");
                        } else {
                            sb.append("      - No se puede determinar directamente con los valores asignados.\n");
                        }
                        sb.append("\n");
                    }
                }

                // d) Evaluar la conjunción
                sb.append("   d) Evaluar la conjunción completa:\n");
                sb.append("      - Si alguna de las preposiciones es FALSA, toda la conjunción es FALSA.\n");
                sb.append("      - En este caso, al sustituir los valores, una de las preposiciones resulta FALSA.\n");
                sb.append("      - ¡CONTRADICCIÓN! El antecedente debería ser VERDADERO, pero resulta FALSO.\n\n");

                // 6. Conclusión
                sb.append("6. CONCLUSIÓN:\n");
                if (esTautologia) {
                    sb.append("   Hemos llegado a una contradicción al asumir el consecuente falso.\n");
                    sb.append("   Por lo tanto, la fórmula ES una TAUTOLOGÍA.\n");
                } else {
                    sb.append("   No se llegó a contradicción en todos los casos posibles.\n");
                    sb.append("   Por lo tanto, la fórmula NO es una tautología.\n");
                }
            } else if ("equivalencia".equals(tipoPrincipal)) {
                sb.append("   La fórmula es una equivalencia lógica donde:\n");
                sb.append("   - Primer miembro: ").append(antecedente).append("\n");
                sb.append("   - Segundo miembro: ").append(consecuente).append("\n\n");
                sb.append(
                        "   Una equivalencia es verdadera si ambos miembros tienen el mismo valor de verdad en todas las interpretaciones.\n\n");
                sb.append("   Para demostrar que es una tautología, analizamos todos los casos posibles:\n");
                sb.append("   - Si ambos miembros son verdaderos, la equivalencia es verdadera.\n");
                sb.append("   - Si ambos miembros son falsos, la equivalencia es verdadera.\n");
                sb.append("   - Si uno es verdadero y el otro falso, la equivalencia es falsa.\n\n");
                sb.append(
                        "   Por lo tanto, para que la fórmula sea una tautología, ambos miembros deben coincidir en valor de verdad en todas las interpretaciones.\n");
            }
        } else {
            sb.append(
                    "No se detectó una implicación o equivalencia principal o la fórmula no es del tipo esperado para este análisis detallado.\n");
        }

        // --- Explicación adicional según el resultado ---
        sb.append("\nEXPLICACIÓN FINAL:\n");
        if (esTautologia) {
            sb.append(
                    "   Una TAUTOLOGÍA es una fórmula que es verdadera bajo cualquier interpretación posible de sus variables.\n");
            sb.append("   En este caso, la fórmula es SIEMPRE VERDADERA.\n");
        } else {
            // Determinar si es contradicción o contingencia
            String tipo = tipoFormula(formula);
            if ("CONTRADICCIÓN".equals(tipo)) {
                sb.append(
                        "   Una CONTRADICCIÓN es una fórmula que es falsa bajo cualquier interpretación posible de sus variables.\n");
                sb.append("   En este caso, la fórmula es SIEMPRE FALSA.\n");
            } else if ("CONTINGENCIA".equals(tipo)) {
                sb.append(
                        "   Una CONTINGENCIA es una fórmula que es verdadera en algunas interpretaciones y falsa en otras.\n");
                sb.append("   En este caso, la fórmula es VERDADERA para algunos valores y FALSA para otros.\n");
            }
        }

        return sb.toString();
    }
}