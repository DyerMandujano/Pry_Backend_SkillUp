package com.skillup.api.rest.dtos;

import java.util.List;
import lombok.Data;

@Data
public class EvaluacionSubmitDTO {
    private int idEstudiante;
    private Integer idCurso;
    private List<RespuestaItemDTO> respuestas;

    public EvaluacionSubmitDTO() {
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public List<RespuestaItemDTO> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaItemDTO> respuestas) {
        this.respuestas = respuestas;
    }

    @Data
    public static class RespuestaItemDTO {
        private int idPregunta;
        private int idOpPregunta;

        public RespuestaItemDTO() {
        }

        public int getIdPregunta() {
            return idPregunta;
        }

        public void setIdPregunta(int idPregunta) {
            this.idPregunta = idPregunta;
        }

        public int getIdOpPregunta() {
            return idOpPregunta;
        }

        public void setIdOpPregunta(int idOpPregunta) {
            this.idOpPregunta = idOpPregunta;
        }
    }
}
