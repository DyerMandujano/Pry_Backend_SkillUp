package com.skillup.api.rest.dtos;

import java.util.List;
import lombok.Data;

@Data
public class EvaluacionSubmitDTO {
    private int idEstudiante;
    private Integer idCurso;
    private List<RespuestaItemDTO> respuestas;

    @Data
    public static class RespuestaItemDTO {
        private int idPregunta;
        private int idOpPregunta;
    }
}
