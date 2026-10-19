package com.skillup.api.rest.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skillup.api.rest.dtos.SolicitudPagoResponseDTO;
import com.skillup.api.rest.model.SolicitudPago;
import com.skillup.api.rest.service.SolicitudPagoService;

@RestController
@RequestMapping("/api/solicitudes-pago")
@CrossOrigin(origins = {"http://192.168.56.1:4200", "http://localhost:4200"})
public class SolicitudPagoController {

    @Autowired
    private SolicitudPagoService solicitudPagoService;

    @PostMapping(value = "/subir", consumes = {"multipart/form-data"})
    public ResponseEntity<?> registrarSolicitudConComprobante(
            @RequestParam("idEstudiante") int idEstudiante,
            @RequestParam("numOperacion") String numOperacion,
            @RequestParam("monto") BigDecimal monto,
            @RequestParam("metodoPago") String metodoPago,
            @RequestParam(value = "observacion", required = false) String observacion,
            @RequestParam(value = "comprobante", required = false) MultipartFile comprobante) {
        try {
            SolicitudPago guardada = solicitudPagoService.registrarSolicitudConComprobante(
                idEstudiante,
                numOperacion,
                monto,
                metodoPago,
                observacion,
                comprobante
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<?> listarPorEstudiante(@PathVariable("idEstudiante") int idEstudiante) {
        try {
            List<SolicitudPagoResponseDTO> lista = solicitudPagoService.listarSolicitudesPorEstudiante(idEstudiante);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al consultar solicitudes: " + e.getMessage());
        }
    }
}
