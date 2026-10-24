package com.skillup.api.rest.dtos;

import java.util.List;
import lombok.Data;

@Data
public class EvaluacionResultadoDTO {
    private int totalPreguntas;
    private int correctas;
    private int incorrectas;
    private String contador;
    private int puntajeTotal;
    private int puntajeMaximo;
    private boolean aprobado;
    private int intentosRestantes;
    private boolean cuentaDeshabilitada;
    private String mensaje;
    private List<DetallePreguntaDTO> detalles;

    public EvaluacionResultadoDTO() {
    }

    public int getTotalPreguntas() {
        return totalPreguntas;
    }

    public void setTotalPreguntas(int totalPreguntas) {
        this.totalPreguntas = totalPreguntas;
    }

    public int getCorrectas() {
        return correctas;
    }

    public void setCorrectas(int correctas) {
        this.correctas = correctas;
    }

    public int getIncorrectas() {
        return incorrectas;
    }

    public void setIncorrectas(int incorrectas) {
        this.incorrectas = incorrectas;
    }

    public String getContador() {
        return contador;
    }

    public void setContador(String contador) {
        this.contador = contador;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(int puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(int puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    public void setIntentosRestantes(int intentosRestantes) {
        this.intentosRestantes = intentosRestantes;
    }

    public boolean isCuentaDeshabilitada() {
        return cuentaDeshabilitada;
    }

    public void setCuentaDeshabilitada(boolean cuentaDeshabilitada) {
        this.cuentaDeshabilitada = cuentaDeshabilitada;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<DetallePreguntaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePreguntaDTO> detalles) {
        this.detalles = detalles;
    }

    @Data
    public static class DetallePreguntaDTO {
        private int idPregunta;
        private int idOpSeleccionada;
        private boolean esCorrecta;
        private Integer idOpCorrecta;

        public DetallePreguntaDTO() {
        }

        public int getIdPregunta() {
            return idPregunta;
        }

        public void setIdPregunta(int idPregunta) {
            this.idPregunta = idPregunta;
        }

        public int getIdOpSeleccionada() {
            return idOpSeleccionada;
        }

        public void setIdOpSeleccionada(int idOpSeleccionada) {
            this.idOpSeleccionada = idOpSeleccionada;
        }

        public boolean isEsCorrecta() {
            return esCorrecta;
        }

        public void setEsCorrecta(boolean esCorrecta) {
            this.esCorrecta = esCorrecta;
        }

        public Integer getIdOpCorrecta() {
            return idOpCorrecta;
        }

        public void setIdOpCorrecta(Integer idOpCorrecta) {
            this.idOpCorrecta = idOpCorrecta;
        }
    }
}
