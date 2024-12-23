/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.micaela.controller;

import com.micaela.model.*;
import com.micaela.Persistencia;
import com.micaela.Persistencia;
import com.micaela.Persistencia;
import com.micaela.Persistencia;
import com.micaela.model.Libre;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Usuario
 */
public class ControladorInscripcion {
    private static ControladorInscripcion instance = null;
    private Facultad facultad;
    
    private ControladorInscripcion(){
        facultad = Facultad.getInstance();
    }
    
    public static ControladorInscripcion getInstance(){
        if (instance==null)
            instance = new ControladorInscripcion();
        return instance;
    }
    
    public String buscarCarrera(int legajo){
        return facultad.obtenerCarrera(legajo).toString();
    }
    
    public void inscripcionAlumnoCarrera(int legajo, String nombreCarrera) {
        Carrera carrera = facultad.buscarCarreraPorNombre(nombreCarrera);
        Alumno alumno = facultad.getAlumno(legajo);
        if (alumno != null) {
            carrera.setAlumno(alumno); 
        }
    }

    public List<String> obtenerMateriasParaCursar(int legajoAlumno, String nombreCarrera) {
        
        Carrera carrera = facultad.buscarCarreraPorNombre(nombreCarrera);
        Alumno alumno = carrera.getAlumno(legajoAlumno);
        var materias = carrera.getMaterias();
        var materiasPodercursar = new ArrayList<String>();
        
        if (legajoAlumno>0) {
            materias.forEach((clave, valor) -> {
                if (carrera.puedeCursar(valor, alumno)) 
                    materiasPodercursar.add(clave);
            });
        }
       return materiasPodercursar;
    }
    
    public String getTipoNota(int legajo, String nombreMateria){
        Materia materia = facultad.buscarMateriaPorNombre(nombreMateria);
        Alumno alumno = facultad.getAlumno(legajo);
        DatoMateria dato = materia.getDatoMateria(alumno);
        var estado = dato.getEstado();
        if (estado instanceof EnCurso) {
            return "nota Parcial";
        } else if (estado instanceof Regular) {
            return "nota Final";
        } else
            return null;
    }

    public List<DatoMateria> obtenerHistoriaAcademica(int legajo) {
        Alumno alumno = facultad.getAlumno(legajo);
        return alumno.getHistoriaAcademica();
    }
}
