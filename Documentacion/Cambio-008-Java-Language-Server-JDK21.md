# Cambio 008 - Servidor de lenguaje Java y JDK mínimo 21

Fecha: 2026-04-07

## Problema

Mensaje en Cursor/VS Code:

`The Java runtime set by 'java.jdt.ls.java.home' does not meet the minimum required version of '21' and will not be used.`

La aplicación no arranca o el proyecto Java queda sin análisis correcto.

## Causa

En `.vscode/settings.json` se había puesto:

`"java.jdt.ls.java.home": "${env:JAVA_HOME}"`

Si **`JAVA_HOME` apunta a Java 17** (u otro &lt; 21), el **Language Server** de la extensión Java **no puede usar ese JDK**: desde versiones recientes exige **JDK 21 o superior** solo para **ejecutar el propio servidor** (no confundir con la versión `--release` de tu código).

## Corrección aplicada

Se **eliminó** `java.jdt.ls.java.home` y el bloque `java.configuration.runtimes` que dependía de `JAVA_HOME` en el workspace, para que la extensión **elija sola** un JDK ≥ 21 entre los que tengas instalados (PATH, instalaciones detectadas, etc.).

## Qué debes hacer tú

1. **Instala un JDK 21 o superior** (tu objetivo es **25**: perfecto para ambos usos).
2. Asegúrate de que Cursor pueda verlo:
   - O bien ese JDK está en el **PATH** del sistema (recomendado).
   - O bien, si quieres fijarlo a mano, vuelve a añadir en `.vscode/settings.json` (ajusta la ruta real):

```json
"java.jdt.ls.java.home": "C:\\Program Files\\Eclipse Adoptium\\jdk-25.x.x-hotspot",
"java.configuration.runtimes": [
  {
    "name": "JavaSE-25",
    "path": "C:\\Program Files\\Eclipse Adoptium\\jdk-25.x.x-hotspot",
    "default": true
  }
]
```

3. En Cursor: **Java: Clean Java Language Server Workspace** → reiniciar.

## Regla práctica

- **Servidor de lenguaje (Cursor):** JDK **≥ 21** (25 vale).
- **Tu app compilada con Java 25:** usa `compile.ps1` o el mismo JDK 25 como `java.jdt.ls.java.home` y runtime por defecto.

## Cambios realizados en

- `../.vscode/settings.json` (carpeta workspace `Documents\TautoTeacher2.0`)
- Este archivo de documentación

## Justificación

No forzar `JAVA_HOME` cuando sigue apuntando a Java 17 evita bloquear el Language Server; el usuario puede unificar todo en JDK 25 o dejar que el IDE autodetecte un JDK ≥ 21.
