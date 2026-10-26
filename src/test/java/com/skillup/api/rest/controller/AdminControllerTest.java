package com.skillup.api.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillup.api.rest.dtos.RechazoPagoRequestDTO;
import com.skillup.api.rest.dtos.SolicitudPagoResponseDTO;
import com.skillup.api.rest.dtos.UsuarioAdminDTO;
import com.skillup.api.rest.model.SolicitudPago;
import com.skillup.api.rest.service.AdminMatriculaService;
import com.skillup.api.rest.service.AdminUsuarioService;
import com.skillup.api.rest.service.AuthService;
import com.skillup.api.rest.service.SolicitudPagoService;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SolicitudPagoService solicitudPagoService;

    @Mock
    private AdminUsuarioService adminUsuarioService;

    @Mock
    private AdminMatriculaService adminMatriculaService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AdminController adminController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("REST-ADMIN-01: GET /api/admin/solicitudes-pago retorna 200 OK con lista")
    void testListarSolicitudesPago() throws Exception {
        SolicitudPagoResponseDTO dto = new SolicitudPagoResponseDTO();
        dto.setIdSolicitud(1);
        dto.setEstado("Pendiente");

        when(solicitudPagoService.listarSolicitudes("todos")).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/admin/solicitudes-pago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_solicitud").value(1));
    }

    @Test
    @DisplayName("REST-ADMIN-02: PUT /api/admin/solicitudes-pago/{id}/aprobar retorna 200 OK con solicitud aprobada")
    void testAprobarSolicitudPago() throws Exception {
        SolicitudPago pago = new SolicitudPago();
        pago.setIdSolicitud(1);
        pago.setEstado("Aprobado");

        when(solicitudPagoService.aprobarSolicitud(1)).thenReturn(pago);

        mockMvc.perform(put("/api/admin/solicitudes-pago/1/aprobar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSolicitud").value(1))
                .andExpect(jsonPath("$.estado").value("Aprobado"));
    }

    @Test
    @DisplayName("REST-ADMIN-03: PUT /api/admin/solicitudes-pago/{id}/rechazar retorna solicitud rechazada con motivo")
    void testRechazarSolicitudPago() throws Exception {
        RechazoPagoRequestDTO req = new RechazoPagoRequestDTO();
        req.setObservacion("Voucher ilegible");

        SolicitudPago pago = new SolicitudPago();
        pago.setIdSolicitud(1);
        pago.setEstado("Rechazado");
        pago.setObservacion("Voucher ilegible");

        when(solicitudPagoService.rechazarSolicitud(eq(1), eq("Voucher ilegible"))).thenReturn(pago);

        mockMvc.perform(put("/api/admin/solicitudes-pago/1/rechazar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("Rechazado"))
                .andExpect(jsonPath("$.observacion").value("Voucher ilegible"));
    }

    @Test
    @DisplayName("REST-ADMIN-04: PUT /api/admin/usuarios/{id}/estado actualiza estado del usuario")
    void testCambiarEstadoUsuario() throws Exception {
        UsuarioAdminDTO dto = new UsuarioAdminDTO();
        dto.setIdUsuario(5);
        dto.setEstado(0);

        when(adminUsuarioService.cambiarEstadoUsuario(5, 0)).thenReturn(dto);

        mockMvc.perform(put("/api/admin/usuarios/5/estado")
                .param("nuevoEstado", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_usuario").value(5))
                .andExpect(jsonPath("$.estado").value(0));
    }
}
