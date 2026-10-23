package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.model.DocenteC;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class DocenteCServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private DocenteCService docenteCService;

    @Test
    @DisplayName("CP-DOCENTEC-01: Listar docentes mapea resultados de Object[] a entidad DocenteC")
    @SuppressWarnings("unchecked")
    void testListarDocentes_MapeoCorrecto() {
        List<Object[]> mockRows = new ArrayList<>();
        mockRows.add(new Object[]{1, "Profesor Carlos Mendoza"});
        mockRows.add(new Object[]{2, "Dra. Ana López"});

        when(entityManager.createStoredProcedureQuery("SP_ListarDocentes")).thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(mockRows);

        List<DocenteC> resultado = docenteCService.listarDocentes();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).getIdDocente());
        assertEquals("Profesor Carlos Mendoza", resultado.get(0).getNombres());
        assertEquals(2, resultado.get(1).getIdDocente());
        assertEquals("Dra. Ana López", resultado.get(1).getNombres());

        verify(entityManager).createStoredProcedureQuery("SP_ListarDocentes");
        verify(storedProcedureQuery).getResultList();
    }

    @Test
    @DisplayName("CP-DOCENTEC-02: Listar docentes retorna lista vacía cuando no hay registros")
    @SuppressWarnings("unchecked")
    void testListarDocentes_ListaVacia() {
        when(entityManager.createStoredProcedureQuery("SP_ListarDocentes")).thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(new ArrayList<>());

        List<DocenteC> resultado = docenteCService.listarDocentes();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }
}
