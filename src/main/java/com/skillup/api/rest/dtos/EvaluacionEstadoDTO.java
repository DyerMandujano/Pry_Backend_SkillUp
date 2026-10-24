package com.skillup.api.rest.dtos;

import lombok.Data;

@Data
public class EvaluacionEstadoDTO {
    private int idSeccion;
    private int idCurso;
    private String nombreCurso;
    private int totalPreguntas;
    private int intentosRestantes;
    private boolean yaAprobado;
    private boolean cuentaDeshabilitada;
    private String mensaje;

    public EvaluacionEstadoDTO() {
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public int getTotalPreguntas() {
        return totalPreguntas;
    }

    public void setTotalPreguntas(int totalPreguntas) {
        this.totalPreguntas = totalPreguntas;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    public void setIntentosRestantes(int intentosRestantes) {
        this.intentosRestantes = intentosRestantes;
    }

    public boolean isYaAprobado() {
        return yaAprobado;
    }

    public void setYaAprobado(boolean yaAprobado) {
        this.yaAprobado = yaAprobado;
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
}
