---
target: todos los archivos HTML del proyecto (src/main/resources/templates; 26 vistas)
total_score: 14
max_score: 40
na_heuristics: 
p0_count: 0
p1_count: 3
target_identity: "file:C:\\Users\\Angelica\\Documents\\marcos\\Proyecto_Biblioteca_Marcos_Web\\src\\main\\resources\\templates"
timestamp: 2026-10-19T18-09-33Z
slug: src-main-resources-templates
---
# Crítica de diseño: todas las plantillas HTML

## Método

Method: dual-agent (A: /root/critique_all_design · B: /root/critique_detector_26)

## Evaluación de especificidad

Las plantillas originales de Bibliot. conservan señales propias de una biblioteca: catálogo en español, paleta papel/taupe/tinta, DM Sans y Playfair Display, y una separación visual entre lector y personal. Las 15 páginas nuevas completan visualmente varios huecos, pero la mayoría usa la misma barra Bootstrap y tarjeta blanca centrada. `roles.html` y `configuracion.html` parecen páginas públicas aunque son tareas administrativas. La identidad y la navegación se fragmentan entre tres estructuras.

## Puntuación de heurísticas

| # | Heurística | Puntuación | Problema principal |
|---|---|---:|---|
| 1 | Visibilidad del estado del sistema | 2/4 | Algunos estados se aclaran con alertas, pero el resultado de acciones se conoce después de navegar. |
| 2 | Correspondencia con el mundo real | 2/4 | Etiquetas y vocabulario son claros; los ejemplos de préstamos parecen datos reales. |
| 3 | Control y libertad | 1/4 | Filtros, vistas, menús y paginación de las listas no tienen comportamiento conectado. |
| 4 | Consistencia y estándares | 1/4 | Las páginas nuevas no comparten las estructuras de catálogo o administración ya establecidas. |
| 5 | Prevención de errores | 1/4 | Enlaces y formularios apuntan a rutas todavía sin mapping en Spring. |
| 6 | Reconocimiento antes que recuerdo | 2/4 | Etiquetas y algunas salidas son visibles; faltan estados persistentes y contexto de cuenta. |
| 7 | Flexibilidad y eficiencia | 1/4 | No hay filtros, exportación ni acciones rápidas funcionales en las listas. |
| 8 | Diseño estético y minimalista | 2/4 | El catálogo original es reconocible; muchas páginas nuevas repiten el patrón genérico Bootstrap. |
| 9 | Ayuda para reconocer, diagnosticar y recuperarse de errores | 1/4 | 404 y sin resultados ofrecen salida, pero los recorridos no disponibles se descubren tras pulsar. |
| 10 | Ayuda y documentación | 1/4 | La página de ayuda existe como maqueta, pero no ofrece un canal de atención utilizable. |
| **Total** |  | **14/40 (35 %, deficiente)** |  |

## Señales automatizadas y límites

El detector encontró 16 advertencias en 15 de las 26 plantillas: 14 de `flat-type-hierarchy`, una de `monotonous-spacing` en `registro.html` y una de `broken-image` en `nuevo-libro.html`. Esta última apunta a `#cover-preview`, una imagen oculta que espera selección de archivo, por lo que parece un falso positivo. Las advertencias tipográficas son probablemente falsos positivos parciales: el detector no resuelve el Bootstrap CDN, y las páginas nuevas sí usan clases tipográficas Bootstrap (`h1`, `h2`). El detector también emitió 26 avisos de resolución de stylesheet; no interpreta la etiqueta `<base th:href="@{/}">` de Thymeleaf, que cambia la resolución relativa en el navegador.

No había herramienta de automatización de navegador disponible en esta sesión; no se pudieron tomar capturas ni revisar los overlays. La evaluación visual se basa en las plantillas y el CSS local.

## Impresión general

La cobertura funcional aparente creció, pero la diferencia entre los enlaces y las rutas que Spring realmente sirve convierte varios recorridos en callejones sin salida. La oportunidad principal es integrar las nuevas vistas en un único modelo de navegación coherente y definir los mappings faltantes antes de considerar los recorridos completos.

## Lo que funciona

- El recorrido base de catálogo y administración sigue siendo reconocible, y las nuevas plantillas cubren cuenta, préstamos, edición y estados de error.
- Las vistas de reserva, lectura y formularios indican que los servicios todavía no están conectados en vez de fingir que se completaron.
- `sin-resultados.html` y `404.html` proporcionan acciones concretas para volver al catálogo o iniciar una búsqueda nueva.

## Problemas prioritarios

### [P1] Enlaces nuevos sin rutas Spring

**Dónde:** links hacia `/perfil`, `/mis-prestamos`, `/reservas/confirmacion`, `/lectura/digital`, `/usuarios/nuevo`, `/usuarios/editar`, `/libros/editar`, `/roles`, `/configuracion`, `/ayuda`, `/notificaciones` y `/sin-resultados`.

