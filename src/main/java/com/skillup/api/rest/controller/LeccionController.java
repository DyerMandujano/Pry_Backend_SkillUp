package com.skillup.api.rest.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skillup.api.rest.model.Leccion;
import com.skillup.api.rest.service.LeccionService;

@RestController
@RequestMapping("/api/lecciones")
public class LeccionController {

    @Autowired
    private LeccionService leccionService;

    @GetMapping("/seccion/{idSeccion}")
    public ResponseEntity<List<Leccion>> listarPorSeccion(@PathVariable int idSeccion) {
        List<Leccion> lecciones = leccionService.listarLeccionesPorSeccion(idSeccion);
        return new ResponseEntity<>(lecciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Leccion> obtenerPorId(@PathVariable int id) {
        Leccion leccion = leccionService.obtenerLeccionPorId(id);
        if (leccion != null) {
            return new ResponseEntity<>(leccion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 🔹 MODIFICADO: Ahora recibe FormData (Leccion en JSON + Archivos físicos)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertar(
            @RequestPart("leccion") Leccion nuevaLeccion,
            @RequestPart(value = "archivos", required = false) List<MultipartFile> archivos,
            @RequestPart(value = "subtitulos", required = false) MultipartFile subtitulos) {
        try {
            guardarArchivosFisicos(nuevaLeccion, archivos);
            procesarArchivoSubtitulos(nuevaLeccion, subtitulos);
            if ((nuevaLeccion.getTranscripcionTexto() == null || nuevaLeccion.getTranscripcionTexto().trim().isEmpty()) && nuevaLeccion.getUrlVideo() != null) {
                String ytTranscript = obtenerTranscripcionRealYouTube(nuevaLeccion.getUrlVideo());
                if (ytTranscript != null) {
                    nuevaLeccion.setTranscripcionTexto(ytTranscript);
                }
            }
            leccionService.insertarLeccion(nuevaLeccion);
            return new ResponseEntity<>("Lección creada correctamente.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear la lección: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 🔹 MODIFICADO: Ahora recibe FormData
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> actualizar(
            @PathVariable int id, 
            @RequestPart("leccion") Leccion leccionActualizada,
            @RequestPart(value = "archivos", required = false) List<MultipartFile> archivos,
            @RequestPart(value = "subtitulos", required = false) MultipartFile subtitulos) {
        try {
            leccionActualizada.setIdLeccion(id);
            guardarArchivosFisicos(leccionActualizada, archivos);
            procesarArchivoSubtitulos(leccionActualizada, subtitulos);
            if ((leccionActualizada.getTranscripcionTexto() == null || leccionActualizada.getTranscripcionTexto().trim().isEmpty()) && leccionActualizada.getUrlVideo() != null) {
                String ytTranscript = obtenerTranscripcionRealYouTube(leccionActualizada.getUrlVideo());
                if (ytTranscript != null) {
                    leccionActualizada.setTranscripcionTexto(ytTranscript);
                }
            }
            leccionService.actualizarLeccion(leccionActualizada);
            return new ResponseEntity<>("Lección actualizada correctamente.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar la lección: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        try {
            leccionService.eliminarLeccion(id);
            return new ResponseEntity<>("Lección eliminada correctamente.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la lección: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

        // 🛠️ FUNCIÓN UTILITARIA: Guarda los archivos en el disco duro y asigna la URL (Refactorizado: máx. anidamiento <= 2)
    private void guardarArchivosFisicos(Leccion leccion, List<MultipartFile> archivos) {
        // Cláusula de guarda para evitar anidamiento innecesario (Nivel 1)
        if (archivos == null || archivos.isEmpty() || leccion.getMateriales() == null) {
            return;
        }

        // Crea una carpeta llamada "uploads" en la raíz del backend si no existe
        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        int archivoIndex = 0;

        for (com.skillup.api.rest.model.Material mat : leccion.getMateriales()) {
            if (archivoIndex >= archivos.size()) {
                break;
            }

            // Solo procesamos si no tiene URL o se marcó vacía para subir nuevo archivo
            if (mat.getUrlMaterial() != null && !mat.getUrlMaterial().trim().isEmpty()) {
                continue;
            }

            MultipartFile file = archivos.get(archivoIndex);
            guardarArchivoEnDisco(mat, file, uploadDir);
            archivoIndex++;
        }
    }

    private void guardarArchivoEnDisco(com.skillup.api.rest.model.Material mat, MultipartFile file, String uploadDir) {
        try {
            // Generamos un nombre único para evitar sobreescribir archivos con el mismo nombre
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Guardamos la ruta relativa en el objeto que irá a SQL Server
            mat.setUrlMaterial(uploadDir + fileName);
            if (mat.getNombreArchivo() == null || mat.getNombreArchivo().trim().isEmpty()) {
                mat.setNombreArchivo(file.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PutMapping(value = "/{id}/transcripcion")
    public ResponseEntity<String> actualizarTranscripcion(
            @PathVariable int id,
            @org.springframework.web.bind.annotation.RequestBody java.util.Map<String, String> payload) {
        try {
            Leccion leccion = leccionService.obtenerLeccionPorId(id);
            if (leccion == null) {
                return new ResponseEntity<>("Lección no encontrada.", HttpStatus.NOT_FOUND);
            }
            String texto = payload.get("transcripcionTexto");
            leccion.setTranscripcionTexto(texto);
            leccionService.actualizarLeccion(leccion);
            return new ResponseEntity<>("Transcripción actualizada correctamente.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar transcripción: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void procesarArchivoSubtitulos(Leccion leccion, MultipartFile subtitulos) {
        if (subtitulos == null || subtitulos.isEmpty()) {
            return;
        }
        try {
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = System.currentTimeMillis() + "_" + subtitulos.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.copy(subtitulos.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            leccion.setUrlSubtitulos(uploadDir + fileName);

            // Si no tiene transcripción en texto, intentamos extraer del archivo VTT/SRT/TXT
            if (leccion.getTranscripcionTexto() == null || leccion.getTranscripcionTexto().trim().isEmpty()) {
                String contenido = new String(subtitulos.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
                leccion.setTranscripcionTexto(contenido);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/transcripcion-youtube")
    public ResponseEntity<String> obtenerTranscripcionPorUrl(@org.springframework.web.bind.annotation.RequestParam("url") String urlVideo) {
        String transcripcion = obtenerTranscripcionRealYouTube(urlVideo);
        if (transcripcion != null && !transcripcion.trim().isEmpty()) {
            return new ResponseEntity<>(transcripcion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudieron extraer subtítulos para este video.", HttpStatus.NOT_FOUND);
        }
    }

    public String obtenerTranscripcionRealYouTube(String urlVideo) {
        try {
            String videoId = extraerVideoId(urlVideo);
            if (videoId == null) return null;

            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            String payload = "{\"context\":{\"client\":{\"clientName\":\"ANDROID\",\"clientVersion\":\"20.10.38\"}},\"videoId\":\"" + videoId + "\"}";

            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("https://www.youtube.com/youtubei/v1/player?prettyPrint=false"))
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "com.google.android.youtube/20.10.38 (Linux; U; Android 14)")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) return null;

            String body = response.body();
            int idx = body.indexOf("\"baseUrl\":\"");
            if (idx == -1) return null;
            int endIdx = body.indexOf("\"", idx + 11);
            String baseUrl = body.substring(idx + 11, endIdx).replace("\\u0026", "&");

            java.net.http.HttpRequest xmlReq = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(baseUrl))
                    .header("User-Agent", "Mozilla/5.0")
                    .GET()
                    .build();

            java.net.http.HttpResponse<String> xmlRes = client.send(xmlReq, java.net.http.HttpResponse.BodyHandlers.ofString());
            String xml = xmlRes.body();

            java.util.regex.Pattern p = java.util.regex.Pattern.compile("<text start=\"([^\"]*)\" dur=\"([^\"]*)\">([^<]*)</text>");
            java.util.regex.Matcher m = p.matcher(xml);

            StringBuilder sb = new StringBuilder();
            while (m.find()) {
                double startSec = Double.parseDouble(m.group(1));
                String rawText = m.group(3).replace("&amp;", "&").replace("&#39;", "'").replace("&quot;", "\"").trim();
                if (rawText.isEmpty() || rawText.equals("[Música]")) continue;
                int totalSec = (int) Math.floor(startSec);
                int mins = totalSec / 60;
                int secs = totalSec % 60;
                sb.append(String.format("[%02d:%02d] %s\n", mins, secs, rawText));
            }
            return sb.length() > 0 ? sb.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String extraerVideoId(String url) {
        if (url == null) return null;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?:youtu\\.be/|watch\\?v=|embed/)([^#&?]{11})");
        java.util.regex.Matcher m = p.matcher(url);
        return m.find() ? m.group(1) : null;
    }

}
