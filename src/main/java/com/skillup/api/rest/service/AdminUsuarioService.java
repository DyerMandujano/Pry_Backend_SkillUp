package com.skillup.api.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillup.api.rest.dtos.UsuarioAdminDTO;
import com.skillup.api.rest.model.Docente;
import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.model.Persona;
import com.skillup.api.rest.model.Usuario;
import com.skillup.api.rest.repository.PersonaRepository;
import com.skillup.api.rest.repository.UsuarioRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Service
public class AdminUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<UsuarioAdminDTO> listarUsuarios(String rolFiltro, String buscar) {
        StringBuilder jpql = new StringBuilder("SELECT u FROM Usuario u WHERE 1=1 ");

        if (rolFiltro != null && !rolFiltro.trim().isEmpty() && !rolFiltro.equalsIgnoreCase("todos")) {
            jpql.append("AND LOWER(u.rol) = LOWER(:rol) ");
        }

        jpql.append("ORDER BY u.idUsuario DESC");

        TypedQuery<Usuario> query = entityManager.createQuery(jpql.toString(), Usuario.class);

        if (rolFiltro != null && !rolFiltro.trim().isEmpty() && !rolFiltro.equalsIgnoreCase("todos")) {
            query.setParameter("rol", rolFiltro.trim().toLowerCase());
        }

        List<Usuario> usuarios = query.getResultList();
        List<UsuarioAdminDTO> resultado = new ArrayList<>();

        String filtroTexto = (buscar != null && !buscar.trim().isEmpty()) ? buscar.trim().toLowerCase() : null;

        for (Usuario u : usuarios) {
            Persona p = entityManager.find(Persona.class, u.getIdPersona());

            String nombres = (p != null && p.getNombres() != null) ? p.getNombres() : "";
            String apellidos = (p != null && p.getApellidos() != null) ? p.getApellidos() : "";
            String dni = (p != null && p.getDni() != null) ? p.getDni() : "";
            String nombreCompleto = (nombres + " " + apellidos).trim();
            if (nombreCompleto.isEmpty()) nombreCompleto = u.getUsername();

            // Filtrado de texto en memoria si se especificó 'buscar'
            if (filtroTexto != null) {
                boolean coincide = u.getUsername().toLowerCase().contains(filtroTexto)
                    || dni.toLowerCase().contains(filtroTexto)
                    || nombres.toLowerCase().contains(filtroTexto)
                    || apellidos.toLowerCase().contains(filtroTexto);

                if (!coincide) {
                    continue;
                }
            }

            UsuarioAdminDTO dto = new UsuarioAdminDTO();
            dto.setIdUsuario(u.getIdUsuario());
            dto.setIdPersona(u.getIdPersona());
            dto.setUsername(u.getUsername());
            dto.setDni(dni);
            dto.setNombres(nombres);
            dto.setApellidos(apellidos);
            dto.setNombreCompleto(nombreCompleto);
            dto.setGenero(p != null ? p.getGenero() : "");
            dto.setRol(u.getRol());
            dto.setEstado(p != null ? p.getEstado() : 1);
            dto.setFechaRegistro(u.getFecRegistro());

            resultado.add(dto);
        }

        return resultado;
    }

    @Transactional
    public UsuarioAdminDTO cambiarEstadoUsuario(int idUsuario, int nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new RuntimeException("No se encontró el usuario con ID: " + idUsuario));

        Persona persona = entityManager.find(Persona.class, usuario.getIdPersona());
        if (persona != null) {
            persona.setEstado(nuevoEstado);
            entityManager.merge(persona);
        }

        // Si es estudiante o docente, actualizamos su estado también
        try {
            if ("docente".equalsIgnoreCase(usuario.getRol())) {
                TypedQuery<Docente> dq = entityManager.createQuery(
                    "SELECT d FROM Docente d WHERE d.idPersona = :idp", Docente.class
                );
                dq.setParameter("idp", usuario.getIdPersona());
                List<Docente> docList = dq.getResultList();
                for (Docente d : docList) {
                    d.setEstado(nuevoEstado);
                    entityManager.merge(d);
                }
            } else if ("estudiante".equalsIgnoreCase(usuario.getRol())) {
                TypedQuery<Estudiante> eq = entityManager.createQuery(
                    "SELECT e FROM Estudiante e WHERE e.idPersona = :idp", Estudiante.class
                );
                eq.setParameter("idp", usuario.getIdPersona());
                List<Estudiante> estList = eq.getResultList();
                for (Estudiante e : estList) {
                    e.setEstado(nuevoEstado);
                    entityManager.merge(e);
                }
            }
        } catch (Exception ignored) {
        }

        UsuarioAdminDTO dto = new UsuarioAdminDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setIdPersona(usuario.getIdPersona());
        dto.setUsername(usuario.getUsername());
        if (persona != null) {
            dto.setDni(persona.getDni());
            dto.setNombres(persona.getNombres());
            dto.setApellidos(persona.getApellidos());
            dto.setNombreCompleto(persona.getNombres() + " " + persona.getApellidos());
            dto.setGenero(persona.getGenero());
            dto.setEstado(persona.getEstado());
        } else {
            dto.setEstado(nuevoEstado);
        }
        dto.setRol(usuario.getRol());
        dto.setFechaRegistro(usuario.getFecRegistro());

        return dto;
    }
}
