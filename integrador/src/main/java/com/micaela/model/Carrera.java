package com.micaela.model;

import com.micaela.model.Alumno;
import java.util.HashMap;
import java.util.Map;

public class Carrera {
    private String nombre, descripcion, resolucion;
    private int cuatrimestres, cantidadOptativas;
    private PlanEstudio plan;
    private Map<String, Materia> materias;
    private Map<Integer, Alumno> alumnos;

    public Carrera(String nombre, String descripcion, String resolucion, int cuatrimestres , int optativas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.resolucion = resolucion;
        this.cuatrimestres = cuatrimestres;
        this.cantidadOptativas = optativas;
        materias = new HashMap<>();
        alumnos = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public int getCuatrimestres() {
        return cuatrimestres;
    }

    public void setCuatrimestres(int cuatrimestres) {
        this.cuatrimestres = cuatrimestres;
    }

    public int getCantidadOptativas() {
        return cantidadOptativas;
    }

    public void setCantidadOptativas(int cantidadOptativas) {
        this.cantidadOptativas = cantidadOptativas;
    }

    public void setAlumno(Alumno alumno) {
        alumnos.put(alumno.getLegajo(),alumno);
    }

    public Alumno getAlumno(int legajo){
        return  alumnos.get(legajo);
    }

    public void setMateria(Materia materia) {
        materias.put(materia.getNombre(),materia);
    }

    public Materia getMateria(String materia){
        return  materias.get(materia);
    }

    public Map<String,Materia> getMaterias(){
        return materias;
    }

    public Map<Integer,Alumno> getAlumnos() {
        return alumnos;
    }

    public PlanEstudio getPlan() {
        return plan;
    }

    public void setPlan(PlanEstudio plan) {
        this.plan = plan;
    }

    public void incribirAMateria(Materia materia, Alumno alumno) {
        materia.setDatoMateria(alumno);
        var datos = materia.getDatoMateria(alumno);
        boolean puede = plan.inscribir(datos);
        if(!puede){
            materia.popDatoMateria();
        } else {
            alumno.setHistoriaAcademica(datos);
        }
    }
    
    public boolean puedeCursar(Materia materia, Alumno alumno){
        DatoMateria dato = materia.getDatoMateria(alumno);
        if (dato==null) {
            materia.setDatoMateria(alumno);
            var datos = materia.getDatoMateria(alumno);
            boolean puedeCursar = plan.inscribir(datos);
            materia.popDatoMateria();
            return puedeCursar;
        }
        return (dato.getEstado() instanceof Libre);
    }


    @Override
    public String toString() {
        return "Carrera{" +
                "cuatrimestres=" + cuatrimestres +
                ", resolucion='" + resolucion + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
