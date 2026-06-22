# Roadmap del léxico LogicScript

Plan acordado para dejar de inflar `core.lgs` con conjugaciones y ampliar cobertura del curso.

---

## Visión: dos capas

```text
┌─────────────────────────────────────────┐
│  PATRONES SEMÁNTICOS (pattern)          │  Estructura: si X Y → (X → Y)
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│  CAPA LÉXICA                            │  Forma: estudio → estudiar
│  1. lemma (excepciones)                 │
│  2. NormalizadorMorfologico (reglas)    │
│  3. identidad (fallback)                │
└─────────────────────────────────────────┘
```

Los `pattern` **no** reemplazan los `lemma` ni la morfología: resuelven problemas distintos.

---

## Fase A — Normalizador morfológico ✅

**Estado:** implementado en v0.4.

**Entregables:**

- `NormalizadorMorfologico.java`
- Integración en `BaseConocimiento`
- `core.lgs` reducido (lemma solo irregulares)
- Casos `morph_*` en regresión
- `docs/LogicScript/NormalizadorMorfologico.md`

**Criterio de éxito:** frases con verbos regulares -ar y casos como *si duermo descanso* sin bloques `lemma` de dormir.

---

## Fase B — Más patrones semánticos ⏳

**Objetivo:** frases del curso que fallan por **estructura**, no por palabras.

Candidatos:

- *si llueve o solea salgo* (antecedente ∨ elíptico)
- Composición `, cuando …` entre bloques
- Otros que fallen en checklist del profesor

**Criterio:** harness + lista manual del curso en verde.

---

## Fase C — `lexrule` en `.lgs` 📋

**Objetivo:** reglas morfológicas editables sin recompilar Java.

Ejemplo conceptual:

```text
lexrule sufijo -o infinitivo -ar si raiz_vocal
lexrule sufijo -o infinitivo -ir si raiz_termina rm
lemma apruebo -> aprobar
```

**Cuándo:** después de validar Fase A en clase; migrar reglas estables de Java a `.lgs`.

---

## Mantenimiento de `core.lgs` tras Fase A

| Añadir `lemma` cuando… | No añadir cuando… |
|------------------------|-------------------|
| Verbo irregular (*apruebo*, *salgo*) | Presente -ar regular (*estudio*, *estudian*) |
| Cambio vocálico (*duerme* si falla) | *practico*, *trabajo*, *descanso* |
| Sustantivo fijo (*gorra*) | Infinitivo ya reconocible |
| Locución (*solea* → *hacer_sol*) | |

---

## Referencias

- `NormalizadorMorfologico.md` — detalle técnico Fase A
- `Lemma.md` — directiva `lemma`
- `Pattern.md` — directiva `pattern`
