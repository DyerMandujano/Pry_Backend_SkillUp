package com.skillup.api.rest.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Opcion_Pregunta")
public class OpcionPregunta {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id_Op_Pregunta")
	private int idOpcPregunta;

    @Column(name = "Opcion", nullable=false,length = 100)
    private String opcion;

    @Column(name = "Es_correcta", nullable=false, length=20)
    private String esCorrecta;

    @Column(name = "Estado", nullable = false)
    private int estado;
      
   @ManyToOne
    @JoinColumn(name = "Id_Pregunta", nullable = false)
    private PreguntaEvaluacion pregEvaluacion;

   public int getIdOpcPregunta() {
	return idOpcPregunta;
   }

   public void setIdOpcPregunta(int idOpcPregunta) {
	this.idOpcPregunta = idOpcPregunta;
   }

   public String getOpcion() {
	return opcion;
   }

   public void setOpcion(String opcion) {
	this.opcion = opcion;
   }

   public String getEsCorrecta() {
	return esCorrecta;
   }

   public void setEsCorrecta(String esCorrecta) {
	this.esCorrecta = esCorrecta;
   }

   public int getEstado() {
	return estado;
   }

   public void setEstado(int estado) {
	this.estado = estado;
   }

   public PreguntaEvaluacion getPregEvaluacion() {
	return pregEvaluacion;
   }

   public void setPregEvaluacion(PreguntaEvaluacion pregEvaluacion) {
	this.pregEvaluacion = pregEvaluacion;
   }

	
    

   
}

