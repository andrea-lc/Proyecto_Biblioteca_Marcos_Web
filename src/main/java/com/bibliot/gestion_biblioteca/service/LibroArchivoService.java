package com.bibliot.gestion_biblioteca.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;

@Service
public class LibroArchivoService {

    private static final long MAX_IMAGEN_BYTES = 5L * 1024 * 1024;
    private static final long MAX_PDF_BYTES = 50L * 1024 * 1024;
    private static final int MAX_TEXTO_EXTRAIDO_CHARS = 5_000_000;

    private final Path raizAlmacenamiento;
    private final Tika detector = new Tika();

    public LibroArchivoService(@Value("${bibliot.storage.location:./storage/uploads}") String location) {
        this.raizAlmacenamiento = Path.of(location).toAbsolutePath().normalize();
    }

    public ArchivoGuardado guardarImagen(MultipartFile imagen) {
        if (imagen == null || imagen.isEmpty()) return null;
        validarTamano(imagen, MAX_IMAGEN_BYTES, "La imagen no debe superar 5 MB.");
        String tipo = detectarTipo(imagen);
        String extension = switch (tipo) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La portada debe ser una imagen JPEG, PNG o WebP.");
        };
        Path destino = raizAlmacenamiento.resolve("covers").resolve(UUID.randomUUID() + extension);
        copiar(imagen, destino);
        return new ArchivoGuardado("/uploads/covers/" + destino.getFileName(), destino, imagen.getSize(), tipo, null);
    }

    public ArchivoGuardado guardarPdfYExtraerTexto(MultipartFile pdf) {
        if (pdf == null || pdf.isEmpty()) return null;
        validarTamano(pdf, MAX_PDF_BYTES, "El PDF no debe superar 50 MB.");
        if (!"application/pdf".equals(detectarTipo(pdf))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo seleccionado no es un PDF válido.");
        }

        Path destino = raizAlmacenamiento.resolve("pdfs").resolve(UUID.randomUUID() + ".pdf");
        copiar(pdf, destino);
        try (InputStream stream = Files.newInputStream(destino)) {
            BodyContentHandler contenido = new BodyContentHandler(MAX_TEXTO_EXTRAIDO_CHARS);
            Metadata metadata = new Metadata();
            String nombreOriginal = pdf.getOriginalFilename();
            if (nombreOriginal != null) {
                metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, Path.of(nombreOriginal).getFileName().toString());
            }
            new AutoDetectParser().parse(stream, contenido, metadata);
            String texto = contenido.toString().trim();
            if (texto.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No se encontró texto extraíble en el PDF. Los documentos escaneados requieren OCR.");
            }
            return new ArchivoGuardado("/uploads/pdfs/" + destino.getFileName(), destino, pdf.getSize(),
                    "application/pdf", texto);
        } catch (ResponseStatusException ex) {
            eliminar(destino);
            throw ex;
        } catch (IOException | SAXException | TikaException | RuntimeException ex) {
            eliminar(destino);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se pudo leer el texto del PDF. Verifica que el archivo no esté dañado o protegido.", ex);
        }
    }

    public void eliminar(ArchivoGuardado archivo) {
        if (archivo != null) eliminar(archivo.path());
    }

    public void eliminarArchivoLocal(String url) {
        if (url == null || url.isBlank()) return;
        String subdirectorio;
        String prefijo;
        if (url.startsWith("/uploads/pdfs/")) {
            subdirectorio = "pdfs";
            prefijo = "/uploads/pdfs/";
        } else if (url.startsWith("/uploads/covers/")) {
            subdirectorio = "covers";
            prefijo = "/uploads/covers/";
        } else {
            return;
        }
        String nombre = url.substring(prefijo.length());
        if (!nombre.matches("[A-Za-z0-9_-]+\\.(pdf|jpg|png|webp)")) return;
        Path carpeta = raizAlmacenamiento.resolve(subdirectorio).normalize();
        Path archivo = carpeta.resolve(nombre).normalize();
        if (archivo.startsWith(carpeta)) eliminar(archivo);
    }

    public Resource abrirPdfLocal(String archivoUrl) {
        if (archivoUrl == null || archivoUrl.isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Este libro no tiene un PDF local disponible.");
        }
        try {
            URI uri = new URI(archivoUrl);
            String ruta = uri.getPath();
            String prefijo = "/uploads/pdfs/";
            if (ruta == null || !ruta.startsWith(prefijo)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El libro no tiene un PDF local disponible.");
            }

            String nombre = ruta.substring(prefijo.length());
            if (!nombre.matches("[A-Za-z0-9_-]+\\.pdf")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La ruta del PDF no es válida.");
            }

            Path directorioPdf = raizAlmacenamiento.resolve("pdfs").normalize();
            Path archivo = directorioPdf.resolve(nombre).normalize();
            if (!archivo.startsWith(directorioPdf) || !Files.isRegularFile(archivo)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el archivo PDF almacenado.");
            }
            return new FileSystemResource(archivo);
        } catch (URISyntaxException | IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La ruta del PDF no es válida.", ex);
        }
    }

    private String detectarTipo(MultipartFile archivo) {
        try (InputStream stream = archivo.getInputStream()) {
            return detector.detect(stream, nombreSeguro(archivo.getOriginalFilename()));
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo leer el archivo enviado.", ex);
        }
    }

    private void validarTamano(MultipartFile archivo, long maximo, String mensaje) {
        if (archivo.getSize() > maximo) throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, mensaje);
    }

    private String nombreSeguro(String nombre) {
        if (nombre == null || nombre.isBlank()) return "archivo";
        try {
            return Path.of(nombre).getFileName().toString().toLowerCase(Locale.ROOT);
        } catch (RuntimeException ex) {
            return "archivo";
        }
    }

    private void copiar(MultipartFile archivo, Path destino) {
        try {
            Files.createDirectories(destino.getParent());
            try (InputStream stream = archivo.getInputStream()) {
                Files.copy(stream, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            eliminar(destino);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo guardar el archivo en el almacenamiento local.", ex);
        }
    }

    private void eliminar(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
            // La limpieza es auxiliar; el error original se conserva.
        }
    }

    public record ArchivoGuardado(String url, Path path, long tamano, String mimeType, String textoExtraido) {}
}
