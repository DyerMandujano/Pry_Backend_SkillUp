package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.model.Docente;
import com.skillup.api.rest.repository.DocenteRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class DocenteServiceTest {

    @Mock
    private DocenteRepository docenteRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private DocenteService docenteService;

    private Docente docentePrueba;

    @BeforeEach
    void setUp() {
        docentePrueba = new Docente();
        docentePrueba.setIdDocente(2);
        docentePrueba.setIdPersona(20);
        docentePrueba.setEspecialidad("Ingeniería de Software");
        docentePrueba.setGradoAcademico("Magíster");
        docentePrueba.setEstado(1);
    }

    @Test
    @DisplayName("CP-DOCENTE-01: Obtener docente por ID existente retorna la entidad correspondiente")
    void testObtenerDocente_Existente() {
        when(entityManager.find(Docente.class, 2)).thenReturn(docentePrueba);

        Docente resultado = docenteService.obtenerDocente(2);

        assertNotNull(resultado);
        assertEquals(2, resultado.getIdDocente());
        assertEquals("Ingeniería de Software", resultado.getEspecialidad());
        verify(entityManager).find(Docente.class, 2);
    }

    @Test
    @DisplayName("CP-DOCENTE-02 [Negativo]: Obtener docente inexistente arroja RuntimeException")
    void testObtenerDocente_NoExiste() {
        when(entityManager.find(Docente.class, 99)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            docenteService.obtenerDocente(99);
        });

        assertEquals("Docente no encontrado con ID: 99", ex.getMessage());
        verify(entityManager).find(Docente.class, 99);
    }

    @Test
    @DisplayName("CP-DOCENTE-03: Listar todos los docentes ejecuta SP dbo.SP_ListarDocente")
    @SuppressWarnings("unchecked")
    void testListarTodosDocentes_Exitoso() {
        when(entityManager.createStoredProcedureQuery("dbo.SP_ListarDocente", Docente.class))
                .thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(Collections.singletonList(docentePrueba));

        List<Docente> resultado = docenteService.listarTodosDocentes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ingeniería de Software", resultado.get(0).getEspecialidad());
        verify(entityManager).createStoredProcedureQuery("dbo.SP_ListarDocente", Docente.class);
    }
}
