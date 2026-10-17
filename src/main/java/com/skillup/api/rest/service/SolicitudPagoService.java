package com.skillup.api.rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillup.api.rest.dtos.SolicitudPagoResponseDTO;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.SolicitudPago;
import com.skillup.api.rest.model.Usuario;
import com.skillup.api.rest.repository.EstudianteRepository;
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

            // Enriquecer datos con el Estudiante / Persona / Usuario
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

            resultado.add(dto);
        }

        return resultado;
    }

    @Transactional
    public SolicitudPago aprobarSolicitud(int idSolicitud) {
        SolicitudPago solicitud = solicitudPagoRepository.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("No se encontró la solicitud de pago con ID: " + idSolicitud));

        solicitud.setEstado("Aprobado");
        solicitud.setFechaRevision(new Date());
        solicitudPagoRepository.save(solicitud);

        // Reactivar la cuenta del estudiante (estado = 1)
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
            }
        } catch (Exception e) {
            // Log si ocurre excepción pero la solicitud queda guardada
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
}
