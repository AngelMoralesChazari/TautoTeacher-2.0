package tautoteacher2.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Círculo con símbolo de dictamen (✔ / ✘ / ○). Se dibuja a mano para evitar
 * recortes o "..." en pantallas pequeñas o con escalado DPI alto.
 */
final class InsigniaDictamen extends JPanel {

    private String simbolo = "";
    private Color fondo = new Color(241, 245, 249);
    private Color colorSimbolo = new Color(100, 116, 139);

    InsigniaDictamen() {
        setOpaque(false);
    }

    void limpiar() {
        simbolo = "";
        fondo = new Color(241, 245, 249);
        colorSimbolo = new Color(100, 116, 139);
        repaint();
    }

    void aplicar(String simbolo, Color fondo, Color colorSimbolo) {
        this.simbolo = simbolo != null ? simbolo : "";
        this.fondo = fondo != null ? fondo : new Color(241, 245, 249);
        this.colorSimbolo = colorSimbolo != null ? colorSimbolo : Color.DARK_GRAY;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        int s = UiEscalado.escalar(56);
        return new Dimension(s, s);
    }

    @Override
    public Dimension getMinimumSize() {
        int s = UiEscalado.escalar(44);
        return new Dimension(s, s);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (simbolo.isBlank()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - UiEscalado.escalar(4);
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        g2.setColor(fondo);
        g2.fillOval(x, y, size, size);

        int tamFuente = UiEscalado.escalar(26);
        Font fuente = new Font("Segoe UI Symbol", Font.BOLD, tamFuente);
        g2.setFont(fuente);
        g2.setColor(colorSimbolo);
        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (size - fm.stringWidth(simbolo)) / 2;
        int ty = y + (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(simbolo, tx, ty);
        g2.dispose();
    }
}
