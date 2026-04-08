# Cambio 002 - Corrección de packages y ejecución en IDE

Fecha: 2026-04-07

## Problema detectado

El proyecto presentaba errores globales de compilación en el IDE:

- `The declared package ... does not match the expected package ...`
- `TautoTeacherApp cannot be resolved to a type`

Además, se estaba ejecutando `Main` sin paquete en la configuración de ejecución.

## Causa raíz

1. Los archivos Java tenían declaraciones como `package main.java.tautoteacher2...`, que no coinciden con la estructura real:
   - Ruta real: `src/main/java/tautoteacher2/...`
   - Package correcto: `tautoteacher2...`
2. `Main.java` importaba `TautoTeacherApp` con prefijo incorrecto.
3. El comando/Run Configuration estaba apuntando a `Main` en lugar de `tautoteacher2.Main`.

## Correcciones aplicadas

- Se normalizaron los `package` a `tautoteacher2...` en el árbol `src/main/java/tautoteacher2`.
- Se corrigieron `import` con prefijo incorrecto hacia `tautoteacher2...`.
- Se dejó `Main.java` con:
  - `package tautoteacher2;`
  - `import tautoteacher2.ui.TautoTeacherApp;`

## Cambios realizados en

- `src/main/java/tautoteacher2/**` (corrección de declaraciones `package` e `import`)
- `src/main/java/tautoteacher2/Main.java` (normalización de paquete y import principal)

## Justificación

Java exige coincidencia exacta entre la ruta fuente y la declaración `package`. Corregir esto elimina errores masivos del IDE y permite que el compilador resuelva correctamente clases como `TautoTeacherApp`.
