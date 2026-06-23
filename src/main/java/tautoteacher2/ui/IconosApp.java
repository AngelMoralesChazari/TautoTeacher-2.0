package tautoteacher2.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Taskbar;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Carga {@code src/main/resources/icons/app-icon.png} y lo aplica a la ventana y la barra de tareas.
 */
public final class IconosApp {

    private static final String RUTA_ICONO = "/icons/TautoTeacher8.png";
    private static final int[] TAMANOS = {16, 32, 48, 64, 128, 256, 512};

    private IconosApp() {
    }

    public static void aplicarIconoVentana(JFrame ventana) {
        List<Image> iconos = cargarIconosEscalados();
        if (iconos.isEmpty()) {
            return;
        }
        ventana.setIconImages(iconos);
        aplicarIconoBarraTareas(iconos);
    }

    private static void aplicarIconoBarraTareas(List<Image> iconos) {
        if (!Taskbar.isTaskbarSupported()) {
            return;
        }
        Taskbar taskbar = Taskbar.getTaskbar();
        if (!taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
            return;
        }
        Image preferido = iconos.get(iconos.size() - 1);
        taskbar.setIconImage(preferido);
    }

    private static List<Image> cargarIconosEscalados() {
        try (InputStream in = IconosApp.class.getResourceAsStream(RUTA_ICONO)) {
            if (in == null) {
                System.err.println("IconosApp: no se encontró " + RUTA_ICONO + " en el classpath.");
                return List.of();
            }
            BufferedImage original = ImageIO.read(in);
            if (original == null) {
                System.err.println("IconosApp: no se pudo leer la imagen del icono.");
                return List.of();
            }
            List<Image> iconos = new ArrayList<>();
            for (int tam : TAMANOS) {
                if (tam <= original.getWidth()) {
                    iconos.add(escalar(original, tam));
                }
            }
            if (iconos.isEmpty()) {
                iconos.add(original);
            }
            return iconos;
        } catch (IOException e) {
            System.err.println("IconosApp: error al cargar icono: " + e.getMessage());
            return List.of();
        }
    }

    private static BufferedImage escalar(BufferedImage original, int size) {
        BufferedImage salida = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = salida.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(original, 0, 0, size, size, null);
        g.dispose();
        return salida;
    }
}
