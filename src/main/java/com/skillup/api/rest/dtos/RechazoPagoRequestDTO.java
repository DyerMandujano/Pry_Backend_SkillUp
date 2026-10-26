package com.skillup.api.rest.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RechazoPagoRequestDTO {

    @NotBlank(message = "El motivo u observación de rechazo es obligatorio")
    private String observacion;

    public RechazoPagoRequestDTO() {
    }

    public RechazoPagoRequestDTO(String observacion) {
        this.observacion = observacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
