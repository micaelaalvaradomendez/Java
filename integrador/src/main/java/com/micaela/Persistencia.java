package com.micaela;

import com.micaela.model.Alumno;
import com.micaela.model.Carrera;
import com.micaela.model.Materia;
import com.micaela.model.PlanEstudio;
import com.micaela.model.TipoA;
import com.micaela.model.TipoB;
import com.micaela.model.TipoC;
import com.micaela.model.TipoD;
import com.micaela.model.TipoE;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Persistencia {

    public static List<Alumno> alumnos = new ArrayList<>();
    public static List<Materia> materias = new ArrayList<>();
    public static List<Carrera> carreras = new ArrayList<>();
    public static List<PlanEstudio> planes = new ArrayList<>();

    static {
        alumnos.add(new Alumno("Micaela Lopez", 54123651, LocalDate.of(1995, 12, 25), true));
        alumnos.add(new Alumno("Juan Perez", 45321456, LocalDate.of(1998, 7, 15), true));
        alumnos.add(new Alumno("Luciana Martinez", 50213647, LocalDate.of(1996, 3, 10), false));
        alumnos.add(new Alumno("Carlos Gomez", 48123451, LocalDate.of(1994, 11, 20), true));
        alumnos.add(new Alumno("Fernanda Suarez", 52346589, LocalDate.of(1997, 5, 5), false));
        alumnos.add(new Alumno("Alejandro Gutierrez", 49567812, LocalDate.of(1999, 8, 30), true));
        alumnos.add(new Alumno("Sofia Ortega", 51839274, LocalDate.of(1995, 1, 12), true));
        alumnos.add(new Alumno("Diego Fernandez", 50786345, LocalDate.of(1996, 4, 18), false));
        alumnos.add(new Alumno("Paula Ramirez", 49213658, LocalDate.of(1998, 6, 22), true));
        alumnos.add(new Alumno("Martin Castillo", 51123467, LocalDate.of(1997, 9, 9), false));
        alumnos.add(new Alumno("Laura Diaz", 54129374, LocalDate.of(1995, 10, 25), true));
        alumnos.add(new Alumno("Federico Morales", 48923451, LocalDate.of(1996, 2, 15), true));
        alumnos.add(new Alumno("Camila Benitez", 51239456, LocalDate.of(1997, 12, 12), false));
        alumnos.add(new Alumno("Javier Alvarez", 52139457, LocalDate.of(1999, 7, 28), true));
        alumnos.add(new Alumno("Valentina Rios", 50123489, LocalDate.of(1998, 3, 5), true));
        alumnos.add(new Alumno("Gabriela Paredes", 54123456, LocalDate.of(2000, 2, 11), true));
        alumnos.add(new Alumno("Marcos Ayala", 53124567, LocalDate.of(1999, 6, 19), true));
        alumnos.add(new Alumno("Daniela Herrera", 51123567, LocalDate.of(2001, 10, 8), false));
        alumnos.add(new Alumno("Rodrigo López", 52123568, LocalDate.of(1998, 3, 14), true));
        alumnos.add(new Alumno("Elena Ruiz", 49123457, LocalDate.of(2000, 7, 21), false));
        alumnos.add(new Alumno("Cristian Navarro", 50123478, LocalDate.of(1999, 11, 2), true));
        alumnos.add(new Alumno("Luisa Torres", 52123489, LocalDate.of(2000, 5, 5), true));

    }
    
    static{
        planes.add(new TipoA(null));
        planes.add(new TipoB(null));
        planes.add(new TipoC(null));
        planes.add(new TipoD(null));
        planes.add(new TipoE(null));
        
        
        
    }


    static {
        Materia auxiliar;
        //materias para Licenciatura en Administración

        materias.add(new Materia("Fundamentos de Administración", "admin_programa1",1,false));//0
        materias.add(new Materia("administracion","admin_programa2",1,false));//1
        auxiliar=new Materia("Planeación Estratégica", "admin_programa3.pdf", 2, false);
        auxiliar.setCorrelativa(materias.get(0));
        materias.add(auxiliar);//2
        auxiliar =new Materia("Marketing Básico", "admin_programa4.pdf", 2, false);
        auxiliar.setCorrelativa(materias.get(0));
        auxiliar.setCorrelativa(materias.get(1));
        materias.add(auxiliar);//3
        auxiliar = new Materia("Presupuestos Empresariales", "admin_programa5.pdf", 3, false);
        auxiliar.setCorrelativa(materias.get(2));
        auxiliar.setCorrelativa(materias.get(3));
        materias.add(auxiliar);//4
        auxiliar = new Materia("Economía para Negocios", "admin_programa6.pdf", 3, false);
        auxiliar.setCorrelativa(materias.get(3));
        materias.add(auxiliar);//5
        auxiliar = new Materia("Finanzas Corporativas", "admin_programa7.pdf", 4, false);
        auxiliar.setCorrelativa(materias.get(4));
        auxiliar.setCorrelativa(materias.get(5));
        materias.add(auxiliar);//6
        auxiliar = new Materia("Gestión de Proyectos", "admin_programa8.pdf", 4, false);
        auxiliar.setCorrelativa(materias.get(5));
        materias.add(auxiliar);//7
        materias.add(new Materia("Comportamiento Organizacional", "admin_programa9.pdf", 4, true));//8
        auxiliar = new Materia("Emprendimiento e Innovación", "admin_programa10.pdf", 4, true);
        auxiliar.setCorrelativa(materias.get(6));
        auxiliar.setCorrelativa(materias.get(7));
        materias.add(auxiliar);//9

        //materias para Ingeniería Civil
        materias.add(new Materia("Mecánica de Materiales", "civil_programa1.pdf", 1, false));//10
        materias.add(new Materia("Dibujo Técnico", "civil_programa2.pdf", 1, false));//11
        auxiliar = new Materia("Estática", "civil_programa3.pdf", 2, false);
        auxiliar.setCorrelativa(materias.get(10));
        materias.add(auxiliar);//12
        auxiliar = new Materia("Topografía", "civil_programa4.pdf", 2, false);
        auxiliar.setCorrelativa(materias.get(10));
        auxiliar.setCorrelativa(materias.get(11));
        materias.add(auxiliar);//13
        auxiliar = new Materia("Dinámica", "civil_programa5.pdf", 3, false);
        auxiliar.setCorrelativa(materias.get(12));
        materias.add(auxiliar);//14
        auxiliar = new Materia("Materiales de Construcción", "civil_programa6.pdf", 3, false);
        auxiliar.setCorrelativa(materias.get(13));
        auxiliar.setCorrelativa(materias.get(12));
        materias.add(auxiliar);//15
        auxiliar = new Materia("Hidráulica Básica", "civil_programa7.pdf", 4, false);
        auxiliar.setCorrelativa(materias.get(15));
        materias.add(auxiliar);//16
        auxiliar = new Materia("Ingeniería de Transporte", "civil_programa8.pdf", 4, false);
        auxiliar.setCorrelativa(materias.get(15));
        materias.add(auxiliar);//17
        auxiliar = new Materia("Diseño de Estructuras", "civil_programa9.pdf", 4, true);
        auxiliar.setCorrelativa(materias.get(16));
        auxiliar.setCorrelativa(materias.get(17));
        materias.add(auxiliar);//18
        auxiliar = new Materia("Gestión de Obras", "civil_programa10.pdf", 4, true);
        auxiliar.setCorrelativa(materias.get(12));
        materias.add(auxiliar);//19


        //ingeniera en computacion
        materias.add(new Materia("Algebra","programa.pdf",1,false));//20
        materias.add(new Materia("Cálculo Diferencial", "programa.pdf", 1, false));//21
        materias.add(new Materia("Programación Básica", "programa.pdf", 1, false));//22
        materias.add(new Materia("Química General", "programa.pdf", 1, false));//23
        materias.add(new Materia("Introducción a la Ingeniería", "programa.pdf", 1, false));//24
        auxiliar = new Materia("Álgebra Lineal", "programa.pdf", 2, false);
        auxiliar.setCorrelativa(materias.get(20));
        auxiliar.setCorrelativa(materias.get(21));
        materias.add(auxiliar);//25
        auxiliar = new Materia("Cálculo Integral", "programa.pdf", 2, false);
        auxiliar.setCorrelativa(materias.get(22));
        materias.add(auxiliar);//26
        auxiliar = new Materia("Estructura de Datos", "programa.pdf", 2, false);
        auxiliar.setCorrelativa(materias.get(23));
        auxiliar.setCorrelativa(materias.get(24));
        materias.add(auxiliar);//27
        auxiliar = new Materia("Física Básica", "programa.pdf", 2, false);
        auxiliar.setCorrelativa(materias.get(20));
        materias.add(auxiliar);//28
        auxiliar = new Materia("Ética Profesional", "programa.pdf", 2, true);
        auxiliar.setCorrelativa(materias.get(22));
        materias.add(auxiliar);//29
        auxiliar = new Materia("Probabilidad y Estadística", "programa.pdf", 3, false);
        auxiliar.setCorrelativa(materias.get(25));
        materias.add(auxiliar);//30
        auxiliar = new Materia("Cálculo Vectorial", "programa.pdf", 3, false);
        auxiliar.setCorrelativa(materias.get(26));
        materias.add(auxiliar);//31
        auxiliar = new Materia("Bases de Datos", "programa.pdf", 3, false);
        auxiliar.setCorrelativa(materias.get(27));
        materias.add(auxiliar);//32
        auxiliar = new Materia("Electricidad y Magnetismo", "programa.pdf", 3, false);
        auxiliar.setCorrelativa(materias.get(28));
        materias.add(auxiliar);//33
        auxiliar = new Materia("Desarrollo Humano", "programa.pdf", 3, true);
        auxiliar.setCorrelativa(materias.get(29));
        materias.add(auxiliar);//34
        auxiliar = new Materia("Métodos Numéricos", "programa.pdf", 4, false);
        auxiliar.setCorrelativa(materias.get(30));
        materias.add(auxiliar);//35
        auxiliar = new Materia("Análisis de Algoritmos", "programa.pdf", 4, false);
        auxiliar.setCorrelativa(materias.get(31));
        materias.add(auxiliar);//36
        auxiliar = new Materia("Sistemas Operativos", "programa.pdf", 4, true);
        auxiliar.setCorrelativa(materias.get(32));
        materias.add(auxiliar);//37
        auxiliar = new Materia("Electrónica Básica", "programa.pdf", 4, false);
        auxiliar.setCorrelativa(materias.get(33));
        materias.add(auxiliar);//38
        auxiliar = new Materia("Taller de Liderazgo", "programa.pdf", 4, true);
        auxiliar.setCorrelativa(materias.get(34));
        materias.add(auxiliar);//39
        auxiliar = new Materia("Taller de Enseñanza", "programa.pdf", 5, false);
        auxiliar.setCorrelativa(materias.get(39));
        materias.add(auxiliar);//40
        auxiliar = new Materia("Tesis", "programa.pdf", 6, false);
        auxiliar.setCorrelativa(materias.get(40));
        materias.add(auxiliar);//41
        auxiliar = new Materia("Tesis aplicada", "programa.pdf", 7, true);
        auxiliar.setCorrelativa(materias.get(41));
        materias.add(auxiliar);//42
    }

    static {
        Carrera administracion = new Carrera(
                "Licenciatura en Administración",
                "Gestión de recursos y liderazgo empresarial",
                "resolucion_admin.pdf",
                4,1
        );

        administracion.setPlan(new TipoA(administracion));
        administracion.setMateria(materias.get(0)); // Fundamentos de Administración
        administracion.setMateria(materias.get(1)); // Contabilidad Básica
        administracion.setMateria(materias.get(2)); // Planeación Estratégica
        administracion.setMateria(materias.get(3)); // Marketing Básico
        administracion.setMateria(materias.get(4)); // Presupuestos Empresariales
        administracion.setMateria(materias.get(5)); // Economía para Negocios
        administracion.setMateria(materias.get(6)); // Finanzas Corporativas
        administracion.setMateria(materias.get(7)); // Gestión de Proyectos
        administracion.setMateria(materias.get(8)); // Comportamiento Organizacional
        administracion.setMateria(materias.get(9)); // Emprendimiento e Innovación
        carreras.add(administracion);

        // nueva carrera: Ingeniería Civil
        Carrera ingenieriaCivil = new Carrera(
                "Ingeniería Civil",
                "Diseño y construcción de infraestructura",
                "resolucion_civil.pdf",
                4,1
        );
        ingenieriaCivil.setPlan(new TipoB(ingenieriaCivil));
        ingenieriaCivil.setMateria(materias.get(10)); // Mecánica de Materiales
        ingenieriaCivil.setMateria(materias.get(11)); // Dibujo Técnico
        ingenieriaCivil.setMateria(materias.get(12)); // Estática
        ingenieriaCivil.setMateria(materias.get(13)); // Topografía
        ingenieriaCivil.setMateria(materias.get(14)); // Dinámica
        ingenieriaCivil.setMateria(materias.get(15)); // Materiales de Construcción
        ingenieriaCivil.setMateria(materias.get(16)); // Hidráulica Básica
        ingenieriaCivil.setMateria(materias.get(17)); // Ingeniería de Transporte
        ingenieriaCivil.setMateria(materias.get(18)); // Diseño de Estructuras
        ingenieriaCivil.setMateria(materias.get(19)); // Gestión de Obras
        carreras.add(ingenieriaCivil);

        Carrera ingenieriaComputacion = new Carrera(
                "Ingeniería en Computación",
                "Desarrollo de sistemas y tecnologías computacionales",
                "resolucion_computacion.pdf",
                4,2
        );

        // Asignar materias a Ingeniería en Computación
        ingenieriaComputacion.setPlan(new TipoC(ingenieriaComputacion));
        ingenieriaComputacion.setMateria(materias.get(20)); // Algebra
        ingenieriaComputacion.setMateria(materias.get(21)); // Cálculo Diferencial
        ingenieriaComputacion.setMateria(materias.get(22)); // Programación Básica
        ingenieriaComputacion.setMateria(materias.get(23)); // Química General
        ingenieriaComputacion.setMateria(materias.get(24)); // Introducción a la Ingeniería
        ingenieriaComputacion.setMateria(materias.get(25)); // Álgebra Lineal
        ingenieriaComputacion.setMateria(materias.get(26)); // Cálculo Integral
        ingenieriaComputacion.setMateria(materias.get(27)); // Estructura de Datos
        ingenieriaComputacion.setMateria(materias.get(28)); // Física Básica
        ingenieriaComputacion.setMateria(materias.get(29)); // Ética Profesional
        ingenieriaComputacion.setMateria(materias.get(30)); // Probabilidad y Estadística
        ingenieriaComputacion.setMateria(materias.get(31)); // Cálculo Vectorial
        ingenieriaComputacion.setMateria(materias.get(32)); // Bases de Datos
        ingenieriaComputacion.setMateria(materias.get(33)); // Electricidad y Magnetismo
        ingenieriaComputacion.setMateria(materias.get(34)); // Desarrollo Humano
        ingenieriaComputacion.setMateria(materias.get(35)); // Métodos Numéricos
        ingenieriaComputacion.setMateria(materias.get(36)); // Análisis de Algoritmos
        ingenieriaComputacion.setMateria(materias.get(37)); // Sistemas Operativos
        carreras.add(ingenieriaComputacion);
        


        // Alumnos a Administracion
        administracion.setAlumno(alumnos.get(0)); // Micaela Lopez
        administracion.setAlumno(alumnos.get(2)); // Luciana Martinez
        administracion.setAlumno(alumnos.get(4)); // Fernanda Suarez
        administracion.setAlumno(alumnos.get(6)); // Sofia Ortega
        administracion.setAlumno(alumnos.get(8)); // Paula Ramirez
        administracion.setAlumno(alumnos.get(10)); // Laura Diaz
        administracion.setAlumno(alumnos.get(12)); // Camila Benitez
        administracion.setAlumno(alumnos.get(14)); // Valentina Rios

        // Asignar alumnos a Ingeniería Civil
        ingenieriaCivil.setAlumno(alumnos.get(1)); // Juan Perez
        ingenieriaCivil.setAlumno(alumnos.get(3)); // Carlos Gomez
        ingenieriaCivil.setAlumno(alumnos.get(5)); // Alejandro Gutierrez
        ingenieriaCivil.setAlumno(alumnos.get(7)); // Diego Fernandez
        ingenieriaCivil.setAlumno(alumnos.get(9)); // Martin Castillo
        ingenieriaCivil.setAlumno(alumnos.get(11)); // Federico Morales
        ingenieriaCivil.setAlumno(alumnos.get(13)); // Javier Alvarez

        // Asignar los alumnos a la carrera Ingeniería en Computación
        ingenieriaComputacion.setAlumno(alumnos.get(15));
        ingenieriaComputacion.setAlumno(alumnos.get(16));
        ingenieriaComputacion.setAlumno(alumnos.get(17));
        ingenieriaComputacion.setAlumno(alumnos.get(18));
        ingenieriaComputacion.setAlumno(alumnos.get(19));
        ingenieriaComputacion.setAlumno(alumnos.get(20));
        ingenieriaComputacion.setAlumno(alumnos.get(21));


        administracion.incribirAMateria(materias.get(0), alumnos.get(0));
        administracion.incribirAMateria(materias.get(1), alumnos.get(0));
        var mate = materias.get(0);
        mate.getDatoMateria(alumnos.get(0)).setNota(6);
        mate.getDatoMateria(alumnos.get(0)).aprobar();
        mate.getDatoMateria(alumnos.get(0)).desaprobar();
        mate = materias.get(1);
        mate.getDatoMateria(alumnos.get(0)).setNota(8);
        mate.getDatoMateria(alumnos.get(0)).aprobar();
        mate.getDatoMateria(alumnos.get(0)).desaprobar();
        administracion.incribirAMateria(materias.get(2), alumnos.get(0));

        Alumno alumno = alumnos.get(3); // Seleccionar al alumno correspondiente

        // Cuatrimestre 1
        ingenieriaComputacion.incribirAMateria(materias.get(20), alumno); // Álgebra
        ingenieriaComputacion.incribirAMateria(materias.get(21), alumno); // Cálculo Diferencial
        ingenieriaComputacion.incribirAMateria(materias.get(22), alumno); // Programación Básica
        ingenieriaComputacion.incribirAMateria(materias.get(23), alumno); // Química General
        ingenieriaComputacion.incribirAMateria(materias.get(24), alumno); // Introducción a la Ingeniería

        // Aprobar todas las materias del cuatrimestre 1
        for (int i = 20; i <= 24; i++) {
            Materia materia = materias.get(i);
            materia.getDatoMateria(alumno).setNota(8);
            materia.getDatoMateria(alumno).aprobar(); // Calificación aprobatoria
        }

    }



}

