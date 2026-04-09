package tautoteacher2.nlp.lexicon;

import java.util.HashMap;
import java.util.Map;

/**
 * Base de conocimiento léxica mínima para LogicScript:
 * - variaciones verbales -> lema canónico
 * Los lemas se cargan desde {@code classpath:logicscript/core.lgs} cuando existe;
 * si no hay recurso o viene vacío, se usan lemas embebidos como respaldo.
 */

public class BaseConocimiento {
    private static final String RECURSO_LEMAS = "logicscript/core.lgs";

    private final Map<String, String> lemas = new HashMap<>();

    public BaseConocimiento() {
        Map<String, String> desdeArchivo = LgsCargador.cargarLemasDesdeClasspath(RECURSO_LEMAS);
        if (!desdeArchivo.isEmpty()) {
            lemas.putAll(desdeArchivo);
        } else {
            registrarLemasIniciales();
        }
    }

    public String canonicalizarFragmento(String fragmento) {
        if (fragmento == null) {
            return "";
        }
        String limpio = fragmento.trim().toLowerCase();
        if (limpio.isEmpty()) {
            return "";
        }

        // Limpieza superficial de palabras funcionales al inicio.
        limpio = limpio.replaceAll("^(el|la|los|las|un|una|unos|unas)\\s+", "");
        limpio = limpio.replaceAll("\\s+", " ").trim();

        String[] palabras = limpio.split(" ");
        for (int i = 0; i < palabras.length; i++) {
            String lema = lemas.get(palabras[i]);
            if (lema != null) {
                palabras[i] = lema;
            }
        }
        return String.join(" ", palabras).trim();
    }

    /** Respaldo si {@code core.lgs} no está en el classpath (p. ej. compilación sin copiar {@code resources}). */
    private void registrarLemasIniciales() {
        // Lluvia
        lemas.put("llueve", "llover");
        lemas.put("llueva", "llover");
        lemas.put("llovio", "llover");
        lemas.put("llovera", "llover");
        lemas.put("lloveria", "llover");

        // Llevar (paraguas, etc.)
        lemas.put("llevo", "llevar");
        lemas.put("lleva", "llevar");
        lemas.put("llevara", "llevar");
        lemas.put("llevaria", "llevar");
        lemas.put("llevare", "llevar");

        // Estudio / aprobar
        lemas.put("estudio", "estudiar");
        lemas.put("estudie", "estudiar");
        lemas.put("apruebo", "aprobar");
        lemas.put("aprueba", "aprobar");
        lemas.put("apruebe", "aprobar");
    }
}
