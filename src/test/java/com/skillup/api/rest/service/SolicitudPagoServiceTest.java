package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.dtos.SolicitudPagoResponseDTO;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Matricula;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.SolicitudPago;
import com.skillup.api.rest.model.Usuario;
import com.skillup.api.rest.repository.EstudianteRepository;
import com.skillup.api.rest.repository.MatriculaRepository;
import com.skillup.api.rest.repository.PersonaRepository;
import com.skillup.api.rest.repository.SolicitudPagoRepository;
import com.skillup.api.rest.repository.UsuarioRepository;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
public class SolicitudPagoServiceTest {

    @Mock
    private SolicitudPagoRepository solicitudPagoRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MatriculaRepository matriculaRepository;

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
    @DisplayName("HU17 Escenario 1: Aprobar solicitud reactiva la cuenta y restablece intentos")
    void testAprobarSolicitudExitoso() {
        when(solicitudPagoRepository.findById(1)).thenReturn(Optional.of(solicitudPendiente));

        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(10);
        estudiante.setIdPersona(5);
        estudiante.setEstado(0); // Inactivo antes del pago

        Persona persona = new Persona();
        persona.setIdPersona(5);
        persona.setEstado(0);

        Matricula matricula = new Matricula();
        matricula.setIdMatricula(1);
        matricula.setIdEstudiante(10);
        matricula.setIntentosExamen(0);

        when(entityManager.find(Estudiante.class, 10)).thenReturn(estudiante);
        when(entityManager.find(Persona.class, 5)).thenReturn(persona);
        when(matriculaRepository.findByIdEstudiante(10)).thenReturn(Collections.singletonList(matricula));

        SolicitudPago resultado = solicitudPagoService.aprobarSolicitud(1);

        assertEquals("Aprobado", resultado.getEstado());
        assertNotNull(resultado.getFechaRevision());
        assertEquals(1, estudiante.getEstado());
        assertEquals(1, persona.getEstado());
        assertEquals(2, matricula.getIntentosExamen());

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
    @DisplayName("HU17 Escenario 3: Rechazar solicitud sin motivo lanza excepción obligatoria")
    void testRechazarSolicitudSinMotivoLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> {
            solicitudPagoService.rechazarSolicitud(1, null);
        });

        assertThrows(RuntimeException.class, () -> {
            solicitudPagoService.rechazarSolicitud(1, "   ");
        });
    }

    @Test
    @DisplayName("CP-PAGO-04: Registrar pago - Validación de N° Operación obligatorio")
    void testRegistrarSolicitud_ValidacionNumOperacionObligatorio() {
        SolicitudPago pagoInvalido = new SolicitudPago();
        pagoInvalido.setNumOperacion(""); // Vacío
        pagoInvalido.setMonto(new BigDecimal("50.00"));

        assertThrows(RuntimeException.class, () -> {
            solicitudPagoService.registrarSolicitud(pagoInvalido);
        });
    }

    @Test
    @DisplayName("CP-PAGO-05: Registrar pago - Validación de Monto obligatorio")
    void testRegistrarSolicitud_ValidacionMontoObligatorio() {
        SolicitudPago pagoInvalido = new SolicitudPago();
        pagoInvalido.setNumOperacion("123456");
        pagoInvalido.setMonto(null); // Nulo

        assertThrows(RuntimeException.class, () -> {
            solicitudPagoService.registrarSolicitud(pagoInvalido);
        });
    }

    @Test
    @DisplayName("CP-PAGO-06: Registrar pago con comprobante - Validación de Monto mayor a cero")
    void testRegistrarSolicitudConComprobante_MontoInvalido() {
        assertThrows(RuntimeException.class, () -> {
            solicitudPagoService.registrarSolicitudConComprobante(
                10, "123456", BigDecimal.ZERO, "Yape", "Pago", null
            );
        });
    }

    @Test
    @DisplayName("CP-PAGO-07: Registrar pago con comprobante - Validación de Método de Pago obligatorio")
    void testRegistrarSolicitudConComprobante_MetodoPagoObligatorio() {
        assertThrows(RuntimeException.class, () -> {
            solicitudPagoService.registrarSolicitudConComprobante(
                10, "123456", new BigDecimal("50.00"), "", "Pago", null
            );
        });
    }

    @Test
    @DisplayName("CP-PAGO-08: Listar solicitudes filtradas por estado Pendiente")
    void testListarSolicitudes_FiltroPorEstadoPendiente() {
        when(solicitudPagoRepository.findByEstadoOrderByFechaSubidaDesc("Pendiente"))
            .thenReturn(Collections.singletonList(solicitudPendiente));

        List<SolicitudPagoResponseDTO> lista = solicitudPagoService.listarSolicitudes("Pendiente");

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("OP-998877", lista.get(0).getNumOperacion());
        verify(solicitudPagoRepository).findByEstadoOrderByFechaSubidaDesc("Pendiente");
    }
}
