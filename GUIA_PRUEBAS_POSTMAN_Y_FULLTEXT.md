# Guía de pruebas del proyecto Biblioteca

Esta guía propone un recorrido manual para probar la API con Postman: autenticación, permisos, CRUD, catálogo, filtros y paginación, PDF e indexación full text, circulación, notificaciones y reportes.

> Ejecuta las pruebas de escritura solo en una base de datos local/de desarrollo. Las solicitudes `POST`, `PUT` y `DELETE` crean o modifican datos. No uses credenciales reales en capturas ni subas tokens al repositorio.

## 1. Preparar el entorno

1. Inicia MySQL y confirma que existe la base de datos configurada para el proyecto. Usa la configuración local del entorno; no publiques credenciales.
2. Desde la raíz del proyecto inicia Spring Boot:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   En Linux/macOS:

   ```bash
   ./mvnw spring-boot:run
   ```

3. En Postman crea un Environment llamado `Biblioteca local` con estas variables:

   | Variable | Valor inicial |
   | --- | --- |
   | `baseUrl` | `http://localhost:8080` |
   | `tokenAdmin` | vacío |
   | `tokenBibliotecario` | vacío |
   | `tokenLector` | vacío |
   | `idAutor` | vacío |
   | `idEditorial` | vacío |
   | `idCategoria` | vacío |
   | `idLibro` | vacío |
   | `idEjemplar` | vacío |
   | `idUsuarioLector` | vacío |
   | `idPrestamo` | vacío |
   | `idReserva` | vacío |
   | `idDevolucion` | vacío |

4. Para una solicitud protegida agrega el encabezado:

   ```text
   Authorization: Bearer {{tokenAdmin}}
   ```

   Sustituye `tokenAdmin` por `tokenBibliotecario` o `tokenLector` según el caso. Las solicitudes JSON también deben llevar `Content-Type: application/json` y normalmente `Accept: application/json`.

## 2. Iniciar sesión y comprobar permisos

### Obtener un token

`POST {{baseUrl}}/auth/login`

Body → raw → JSON:

```json
{
  "email": "correo-de-prueba",
  "password": "contraseña-de-prueba"
}
```

Una autenticación válida devuelve `200`. La respuesta es el token JWT como texto JSON. En la pestaña **Tests** de Postman puedes guardarlo en `tokenAdmin`:

```javascript
pm.test("login correcto", function () {
  pm.response.to.have.status(200);
  pm.environment.set("tokenAdmin", pm.response.json());
});
```

Repite el login con cuentas de bibliotecario y lector, cambiando en el script el nombre de la variable. Las cuentas deben existir en la base de desarrollo; no se incluyen credenciales por defecto en esta guía.

### Matriz básica de autorización

| Solicitud | Admin | Bibliotecario | Lector | Sin token |
| --- | ---: | ---: | ---: | ---: |
| `GET /api/admin/libros` | 200 | 403 | 403 | 401 |
| `GET /api/bibliotecario/libros` | 200 | 200 | 403 | 401 |
| `GET /api/catalogo/libros` | 200 | 200 | 200 | 200 |
| `GET /api/lector/me` | 200 | 403 | 200 | 401 |

Los errores de autorización se responden como `401` (sin autenticación válida) o `403` (rol sin permiso). El catálogo público permite consultas `GET`.

## 3. CRUD de catálogos y usuarios

Para cada recurso, guarda el identificador recibido en el `POST`; úsalo en las solicitudes `GET`, `PUT` y `DELETE`. Un CRUD típico espera `200` al leer/actualizar, `201` al crear y `204` al borrar.

### Autores, editoriales, categorías y roles

| Recurso | Colección | Lectura individual | CRUD |
| --- | --- | --- | --- |
| Autores | `/api/admin/autores` | `/api/admin/autores/{{idAutor}}` | Admin: `GET`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Editoriales | `/api/admin/editoriales` | `/api/admin/editoriales/{{idEditorial}}` | Admin: `GET`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Categorías | `/api/admin/categorias` | `/api/admin/categorias/{{idCategoria}}` | Admin: `GET`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Roles | `/api/admin/roles` | `/api/admin/roles/{{idRol}}` | Admin: `GET`, `POST`, `PUT /{id}`, `DELETE /{id}` |

