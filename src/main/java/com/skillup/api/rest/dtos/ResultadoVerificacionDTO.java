// com.skillup.api.rest.dtos.ResultadoVerificacionDTO
package com.skillup.api.rest.dtos;

import lombok.Data;

@Data
public class ResultadoVerificacionDTO {
    private int totalPreguntas;
    private int correctas;
    private String resultado;
}
