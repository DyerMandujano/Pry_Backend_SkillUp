package com.skillup.api.rest.controller;


import com.skillup.api.rest.model.DocenteC;
import com.skillup.api.rest.service.DocenteCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
