package com.skillup.api.rest.service;

import com.skillup.api.rest.model.DocenteC;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocenteCService {

	@PersistenceContext
    private EntityManager entityManager;

    // 🔹 Listar todos los docentes
    public List<DocenteC> listarDocentes() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("SP_ListarDocentes");

        List<Object[]> resultados = query.getResultList();
        List<DocenteC> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            DocenteC docente = new DocenteC();
            docente.setIdDocente((Integer) fila[0]);
            docente.setNombres((String) fila[1]);
            lista.add(docente);
        }

        return lista;
    }
}
