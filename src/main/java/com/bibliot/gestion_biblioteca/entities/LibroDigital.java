package com.bibliot.gestion_biblioteca.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Entity
@Table(name = "libro_digital")
@Getter
@Setter
@NoArgsConstructor
public class LibroDigital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro_digital")
    private Integer idLibroDigital;

    @NotNull(message = "Debe asociarse el archivo a un libro")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_libro", nullable = false, unique = true)
    private Libro libro;

    @Column(name = "archivo_url", length = 255)
    @Size(max = 255)
    @URL
    private String archivoUrl;

    @Column(name = "formato", length = 30)
    @Size(max = 30)
    private String formato;

    @Column(name = "tamano_archivo")
    @Positive(message = "El tamaño del archivo debe ser mayor que cero")
    private Long tamanoArchivo;

    @Column(name = "disponible")
    @NotNull(message = "Debe indicarse si el libro digital está disponible")
    private Boolean disponible;

    @Column(name = "fecha_registro")
    @PastOrPresent(message = "La fecha de registro no puede ser futura")
    private LocalDateTime fechaRegistro;
}