El bibliotecario puede consultar roles con `GET /api/bibliotecario/roles`. Para consultar autores/editoriales como bibliotecario usa `/api/bibliotecario/autores` y `/api/bibliotecario/editoriales`; son de solo lectura.

Ejemplos de creación:

```json
// POST /api/admin/autores
{
  "nombres": "Autora Prueba",
  "apellidos": "Full Text",
  "nacionalidad": "Colombiana",
  "biografia": "Registro temporal para pruebas."
}
```

```json
// POST /api/admin/editoriales
{
  "nombre": "Editorial Temporal QA",
  "pais": "Colombia",
  "sitioWeb": "https://example.com"
}
```

```json
// POST /api/admin/categorias
{
  "nombre": "Categoría QA",
  "descripcion": "Categoría temporal para pruebas."
}
```

Después del `POST`, guarda el ID automáticamente. Por ejemplo, en **Tests** para autor:

```javascript
pm.test("autor creado", function () {
  pm.response.to.have.status(201);
  pm.environment.set("idAutor", pm.response.json().idAutor);
});
```

Editorial usa `idEditorial`; categoría usa `idCategoria`. Para un `PUT`, envía el DTO actualizado a la URL con el ID. Prueba también que el borrado de un autor/editorial/categoría asociada a libros responda de forma controlada; no borres datos compartidos del entorno.

### Usuarios

- Lectura para admin/bibliotecario: `GET /api/admin/usuarios` o `GET /api/bibliotecario/usuarios`.
- CRUD solo admin: `POST /api/admin/usuarios`, `PUT /api/admin/usuarios/{id}`, `DELETE /api/admin/usuarios/{id}`.
- Consultas: `/api/admin/usuarios/{id}`, `/api/admin/usuarios/email/{email}` y, para admin, `/api/admin/usuarios/dni/{dni}`.

El alta requiere DNI de 8 dígitos, teléfono de 9 dígitos, correo válido, `idRol` existente y contraseña de al menos 8 caracteres:

```json
{
  "nombres": "Lector QA",
  "apellidos": "Temporal",
  "dni": "87654321",
  "email": "lector.qa@example.com",
  "telefono": "300123456",
  "direccion": "Dirección temporal",
  "idRol": 3,
  "password": "PruebaLocal123",
  "activo": true
}
```

Consulta primero `GET /api/admin/roles` para confirmar el ID real del rol lector; no supongas que siempre es `3`. Usa un correo/DNI únicos en cada ejecución. No reutilices la contraseña del ejemplo en ningún entorno real.

## 4. Libros: CRUD, filtros y paginación

El CRUD administrativo está en `/api/admin/libros`; el bibliotecario tiene lectura en `/api/bibliotecario/libros`. El `GET` sin `/pagina` se conserva como lista completa para las vistas existentes. Para probar la paginación de la interfaz/API usa:

```text
GET {{baseUrl}}/api/admin/libros/pagina?page=0&size=20
GET {{baseUrl}}/api/bibliotecario/libros/pagina?page=0&size=20
```

La respuesta incluye `content`, `page`, `size`, `totalElements`, `totalPages`, `first` y `last`. Prueba `size=10`, las páginas siguientes, la última página y una búsqueda vacía.

Los filtros del listado administrativo se combinan con `q` y paginación:

```text
GET {{baseUrl}}/api/admin/libros/pagina?page=0&size=10&q=bosque&autorId={{idAutor}}&editorialId={{idEditorial}}&categoriaId={{idCategoria}}&estado=Disponible&formato=DIGITAL
```

Valores admitidos para `formato`: `FISICO`, `DIGITAL`, `AMBOS`, `SIN_FORMATO`. Para obtener IDs válidos de filtros, consulta `GET /api/catalogo/filtros`.

