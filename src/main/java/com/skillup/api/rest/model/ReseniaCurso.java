package com.skillup.api.rest.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Resenia_Curso")
public class ReseniaCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Resenia_Curso")
    private Integer idReseniaCurso;

    @Column(name = "Id_Curso", nullable = false)
    private Integer idCurso;

    @Column(name = "Id_Estudiante", nullable = false)
    private Integer idEstudiante;

    @Column(name = "Comentario", nullable = false, length = 500)
    private String comentario;

    @Column(name = "Calificacion", nullable = false)
    private Integer calificacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "Fecha_Resenia", nullable = false)
    private Date fechaResenia;

    @Column(name = "Estado", nullable = false)
    private Integer estado; // 1 = Activo / Visible
}
