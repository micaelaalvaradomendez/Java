package com.micaela;

import com.micaela.controller.ControladorGestion;
import com.micaela.controller.ControladorInscripcion;
import com.micaela.view.*;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        var controlador = ControladorGestion.getInstance();
        var otroControlador = ControladorInscripcion.getInstance();
        var interfaz = Principal.getInstance(true);
    }
}