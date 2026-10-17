package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.SolicitudPago;
import com.skillup.api.rest.repository.SolicitudPagoRepository;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
public class SolicitudPagoServiceTest {

    @Mock
    private SolicitudPagoRepository solicitudPagoRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SolicitudPagoService solicitudPagoService;

    private SolicitudPago solicitudPendiente;

    @BeforeEach
    void setUp() {
        solicitudPendiente = new SolicitudPago();
        solicitudPendiente.setIdSolicitud(1);
        solicitudPendiente.setIdEstudiante(10);
        solicitudPendiente.setNumOperacion("OP-998877");
        solicitudPendiente.setMonto(new BigDecimal("150.00"));
        solicitudPendiente.setMetodoPago("Yape");
        solicitudPendiente.setEstado("Pendiente");
        solicitudPendiente.setFechaSubida(new Date());
    }

    @Test
    @DisplayName("HU17 Escenario 1: Aprobar solicitud reactiva la membresía del estudiante por 30 días")
    void testAprobarSolicitudExitoso() {
        when(solicitudPagoRepository.findById(1)).thenReturn(Optional.of(solicitudPendiente));

        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(10);
        estudiante.setIdPersona(5);
        estudiante.setEstado(0); // Inactivo antes del pago

        Persona persona = new Persona();
        persona.setIdPersona(5);
        persona.setEstado(0);

        when(entityManager.find(Estudiante.class, 10)).thenReturn(estudiante);
        when(entityManager.find(Persona.class, 5)).thenReturn(persona);

        SolicitudPago resultado = solicitudPagoService.aprobarSolicitud(1);

        assertEquals("Aprobado", resultado.getEstado());
        assertNotNull(resultado.getFechaRevision());
        assertEquals(1, estudiante.getEstado());
        assertEquals(1, persona.getEstado());

        verify(solicitudPagoRepository).save(solicitudPendiente);
    }

    @Test
    @DisplayName("HU17 Escenario 2: Rechazar solicitud con motivo válido registra observación y fecha")
    void testRechazarSolicitudConMotivoValido() {
        when(solicitudPagoRepository.findById(1)).thenReturn(Optional.of(solicitudPendiente));
        when(solicitudPagoRepository.save(any(SolicitudPago.class))).thenAnswer(i -> i.getArgument(0));

        String motivo = "El voucher no es legible y el número de operación no coincide.";
        SolicitudPago resultado = solicitudPagoService.rechazarSolicitud(1, motivo);

        assertEquals("Rechazado", resultado.getEstado());
        assertEquals(motivo, resultado.getObservacion());
        assertNotNull(resultado.getFechaRevision());
    }

    @Test
    @DisplayName("HU17 Escenario 3: Rechazar solicitud sin motivo o con motivo en blanco lanza excepción obligatoria")
    void testRechazarSolicitudSinMotivoLanzaExcepcion() {
        // Intento con null
        RuntimeException exNull = assertThrows(RuntimeException.class, () -> {
            solicitudPagoService.rechazarSolicitud(1, null);
        });
        assertTrue(exNull.getMessage().contains("El motivo u observación de rechazo es obligatorio"));

        // Intento con espacios en blanco
        RuntimeException exBlank = assertThrows(RuntimeException.class, () -> {
            solicitudPagoService.rechazarSolicitud(1, "   ");
        });
        assertTrue(exBlank.getMessage().contains("El motivo u observación de rechazo es obligatorio"));
    }
}
