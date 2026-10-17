package com.skillup.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillup.api.rest.model.SolicitudPago;

@Repository
public interface SolicitudPagoRepository extends JpaRepository<SolicitudPago, Integer> {
    List<SolicitudPago> findByEstadoOrderByFechaSubidaDesc(String estado);
    List<SolicitudPago> findByIdEstudiante(int idEstudiante);
}
