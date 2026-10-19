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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@Entity 
@Table(name = "Usuario") 
public class Usuario {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "Id_Usuario")
    private int idUsuario;
    
    @Column(name = "Id_Persona")
    private int idPersona;
    
    @Column(name = "Usuario")
    private String username;
    
    @Column(name = "Contrasenia")
    private String contrasenia;
    
    @Column(name = "Rol")
    private String rol;
    
    @Column(name = "Fec_Registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecRegistro;

    @Column(name = "Intentos_Fallidos")
    private int intentosFallidos;

    @Column(name = "Fec_Bloqueo_Hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecBloqueoHasta;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Date getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(Date fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public Date getFecBloqueoHasta() {
        return fecBloqueoHasta;
    }

    public void setFecBloqueoHasta(Date fecBloqueoHasta) {
        this.fecBloqueoHasta = fecBloqueoHasta;
    }
}