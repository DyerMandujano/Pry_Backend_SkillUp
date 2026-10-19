package com.skillup.api.rest.dtos;

import lombok.Data;

@Data
public class RecuperarPasswordDTO {

    private String identificador; // Puede ser username o DNI
    private String nuevaContrasenia;
    private String confirmarContrasenia;
    private String codigoSms; // Opcional para validación SMS OTP

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
}
