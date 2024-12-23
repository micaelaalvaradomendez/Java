package com.micaela.model;

import com.micaela.model.Alumno;

public class DatoMateria {
    private Materia materia;
    private EstadoMateria estado;
    private Alumno alumno;
    private int nota;


    public DatoMateria(Materia materia, Alumno alumno) {
        this.materia = materia;
        this.alumno = alumno;
        estado = new EnCurso(this);
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }


    public void setNota(int nota){
        this.nota = nota;
    }
    
    public int getNota() {
        return nota;
    }

    public EstadoMateria getEstado() {
        return estado;
    }

    public void setEstado(EstadoMateria estado) {
        this.estado = estado;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public void inscribir(){
        estado.inscribir();
    }
    
    public void aprobar(){
        estado.aprobar();
    }
    
    public void desaprobar(){
        estado.desaprobar();
    }
    
    public String getNombreEstado(){
        return estado.toString();
    }

    @Override
    public String toString() {
        return "DatoMateria{" +
                "estado=" + estado +
                ", alumno=" + alumno +
                '}';
    }
}
