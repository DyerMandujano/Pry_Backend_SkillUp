package com.skillup.api.rest.controller;

import com.skillup.api.rest.model.Certificado;
import com.skillup.api.rest.repository.CertificadoRepository;
import com.skillup.api.rest.service.CertificadoService;
import com.skillup.api.rest.dtos.CertificadoPorEstudiante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/certificados")
@CrossOrigin(origins = "http://localhost:4200")
public class CertificadoController {

    
    @Autowired
    private CertificadoService certificadoService;

    
    @Autowired
    private CertificadoRepository certificadoRepository;


    @GetMapping("/estudiante/{id}")
    public List<CertificadoPorEstudiante> listarCertificadosPorEstudiante(@PathVariable("id") int idEstudiante) {
        return certificadoService.listarCertificados(idEstudiante);
    }

   
    @PostMapping("/generar")
    public ResponseEntity<?> generarOObtenerCertificado(@RequestParam int idEstudiante, @RequestParam int idCurso) {
        
       
        Optional<Certificado> certificadoExistente = certificadoRepository.findByEstudianteAndCurso(idEstudiante, idCurso);

        if (certificadoExistente.isPresent()) {
            return ResponseEntity.ok(certificadoExistente.get());
        }

        
        Integer idMatricula = certificadoRepository.findIdMatricula(idEstudiante, idCurso);

        if (idMatricula == null) {
            return ResponseEntity.badRequest().body("El estudiante no está matriculado en este curso.");
        }

        Certificado nuevoCertificado = new Certificado();
        nuevoCertificado.setIdMatricula(idMatricula);
        nuevoCertificado.setFechaEmision(LocalDate.now());
        nuevoCertificado.setCodigoCertificado("CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        nuevoCertificado.setMensaje("Por completar exitosamente el curso de Especialización Técnica.");

        Certificado certificadoGuardado = certificadoRepository.save(nuevoCertificado);

        return ResponseEntity.ok(certificadoGuardado);
    }
}
