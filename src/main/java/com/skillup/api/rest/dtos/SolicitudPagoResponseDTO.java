package com.skillup.api.rest.dtos;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SolicitudPagoResponseDTO {

    @JsonProperty("id_solicitud")
    private int idSolicitud;

    @JsonProperty("id_estudiante")
    private int idEstudiante;

    @JsonProperty("id_usuario")
    private Integer idUsuario;

    private String estudianteNombre;
    private String estudianteDni;
    private String username;

    @JsonProperty("num_operacion")
    private String numOperacion;

    private BigDecimal monto;

    @JsonProperty("metodo_pago")
    private String metodoPago;

    @JsonProperty("fecha_subida")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaSubida;

    @JsonProperty("fecha_revision")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaRevision;

    private String observacion;

    @JsonProperty("imagen_comprobante")
    private String imagenComprobante;

    private String estado;

    public SolicitudPagoResponseDTO() {
    }
}