El catálogo público tiene filtros propios: `GET /api/catalogo/libros` admite `q`, `autorId`, `categoriaId`, `editorialId`, `categoria`, `paginas` (`menos200`, `200a400`, `mas400`), `anioDesde` y `anioHasta`. Puedes combinar varios y comprobar que cada libro devuelto cumple todos los criterios.

Ejemplo de alta de libro JSON:

```json
{
  "titulo": "Libro temporal QA",
  "isbn": null,
  "paginas": 175,
  "fechaPublicacion": "2020-05-15",
  "estado": "Disponible",
  "descripcion": "Registro temporal para CRUD.",
  "contenidoTexto": "Texto de prueba no confidencial.",
  "idEditorial": {{idEditorial}},
  "autorIds": [{{idAutor}}],
  "categoriaIds": [{{idCategoria}}]
}
```

`POST /api/admin/libros` crea el registro; guarda `idLibro`. Luego prueba `GET /api/admin/libros/{id}`, `PUT /api/admin/libros/{id}` con el mismo DTO modificado y finalmente `DELETE /api/admin/libros/{id}` solo si es un registro descartable. En una prueba CRUD, confirma cada cambio consultando de nuevo el recurso.

## 5. Carga de portada/PDF y búsqueda full text

### Crear un libro con un PDF de texto

La ruta de carga es `POST /api/admin/libros/con-archivos`, con `multipart/form-data`:

| Clave | Tipo Postman | Contenido |
| --- | --- | --- |
| `datos` | Text | JSON de `LibroDtos.Request` como texto; asigna `application/json` a esta parte si Postman muestra esa opción |
| `imagen` | File, opcional | JPEG, PNG o WebP de hasta 5 MB |
| `archivoPdf` | File, opcional | PDF válido de hasta 50 MB con texto seleccionable |

Ejemplo del contenido de `datos`:

```json
{
  "titulo": "Prueba de indexación 4729",
  "isbn": null,
  "paginas": 1,
  "fechaPublicacion": "2024-01-10",
  "estado": "Disponible",
  "descripcion": "Documento temporal de control.",
  "contenidoTexto": "",
  "idEditorial": {{idEditorial}},
  "autorIds": [{{idAutor}}],
  "categoriaIds": [{{idCategoria}}]
}
```

No escribas manualmente el encabezado `Content-Type` global para multipart; deja que Postman incluya el `boundary`. Al terminar, la respuesta debe incluir un `idLibro` y una `archivoUrl` si enviaste PDF. La URL local suele tener el patrón `/uploads/pdfs/...`; la imagen `/uploads/covers/...`.

### Prompt para generar el PDF de prueba

No necesitas descargar un libro ni usar un documento con derechos de autor. Pega este prompt en una herramienta que pueda generar un PDF descargable:

> Genera un PDF de una sola página, tamaño A4, llamado `prueba-fulltext-biblioteca.pdf`. Debe ser un PDF real con texto seleccionable y copiable, no una imagen escaneada ni un documento protegido. En el cuerpo incluye exactamente el identificador único `TESTFULLTEXTORQUIDEA4729` una sola vez y también esta frase: “La conservación de orquídeas nativas requiere proteger los bosques húmedos y registrar cada especie.” No agregues ese identificador al nombre del archivo, título visible, portada ni metadatos. Usa diseño sencillo, sin imágenes y sin información personal. Devuelve el archivo PDF para descargar.

El identificador único debe aparecer solo en el texto del PDF: así reduces falsos positivos de título y metadatos.

### Consultar el índice

Usa la búsqueda del catálogo público, que es la ruta que invoca Hibernate Search:

```text
GET {{baseUrl}}/api/catalogo/libros?q=TESTFULLTEXTORQUIDEA4729
```

La respuesta debe incluir el libro de prueba. Prueba también `q=conservacion` para comprobar el análisis de tildes. La configuración usa minúsculas, plegado de acentos y stemming en español. La búsqueda simple combina los términos; usa el token único para la comprobación más determinista.

