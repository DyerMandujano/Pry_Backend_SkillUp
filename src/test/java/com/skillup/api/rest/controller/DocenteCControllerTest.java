package com.skillup.api.rest.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.skillup.api.rest.model.DocenteC;
import com.skillup.api.rest.service.DocenteCService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DocenteCControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DocenteCService docenteCService;

    @InjectMocks
    private DocenteCController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("REST-DOCC-01: GET /api/docentess retorna lista de docentes")
    void testListarDocentes() throws Exception {
        DocenteC doc = new DocenteC();
        doc.setIdDocente(5);
        doc.setNombres("Profesor Ana");

        when(docenteCService.listarDocentes()).thenReturn(List.of(doc));

        mockMvc.perform(get("/api/docentess")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDocente").value(5))
                .andExpect(jsonPath("$[0].nombres").value("Profesor Ana"));

        verify(docenteCService, times(1)).listarDocentes();
    }
}
