---
name: Bibliot.
description: Un sistema editorial cálido que equilibra el descubrimiento de libros y la gestión de biblioteca.
colors:
  paper: "#f7f1e7"
  primary: "#786b55"
  active: "#665941"
  ink: "#241f1a"
  slate: "#58636d"
  divider: "#ded8ce"
typography:
  display:
    fontFamily: "Playfair Display, Georgia, serif"
    fontSize: "2.7rem"
    fontWeight: 800
    lineHeight: 1
    letterSpacing: "-1.5px"
  headline:
    fontFamily: "Playfair Display, Georgia, serif"
    fontSize: "clamp(1.5rem, 2.2vw, 2rem)"
    fontWeight: 700
    lineHeight: 1.2
  title:
    fontFamily: "Playfair Display, Georgia, serif"
    fontSize: "1.35rem"
    fontWeight: 700
    lineHeight: 1.3
  body:
    fontFamily: "DM Sans, Arial, sans-serif"
    fontSize: "1rem"
    fontWeight: 400
    lineHeight: 1.5
  label:
    fontFamily: "DM Sans, Arial, sans-serif"
    fontSize: ".9rem"
    fontWeight: 400
rounded:
  xs: "3px"
  sm: "4px"
  control: "6px"
  md: "8px"
  lg: "12px"
  pill: "999px"
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "#ffffff"
    rounded: "5px"
    height: "38px"
  button-sand:
    backgroundColor: "{colors.active}"
    textColor: "#ffffff"
    rounded: "5px"
    height: "42px"
  input:
    backgroundColor: "#ffffff"
    textColor: "{colors.ink}"
    rounded: "{rounded.control}"
    height: "44px"
  nav-active:
    backgroundColor: "{colors.active}"
    textColor: "#ffffff"
    rounded: "{rounded.md}"
    height: "44px"
  card:
    backgroundColor: "#ffffff"
    rounded: "{rounded.md}"
    padding: "24px"
---

# Design System: Bibliot.

## Overview

**Creative North Star: "El archivo editorial cálido"**

Bibliot. combina la lectura editorial con las tareas prácticas de una biblioteca. La tipografía serif de los encabezados aporta carácter de publicación; la sans serif mantiene legibles las búsquedas, los formularios y las tablas. La paleta cálida y los componentes contenidos conectan las vistas públicas y administrativas sin hacerlas parecer la misma superficie.

La composición alterna una navegación de catálogo con portadas de libros y una estructura administrativa con barra lateral, tablas y tarjetas de datos. Las sombras se reservan para dar separación perceptible a las tarjetas; los campos y controles mantienen bordes sencillos. En pantallas estrechas, la navegación administrativa pasa a una fila desplazable, el catálogo mueve su navegación principal arriba y oculta la inferior, y los formularios y paneles se apilan.

**Key Characteristics:**
- Editorial y cálido, con la tipografía de lectura reservada para encabezados.
- Equilibrio entre descubrimiento de libros y operación administrativa.
- Fondos de papel, tinta oscura y acentos topo apagados.
- Tarjetas elevadas con sombras suaves y controles de forma compacta.

## Colors

La paleta efectiva combina tonos inspirados en papel y encuadernación con una tinta oscura y un gris pizarra para la información secundaria. Los nombres describen la función y el carácter de los colores, no crean tonos nuevos.

### Primary
- **Topo de encuadernación** (`primary`): acento de acciones, navegación seleccionada y controles destacados.
- **Encuadernación profunda** (`active`): tono más oscuro para estados activos y acciones de envío que requieren mayor contraste.

### Neutral
- **Papel de archivo** (`paper`): fondo cálido para páginas públicas y superficies administrativas suaves.
- **Tinta sepia** (`ink`): texto principal y encabezados.
- **Pizarra de catálogo** (`slate`): texto secundario, metadatos y contenido auxiliar.
- **Borde de papel** (`divider`): bordes y separadores discretos.

**The Paper-and-Ink Rule.** Mantén los fondos y el texto en los tonos de papel y tinta; reserva el acento topo para acciones y navegación activa.

## Typography

**Display Font:** Playfair Display (with Georgia, serif fallback)  
**Body Font:** DM Sans (with Arial, sans-serif fallback)

**Character:** Playfair Display da a la marca y a los títulos un tono editorial; DM Sans sostiene la lectura continua y la densidad de las tareas administrativas.

### Hierarchy
- **Display** (800, 2.7rem, line-height 1): marca y palabra principal de identidad.
- **Headline** (700, responsive 1.5–2rem, line-height 1.2): títulos destacados de vistas y detalles de libro.
- **Title** (700, 1.35rem, line-height 1.3): títulos de secciones y tarjetas.
- **Body** (400, 1rem, line-height 1.5): descripciones y lectura general.
- **Label** (400, alrededor de .9rem): etiquetas de formularios y metadatos; las tablas reducen el tamaño cuando necesitan mayor densidad.

**The Two-Font Rule.** Usa la serif para identidad y jerarquía editorial; conserva la sans serif para controles, tablas y lectura operativa.

## Layout

Las pantallas administrativas usan una barra lateral fija de 250px y una barra superior; en móvil, la navegación administrativa se vuelve una fila desplazable y el contenido ocupa el ancho disponible. Las vistas de catálogo separan navegación, búsqueda y resultados, con portadas como elementos visuales principales. Los formularios de alta de libro usan una columna para portada y otra para los campos en escritorio, y se apilan en móvil.

