package com.micaela.model;
public class Regular extends EstadoMateria{

    public Regular(DatoMateria materia) {
        super(materia);
    }

    @Override
    public void inscribir() {}

    @Override
    public void aprobar() {
        if (materia.getNota()>=6){
            materia.setEstado(new Aprobado(materia));
        }
    }

    @Override
    public void desaprobar() {}
    
    @Override
    public String toString(){
        return "Regular";
    }
}