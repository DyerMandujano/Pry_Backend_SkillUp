package com.skillup.api.rest.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Evaluacion")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "Id_Evaluacion")
    private int idEvaluacion;

    @Column(name = "Titulo", nullable = false, length = 50)
    private String titulo;

    @Column(name = "Fecha_Creacion")
    private Date fechaCreacion;

    @Column(name = "Estado", nullable = false)
    private int estado;

    @ManyToOne
    @JoinColumn(name = "Id_Seccion", nullable = false)
    private Seccion seccion;

    @OneToMany(mappedBy = "evaluacion")
    private List<PreguntaEvaluacion> preguntas;

    // Getters y setters
    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    public List<PreguntaEvaluacion> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaEvaluacion> preguntas) {
        this.preguntas = preguntas;
    }
}

