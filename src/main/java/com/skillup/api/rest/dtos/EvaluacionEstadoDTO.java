package com.skillup.api.rest.dtos;

import lombok.Data;

@Data
public class EvaluacionEstadoDTO {
    private int idSeccion;
    private int idCurso;
    private String nombreCurso;
    private int totalPreguntas;
    private int intentosRestantes; // 2, 1, o 0
    private boolean yaAprobado; // Si ya contestó todas correctamente
    private boolean cuentaDeshabilitada; // Si la cuenta está deshabilitada
    private String mensaje;
}
