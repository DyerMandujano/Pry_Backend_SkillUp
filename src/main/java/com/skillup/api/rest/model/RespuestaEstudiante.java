package com.skillup.api.rest.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Respuesta_Estudiante")
public class RespuestaEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Resp_EstudIante")
    private int idRespEstudiante;

    @ManyToOne
    @JoinColumn(name = "Id_Estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "Id_Op_Pregunta", nullable = false)
    private OpcionPregunta opcionPregunta;

    @Column(name = "Texto_Respuesta")
    private String textoRespuesta;

    @Column(name = "Puntaje", nullable = false)
    private int puntaje;

    @Column(name = "Fec_Respuesta", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecRespuesta;

    @Column(name = "Estado", nullable = false)
    private int estado;

    // Getters y Setters
    public int getIdRespEstudiante() {
        return idRespEstudiante;
    }

    public void setIdRespEstudiante(int idRespEstudiante) {
        this.idRespEstudiante = idRespEstudiante;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public OpcionPregunta getOpcionPregunta() {
        return opcionPregunta;
    }

    public void setOpcionPregunta(OpcionPregunta opcionPregunta) {
        this.opcionPregunta = opcionPregunta;
    }

    public String getTextoRespuesta() {
        return textoRespuesta;
    }

    public void setTextoRespuesta(String textoRespuesta) {
        this.textoRespuesta = textoRespuesta;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public Date getFecRespuesta() {
        return fecRespuesta;
    }

    public void setFecRespuesta(Date fecRespuesta) {
        this.fecRespuesta = fecRespuesta;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
