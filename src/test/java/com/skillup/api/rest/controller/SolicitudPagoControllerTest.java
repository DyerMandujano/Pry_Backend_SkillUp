package com.skillup.api.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.skillup.api.rest.dtos.SolicitudPagoResponseDTO;
import com.skillup.api.rest.model.SolicitudPago;
import com.skillup.api.rest.service.SolicitudPagoService;

@ExtendWith(MockitoExtension.class)
public class SolicitudPagoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SolicitudPagoService solicitudPagoService;

    @InjectMocks
    private SolicitudPagoController solicitudPagoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(solicitudPagoController).build();
    }

    @Test
    @DisplayName("REST-PAGO-01: POST /api/solicitudes-pago/subir registra solicitud y retorna 201 Created")
    void testRegistrarSolicitudConComprobante_Exitoso() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "comprobante",
            "voucher.png",
            "image/png",
            "dummy-image-bytes".getBytes()
        );

        SolicitudPago pago = new SolicitudPago();
        pago.setIdSolicitud(1);
        pago.setNumOperacion("OP-778899");
        pago.setMonto(new BigDecimal("50.00"));

        when(solicitudPagoService.registrarSolicitudConComprobante(
            anyInt(), anyString(), any(BigDecimal.class), anyString(), any(), any()
        )).thenReturn(pago);

        mockMvc.perform(multipart("/api/solicitudes-pago/subir")
                .file(file)
                .param("idEstudiante", "10")
                .param("numOperacion", "OP-778899")
                .param("monto", "50.00")
                .param("metodoPago", "Yape"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idSolicitud").value(1))
                .andExpect(jsonPath("$.num_operacion").value("OP-778899"));
    }

    @Test
    @DisplayName("REST-PAGO-02: GET /api/solicitudes-pago/estudiante/{id} retorna historial de solicitudes")
    void testListarPorEstudiante() throws Exception {
        SolicitudPagoResponseDTO dto = new SolicitudPagoResponseDTO();
        dto.setIdSolicitud(1);
        dto.setIdEstudiante(10);
        dto.setNumOperacion("OP-778899");
        dto.setEstado("Pendiente");

        when(solicitudPagoService.listarSolicitudesPorEstudiante(10))
                .thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/solicitudes-pago/estudiante/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_solicitud").value(1))
                .andExpect(jsonPath("$[0].estado").value("Pendiente"));
    }
}
