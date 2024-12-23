package com.micaela.model;
public class EnCurso extends EstadoMateria{
    public EnCurso(DatoMateria materia) {
        super(materia);
    }

    @Override
    public void inscribir() {}

    @Override
    public void aprobar() {
        int nota = materia.getNota();
        if (nota<6) {
            return;
        }
        else if (nota<8){
            materia.setEstado(new Regular(materia));
        }
        else {
            materia.setEstado(new Aprobado(materia));
        }
    }

    @Override
    public void desaprobar() {
        if (materia.getNota()<6)
            materia.setEstado(new Libre(materia));
    }

    @Override
    public String toString(){
        return "Cursando";
    }
}

