# Cambio 006 - Compilación alineada a Java 17 (UnsupportedClassVersionError)

Fecha: 2026-04-07

## Problema

Al ejecutar con Adoptium JDK 17 aparecía:

`UnsupportedClassVersionError: ... class file version 69.0 ... only recognizes ... up to 61.0`

La carpeta `out/` contenía bytecode generado con un JDK más reciente (p. ej. el que usa el entorno al compilar con `javac` por defecto), incompatible con Java 17 en tiempo de ejecución.

## Solución

Recompilar siempre con el mismo JDK que se usa para ejecutar, fijando el nivel de lenguaje:

```text
"C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot\bin\javac.exe" --release 17 -encoding UTF-8 -d out <fuentes...>
```

Ejecutar:

```text
"C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot\bin\java.exe" -cp out tautoteacher2.Main
```

## Cambios realizados en

- No se modificó código fuente; se documenta el procedimiento y se recomienda usar `--release 17` en compilaciones manuales o en la configuración del IDE (Java 17 como compliance / target).

## Justificación

`--release 17` garantiza bytecode y API compatibles con Java 17, evitando desajustes entre el compilador “por defecto” del sistema y el runtime del usuario.
