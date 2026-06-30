package com.skillup.api.rest.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillup.api.rest.model.DocenteC;
import com.skillup.api.rest.service.DocenteCService;

@RestController
@RequestMapping("/api/docentess")
@CrossOrigin(origins = "*")
public class DocenteCController {

	
	@Autowired
    private DocenteCService docenteCService;

    // 🔹 GET: Listar docentes (SP_ListarDocentes)
    @GetMapping
    public List<DocenteC> listarDocentes() {
        return docenteCService.listarDocentes();
    }
}
