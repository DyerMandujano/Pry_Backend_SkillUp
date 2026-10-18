package com.skillup.api.rest.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "Solicitud_Pago")
public class SolicitudPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Solicitud")
    private int idSolicitud;

    @Column(name = "Id_Estudiante")
    @JsonProperty("id_estudiante")
    private int idEstudiante;

    @Column(name = "Id_Usuario")
    @JsonProperty("id_usuario")
    private Integer idUsuario;

    @Column(name = "Num_Operacion")
    @JsonProperty("num_operacion")
    private String numOperacion;

    @Column(name = "Monto")
    private BigDecimal monto;

    @Column(name = "Metodo_Pago")
    @JsonProperty("metodo_pago")
    private String metodoPago;

    @Column(name = "Fecha_Subida")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("fecha_subida")
    private Date fechaSubida;

    @Column(name = "Fecha_Revision")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("fecha_revision")
    private Date fechaRevision;

    @Column(name = "Observacion")
    private String observacion;

    @Column(name = "Imagen_Comprobante")
    @JsonProperty("imagen_comprobante")
    private String imagenComprobante;

    @Column(name = "Estado")
    private String estado; // "Pendiente", "Aprobado", "Rechazado"
}
