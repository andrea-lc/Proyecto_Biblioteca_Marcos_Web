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

Las tarjetas usan sombras suaves pero perceptibles para separarse del fondo, con bordes cálidos como apoyo. Los campos permanecen planos y usan borde y tratamiento de foco. El foco visible se expresa con un contorno contrastado separado de las sombras de elevación.

### Shadow Vocabulary
- **Tarjeta suave** (`0 2px 8px rgb(33 27 22 / 5%)`): paneles de perfil y superficies de datos.
- **Tarjeta elevada** (`0 8px 24px rgba(47, 39, 28, .06)`): tarjetas Bootstrap en páginas con fondo cálido.
- **Foco de campo** (`0 0 0 .2rem rgba(120, 107, 85, .2)`): estado de foco en campos; acompáñalo del borde de acento.

**The Card-Only Elevation Rule.** Reserva las sombras de elevación para tarjetas y paneles; no conviertas cada campo o sección en una superficie flotante.

## Shapes

La forma es compacta y funcional: controles y portadas suelen tener esquinas pequeñas, mientras que navegación y tarjetas usan radios moderados. Los indicadores de estado pueden usar forma de píldora. Los bordes son finos y cálidos; el foco visible tiene un contorno más amplio y separado para seguir siendo reconocible.

## Components

Los componentes son cálidos y contenidos: su forma acompaña la tarea y deja protagonismo al título, la portada o los datos.

### Buttons
- **Shape:** esquinas compactas (5px).
- **Primary:** acento topo, texto blanco y altura mínima de 38px.
- **Book-form action:** variante arena más oscura, texto blanco y altura mínima de 42px.
- **Hover / Focus:** el botón primario oscurece su fondo; el foco usa contorno visible con separación.
- **Secondary:** contorno o estilo Bootstrap para acciones de retorno y cancelación.

### Cards / Containers
- **Corner Style:** radio moderado (8px) para tarjetas de datos; las portadas usan esquinas compactas (4px).
- **Background:** blanco sobre fondos cálidos o superficies claras.
- **Shadow Strategy:** sombra suave pero perceptible para elevar tarjetas; véase Elevation & Depth.
- **Border:** borde cálido y discreto cuando la superficie lo necesita.
- **Internal Padding:** alrededor de 24px en tarjetas de datos; se reduce en móvil.

### Inputs / Fields
- **Style:** fondo claro, borde arena, esquinas compactas (3–6px según la vista) y altura aproximada de 42–44px.
- **Focus:** borde de acento, halo suave y contorno visible separado.
- **Error / Disabled:** conserva la estructura del control y comunica el estado con colores semánticos o menor opacidad; no uses solo color si el estado afecta una acción.

### Navigation
- **Style:** navegación lateral en escritorio para administración y catálogo, con el acento topo en el elemento activo.
- **Mobile:** las barras laterales se convierten en filas horizontales; la navegación administrativa puede desplazarse horizontalmente y la navegación inferior del catálogo se oculta.
- **Typography:** DM Sans, con contraste de peso y fondo en el estado activo.

### Book Covers
- Las portadas mantienen proporción vertical, borde discreto y radio compacto. En catálogo forman una cuadrícula; en el detalle se integran en un resumen de libro junto con autor y metadatos.

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
