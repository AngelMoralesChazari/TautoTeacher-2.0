# Cambio 009 - Comando "Java: Clean Java Language Server Workspace" no encontrado

Fecha: 2026-04-07

## Síntoma

Al ejecutar el comando aparece:

`command 'java.clean.workspace' not found`

## Causa

Ese comando lo registra la extensión de soporte Java (p. ej. **Extension Pack for Java** / **Language Support for Java by Red Hat**). Si no está instalada o no ha cargado, Cursor no expone el comando.

## Acción recomendada

1. Instalar **Extension Pack for Java** (Microsoft) en Cursor.
2. **Developer: Reload Window**.
3. Volver a invocar la limpieza del workspace Java si hace falta.

## Alternativa

Ejecutar la aplicación compilando con `compile.ps1` y `java -cp out tautoteacher2.Main` sin depender del Language Server.

## Cambios realizados en

- Solo documentación (este archivo).

## Justificación

Documentar el error evita confundirlo con un fallo del proyecto; es configuración del editor/extensiones.
