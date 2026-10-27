package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
import java.time.*;

/** Contratos REST: las relaciones se representan mediante identificadores. */
public final class ApiDtos {
    private ApiDtos() {
    }

    public record RolDto(Integer idRol, @NotBlank @Size(max = 50) String nombre) {
        public static RolDto from(Rol x) {
            return new RolDto(x.getIdRol(), x.getNombre());
        }

        public Rol toEntity() {
            Rol x = new Rol();
            x.setIdRol(idRol);
            x.setNombre(nombre);
            return x;
        }
    }

    public record AutorDto(Integer idAutor, @NotBlank @Size(max = 100) String nombres,
            @NotBlank @Size(max = 100) String apellidos, @Size(max = 100) String nacionalidad,
            @Size(max = 5000) String biografia) {
        public static AutorDto from(Autor x) {
            return new AutorDto(x.getIdAutor(), x.getNombres(), x.getApellidos(), x.getNacionalidad(),
                    x.getBiografia());
        }

        public Autor toEntity() {
            Autor x = new Autor();
            x.setIdAutor(idAutor);
            x.setNombres(nombres);
            x.setApellidos(apellidos);
            x.setNacionalidad(nacionalidad);
            x.setBiografia(biografia);
            return x;
        }
    }

    public record EditorialDto(Integer idEditorial, @NotBlank @Size(max = 150) String nombre,
            @Size(max = 100) String pais, @URL String sitioWeb) {
        public static EditorialDto from(Editorial x) {
            return new EditorialDto(x.getIdEditorial(), x.getNombre(), x.getPais(), x.getSitioWeb());
        }

        public Editorial toEntity() {
            Editorial x = new Editorial();
            x.setIdEditorial(idEditorial);
            x.setNombre(nombre);
            x.setPais(pais);
            x.setSitioWeb(sitioWeb);
            return x;
        }
    }

    public record CategoriaDto(Integer idCategoria, @NotBlank @Size(max = 100) String nombre,
            @Size(max = 300) String descripcion) {
        public static CategoriaDto from(Categoria x) {
            return new CategoriaDto(x.getIdCategoria(), x.getNombre(), x.getDescripcion());
        }

        public Categoria toEntity() {
            Categoria x = new Categoria();
            x.setIdCategoria(idCategoria);
            x.setNombre(nombre);
            x.setDescripcion(descripcion);
            return x;
        }
    }

    public record EjemplarDto(Integer idEjemplar, @NotNull @Positive Integer idLibro,
            @NotBlank @Size(max = 100) String codigoInventario, @Size(max = 100) String ubicacion,
            @NotBlank @Size(max = 30) String estado, @PastOrPresent LocalDate fechaAdquisicion) {
        public static EjemplarDto from(Ejemplar x) {
            return new EjemplarDto(x.getIdEjemplar(), x.getLibro().getIdLibro(), x.getCodigoInventario(),
                    x.getUbicacion(), x.getEstado(), x.getFechaAdquisicion());
        }

        public Ejemplar toEntity() {
            Ejemplar x = new Ejemplar();
            x.setIdEjemplar(idEjemplar);
            Libro l = new Libro();
            l.setIdLibro(idLibro);
            x.setLibro(l);
            x.setCodigoInventario(codigoInventario);
            x.setUbicacion(ubicacion);
            x.setEstado(estado);
            x.setFechaAdquisicion(fechaAdquisicion);
            return x;
        }
    }

    public record LibroDigitalDto(Integer idLibroDigital, @NotNull @Positive Integer idLibro,
            @Size(max = 255) @URL String archivoUrl, @Size(max = 30) String formato,
            @Positive Long tamanoArchivo, @NotNull Boolean disponible, @PastOrPresent LocalDateTime fechaRegistro) {
        public static LibroDigitalDto from(LibroDigital x) {
            return new LibroDigitalDto(x.getIdLibroDigital(), x.getLibro().getIdLibro(), x.getArchivoUrl(),
                    x.getFormato(), x.getTamanoArchivo(), x.getDisponible(), x.getFechaRegistro());
        }

        public LibroDigital toEntity() {
            LibroDigital x = new LibroDigital();
            x.setIdLibroDigital(idLibroDigital);
            Libro l = new Libro();
            l.setIdLibro(idLibro);
            x.setLibro(l);
            x.setArchivoUrl(archivoUrl);
            x.setFormato(formato);
            x.setTamanoArchivo(tamanoArchivo);
            x.setDisponible(disponible);
            x.setFechaRegistro(fechaRegistro);
            return x;
        }
    }

    public record UsuarioDto(Integer idUsuario, @NotBlank @Size(max = 100) String nombres,
            @NotBlank @Size(max = 100) String apellidos, @NotBlank @Pattern(regexp = "^[0-9]{8}$") String dni,
            @NotBlank @Email @Size(max = 150) String email, @NotBlank @Pattern(regexp = "^[0-9]{9}$") String telefono,
            @Size(max = 200) String direccion, @NotNull @Positive Integer idRol, @Size(max = 255) String password,
            @PastOrPresent LocalDateTime fechaRegistro, Boolean activo) {
        public static UsuarioDto from(Usuario x) {
            return new UsuarioDto(x.getIdUsuario(), x.getNombres(), x.getApellidos(), x.getDni(), x.getEmail(),
                    x.getTelefono(), x.getDireccion(), x.getRol().getIdRol(), null, x.getFechaRegistro(),
                    x.getActivo());
        }

        public Usuario toEntity() {
            Usuario x = new Usuario();
            x.setIdUsuario(idUsuario);
            x.setNombres(nombres);
            x.setApellidos(apellidos);
            x.setDni(dni);
            x.setEmail(email);
            x.setTelefono(telefono);
            x.setDireccion(direccion);
            Rol r = new Rol();
            r.setIdRol(idRol);
            x.setRol(r);
            x.setPassword(password);
            x.setFechaRegistro(fechaRegistro);
            if (activo != null)
                x.setActivo(activo);
            return x;
        }
    }