**Por qué importa:** las plantillas no pueden renderizarse por sí solas en Spring MVC; esos enlaces terminarán en 404 hasta que existan mappings. Las plantillas 403 y 404 también requieren configuración global de errores.

**Corrección:** añadir mappings de página para las vistas, y una condición de resultados vacíos para `/explorar`. Conectar aparte persistencia/servicios cuando estén definidos.

**Comando sugerido:** `/impeccable harden`.

### [P1] Cambia el marco de navegación entre páginas

**Dónde:** `index.html`, `explorar.html` y detalles usan sidebar/topbar; dashboard, usuarios y libros usan shell administrativo; las nuevas vistas repiten una navbar Bootstrap genérica. `roles.html` y `configuracion.html` quedan visualmente dentro del espacio lector.

**Por qué importa:** al seguir un enlace, el usuario pierde orientación y no sabe si está en tareas de lector o de personal.

**Corrección:** reutilizar la navegación pública para cuenta, préstamos, ayuda y notificaciones; reutilizar la navegación administrativa para roles, configuración y formularios de gestión. Mantener términos y errores con tratamiento independiente.

**Comando sugerido:** `/impeccable layout`.

### [P1] Las acciones anticipan servicios que no están disponibles

**Dónde:** el botón “Solicitar reserva” lleva a una pantalla que confirma que no envió la solicitud; “Leer” lleva a un visor vacío. En `usuarios.html`, `libros.html`, `explorar.html` y `dashboard.html`, filtros, toggles, exportación, menús y paginación parecen controles activos sin comportamiento.

**Por qué importa:** las personas descubren las limitaciones después de iniciar una tarea y pueden interpretar resultados estáticos como reales.

**Corrección:** mostrar disponibilidad antes del CTA o deshabilitarlo con explicación; identificar los datos de muestra en contexto y marcar los controles aún sin interacción.

**Comando sugerido:** `/impeccable clarify`.

### [P2] Los ejemplos necesitan contexto junto a cada estado

**Dónde:** `mis-prestamos.html`, `dashboard.html`, `perfil-lector.html` y `notificaciones.html`.

**Por qué importa:** la alerta de datos de ejemplo compite con filas y estados que parecen reales; la página de notificaciones a la vez dice que el servicio no está conectado y que no hay notificaciones.

**Corrección:** usar estados vacíos en datos sin fuente, o marcar cada bloque de demostración como muestra; combinar en notificaciones una única explicación de servicio pendiente.

**Comando sugerido:** `/impeccable clarify`.

### [P2] La navegación de listas todavía contiene una pestaña sin destino de contenido

**Dónde:** la pestaña “Categorías” en `libros.html` envía un parámetro a la misma lista, pero la plantilla/controlador actuales no muestran una vista categorizada.

**Por qué importa:** parece cambiar de sección, pero la página permanece igual.

**Corrección:** implementarla como filtro/vista y reflejar el estado seleccionado, o quitar la pestaña hasta que exista esa función.

**Comando sugerido:** `/impeccable clarify`.

## Personas con riesgo

- **Lector nuevo:** llega a la reserva o lectura y solo entonces descubre que no se realizó o que no hay archivo.
- **Personal bibliotecario:** los controles de exportación, filtros y paginación parecen operativos pero no hacen nada; además, la navegación de roles/configuración parece del área pública.
- **Persona usuaria móvil o con baja visión:** las barras de navegación y las tablas densas pueden ser difíciles de recorrer; el menú móvil del catálogo y los iconos de opciones no tienen resultado asociado.

## Carga cognitiva y recorrido emocional

Explorar ofrece búsqueda, búsqueda avanzada, cinco filtros y selector de vista al mismo tiempo; el botón de búsqueda avanzada no revela contenido. La navegación nueva también salta entre tres marcos visuales. El inicio y las portadas generan expectativa; al pulsar reservar o leer, la pantalla revela que la función no está conectada, creando una caída al final del recorrido. Los mensajes son honestos, pero conviene advertirlo antes de la acción.

## Observaciones menores

- `confirmacion-reserva.html` tiene un título que suena a confirmación aunque el encabezado aclara que no se envió nada.
- `ayuda.html` explica que los canales de soporte deben ser aportados por la biblioteca, por lo que no permite pedir ayuda todavía.
- Los botones de guardar en edición/alta están desactivados, pero no explican junto al control por qué no se pueden usar.

## Preguntas para decidir el siguiente paso

1. ¿Qué navegación debe ser el marco común de las páginas de lector y cuál el de las páginas administrativas?
2. ¿Prefieres que “Solicitar reserva” y “Leer” estén deshabilitados hasta conectar los servicios, o que sigan llevando a una maqueta informativa?
3. ¿Los datos actuales de préstamos y reportes deben mostrarse como ejemplos visibles o sustituirse por estados vacíos hasta disponer de datos reales?
