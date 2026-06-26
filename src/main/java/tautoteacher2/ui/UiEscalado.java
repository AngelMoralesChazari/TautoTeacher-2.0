package tautoteacher2.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

/**
 * Escala fuentes y medidas según la resolución del monitor principal.
 * Referencia: 1080 px de alto (portátil 14" típico a 1920×1080).
 */
public final class UiEscalado {

    private static final double FACTOR;
    private static final int ANCHO_REFERENCIA = 1100;

    static {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        double porAlto = pantalla.height / 1080.0;
        double porAncho = pantalla.width / (double) ANCHO_REFERENCIA;
        FACTOR = Math.max(0.8, Math.min(1.2, Math.min(porAlto, porAncho)));
    }

    private UiEscalado() {
    }

    public static double factor() {
        return FACTOR;
    }

    public static int escalar(int px) {
        return Math.max(1, (int) Math.round(px * FACTOR));
    }

    public static Font fuente(String familia, int estilo, int tamanoBase) {
        return new Font(familia, estilo, escalar(tamanoBase));
    }

    /** Tamaño inicial razonable de la ventana (~90 % del área útil). */
    public static Dimension tamanoVentanaInicial() {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int ancho = (int) (pantalla.width * 0.92);
        int alto = (int) (pantalla.height * 0.88);
        return new Dimension(
                Math.max(escalar(720), ancho),
                Math.max(escalar(560), alto));
    }
}