Los espacios usan valores responsivos mediante `clamp()` y el sistema de rejilla de Bootstrap; no existe una escala de espaciado propia que deba imponerse a nuevas superficies. Los puntos de adaptación visibles en la hoja de estilos incluyen 1199.98px, 1099.98px, 767.98px y 420px.

## Elevation & Depth

El panel administrativo prioriza **bordes finos de `1px`** (`--border-hairline: #ded8ce`) sobre superficies crema tipo papel (`--surface-card: #fffdf9` sobre `--surface-paper: #f7f1e7`) en lugar de sombras pesadas. Las sombras quedan reducidas a un anclaje mínimo (`--shadow-subtle: 0 1px 2px rgba(36, 31, 26, 0.04)`) y a capas flotantes como modales o popovers (`--shadow-popover`). El foco visible usa `outline: 2px solid var(--accent-deep)` con `outline-offset: 2px` y `--focus-ring`.

### Design Tokens (`:root` en `bibliot.css`)
- **Superficies:** `--surface-paper` (`#f7f1e7`), `--surface-paper-deep` (`#f0e7d8`), `--surface-card` (`#fffdf9`), `--surface-subtle` (`#f4eee3`), `--surface-hover` (`#efe7d9`).
- **Tinta y acentos:** `--ink-primary` (`#241f1a`), `--ink-secondary` (`#3f3830`), `--ink-slate` (`#58636d`), `--accent` (`#786b55`), `--accent-deep` (`#665941`), `--accent-darker` (`#524733`).
- **Estados semánticos (contraste ≥ 4.5:1):** `--status-success-*`, `--status-info-*`, `--status-warning-*`, `--status-danger-*`, `--status-neutral-*`.
- **Radios y movimiento:** `--radius-control` (`8px`), `--radius-card` (`10px`), `--radius-lg` (`12px`), `--radius-pill` (`999px`); `--ease-out` (`cubic-bezier(0.23, 1, 0.32, 1)`), `--dur-fast` (`150ms`), `--dur-base` (`200ms`), `--dur-slow` (`250ms`).

## Components (Sistema `.ed-*` — Panel y Portal)

Todos los componentes del panel administrativo y del portal público están centralizados en `src/main/resources/static/css/bibliot.css` bajo tokens compartidos `:root`, sin estilos inline ni duplicados en HTML:

- **Panel administrativo (`.admin-body`):** `.admin-sidebar` (pergamino profundo `#f0e7d8` con borde `1px`), `.admin-topbar` (translúcida fija con `backdrop-filter: blur(8px)`), `.ed-topbar-back`, `.ed-breadcrumb`, `.ed-page-header`, `.ed-card` / `.report-card`, `.ed-tabs`, `.ed-toolbar`, `.ed-table-wrap` (`tabindex="0" role="region"`, `thead` `position: sticky`, `tabular-nums`), `.ed-row-actions` (visibles en `:hover`, `:focus-within` y `@media (hover: none)`), `.ed-form-shell` / `.ed-form-card` / `.ed-form-section` / `.ed-form-footer`, `.ed-skeleton--*`, `.ed-empty` y `.ed-enter`.
- **Portal del lector (`.catalog-body` y `.ed-portal-page`):** `.catalog-sidebar`, `.catalog-topbar`, `.catalog-hero` con titular `Playfair Display`, `.catalog-search`, `.category-strip` deslizable (`scroll-snap-type`), tarjetas de libro `.featured-book` / `.explore-book` con portada `2:3` y elevación sutil al hover (`@media (hover: hover)`), `.ed-filter-panel`, ficha de detalle `.book-summary`, `.ed-portal-navbar`, `.ed-dl`, `.ed-reader-frame`, `.ed-accordion`, `.ed-portal-toc`, `.ed-legal-section`, `.ed-portal-empty-card` y `.bibliot-notifications-dialog` armonizado para el portal.
- **Acceso y errores (`.ed-auth-page` / `.ed-error-code`):** `.ed-auth-card` con etiquetas visibles, mensajes de error junto al campo, estados `hover` / `focus-visible` / `disabled` / `aria-busy`, y páginas `404` / `403` sobre `.ed-portal-empty-card`.
- **Micro-interacciones y accesibilidad:** Respuesta táctil `:active { transform: scale(0.97) }`, hover condicionado a `@media (hover: hover) and (pointer: fine)`, foco visible de alto contraste, `font-variant-numeric: tabular-nums` en cifras y fechas, y respeto estricto de `@media (prefers-reduced-motion: reduce)`.

## Do's and Don'ts

### Do:
- **Do** mantener la serif para la marca y los encabezados editoriales, y la sans serif para lectura funcional y controles.
- **Do** usar los tonos de papel y tinta como base, el topo como acento y la pizarra para información secundaria.
- **Do** mantener las tarjetas visualmente separadas con sombra suave, sin elevar los campos.
- **Do** conservar el foco visible y adaptar navegación y rejillas al ancho móvil.

### Don't:
- **Don't** introducir colores de acento nuevos sin una función de estado o una decisión de marca.
- **Don't** aplicar sombras de tarjeta a cada control o contenedor.
- **Don't** usar la tipografía serif en tablas, etiquetas o párrafos largos.
- **Don't** ocultar el contorno de foco para conservar una apariencia más limpia.
