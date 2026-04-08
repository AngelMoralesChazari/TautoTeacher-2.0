# Cambio 007 - Proyecto orientado a Java 25

Fecha: 2026-04-07

## Objetivo

Usar **Java 25** de forma coherente: mismo JDK para compilar y ejecutar, y bytecode acorde (`--release 25`).

## Qué debes hacer en tu PC

1. **Instalar JDK 25** (por ejemplo Eclipse Temurin 25 desde [Adoptium](https://adoptium.net/) u otra distribución que ofrezca Java 25).
2. **Definir `JAVA_HOME`** apuntando a la carpeta raíz del JDK 25, por ejemplo:
   - `C:\Program Files\Eclipse Adoptium\jdk-25.x.x-hotspot`
3. **PATH**: añadir `%JAVA_HOME%\bin` (o el equivalente en PowerShell) para que `java` y `javac` sean la versión 25.
4. Comprobar en terminal:
   - `java -version` → debe indicar **25**
   - `javac -version` → debe indicar **25**

## Cambios en el repositorio

- **`.vscode/settings.json`** (carpeta workspace padre): el servidor de lenguaje Java de Cursor/VS Code usa `JAVA_HOME` como JDK del proyecto (`java.jdt.ls.java.home` y runtime por defecto).
- **`compile.ps1`** (raíz del proyecto Java): compila todo con `javac --release 25` usando el JDK indicado por `JAVA_HOME` o por `-JdkHome`.

## Uso rápido

Desde `TautoTeacher2.0\TautoTeacher2.0`:

```powershell
$env:JAVA_HOME = "C:\ruta\a\tu\jdk-25"
.\compile.ps1
& "$env:JAVA_HOME\bin\java.exe" -cp out tautoteacher2.Main
```

## Cambios realizados en

- `../.vscode/settings.json` (workspace `Documents\TautoTeacher2.0`)
- `compile.ps1`
- Este archivo de documentación

## Nota sobre el error anterior (class file 69 vs 61)

- **69** = bytecode de un JDK reciente (p. ej. 25).
- **61** = Java 17.

Si compilas con **25** y ejecutas con **17**, vuelve a fallar. Con Java 25 en `JAVA_HOME` y en el Run/Debug de Cursor, compilación y ejecución deben coincidir.

## Justificación

Centralizar la versión en JDK 25 y en `--release 25` evita mezclar runtimes y mantiene el proyecto alineado con la intención del desarrollador y con `.idea/misc.xml` (project-jdk-name 25).
