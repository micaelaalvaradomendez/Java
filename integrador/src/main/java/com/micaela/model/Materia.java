package com.micaela.model;

import com.micaela.model.Alumno;
import java.util.ArrayList;
import java.util.List;

public class Materia {
    private String nombre, programa;
    private int cuatrimestre;
    private List<Materia> correlativas;
    private boolean optativa;
    private List <DatoMateria> curso;

    public Materia(String nombre, String programa, int cuatrimestre, boolean optativa) {
        this.nombre = nombre;
        this.programa = programa;
        this.cuatrimestre = cuatrimestre;
        this.optativa = optativa;
        correlativas = new ArrayList<>();
        curso = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public List<Materia> getCorrelativas() {
        return correlativas;
    }

    public void setCorrelativa(Materia correlativa) {
        correlativas.add(correlativa);
    }

    public boolean isOptativa() {
        return optativa;
    }

    public void setOptativa(boolean optativa) {
        this.optativa = optativa;
    }

    public void setDatoMateria(Alumno alumno){
        DatoMateria dato = new DatoMateria(this,alumno);  
        curso.add(dato);
        dato.inscribir();
    }

    public List<DatoMateria> getCurso() {
        return curso;
    }

    public DatoMateria getDatoMateria(Alumno alumno){
        for ( var dato:curso ) {
            if(dato.getAlumno()==alumno) return dato;
        }
        return null;
    }

    public void popDatoMateria(){
        curso.removeLast();
    }
    
    public boolean isCorrelativa(Materia materia){
        return correlativas.contains(materia);
    }

    @Override
    public String toString() {
        return "Materia{" +
                "nombre='" + nombre + '\'' +
                ", programa='" + programa + '\'' +
                ", cuatrimestre=" + cuatrimestre +
                ", optativa=" + optativa +
                '}';
    }
}
