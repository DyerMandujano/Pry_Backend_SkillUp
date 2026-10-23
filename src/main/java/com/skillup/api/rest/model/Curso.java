package com.skillup.api.rest.model;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "Curso")
public class Curso {

	public Curso() {
	}

	public Curso(Integer idCurso, Integer idDocente, Integer idCategoria, String nombreCurso,
				 String descripcion, String resumen, String nivel, Integer duracionMin,
				 LocalDate fechaPublicacion, String imagenCurso1, String imagenCurso2, Integer estado) {
		this.idCurso = idCurso;
		this.idDocente = idDocente;
		this.idCategoria = idCategoria;
		this.nombreCurso = nombreCurso;
		this.descripcion = descripcion;
		this.resumen = resumen;
		this.nivel = nivel;
		this.duracionMin = duracionMin;
		this.fechaPublicacion = fechaPublicacion;
		this.imagenCurso1 = imagenCurso1;
		this.imagenCurso2 = imagenCurso2;
		this.estado = estado;
	}
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Curso")
    private Integer idCurso;

    @Column(name = "Id_Docente")
    private Integer idDocente;

    @Column(name = "Id_Categoria")
    private Integer idCategoria;

    @Column(name = "Nombre_Curso", length = 100)
    private String nombreCurso;

    @Column(name = "Descripcion", columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    @Column(name = "Resumen", length = 200)
    private String resumen;

    @Column(name = "Nivel", length = 10)
    private String nivel;

    @Column(name = "Duracion_Min")
    private Integer duracionMin;

    @Column(name = "Fecha_Publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "Imagen_Curso1", columnDefinition = "VARCHAR(MAX)")
    private String imagenCurso1;

    @Column(name = "Imagen_Curso2", columnDefinition = "VARCHAR(MAX)")
    private String imagenCurso2;

    @Column(name = "Estado")
    private Integer estado;

    public Integer getIdCurso() { return idCurso; }
    public void setIdCurso(Integer idCurso) { this.idCurso = idCurso; }

    public Integer getIdDocente() { return idDocente; }
    public void setIdDocente(Integer idDocente) { this.idDocente = idDocente; }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Integer getDuracionMin() { return duracionMin; }
    public void setDuracionMin(Integer duracionMin) { this.duracionMin = duracionMin; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public String getImagenCurso1() { return imagenCurso1; }
    public void setImagenCurso1(String imagenCurso1) { this.imagenCurso1 = imagenCurso1; }

    public String getImagenCurso2() { return imagenCurso2; }
    public void setImagenCurso2(String imagenCurso2) { this.imagenCurso2 = imagenCurso2; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }
}
