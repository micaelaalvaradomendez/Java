package com.micaela.model;
public class Libre extends EstadoMateria {

    public Libre(DatoMateria materia) {
        super(materia);
    }

    @Override
    public void inscribir() {
        materia.setEstado(new EnCurso(materia));
    }

    @Override
    public void aprobar() {}

    @Override
    public void desaprobar() {}
    
    @Override
    public String toString(){
        return "Libre";
    }
}
