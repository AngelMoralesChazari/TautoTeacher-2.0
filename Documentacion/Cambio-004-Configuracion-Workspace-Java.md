# Cambio 004 - Configuración de workspace Java (Cursor)

Fecha: 2026-04-07

## Problema

Persistían errores de `package` en casi todos los archivos aunque el código ya tenía paquetes correctos.

## Causa raíz

El workspace abierto en Cursor es la carpeta externa:

- `C:\Users\angel\Documents\TautoTeacher2.0`

Pero el proyecto Java real está anidado en:

- `TautoTeacher2.0/src/main/java`

Sin configurar esto, el servidor Java interpreta mal el source root y marca errores masivos de paquete.

## Cambios realizados

- Se creó `C:\Users\angel\Documents\TautoTeacher2.0\.vscode\settings.json` con:
  - `java.project.sourcePaths = ["TautoTeacher2.0/src/main/java"]`
  - `java.project.outputPath = "TautoTeacher2.0/out"`
- Se creó `C:\Users\angel\Documents\TautoTeacher2.0\.vscode\launch.json` para ejecutar:
  - `mainClass = tautoteacher2.Main`

## Cambios realizados en

- `.vscode/settings.json`
- `.vscode/launch.json`

## Justificación

La configuración alinea el workspace raíz con la ubicación real de fuentes Java y evita falsos positivos de paquetes en el IDE.
