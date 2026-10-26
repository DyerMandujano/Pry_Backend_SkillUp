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
    private String estado;

    public int getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(int idSolicitud) { this.idSolicitud = idSolicitud; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNumOperacion() { return numOperacion; }
    public void setNumOperacion(String numOperacion) { this.numOperacion = numOperacion; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Date getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(Date fechaSubida) { this.fechaSubida = fechaSubida; }

    public Date getFechaRevision() { return fechaRevision; }
    public void setFechaRevision(Date fechaRevision) { this.fechaRevision = fechaRevision; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getImagenComprobante() { return imagenComprobante; }
    public void setImagenComprobante(String imagenComprobante) { this.imagenComprobante = imagenComprobante; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
