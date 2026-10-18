package com.skillup.api.rest.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarReseniaRequestDTO {

    @NotNull(message = "El identificador del estudiante es obligatorio")
    private Integer idEstudiante;

    @NotNull(message = "La calificación es obligatoria y debe ser de 1 a 5 estrellas")
    @Min(value = 1, message = "La calificación mínima es 1 estrella")
    @Max(value = 5, message = "La calificación máxima es 5 estrellas")
    private Integer calificacion;

    @NotBlank(message = "El comentario de la reseña es obligatorio")
    private String comentario;
}
