package com.micaela.model;
public class TipoB extends PlanEstudio {

    public TipoB(Carrera carrera) {
            super(carrera);
    }

    @Override
    public boolean inscribir(DatoMateria cursada) {
        for (var materia: cursada.getMateria().getCorrelativas()) {
            var detalle = materia.getDatoMateria(cursada.getAlumno());
            if (detalle == null || detalle.getEstado() instanceof Libre || detalle.getEstado() instanceof EnCurso || detalle.getEstado() instanceof Regular )
                return false;
        }
        return true;
    }

    @Override
    public String toString(){
        return "TipoB";
    }
}
