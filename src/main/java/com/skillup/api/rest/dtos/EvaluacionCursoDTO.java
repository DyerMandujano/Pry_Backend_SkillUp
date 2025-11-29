package com.skillup.api.rest.dtos;

import lombok.Data;

@Data
public class EvaluacionCursoDTO {


    private int idPregunta;
    private String enunciado;

    private String opcion1;
    private String opcion2;
    private String opcion3;

    private Integer idOp1;
    private Integer idOp2;
    private Integer idOp3;
}
