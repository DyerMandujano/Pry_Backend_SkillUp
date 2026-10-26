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

    public EvaluacionCursoDTO() {
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public void setOpcion1(String opcion1) {
        this.opcion1 = opcion1;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public void setOpcion2(String opcion2) {
        this.opcion2 = opcion2;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public void setOpcion3(String opcion3) {
        this.opcion3 = opcion3;
    }

    public Integer getIdOp1() {
        return idOp1;
    }

    public void setIdOp1(Integer idOp1) {
        this.idOp1 = idOp1;
    }

    public Integer getIdOp2() {
        return idOp2;
    }

    public void setIdOp2(Integer idOp2) {
        this.idOp2 = idOp2;
    }

    public Integer getIdOp3() {
        return idOp3;
    }

    public void setIdOp3(Integer idOp3) {
        this.idOp3 = idOp3;
    }
}
