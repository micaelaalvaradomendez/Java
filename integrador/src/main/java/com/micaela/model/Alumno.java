package com.micaela.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Alumno {
    private static int legajoGenerador = 0;
    private String nombre;
    private int dni, legajo;
    private LocalDate fechaNacimiento;
    private boolean certificado;
    private boolean graduado;
    private List<DatoMateria> historiaAcademica;

    public Alumno(String nombre, int dni, LocalDate fechaNacimiento, boolean certificado) {
        this.nombre = nombre;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.certificado = certificado;
        this.legajo = ++legajoGenerador;
        graduado = false;
        historiaAcademica = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isCertificado() {
        return certificado;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
    }

    public boolean isGraduado() {
        return graduado;
    }
    
    public void setGraduado(boolean graduado) {
        this.graduado = graduado;    
    }

    public int getLegajo() {
        return legajo;
    }

    public List<DatoMateria> getHistoriaAcademica() {
        return historiaAcademica;
    }


    public void setHistoriaAcademica(DatoMateria materia) {
        historiaAcademica.add(materia);
    }



    @Override
    public String toString() {
        return "Alumno{" +
                "nombre='" + nombre + '\'' +
                ", dni=" + dni +
                ", legajo=" + legajo +
                ", fechaNacimiento=" + fechaNacimiento +
                ", certificado=" + certificado +
                ", graduado=" + graduado +
                '}';
    }

    

}
