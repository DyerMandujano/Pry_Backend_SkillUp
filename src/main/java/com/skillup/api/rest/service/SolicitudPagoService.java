package com.skillup.api.rest.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Service
public class SolicitudPagoService {

    @Autowired
    private SolicitudPagoRepository solicitudPagoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<SolicitudPagoResponseDTO> listarSolicitudes(String estadoFiltro) {
        List<SolicitudPago> lista;
        if (estadoFiltro != null && !estadoFiltro.trim().isEmpty() && !estadoFiltro.equalsIgnoreCase("todos")) {
            lista = solicitudPagoRepository.findByEstadoOrderByFechaSubidaDesc(estadoFiltro.trim());
        } else {
            lista = solicitudPagoRepository.findAll();
            lista.sort((a, b) -> {
                if (a.getFechaSubida() == null) return 1;
                if (b.getFechaSubida() == null) return -1;
                return b.getFechaSubida().compareTo(a.getFechaSubida());
            });
        }

        List<SolicitudPagoResponseDTO> resultado = new ArrayList<>();
        for (SolicitudPago sp : lista) {
            resultado.add(convertirADTO(sp));
        }

        return resultado;
    }

    public List<SolicitudPagoResponseDTO> listarSolicitudesPorEstudiante(int idEstudiante) {
        List<SolicitudPago> lista = solicitudPagoRepository.findByIdEstudianteOrderByFechaSubidaDesc(idEstudiante);
        List<SolicitudPagoResponseDTO> resultado = new ArrayList<>();
        for (SolicitudPago sp : lista) {
            resultado.add(convertirADTO(sp));
        }
        return resultado;
    }

    private SolicitudPagoResponseDTO convertirADTO(SolicitudPago sp) {
        SolicitudPagoResponseDTO dto = new SolicitudPagoResponseDTO();
        dto.setIdSolicitud(sp.getIdSolicitud());
        dto.setIdEstudiante(sp.getIdEstudiante());
        dto.setIdUsuario(sp.getIdUsuario());
        dto.setNumOperacion(sp.getNumOperacion());
        dto.setMonto(sp.getMonto());
        dto.setMetodoPago(sp.getMetodoPago());
        dto.setFechaSubida(sp.getFechaSubida());
        dto.setFechaRevision(sp.getFechaRevision());
        dto.setObservacion(sp.getObservacion());
        dto.setImagenComprobante(sp.getImagenComprobante());
        dto.setEstado(sp.getEstado());

        try {
            Estudiante est = entityManager.find(Estudiante.class, sp.getIdEstudiante());
            if (est != null) {
                Persona p = entityManager.find(Persona.class, est.getIdPersona());
                if (p != null) {
                    dto.setEstudianteNombre(p.getNombres() + " " + p.getApellidos());
                    dto.setEstudianteDni(p.getDni());
                }
                TypedQuery<Usuario> uQuery = entityManager.createQuery(
                    "SELECT u FROM Usuario u WHERE u.idPersona = :idp", Usuario.class
                );
                uQuery.setParameter("idp", est.getIdPersona());
                List<Usuario> uList = uQuery.getResultList();
                if (!uList.isEmpty()) {
                    dto.setUsername(uList.get(0).getUsername());
                    if (dto.getIdUsuario() == null) {
                        dto.setIdUsuario(uList.get(0).getIdUsuario());
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return dto;
    }

    @Transactional
    public SolicitudPago aprobarSolicitud(int idSolicitud) {
        SolicitudPago solicitud = solicitudPagoRepository.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("No se encontró la solicitud de pago con ID: " + idSolicitud));

        solicitud.setEstado("Aprobado");
        solicitud.setFechaRevision(new Date());
        solicitudPagoRepository.save(solicitud);

        // 1. Reactivar la cuenta del estudiante (estado = 1) y persona (estado = 1)
        try {
            Estudiante est = entityManager.find(Estudiante.class, solicitud.getIdEstudiante());
            if (est != null) {
                est.setEstado(1);
                entityManager.merge(est);

                Persona persona = entityManager.find(Persona.class, est.getIdPersona());
                if (persona != null) {
                    persona.setEstado(1);
                    entityManager.merge(persona);
                }

                // 2. Desbloquear usuario si estaba bloqueado
                usuarioRepository.findByIdPersona(est.getIdPersona()).ifPresent(u -> {
                    u.setIntentosFallidos(0);
                    u.setFecBloqueoHasta(null);
                    usuarioRepository.save(u);
                });

                // 3. Restablecer los intentos de evaluación en Matricula (Intentos_Examen = 2)
                List<Matricula> matriculas = matriculaRepository.findByIdEstudiante(solicitud.getIdEstudiante());
                for (Matricula m : matriculas) {
                    m.setIntentosExamen(2);
                    matriculaRepository.save(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return solicitud;
    }

    @Transactional
    public SolicitudPago rechazarSolicitud(int idSolicitud, String observacion) {
        if (observacion == null || observacion.trim().isEmpty()) {
            throw new RuntimeException("El motivo u observación de rechazo es obligatorio");
        }

        SolicitudPago solicitud = solicitudPagoRepository.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("No se encontró la solicitud de pago con ID: " + idSolicitud));

        solicitud.setEstado("Rechazado");
        solicitud.setObservacion(observacion.trim());
        solicitud.setFechaRevision(new Date());

        return solicitudPagoRepository.save(solicitud);
    }

    @Transactional
    public SolicitudPago registrarSolicitud(SolicitudPago solicitud) {
        if (solicitud.getNumOperacion() == null || solicitud.getNumOperacion().trim().isEmpty()) {
            throw new RuntimeException("El número de operación es obligatorio");
        }
        if (solicitud.getMonto() == null) {
            throw new RuntimeException("El monto es obligatorio");
        }

        solicitud.setFechaSubida(new Date());
        solicitud.setEstado("Pendiente");
        return solicitudPagoRepository.save(solicitud);
    }

    @Transactional
    public SolicitudPago registrarSolicitudConComprobante(
            int idEstudiante,
            String numOperacion,
            BigDecimal monto,
            String metodoPago,
            String observacion,
            MultipartFile comprobante) {

        if (numOperacion == null || numOperacion.trim().isEmpty()) {
            throw new RuntimeException("El número de operación es obligatorio");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor a 0");
        }
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new RuntimeException("El método de pago es obligatorio");
        }

        SolicitudPago sp = new SolicitudPago();
        sp.setIdEstudiante(idEstudiante);
        sp.setNumOperacion(numOperacion.trim());
        sp.setMonto(monto);
        sp.setMetodoPago(metodoPago.trim());
        sp.setObservacion(observacion != null ? observacion.trim() : null);
        sp.setFechaSubida(new Date());
        sp.setEstado("Pendiente");

        if (comprobante != null && !comprobante.isEmpty()) {
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = "pago_" + System.currentTimeMillis() + "_" + comprobante.getOriginalFilename().replaceAll("\\s+", "_");
            Path filePath = Paths.get(uploadDir + fileName);
            try {
                Files.copy(comprobante.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                sp.setImagenComprobante(uploadDir + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el comprobante de pago: " + e.getMessage());
            }
        }

        return solicitudPagoRepository.save(sp);
    }
}
