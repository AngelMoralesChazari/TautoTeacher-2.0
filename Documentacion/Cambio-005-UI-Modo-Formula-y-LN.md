# Cambio 005 - UI: modo Fórmula lógica y LN (lenguaje natural)

Fecha: 2026-04-07

## Objetivo

Mantener la entrada por fórmula lógica como modo por defecto (con símbolos y botón de verificación) y añadir un apartado explícito para **LN (lenguaje natural)**, sin quitar las tarjetas superiores (Análisis / Visualización / Educativo).

## Archivos modificados

1. `src/main/java/tautoteacher2/ui/PanelEntradaNatural.java`
2. `src/main/java/tautoteacher2/ui/VentanaPrincipal.java` (texto de instrucciones)
3. `src/main/java/tautoteacher2/ui/TautoTeacherApp.java` (demo de resultado según modo)

## Cambios realizados

- Selector con radio buttons: **Fórmula lógica** (predeterminado) y **LN (lenguaje natural)**.
- `CardLayout` interno:
  - Tarjeta fórmula: panel de símbolos lógicos + área de texto + botón compartido **Verificar Tautología**.
  - Tarjeta LN: texto de ayuda + área para enunciado en español + mismo botón (pipeline NLP pendiente).
- API en `PanelEntradaNatural`:
  - `ModoEntrada getModoEntrada()`
  - `getTexto()` devuelve el contenido del modo activo
  - `getTextoFormula()` / `getTextoLenguajeNatural()` para uso futuro del servicio.

## Cambios realizados en

- `PanelEntradaNatural.java`: implementación del doble modo de entrada.
- `VentanaPrincipal.java`: instrucciones actualizadas para ambos modos.
- `TautoTeacherApp.java`: la simulación muestra el modo activo en el resultado.

## Justificación

Separa claramente la experiencia “fórmula ya formal” de la futura “texto en español → fórmula”, alineado con el plan v2.0, sin romper la disposición general de la ventana.
