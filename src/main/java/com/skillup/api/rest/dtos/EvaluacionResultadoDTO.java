package com.skillup.api.rest.dtos;

import java.util.List;
import lombok.Data;

@Data
public class EvaluacionResultadoDTO {
    private int totalPreguntas;
    private int correctas;
    private int incorrectas;
    private String contador; // ej: "5/6" o "5/10"
    private int puntajeTotal;
    private int puntajeMaximo;
    private boolean aprobado; // true si correctas == totalPreguntas
    private int intentosRestantes; // ej: 2, 1, 0
    private boolean cuentaDeshabilitada; // true si falló los 2 intentos
    private String mensaje;
    private List<DetallePreguntaDTO> detalles;

    @Data
    public static class DetallePreguntaDTO {
        private int idPregunta;
        private int idOpSeleccionada;
        private boolean esCorrecta;
        private Integer idOpCorrecta;
    }
}