    public record PrestamoDto(Integer idPrestamo, @NotNull @Positive Integer idUsuario,
            @NotNull @Positive Integer idEjemplar, @NotNull @PastOrPresent LocalDateTime fechaPrestamo,
            @NotNull @FutureOrPresent LocalDateTime fechaVencimiento, @NotBlank @Size(max = 30) String estado,
            String tituloLibro) {
        public static PrestamoDto from(Prestamo x) {
            return new PrestamoDto(x.getIdPrestamo(), x.getUsuario().getIdUsuario(), x.getEjemplar().getIdEjemplar(),
                    x.getFechaPrestamo(), x.getFechaVencimiento(), x.getEstado(),
                    x.getEjemplar().getLibro() == null ? null : x.getEjemplar().getLibro().getTitulo());
        }

        public Prestamo toEntity() {
            Prestamo x = new Prestamo();
            x.setIdPrestamo(idPrestamo);
            Usuario u = new Usuario();
            u.setIdUsuario(idUsuario);
            Ejemplar e = new Ejemplar();
            e.setIdEjemplar(idEjemplar);
            x.setUsuario(u);
            x.setEjemplar(e);
            x.setFechaPrestamo(fechaPrestamo);
            x.setFechaVencimiento(fechaVencimiento);
            x.setEstado(estado);
            return x;
        }
    }

    public record ReservaDto(Integer idReserva, @NotNull @Positive Integer idUsuario,
            @NotNull @Positive Integer idLibro, @NotNull @PastOrPresent LocalDateTime fechaReserva,
            @NotNull @FutureOrPresent LocalDateTime fechaExpiracion, @NotBlank @Size(max = 30) String estado) {
        public static ReservaDto from(Reserva x) {
            return new ReservaDto(x.getIdReserva(), x.getUsuario().getIdUsuario(), x.getLibro().getIdLibro(),
                    x.getFechaReserva(), x.getFechaExpiracion(), x.getEstado());
        }

        public Reserva toEntity() {
            Reserva x = new Reserva();
            x.setIdReserva(idReserva);
            Usuario u = new Usuario();
            u.setIdUsuario(idUsuario);
            Libro l = new Libro();
            l.setIdLibro(idLibro);
            x.setUsuario(u);
            x.setLibro(l);
            x.setFechaReserva(fechaReserva);
            x.setFechaExpiracion(fechaExpiracion);
            x.setEstado(estado);
            return x;
        }
    }

    public record DevolucionDto(Integer idDevolucion, @NotNull @Positive Integer idPrestamo,
            @NotNull @PastOrPresent LocalDateTime fechaDevolucion, @Size(max = 2000) String observacion) {
        public static DevolucionDto from(Devolucion x) {
            return new DevolucionDto(x.getIdDevolucion(), x.getPrestamo().getIdPrestamo(), x.getFechaDevolucion(),
                    x.getObservacion());
        }

        public Devolucion toEntity() {
            Devolucion x = new Devolucion();
            x.setIdDevolucion(idDevolucion);
            Prestamo p = new Prestamo();
            p.setIdPrestamo(idPrestamo);
            x.setPrestamo(p);
            x.setFechaDevolucion(fechaDevolucion);
            x.setObservacion(observacion);
            return x;
        }
    }

    public record SancionDto(Integer idSancion, @NotNull @Positive Integer idDevolucion,
            @Size(max = 255) String motivo, @NotNull @PastOrPresent LocalDate fechaInicio,
            @NotNull LocalDate fechaFin, @NotBlank @Size(max = 30) String estado) {
        public static SancionDto from(Sancion x) {
            return new SancionDto(x.getIdSancion(), x.getDevolucion().getIdDevolucion(), x.getMotivo(),
                    x.getFechaInicio(), x.getFechaFin(), x.getEstado());
        }

        public Sancion toEntity() {
            Sancion x = new Sancion();
            x.setIdSancion(idSancion);
            Devolucion d = new Devolucion();
            d.setIdDevolucion(idDevolucion);
            x.setDevolucion(d);
            x.setMotivo(motivo);
            x.setFechaInicio(fechaInicio);
            x.setFechaFin(fechaFin);
            x.setEstado(estado);
            return x;
        }
    }

    public record LibroAutorDto(@NotNull @Positive Integer idLibro, @NotNull @Positive Integer idAutor) {
        public static LibroAutorDto from(LibroAutor x) {
            return new LibroAutorDto(x.getLibro().getIdLibro(), x.getAutor().getIdAutor());
        }
    }

    public record LibroCategoriaDto(@NotNull @Positive Integer idLibro, @NotNull @Positive Integer idCategoria) {
        public static LibroCategoriaDto from(LibroCategoria x) {
            return new LibroCategoriaDto(x.getLibro().getIdLibro(), x.getCategoria().getIdCategoria());
        }
    }
}
