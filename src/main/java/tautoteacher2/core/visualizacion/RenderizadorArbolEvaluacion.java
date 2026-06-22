package tautoteacher2.core.visualizacion;

/**
 * Renderiza un {@link NodoArbolEvaluacion} como árbol ASCII (V/F por nodo).
 */
public final class RenderizadorArbolEvaluacion {

    private RenderizadorArbolEvaluacion() {
    }

    public static String renderizar(NodoArbolEvaluacion raiz) {
        if (raiz == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Árbol de evaluación\n");
        sb.append("───────────────────\n\n");
        dibujar(raiz, sb, "", true);
        sb.append("\nResultado raíz: ").append(raiz.valor() ? "V" : "F");
        return sb.toString();
    }

    private static void dibujar(NodoArbolEvaluacion nodo, StringBuilder sb, String prefijo, boolean esUltimo) {
        if (!prefijo.isEmpty()) {
            sb.append(prefijo);
            sb.append(esUltimo ? "└── " : "├── ");
        }
        sb.append(nodo.valor() ? "V" : "F");
        sb.append(" : ");
        sb.append(nodo.etiqueta());
        sb.append('\n');

        var hijos = nodo.hijos();
        for (int i = 0; i < hijos.size(); i++) {
            dibujar(
                    hijos.get(i),
                    sb,
                    prefijo + (esUltimo ? "    " : "│   "),
                    i == hijos.size() - 1);
        }
    }
}
