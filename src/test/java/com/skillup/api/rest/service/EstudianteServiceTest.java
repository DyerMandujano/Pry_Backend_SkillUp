package com.skillup.api.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillup.api.rest.model.Estudiante;
import com.skillup.api.rest.repository.EstudianteRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;

@ExtendWith(MockitoExtension.class)
public class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private EstudianteService estudianteService;

    private Estudiante estudiantePrueba;

    @BeforeEach
    void setUp() {
        estudiantePrueba = new Estudiante();
        estudiantePrueba.setIdEstudiante(1);
        estudiantePrueba.setIdPersona(10);
        estudiantePrueba.setNivelEducativo("Universitario");
        estudiantePrueba.setEstado(1);
    }

    @Test
    @DisplayName("CP-ESTUDIANTE-01: Obtener estudiante por ID existente retorna la entidad correspondiente")
    void testObtenerEstudiante_Existente() {
        when(entityManager.find(Estudiante.class, 1)).thenReturn(estudiantePrueba);

        Estudiante resultado = estudianteService.obtenerEstudiante(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdEstudiante());
        assertEquals("Universitario", resultado.getNivelEducativo());
        verify(entityManager).find(Estudiante.class, 1);
    }

    @Test
    @DisplayName("CP-ESTUDIANTE-02 [Negativo]: Obtener estudiante inexistente arroja RuntimeException controlada")
    void testObtenerEstudiante_NoEncontrado() {
        when(entityManager.find(Estudiante.class, 99)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            estudianteService.obtenerEstudiante(99);
        });

        assertEquals("Estudiante no encontrado con ID: 99", ex.getMessage());
        verify(entityManager).find(Estudiante.class, 99);
    }

    @Test
    @DisplayName("CP-ESTUDIANTE-03: Listar estudiantes ejecuta SP dbo.SP_ListarEstudiante y retorna lista")
    @SuppressWarnings("unchecked")
    void testListarEstudiantes_Exitoso() {
        List<Estudiante> mockLista = Arrays.asList(estudiantePrueba);
        when(entityManager.createStoredProcedureQuery("dbo.SP_ListarEstudiante", Estudiante.class))
                .thenReturn(storedProcedureQuery);
        when(storedProcedureQuery.getResultList()).thenReturn(mockLista);

        List<Estudiante> resultado = estudianteService.listarEstudiantes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Universitario", resultado.get(0).getNivelEducativo());
        verify(entityManager).createStoredProcedureQuery("dbo.SP_ListarEstudiante", Estudiante.class);
        verify(storedProcedureQuery).getResultList();
    }
}
