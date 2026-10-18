package com.skillup.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.api.rest.dtos.MatriculaSeguimientoDTO;
import com.skillup.api.rest.dtos.RechazoPagoRequestDTO;
import com.skillup.api.rest.dtos.RegistroRequest;
import com.skillup.api.rest.dtos.SolicitudPagoResponseDTO;
import com.skillup.api.rest.dtos.UsuarioAdminDTO;
import com.skillup.api.rest.model.SolicitudPago;
import com.skillup.api.rest.service.AdminMatriculaService;
import com.skillup.api.rest.service.AdminUsuarioService;
import com.skillup.api.rest.service.AuthService;
import com.skillup.api.rest.service.SolicitudPagoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://192.168.56.1:4200", "http://localhost:4200", "http://127.0.0.1:4200"})
public class AdminController {

    @Autowired
    private SolicitudPagoService solicitudPagoService;

    @Autowired
    private AdminUsuarioService adminUsuarioService;

    @Autowired
    private AdminMatriculaService adminMatriculaService;

    @Autowired
    private AuthService authService;

    // ==========================================
    // HU17: GESTIÓN DE RENOVACIÓN DE MEMBRESÍAS
    // ==========================================

    @GetMapping("/solicitudes-pago")
    public ResponseEntity<?> listarSolicitudesPago(
            @RequestParam(value = "estado", required = false, defaultValue = "todos") String estado) {
        try {
            List<SolicitudPagoResponseDTO> lista = solicitudPagoService.listarSolicitudes(estado);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al listar solicitudes: " + e.getMessage());
        }
    }

    @PutMapping("/solicitudes-pago/{id}/aprobar")
    public ResponseEntity<?> aprobarSolicitudPago(@PathVariable("id") int idSolicitud) {
        try {
            SolicitudPago aprobada = solicitudPagoService.aprobarSolicitud(idSolicitud);
            return ResponseEntity.ok(aprobada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/solicitudes-pago/{id}/rechazar")
    public ResponseEntity<?> rechazarSolicitudPago(
            @PathVariable("id") int idSolicitud,
            @Valid @RequestBody RechazoPagoRequestDTO request) {
        try {
            if (request == null || request.getObservacion() == null || request.getObservacion().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El motivo u observación de rechazo es obligatorio");
            }
            SolicitudPago rechazada = solicitudPagoService.rechazarSolicitud(idSolicitud, request.getObservacion());
            return ResponseEntity.ok(rechazada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/solicitudes-pago")
    public ResponseEntity<?> registrarSolicitudPago(@RequestBody SolicitudPago solicitud) {
        try {
            SolicitudPago nueva = solicitudPagoService.registrarSolicitud(solicitud);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // HU18: GESTIÓN DE CUENTAS DE USUARIOS
    // ==========================================

    @GetMapping("/usuarios")
    public ResponseEntity<?> listarUsuarios(
            @RequestParam(value = "rol", required = false, defaultValue = "todos") String rol,
            @RequestParam(value = "buscar", required = false) String buscar) {
        try {
            List<UsuarioAdminDTO> usuarios = adminUsuarioService.listarUsuarios(rol, buscar);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al listar usuarios: " + e.getMessage());
        }
    }

    @PutMapping("/usuarios/{idUsuario}/estado")
    public ResponseEntity<?> cambiarEstadoUsuario(
            @PathVariable("idUsuario") int idUsuario,
            @RequestParam("nuevoEstado") int nuevoEstado) {
        try {
            UsuarioAdminDTO actualizado = adminUsuarioService.cambiarEstadoUsuario(idUsuario, nuevoEstado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // CRUD ADMIN: CREACIÓN EXCLUSIVA DE ADMIN
    // ==========================================

    @PostMapping("/usuarios/crear-admin")
    public ResponseEntity<?> crearAdmin(
            @RequestBody RegistroRequest request,
            @RequestHeader(value = "X-User-Role", required = false) String rolHeader,
            @RequestParam(value = "rolEjecutor", required = false) String rolParam) {
        try {
            String rolEjecutor = (rolHeader != null && !rolHeader.trim().isEmpty()) ? rolHeader : rolParam;
            if (rolEjecutor == null || !rolEjecutor.equalsIgnoreCase("admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: solo un usuario con rol ADMIN puede crear otro usuario ADMIN.");
            }

            authService.registrarAdmin(request, rolEjecutor);
            return ResponseEntity.ok().body("Usuario Administrador creado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // HU19: SEGUIMIENTO DE INSCRIPCIONES
    // ==========================================

    @GetMapping("/matriculas")
    public ResponseEntity<?> listarMatriculasSeguimiento(
            @RequestParam(value = "idCurso", required = false) Integer idCurso,
            @RequestParam(value = "buscar", required = false) String buscar) {
        try {
            List<MatriculaSeguimientoDTO> lista = adminMatriculaService.listarMatriculasSeguimiento(idCurso, buscar);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al consultar matrículas: " + e.getMessage());
        }
    }

    @GetMapping("/matriculas/exportar")
    public ResponseEntity<?> exportarMatriculas(
            @RequestParam(value = "idCurso", required = false) Integer idCurso,
            @RequestParam(value = "buscar", required = false) String buscar) {
        try {
            List<MatriculaSeguimientoDTO> lista = adminMatriculaService.listarMatriculasSeguimiento(idCurso, buscar);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al exportar matrículas: " + e.getMessage());
        }
    }
}
