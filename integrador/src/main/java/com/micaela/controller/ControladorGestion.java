/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.micaela.controller;

import com.micaela.model.Materia;
import com.micaela.model.DatoMateria;
import com.micaela.model.PlanEstudio;
import com.micaela.model.Carrera;
import com.micaela.model.Alumno;
import com.micaela.Persistencia;
import com.micaela.Persistencia;
import com.micaela.Persistencia;
import com.micaela.Persistencia;
import com.micaela.model.Facultad;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Usuario
 */
public class ControladorGestion {
    private static ControladorGestion instance = null;
    private Facultad facultad;
    
    public ControladorGestion() {
        facultad = Facultad.getInstance();
    }

    public static ControladorGestion getInstance(){
        if (instance==null)
            instance = new ControladorGestion();
        return instance;
    }
    
    public Carrera getCarrera(String nombre) {
        var carreras = facultad.getCarreras();
        if(carreras == null || carreras.isEmpty()){
            System.out.println("No hay carreras en el sistema");
            return null;
        }
        for (Carrera c: carreras) {
            if(c.getNombre().equalsIgnoreCase(nombre)){
                return c;
            }
        }
        System.out.println("carrera no encontrada");
        return null;
    }
        
    public void agregarCorrelativa(String materia, String correlativa) {
 
        Materia materiaPrincipal = facultad.buscarMateriaPorNombre(materia);
    
        if (materiaPrincipal != null) {
            Materia materiaCorrelativa = facultad.buscarMateriaPorNombre(correlativa);
            if (materiaCorrelativa != null) {
                materiaPrincipal.setCorrelativa(materiaCorrelativa);
            } else {
                System.out.println("La correlativa " + correlativa + " no existe.");
            }
        } else {
            System.out.println("La materia " + materia + " no existe.");
        }
    }


    public void agregarMateriaACarrera(String nombreCarrera, Materia materia) {
        
        Carrera carrera = facultad.buscarCarreraPorNombre(nombreCarrera);
    
        if (carrera != null) {
            carrera.setMateria(materia);
            System.out.println("Materia " + materia.getNombre() + " agregada a la carrera: " + nombreCarrera);
        } else {
            System.out.println("Carrera no encontrada: " + nombreCarrera);
        }
    }
 
    public PlanEstudio convertirStringAPlan(String nombrePlan) {
        List<PlanEstudio> todosLosPlanes = getTodosLosPlanes();
        if (todosLosPlanes == null || todosLosPlanes.isEmpty()) {
            System.out.println("No hay planes de estudio en el sistema.");
            return null;
        }
        for (PlanEstudio plan : todosLosPlanes) {
            if (plan.toString().equals(nombrePlan)) {
                return plan;
            }
        }
        System.out.println("Plan de estudio no encontrado: " + nombrePlan);
        return null;
    }
    
    public List<PlanEstudio> getTodosLosPlanes() {
        List<PlanEstudio> listaDePlanes = null;
        listaDePlanes=Persistencia.planes;
        return listaDePlanes; 
    }
    
    public List<String> getPlanes() {
        List<String> planes = new ArrayList<>();
        for (PlanEstudio plan : facultad.getPlanes()) {
            planes.add((String) plan.toString());
        }
        return planes;
    }
}