# Cambio 003 - Hotfix de packages y codificación de texto

Fecha: 2026-04-07

## Problema reportado

- Persistían errores de `package` en el IDE.
- El texto en ejecución aparecía corrupto (`Ã`, `â`, `Â`), por ejemplo en conectivos lógicos y acentos.

## Causa raíz

1. `MotorLogico.java` tenía un `package` incorrecto/inconsistente.
2. Varios archivos quedaron con mojibake (doble mala interpretación de UTF-8), afectando símbolos:
   - `∧`, `∨`, `¬`, `→`, `↔`
   - acentos en mensajes al usuario.

## Correcciones aplicadas

- Se normalizó el paquete de `MotorLogico.java` a:
  - `package tautoteacher2.core.logica;`
- Se reparó la codificación de archivos Java afectados para recuperar caracteres UTF-8 correctos.
- Se verificó que ya no existan patrones de texto corrupto (`Ã`, `â`, `Â`) en `src/main/java`.

## Verificación

- Compilación completa OK con `javac` sobre módulos `ui`, `servicio`, `core`, `nlp`.

## Cambios realizados en

- `src/main/java/tautoteacher2/core/logica/MotorLogico.java`
- `src/main/java/tautoteacher2/ui/VentanaPrincipal.java`
- `src/main/java/tautoteacher2/ui/PanelEntradaNatural.java`
- `src/main/java/tautoteacher2/ui/PanelResultadoLogico.java`
- `src/main/java/tautoteacher2/ui/PanelVisualizacion.java`
- `src/main/java/tautoteacher2/ui/TautoTeacherApp.java`

## Justificación

Este hotfix restablece consistencia de paquetes y codificación legible, eliminando errores de resolución de clases y evitando salidas corruptas en la interfaz.
