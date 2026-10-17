package com.skillup.api.rest.model;

public class CursoMatricula {

    private int idEstudiante;
    private int idCurso;
    private String nombreCurso;
    private String nivel;
    private int duracionMin;
    private String imagenCurso1;
    private String imagenCurso2;

    public CursoMatricula() {
    }

    public CursoMatricula(int idEstudiante, int idCurso, String nombreCurso, String nivel, int duracionMin, String imagenCurso1, String imagenCurso2) {
        this.idEstudiante = idEstudiante;
        this.idCurso = idCurso;
        this.nombreCurso = nombreCurso;
        this.nivel = nivel;
        this.duracionMin = duracionMin;
        this.imagenCurso1 = imagenCurso1;
        this.imagenCurso2 = imagenCurso2;
    }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public int getDuracionMin() { return duracionMin; }
    public void setDuracionMin(int duracionMin) { this.duracionMin = duracionMin; }

    public String getImagenCurso1() { return imagenCurso1; }
    public void setImagenCurso1(String imagenCurso1) { this.imagenCurso1 = imagenCurso1; }

    public String getImagenCurso2() { return imagenCurso2; }
    public void setImagenCurso2(String imagenCurso2) { this.imagenCurso2 = imagenCurso2; }
}
