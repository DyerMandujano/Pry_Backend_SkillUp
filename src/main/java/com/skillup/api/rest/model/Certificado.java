package com.skillup.api.rest.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public Certificado() {
    }

    public Certificado(int idCertificado, int idMatricula, String mensaje, String codigoCertificado, LocalDate fechaEmision) {
        this.idCertificado = idCertificado;
        this.idMatricula = idMatricula;
        this.mensaje = mensaje;
        this.codigoCertificado = codigoCertificado;
        this.fechaEmision = fechaEmision;
    }

    public int getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(int idCertificado) {
        this.idCertificado = idCertificado;
    }

    public int getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(int idMatricula) {
        this.idMatricula = idMatricula;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCodigoCertificado() {
        return codigoCertificado;
    }

    public void setCodigoCertificado(String codigoCertificado) {
        this.codigoCertificado = codigoCertificado;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
}
