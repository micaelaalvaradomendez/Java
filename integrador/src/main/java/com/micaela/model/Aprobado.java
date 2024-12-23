package com.micaela.model;

public class Aprobado extends EstadoMateria{

    public Aprobado(DatoMateria materia) {
        super(materia);
    }

    @Override
    public void inscribir() {}

    @Override
    public void aprobar() {}

    @Override
    public void desaprobar() {}
    
    @Override
    public String toString(){
        return "Aprobado";
    }
}
