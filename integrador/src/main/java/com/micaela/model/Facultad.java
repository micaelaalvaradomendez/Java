/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.micaela.model;

import com.micaela.Persistencia;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class Facultad {
    private static Facultad instance = null;
    private List<Carrera> listaCarreras;
    private List<Materia> listaMaterias;
    private List<Alumno> listaAlumnos;
    private List<PlanEstudio> listaplanestudios;
    
    private Facultad(){
        listaCarreras = Persistencia.carreras;;
        listaMaterias = Persistencia.materias;
        listaAlumnos = Persistencia.alumnos;
        listaplanestudios = Persistencia.planes;
    }
    
    /* METODOS INSDISPENSABLES */
    
    public void agregarCarrera(String nombre, String descripcion, String resolucion, PlanEstudio plan, int cuatrimestre,int optativas) {
       Carrera nuevaCarrera = new Carrera(nombre, descripcion, resolucion, cuatrimestre, optativas);
       nuevaCarrera.setPlan(plan);
       listaCarreras.add(nuevaCarrera);
    }
    
    public void agregarMateria(String nombreCarrera, String nombre, String programa, int cuatrimestre, boolean obligatoria) {
        Materia nuevaMateria = new Materia(nombre,programa,cuatrimestre,obligatoria);
        listaMaterias.add(nuevaMateria);
        Carrera carrera = buscarCarreraPorNombre(nombreCarrera);
        carrera.setMateria(nuevaMateria);
    }
    
    public void definirPlanEstudio(String nombreCarrera, PlanEstudio plan) {
        
        Carrera carrera = buscarCarreraPorNombre(nombreCarrera);
    
        if (carrera != null) {
            carrera.setPlan(plan);
            System.out.println("Plan de estudios asignado a la carrera: " + nombreCarrera);
        } else {
            System.out.println("Carrera no encontrada: " + nombreCarrera);
        }  
    }
    
    public int altaAlumno(String nombre, int dni, LocalDate fechaNac, boolean certificado) {        
        for (var alumno : listaAlumnos){
            if ( alumno.getDni()== dni){
                return -1;
            }
        }
        Alumno nuevoAlumno = new Alumno(nombre, dni, fechaNac, certificado);
        listaAlumnos.add(nuevoAlumno);
        return nuevoAlumno.getLegajo();
    }
    
    public void inscripcionAlumnoMateria(String nombreCarrera, int legajo, String nombreMateria) {
        Materia materia = buscarMateriaPorNombre(nombreMateria);
        Alumno alumno = getAlumno(legajo);
        DatoMateria dato = materia.getDatoMateria(alumno);
        
        if (dato==null) {
            Carrera carrera = buscarCarreraPorNombre(nombreCarrera);
            carrera.incribirAMateria(materia, alumno);
        } else {
            dato.inscribir();
        }
    }
    
    public void cargarNota(int legajo, String nombreMateria, int nota){
        Alumno alumno = getAlumno(legajo);
        Materia materia = buscarMateriaPorNombre(nombreMateria);
        DatoMateria dato = materia.getDatoMateria(alumno);
        if (dato!=null) {
            dato.setNota(nota);
            dato.aprobar();
            dato.desaprobar();
        }
    }
    
    public boolean isGraduado(int legajo) {
        
        Alumno alumno = getAlumno(legajo);
        Carrera carrera = obtenerCarrera(legajo);
        if (alumno.isGraduado()) return true;
        
        int optativasAprobadas = 0;
        var materias = carrera.getMaterias().values();
        var historia = alumno.getHistoriaAcademica();
        
        for (Materia materia : materias) {
            boolean encontrado = false;
            for (var dato: historia) {
                String nombreMateria = dato.getMateria().getNombre();
                if (nombreMateria.equals(materia.getNombre())) {
                    encontrado = true;
                    if (materia.isOptativa()) {
                        if (dato.getEstado() instanceof Aprobado)
                            optativasAprobadas++;
                    } else {
                        if (!(dato.getEstado() instanceof Aprobado))
                            return false;
                    }
                    break;
                }
            }
            if (!encontrado) return false;
        }
        
        if (optativasAprobadas>=carrera.getCantidadOptativas())
            alumno.setGraduado(true);
        
        return alumno.isGraduado();
    }
    
    /* METODOS EXTRA */
    
    public boolean eliminarMateria(String nombreCarrera, String nombreMateria){
        
        Carrera c = buscarCarreraPorNombre(nombreCarrera);
        Materia m = buscarMateriaPorNombre(nombreMateria);
        
        if (m==null || c==null) {
           return false;
        }
        
        var materias = c.getMaterias();
        materias.forEach((String clave, Materia materia) -> materia.getCorrelativas().remove(m));
        c.getMaterias().remove(nombreMateria);
        return true;
    }
    
    
    /* BUSCADORES DE ELEMENTOS */
    
    public Carrera buscarCarreraPorNombre(String nombreCarrera) {
        for (Carrera c : listaCarreras) {
            if (c.getNombre().equals(nombreCarrera)) {
                return c;
            }
        }
        return null;
    }
    
    public Materia buscarMateriaPorNombre(String nombreMateria) {
    
        for (Materia m : listaMaterias) {
            if (m.getNombre().equals(nombreMateria)) {
                return m;
            }
        }
        return null; 
    }
    
    public Alumno buscarAlumno(String tipo, String valor) {
        for (var alumno : listaAlumnos) {
            if (tipo.equalsIgnoreCase("nombre") && alumno.getNombre().equals(valor)) {
                return alumno;
            } else if (tipo.equalsIgnoreCase("dni") && String.valueOf(alumno.getDni()).equals(valor)) {
                return alumno;
            } else if (tipo.equalsIgnoreCase("legajo") && String.valueOf(alumno.getLegajo()).equals(valor)) {
                return alumno;
            }
        }
        return null;
    }
    
    public static Facultad getInstance(){
        if (instance==null)
            instance = new Facultad();
        return instance;
    }
    
    /* GETTERS Y SETTERS */
    
    public List<Carrera> getCarreras(){
        return listaCarreras;
    }
    
    public List<Alumno> getAlumnos() {
        return listaAlumnos;
    }
    
    public List<String> getAlumnos(String tipo){
        tipo = tipo.toLowerCase();
        var lista = new ArrayList<String>();
        
        if (tipo.equals("nombre")) {
            listaAlumnos.sort((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
            listaAlumnos.forEach(alumno -> lista.add(alumno.getNombre()));
        } else if (tipo.equals("dni")) {
            listaAlumnos.sort((a, b) -> a.getDni() - b.getDni());
            listaAlumnos.forEach(alumno -> lista.add(""+alumno.getDni()));
        } else if (tipo.equals("legajo")) {
            listaAlumnos.sort((a, b) -> a.getLegajo() - b.getLegajo());
            listaAlumnos.forEach(alumno -> lista.add(""+alumno.getLegajo()));
        }
        return lista;
    }
    
    public Alumno getAlumno(int legajo) {
        if (listaAlumnos == null || listaAlumnos.isEmpty()){
            System.out.println("El alumno no existe");
            return null;
        }
        for (Alumno a: listaAlumnos) {
            if(a.getLegajo()== legajo){
                return a;
            }
        }
        System.out.println("alumno no encontrado");
        return null;
    }
    
    public Carrera obtenerCarrera (int legajo){
        for (Carrera carrera : listaCarreras) {
            if (carrera.getAlumnos() != null && carrera.getAlumnos().containsKey(legajo)) {
                return carrera; 
            }
        }
        return null;
    }
    
    public List<Materia> getMaterias() {
        return listaMaterias;
    }
    
    public List<PlanEstudio> getPlanes(){
        return listaplanestudios;
    }
}