`GET /api/catalogo/buscar?titulo=...` y `GET /api/admin/libros?q=...` son búsquedas por título, no sirven para verificar extracción del texto del PDF. Los PDFs escaneados sin capa de texto no pasan esta prueba: esta aplicación no hace OCR.

Si el resultado no aparece:

1. Abre el PDF y confirma que puedes seleccionar/copiar el identificador.
2. Confirma que el alta respondió `201` y que devolvió una `archivoUrl`.
3. Revisa los logs de Tika/Hibernate Search y que el libro se haya guardado.
4. Al iniciar, el proyecto ejecuta una reindexación masiva por defecto. Espera a que termine antes de probar, sobre todo si hay muchos registros. El índice local está bajo `storage/lucene-index`.
5. No borres el índice como primer intento; conserva el log del error y la respuesta HTTP para diagnosticar.

Prueba negativa: vuelve a enviar una imagen como `archivoPdf`, un PDF dañado o un PDF sin texto. Se espera rechazo `400`; un archivo por encima del límite debe rechazarse. Verifica también que un archivo no aceptado no deje un libro creado a medias.

## 6. Ejemplares, préstamos, reservas y devoluciones

Haz este recorrido con registros temporales y IDs devueltos por la API:

1. Crea un ejemplar físico con `POST /api/admin/ejemplares` (o la ruta bibliotecaria equivalente). DTO:

   ```json
   {
     "idLibro": {{idLibro}},
     "codigoInventario": "QA-INV-4729",
     "ubicacion": "Estante QA",
     "estado": "Disponible",
     "fechaAdquisicion": "2024-01-10"
   }
   ```

2. Consulta ejemplares con `GET /api/admin/ejemplares` o `GET /api/bibliotecario/ejemplares`; guarda `idEjemplar`.
3. Como bibliotecario, crea un préstamo con `POST /api/bibliotecario/prestamos`:

   ```json
   {
     "idUsuario": {{idUsuarioLector}},
     "idEjemplar": {{idEjemplar}},
     "fechaPrestamo": "2025-01-15T10:00:00",
     "fechaVencimiento": "2027-01-15T10:00:00",
     "estado": "Activo"
   }
   ```

   Ajusta las fechas: `fechaPrestamo` no puede ser futura y `fechaVencimiento` debe ser actual o futura. Verifica `GET /api/bibliotecario/prestamos`, `/activos` y `/api/lector/me/prestamos` con el token del lector.

4. Prueba reserva con `POST /api/lector/reservar-libro`, usando el propio ID de usuario lector y un `idLibro` válido. El DTO requiere `fechaReserva`, `fechaExpiracion` y `estado`; fechas ISO-8601 `LocalDateTime`. Comprueba con `GET /api/lector/me/reservas` y `GET /api/bibliotecario/reservas`.
5. Como bibliotecario registra devolución con `POST /api/bibliotecario/devoluciones` (`idPrestamo`, `fechaDevolucion`, `observacion`) y consulta `/api/bibliotecario/devoluciones/prestamo/{idPrestamo}`.
6. Las sanciones CRUD están en `/api/bibliotecario/sanciones`; usa una devolución existente y fechas ISO (`yyyy-MM-dd`) para `fechaInicio` y `fechaFin`.
7. La pantalla de circulación usa `GET /api/bibliotecario/circulacion/prestamos?page=0&size=25` y `/reservas`; actualiza estado con `PATCH /api/bibliotecario/circulacion/prestamos/{id}/estado` o `/reservas/{id}/estado`, body `{"estado":"..."}`.

Para el lector, la API ofrece `GET /api/lector/me`, `/prestamos` y `/reservas`; el ID se obtiene del usuario autenticado, por eso es preferible este grupo a consultar datos ajenos.

## 7. Notificaciones, reportes y monitoreo

