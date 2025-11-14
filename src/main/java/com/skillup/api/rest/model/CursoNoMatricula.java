package com.skillup.api.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoNoMatricula {

    private int idCurso;
    private String nombreCurso;
    private String nivel;
    private int duracionMin;
    private String imagenCurso1;
    private String imagenCurso2;
}
