package com.bibliot.gestion_biblioteca.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.ISBN;
import java.time.LocalDate;
import java.util.List;

public record LibroRequest(
                @NotBlank @Size(max = 255) String titulo,
                @ISBN String isbn,
                @Positive Integer paginas,
                @PastOrPresent LocalDate fechaPublicacion,
                @Size(max = 30) String estado,
                @Size(max = 5000) String descripcion,
                String contenidoTexto,
                @Positive Integer idEditorial,
                List<@Positive Integer> autorIds,
                List<@Positive Integer> categoriaIds) {
}
