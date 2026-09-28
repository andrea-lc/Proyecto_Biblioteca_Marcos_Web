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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_libro", nullable = false, unique = true)
    private Libro libro;

    @Column(name = "archivo_url", length = 255)
    private String archivoUrl;

    @Column(name = "formato", length = 30)
    private String formato;

    @Column(name = "tamano_archivo")
    private Long tamanoArchivo;

    @Column(name = "disponible")
    private Boolean disponible;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
}
