import java.util.Map;
import tautoteacher2.core.logica.ExplicacionEducativaBuilder;
public class T { public static void main(String[] a) {
  var m = Map.of("estudiar","p","aprobar","q");
  System.out.println(ExplicacionEducativaBuilder.construir("test", "(p → q)", "CONTINGENCIA", m).substring(0, 500));
}}