- Con token lector: `GET /api/notificaciones` y `GET /api/notificaciones/no-leidas`. Marca una notificación con `POST /api/notificaciones/{id}/leida`; se espera `204` y que el contador disminuya.
- Con token admin, crea un libro y confirma la notificación de nuevo libro para lectores. El stream está en `GET /api/notificaciones/stream` y responde como `text/event-stream`; en Postman puedes comprobar conexión/headers, pero la prueba de eventos en vivo es más clara desde la interfaz.
- Para vencimientos, el proceso revisa cada minuto. Prueba con un préstamo de vencimiento cercano o vencido en una base local y espera el ciclo del scheduler. Comprueba notificaciones de staff y lector. La notificación informa; no uses un usuario real ni un entorno productivo para esta prueba.
- Descargas: `GET /reportes/libros/pdf`, `/reportes/libros/excel`, `/reportes/usuarios/pdf` y `/reportes/usuarios/excel`. Verifica `200`, `Content-Type` y `Content-Disposition`, guarda el body como archivo y abre el PDF/XLSX.
- Dashboard: con credencial autorizada consulta `GET /api/bibliotecario/reportes/dashboard`.
- Actuator: `GET /actuator/health` y métricas documentadas por la app; están bajo autorización de administrador.

**Nota de permisos detectada al revisar el código:** actualmente las rutas `/reportes/**` no tienen un matcher específico en `SecurityConfig` y caen en `.anyRequest().permitAll()`. Por tanto, prueba y trata esos endpoints como públicamente accesibles hasta que se decida y corrija la política. No uses en ellos datos confidenciales.

## 8. Pruebas negativas y checklist de aceptación

Repite algunos casos con datos inválidos para confirmar validación y permisos:

- Login con contraseña incorrecta → `401`.
- Llamada admin sin token → `401`; con token lector/bibliotecario a ruta admin → `403`.
- Crear autor sin nombres, libro sin título, email inválido, DNI/teléfono con longitud incorrecta → `400`.
- Crear libro con editorial/autor/categoría inexistente → comprobar error controlado y que no quede una relación parcial.
- Buscar ID inexistente → `404` o el código de error definido por ese endpoint.
- Intentar imagen no JPEG/PNG/WebP, PDF que no sea PDF real, PDF protegido/dañado o texto escaneado → `400`; exceso de tamaño → `413`.
- Página negativa o tamaño fuera de rango en `/pagina` → `400`.
- Borrar recursos vinculados a préstamos/libros → validar que no se rompan relaciones; usar solo datos temporales.

Antes de cerrar cada recorrido, confirma:

1. El status HTTP es el esperado y el JSON contiene el identificador.
2. `GET` posterior refleja el alta/cambio; tras `DELETE` el recurso ya no existe.
3. Las búsquedas full text encuentran el texto extraído y no solo el título.
4. Filtros combinados reducen resultados correctamente y la paginación conserva `q` y filtros.
5. Los permisos se mantienen por rol.
6. Limpias únicamente los registros y archivos de prueba identificados por sus códigos `QA`.

## Endpoints principales por área

| Área | Rutas principales |
| --- | --- |
| Auth | `POST /auth/login`, `POST /registro` (registro HTML) |
| Libros | `/api/admin/libros`, `/api/admin/libros/pagina`, `/api/admin/libros/con-archivos`, `/api/bibliotecario/libros` |
| Full text/catalogo público | `/api/catalogo/libros`, `/api/catalogo/buscar`, `/api/catalogo/filtros` |
| Autores/editoriales | `/api/admin/autores`, `/api/admin/editoriales`; lectura de bibliotecario bajo `/api/bibliotecario/...` |
| Usuarios/roles | `/api/admin/usuarios`, `/api/bibliotecario/usuarios`, `/api/admin/roles`, `/api/bibliotecario/roles` |
| Circulación | `/api/bibliotecario/prestamos`, `/api/bibliotecario/reservas`, `/api/bibliotecario/devoluciones`, `/api/bibliotecario/sanciones` |
| Cuenta lector | `/api/lector/me`, `/api/lector/me/prestamos`, `/api/lector/me/reservas` |
| Notificaciones | `/api/notificaciones`, `/api/notificaciones/no-leidas`, `/api/notificaciones/stream` |

