package com.skillup.api.rest.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Certificado")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Certificado")
    private int idCertificado;

    @Column(name = "Id_Matricula")
    private int idMatricula;

    @Column(name = "Mensaje")
    private String mensaje;

    @Column(name = "Codigo_Certificado")
    private String codigoCertificado;

    @Column(name = "Fecha_Emision")
    private LocalDate fechaEmision;
}
