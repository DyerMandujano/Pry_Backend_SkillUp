package com.skillup.api.rest.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReseniaCursoDTO {
    private Integer idReseniaCurso;
    private Integer idCurso;
    private Integer idEstudiante;
    private String nombreEstudiante;
    private Integer calificacion;
    private String comentario;
    private Date fechaResenia;
    private Integer estado;
}
