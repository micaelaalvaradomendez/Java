package com.micaela.model;
import java.util.ArrayList;
import java.util.List;

public class TipoD extends PlanEstudio{
    public TipoD(Carrera carrera) {
        super(carrera);
    }

    @Override
    public boolean inscribir(DatoMateria cursada) {

        for (var materia : cursada.getMateria().getCorrelativas()) {
            var detalle = materia.getDatoMateria(cursada.getAlumno());
            if (detalle == null || detalle.getEstado() instanceof Libre || detalle.getEstado() instanceof EnCurso)
                return false;
        }

        var cuatriActual = cursada.getMateria().getCuatrimestre();

        if (cuatriActual>3) {
            // Filtrar materias de 3 cuatrimestres previos
            List<Materia> materiasPrevias = new ArrayList<>();
            carrera.getMaterias().forEach( (clave, materia) ->  {
                var cuatriMateria = materia.getCuatrimestre();
                var diferencia = cuatriActual - cuatriMateria;
                if (diferencia>=1 && diferencia<=3){
                    materiasPrevias.add(materia);
                }
            });
            for (var materia: materiasPrevias) {
                var detalle = materia.getDatoMateria(cursada.getAlumno());
                if (detalle == null || !(detalle.getEstado() instanceof Aprobado))
                    return false;
            }
        }

        return true;
    }
    
    @Override
    public String toString(){
        return "TipoD";
    }
}

