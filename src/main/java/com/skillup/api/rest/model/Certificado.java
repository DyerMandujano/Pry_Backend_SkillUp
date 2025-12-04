package com.skillup.api.rest.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

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
