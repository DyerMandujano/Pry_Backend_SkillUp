package com.skillup.api.rest.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Pregunta_Evaluacion")
public class PreguntaEvaluacion {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id_Pregunta")
	private int idPregunta;

    @Column(name = "Tipo_Pregunta", nullable=false  ,length = 20)
    private String tipoPregunta;

    @Column(name = "Enunciado", nullable=false)
    private String enunciado;

    @Column(name = "Puntuacion", nullable = false)
    private int puntuacion;
    
    @Column(name="Estado", nullable=false)
    private int estado;
    
    @OneToMany(mappedBy = "pregEvaluacion")
    private List<OpcionPregunta> opcPregunta;

    @ManyToOne
    @JoinColumn(name = "Id_Evaluacion", nullable = false)
    private Evaluacion evaluacion;

	public int getIdPregunta() {
		return idPregunta;
	}

	public void setIdPregunta(int idPregunta) {
		this.idPregunta = idPregunta;
	}

	public String getTipoPregunta() {
		return tipoPregunta;
	}

	public void setTipoPregunta(String tipoPregunta) {
		this.tipoPregunta = tipoPregunta;
	}

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}

	public List<OpcionPregunta> getOpcPregunta() {
		return opcPregunta;
	}

	public void setOpcPregunta(List<OpcionPregunta> opcPregunta) {
		this.opcPregunta = opcPregunta;
	}

	public Evaluacion getEvaluacion() {
		return evaluacion;
	}

	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	

    

   
}

