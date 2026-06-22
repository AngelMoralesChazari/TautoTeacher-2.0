package tautoteacher2.core.visualizacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Nodo de un árbol de evaluación lógica (operador o valor V/F). */
public final class NodoArbolEvaluacion {

    private final String etiqueta;
    private final boolean valor;
    private final List<NodoArbolEvaluacion> hijos;

    public NodoArbolEvaluacion(String etiqueta, boolean valor) {
        this(etiqueta, valor, List.of());
    }

    public NodoArbolEvaluacion(String etiqueta, boolean valor, List<NodoArbolEvaluacion> hijos) {
        this.etiqueta = etiqueta;
        this.valor = valor;
        this.hijos = Collections.unmodifiableList(new ArrayList<>(hijos));
    }

    public String etiqueta() {
        return etiqueta;
    }

    public boolean valor() {
        return valor;
    }

    public List<NodoArbolEvaluacion> hijos() {
        return hijos;
    }

    public boolean esHoja() {
        return hijos.isEmpty();
    }
}
