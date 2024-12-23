package com.micaela.model;

public abstract class PlanEstudio {
    protected Carrera carrera;
    

    public PlanEstudio(Carrera carrera){
        this.carrera = carrera;
    }
    // metodos a cada tipo de plan
    public abstract boolean inscribir(DatoMateria materia);

    

}


