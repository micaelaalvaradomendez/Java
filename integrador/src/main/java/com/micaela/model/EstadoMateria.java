package com.micaela.model;

public abstract class EstadoMateria {

    protected DatoMateria materia;

    public EstadoMateria(DatoMateria materia) {
        this.materia=materia;
    }

    public abstract void inscribir();
    public abstract void aprobar();
    public abstract void desaprobar();
    
    
}
