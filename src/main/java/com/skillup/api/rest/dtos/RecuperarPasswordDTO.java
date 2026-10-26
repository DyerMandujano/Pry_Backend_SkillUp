package com.skillup.api.rest.dtos;

import lombok.Data;

@Data
public class RecuperarPasswordDTO {

    private String identificador;
    private String nuevaContrasenia;
    private String confirmarContrasenia;
    private String codigoSms;

    public RecuperarPasswordDTO() {
    }

    public RecuperarPasswordDTO(String identificador, String nuevaContrasenia, String confirmarContrasenia) {
        this.identificador = identificador;
        this.nuevaContrasenia = nuevaContrasenia;
        this.confirmarContrasenia = confirmarContrasenia;
    }

    public RecuperarPasswordDTO(String identificador, String nuevaContrasenia, String confirmarContrasenia, String codigoSms) {
        this.identificador = identificador;
        this.nuevaContrasenia = nuevaContrasenia;
        this.confirmarContrasenia = confirmarContrasenia;
        this.codigoSms = codigoSms;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNuevaContrasenia() {
        return nuevaContrasenia;
    }

    public void setNuevaContrasenia(String nuevaContrasenia) {
        this.nuevaContrasenia = nuevaContrasenia;
    }

    public String getConfirmarContrasenia() {
        return confirmarContrasenia;
    }

    public void setConfirmarContrasenia(String confirmarContrasenia) {
        this.confirmarContrasenia = confirmarContrasenia;
    }

    public String getCodigoSms() {
        return codigoSms;
    }

    public void setCodigoSms(String codigoSms) {
        this.codigoSms = codigoSms;
    }
}
