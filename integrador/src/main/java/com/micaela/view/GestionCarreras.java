/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.micaela.view;

import com.micaela.controller.ControladorGestion;
import com.micaela.model.Materia;
import com.micaela.model.PlanEstudio;
import com.micaela.model.Carrera;
import com.micaela.model.Facultad;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class GestionCarreras extends javax.swing.JFrame {
    
    private Facultad facultad;
    
    private ControladorGestion controlador;

    private static GestionCarreras instance = null;
    
    private GestionCarreras() {
        initComponents();
        configuracion();
        facultad = Facultad.getInstance();
        controlador = ControladorGestion.getInstance();
        
        /* Barra de Navegación */
        
        inicio.addActionListener(e -> {
            Principal.getInstance(true);
            this.dispose();
        });
        alumnos.addActionListener(e -> {
            GestionAlumnos.getInstance(true);
            this.dispose();
        });
        
        /* Panel de Gestion de Carreras */
        
        // Elegir - Carrera
        
        carreras.addActionListener(e -> actualizarCarrera());
        areaPlanEstudio.setEditable(false);
        cargarCarreras();
        actualizarCarrera();
        
        // Cargar - Carrera
        
        cargarPlanesCarrera();
        cantidadDeOptativas.removeAllItems();
        cantidadDeOptativas.addItem("0");
        cantidadDeOptativas.addItem("1");
        cantidadDeOptativas.addItem("2");
        agregarCarrera.addActionListener(e -> agregarCarrera());
        
        /* Panel de Gestion de Materias */
        
        agregarMateria.addActionListener(e -> agregarMateria());
        cambiarPlan.addActionListener(e -> cambiarPlan());
        materiaSeleccionada.addActionListener(e -> actualizarCorrelativas());
        agregarCorrelativa.addActionListener(e -> agregarCorrelativa());
        actualizarCorrelativas();
        
        /* Panel Central */
        
        // Eliminar Materia
        
        eliminarMateria.addActionListener(e -> eliminarMateria());
    }
    
    public static GestionCarreras getInstance(boolean mostrar){
        if (instance==null)
            instance = new GestionCarreras();
        instance.setVisible(mostrar);
        return instance;
    }
    
    private void actualizarCorrelativas(){
        correlativaSeleccionada.removeAllItems();
        correlativaSeleccionada.addItem("Ninguna");
        String nombreMateria = (String) materiaSeleccionada.getSelectedItem();
        String nombreCarrera = (String) carreras.getSelectedItem();
        if (nombreMateria==null) return;
        Materia m = facultad.buscarMateriaPorNombre(nombreMateria);
        Carrera c = facultad.buscarCarreraPorNombre(nombreCarrera);
        int cuatriMate = m.getCuatrimestre();
        var materias = c.getMaterias();
        materias.forEach((clave, materia) -> {
            if (materia.getCuatrimestre() < cuatriMate) correlativaSeleccionada.addItem(materia.getNombre());
        });
    }
    
    private void agregarCorrelativa(){
        String nombreMateria = (String) materiaSeleccionada.getSelectedItem();
        String nombreCorrelativa = (String) correlativaSeleccionada.getSelectedItem();
        if (nombreCorrelativa.toLowerCase().equals("ninguna")) return;
        Materia m = facultad.buscarMateriaPorNombre(nombreMateria);
        Materia mc = facultad.buscarMateriaPorNombre(nombreCorrelativa);
        m.setCorrelativa(mc);
    }
    
    private void eliminarMateria(){
        String nombreCarrera = (String) carreras.getSelectedItem();
        String nombreMateria = (String) materiaAborrar.getSelectedItem();
        if (nombreMateria!=null && nombreCarrera!=null){
            boolean exito = facultad.eliminarMateria(nombreCarrera, nombreMateria);
            if (exito) actualizarCarrera();
        }
    }
    
    private void configuracion(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    private void cargarCarreras() {
        carreras.removeAllItems();
        for (Carrera carrera : facultad.getCarreras()) {
            carreras.addItem(carrera.getNombre());
        }
    }
    
    private void actualizarCarreras(String seleccion){
        cargarCarreras();
        carreras.setSelectedItem(seleccion); // Seleccionar la nueva carrera
    }
    
    private void actualizarCarrera(){
        String carreraSeleccionada = (String) carreras.getSelectedItem();
        if (carreraSeleccionada != null) {
            Carrera carrera = facultad.buscarCarreraPorNombre(carreraSeleccionada);
            if (carrera != null) {
                areaPlanEstudio.setText(carrera.getPlan().toString());
                cargarMaterias(carrera);
            }
        }
    }
    
    private void agregarCarrera() {
        String nombreCarrera = carreraNombre.getText();
        String resolucion = carreraResolucion.getText();
        String descripcion = carreraDescripcion.getText();
        String nombrePlan = (String) planesEstudio.getSelectedItem();
        int optativas = Integer.parseInt((String)cantidadDeOptativas.getSelectedItem());

        if (!nombreCarrera.isEmpty() && !resolucion.isEmpty() && !descripcion.isEmpty() && nombrePlan != null) {
            PlanEstudio planSeleccionado = controlador.convertirStringAPlan(nombrePlan);
            if (planSeleccionado != null) {
                facultad.agregarCarrera(nombreCarrera, descripcion, resolucion, planSeleccionado, 0, optativas);
                actualizarCarreras(nombreCarrera);
                cargarCarreras();
            }
        }
    }
    
    private void cargarMaterias(Carrera carrera) {
        DefaultTableModel model = (DefaultTableModel) tablaMaterias.getModel();
        model.setRowCount(0); // Limpiar la tabla
        materiaAborrar.removeAllItems(); // Limpiar la vista
        materiaSeleccionada.removeAllItems();
        correlativaSeleccionada.removeAllItems();
        for (Materia materia : carrera.getMaterias().values()) {
            model.addRow(new Object[]{materia.getNombre(), materia.getCuatrimestre(), materia.isOptativa()});
            materiaAborrar.addItem(materia.getNombre());
            materiaSeleccionada.addItem(materia.getNombre());
        }
        correlativaSeleccionada.addItem("Ninguna");
    }
    
    public void agregarMateria() {
        String carreraSeleccionada = (String) carreras.getSelectedItem();
        if (carreraSeleccionada != null) {
            String nombreMateria = materiaNombre.getText();
            String programa = materiaPrograma.getText();
            int cuatrimestre = Integer.parseInt(materiaCuatrimestre.getText());
            boolean optativa = isOptativa.isSelected();

            facultad.agregarMateria(carreraSeleccionada, nombreMateria, programa, cuatrimestre, optativa);
            actualizarCarrera();
        }
    }
    
    private void cargarPlanesCarrera() {
        planesEstudio.removeAllItems();
        planesCarrera.removeAllItems();
        var planes = controlador.getPlanes(); // Asegúrate de que getPlanes() devuelva los datos correctos
        for (String plan : planes) {
            planesEstudio.addItem(plan.toString());
            planesCarrera.addItem(plan.toString());
        }
        planesEstudio.revalidate();
        planesEstudio.repaint();
        planesCarrera.revalidate();
        planesCarrera.repaint();
    }
    
    private void cambiarPlan(){
        String carreraSeleccionada = (String) carreras.getSelectedItem();
        if (carreraSeleccionada != null) {
            String planSeleccionado = (String) planesCarrera.getSelectedItem();
            if (planSeleccionado != null) {
                PlanEstudio nuevoPlan = controlador.convertirStringAPlan(planSeleccionado);
                if (nuevoPlan != null) {
                    facultad.definirPlanEstudio(carreraSeleccionada, nuevoPlan);
                    actualizarCarrera();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new javax.swing.JPanel();
        nav = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        inicio = new javax.swing.JButton();
        alumnos = new javax.swing.JButton();
        body = new javax.swing.JPanel();
        panelCarreras = new javax.swing.JPanel();
        subtitulo1 = new javax.swing.JLabel();
        carreras = new javax.swing.JComboBox<>();
        scrollPlan = new javax.swing.JScrollPane();
        areaPlanEstudio = new javax.swing.JTextPane();
        subtitulo2 = new javax.swing.JLabel();
        labelCnombre = new javax.swing.JLabel();
        labelCresolucion = new javax.swing.JLabel();
        labelCdescripcion = new javax.swing.JLabel();
        agregarCarrera = new javax.swing.JButton();
        planesEstudio = new javax.swing.JComboBox<>();
        cantidadDeOptativas = new javax.swing.JComboBox<>();
        carreraNombre = new javax.swing.JTextField();
        carreraResolucion = new javax.swing.JTextField();
        carreraDescripcion = new javax.swing.JTextField();
        agregarMateria2 = new javax.swing.JButton();
        PanelMaterias = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        materiaAborrar = new javax.swing.JComboBox<>();
        eliminarMateria = new javax.swing.JButton();
        scrollMaterias = new javax.swing.JScrollPane();
        tablaMaterias = new javax.swing.JTable();
        PanelGestion = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        materiaNombre = new java.awt.TextField();
        materiaPrograma = new java.awt.TextField();
        materiaCuatrimestre = new java.awt.TextField();
        materiaSeleccionada = new javax.swing.JComboBox<>();
        correlativaSeleccionada = new javax.swing.JComboBox<>();
        agregarMateria = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        planesCarrera = new javax.swing.JComboBox<>();
        cambiarPlan = new javax.swing.JButton();
        isOptativa = new javax.swing.JCheckBox();
        agregarCorrelativa = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        main.setBackground(new java.awt.Color(25, 42, 81));

        nav.setBackground(new java.awt.Color(150, 122, 161));

        titulo.setBackground(new java.awt.Color(255, 255, 255));
        titulo.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setText("Gestion Carreras");
        titulo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        inicio.setBackground(new java.awt.Color(245, 230, 232));
        inicio.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        inicio.setText("Inicio");

        alumnos.setBackground(new java.awt.Color(245, 230, 232));
        alumnos.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        alumnos.setText("Gestion Alumnos");

        javax.swing.GroupLayout navLayout = new javax.swing.GroupLayout(nav);
        nav.setLayout(navLayout);
        navLayout.setHorizontalGroup(
            navLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titulo)
                .addGap(275, 275, 275)
                .addComponent(alumnos)
                .addGap(15, 15, 15))
        );
        navLayout.setVerticalGroup(
            navLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(navLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(alumnos, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(inicio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(titulo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        body.setBackground(new java.awt.Color(150, 122, 161));

        panelCarreras.setBackground(new java.awt.Color(213, 198, 224));

        subtitulo1.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        subtitulo1.setText("Carreras");

        carreras.setBackground(new java.awt.Color(170, 161, 200));
        carreras.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        carreras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        carreras.setToolTipText("Seleccionar carrera");

        areaPlanEstudio.setBackground(new java.awt.Color(245, 230, 232));
        areaPlanEstudio.setBorder(null);
        scrollPlan.setViewportView(areaPlanEstudio);

        subtitulo2.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        subtitulo2.setText("Agregar Carrera");

        labelCnombre.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        labelCnombre.setText("Nombre");

        labelCresolucion.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        labelCresolucion.setText("Resolucion");

        labelCdescripcion.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        labelCdescripcion.setText("Descripcion");

        agregarCarrera.setBackground(new java.awt.Color(245, 230, 232));
        agregarCarrera.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        agregarCarrera.setText("Agregar Carrera");
        agregarCarrera.setPreferredSize(new java.awt.Dimension(178, 28));

        planesEstudio.setBackground(new java.awt.Color(170, 161, 200));
        planesEstudio.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        planesEstudio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        planesEstudio.setToolTipText("Seleccionar tipo de plan");

        cantidadDeOptativas.setBackground(new java.awt.Color(170, 161, 200));
        cantidadDeOptativas.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        cantidadDeOptativas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cantidadDeOptativas.setToolTipText("Seleccionar cantidad de optativas necesarias para graduacion");

        agregarMateria2.setBackground(new java.awt.Color(245, 230, 232));
        agregarMateria2.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        agregarMateria2.setText("Descripcion de Planes");

        javax.swing.GroupLayout panelCarrerasLayout = new javax.swing.GroupLayout(panelCarreras);
        panelCarreras.setLayout(panelCarrerasLayout);
        panelCarrerasLayout.setHorizontalGroup(
            panelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCarrerasLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCarrerasLayout.createSequentialGroup()
                        .addComponent(labelCdescripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(carreraDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelCarrerasLayout.createSequentialGroup()
                        .addGroup(panelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(agregarCarrera, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelCarrerasLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(cantidadDeOptativas, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))
                            .addGroup(panelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(panelCarrerasLayout.createSequentialGroup()
                                    .addComponent(labelCresolucion)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(carreraResolucion))
                                .addComponent(subtitulo2)
                                .addComponent(subtitulo1)
                                .addComponent(carreras, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelCarrerasLayout.createSequentialGroup()
                                    .addComponent(labelCnombre)
                                    .addGap(18, 18, 18)
                                    .addComponent(carreraNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(agregarMateria2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(scrollPlan))
                            .addComponent(planesEstudio, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(158, 158, 158))))
        );
        panelCarrerasLayout.setVerticalGroup(
            panelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCarrerasLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(subtitulo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(carreras, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPlan, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(agregarMateria2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(subtitulo2)
                .addGap(18, 18, 18)
                .addGroup(panelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCnombre)
                    .addComponent(carreraNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(carreraResolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCresolucion))
                .addGap(18, 18, 18)
                .addGroup(panelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCdescripcion)
                    .addComponent(carreraDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(planesEstudio, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cantidadDeOptativas, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(agregarCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelMaterias.setBackground(new java.awt.Color(213, 198, 224));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Materias");
        jLabel10.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel17.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabel17.setText("Eliminar Materia");

        materiaAborrar.setBackground(new java.awt.Color(170, 161, 200));
        materiaAborrar.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        materiaAborrar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        materiaAborrar.setToolTipText("Seleccionar materia");

        eliminarMateria.setBackground(new java.awt.Color(245, 230, 232));
        eliminarMateria.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        eliminarMateria.setText("Eliminar");

        tablaMaterias.setBackground(new java.awt.Color(245, 230, 232));
        tablaMaterias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nombre", "Cuatrimestre", "Optativa"
            }
        ));
        scrollMaterias.setViewportView(tablaMaterias);

        javax.swing.GroupLayout PanelMateriasLayout = new javax.swing.GroupLayout(PanelMaterias);
        PanelMaterias.setLayout(PanelMateriasLayout);
        PanelMateriasLayout.setHorizontalGroup(
            PanelMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelMateriasLayout.createSequentialGroup()
                .addGroup(PanelMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelMateriasLayout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(jLabel10))
                    .addGroup(PanelMateriasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrollMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelMateriasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PanelMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addGroup(PanelMateriasLayout.createSequentialGroup()
                                .addComponent(materiaAborrar, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(eliminarMateria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelMateriasLayout.setVerticalGroup(
            PanelMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelMateriasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(materiaAborrar)
                    .addComponent(eliminarMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        PanelGestion.setBackground(new java.awt.Color(213, 198, 224));

        jLabel19.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabel19.setText("Agregar materia");

        jLabel20.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jLabel20.setText("Nombre Materia");

        jLabel21.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jLabel21.setText("Programa");

        jLabel22.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jLabel22.setText("Cuatrimestre");

        materiaNombre.setBackground(new java.awt.Color(213, 198, 224));
        materiaNombre.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N

        materiaPrograma.setBackground(new java.awt.Color(213, 198, 224));
        materiaPrograma.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N

        materiaCuatrimestre.setBackground(new java.awt.Color(213, 198, 224));
        materiaCuatrimestre.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N

        materiaSeleccionada.setBackground(new java.awt.Color(170, 161, 200));
        materiaSeleccionada.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        materiaSeleccionada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        materiaSeleccionada.setToolTipText("Seleccionar optativa ");

        correlativaSeleccionada.setBackground(new java.awt.Color(170, 161, 200));
        correlativaSeleccionada.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        correlativaSeleccionada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        correlativaSeleccionada.setToolTipText("Seleccionar optativa");

        agregarMateria.setBackground(new java.awt.Color(245, 230, 232));
        agregarMateria.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        agregarMateria.setText("Agregar Materia");

        jLabel15.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabel15.setText("Cambiar Plan de estudio");

        planesCarrera.setBackground(new java.awt.Color(170, 161, 200));
        planesCarrera.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        planesCarrera.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        planesCarrera.setToolTipText("Elegir tipo de plan de estudio");

        cambiarPlan.setBackground(new java.awt.Color(245, 230, 232));
        cambiarPlan.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        cambiarPlan.setText("Cambiar");

        isOptativa.setText("Optativa");

        agregarCorrelativa.setBackground(new java.awt.Color(245, 230, 232));
        agregarCorrelativa.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        agregarCorrelativa.setText("Agregar Correlativa");

        jLabel24.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jLabel24.setText("Correlativa:");

        jLabel25.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jLabel25.setText("Materia:");

        javax.swing.GroupLayout PanelGestionLayout = new javax.swing.GroupLayout(PanelGestion);
        PanelGestion.setLayout(PanelGestionLayout);
        PanelGestionLayout.setHorizontalGroup(
            PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGestionLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25)
                    .addComponent(jLabel15)
                    .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(PanelGestionLayout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(materiaPrograma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(materiaSeleccionada, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(agregarMateria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(PanelGestionLayout.createSequentialGroup()
                            .addComponent(jLabel22)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(isOptativa)
                                .addComponent(materiaCuatrimestre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(agregarCorrelativa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(correlativaSeleccionada, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(PanelGestionLayout.createSequentialGroup()
                            .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel19)
                                .addGroup(PanelGestionLayout.createSequentialGroup()
                                    .addComponent(jLabel20)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(materiaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(PanelGestionLayout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(planesCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(35, 35, 35)
                                    .addComponent(cambiarPlan)))
                            .addGap(6, 6, 6))))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        PanelGestionLayout.setVerticalGroup(
            PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGestionLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20)
                    .addComponent(materiaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21)
                    .addComponent(materiaPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22)
                    .addComponent(materiaCuatrimestre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(isOptativa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(agregarMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(materiaSeleccionada, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(correlativaSeleccionada, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(agregarCorrelativa, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(planesCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cambiarPlan, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCarreras, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelGestion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PanelMaterias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCarreras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelGestion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
        main.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(nav, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(body, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelGestion;
    private javax.swing.JPanel PanelMaterias;
    private javax.swing.JButton agregarCarrera;
    private javax.swing.JButton agregarCorrelativa;
    private javax.swing.JButton agregarMateria;
    private javax.swing.JButton agregarMateria2;
    private javax.swing.JButton alumnos;
    private javax.swing.JTextPane areaPlanEstudio;
    private javax.swing.JPanel body;
    private javax.swing.JButton cambiarPlan;
    private javax.swing.JComboBox<String> cantidadDeOptativas;
    private javax.swing.JTextField carreraDescripcion;
    private javax.swing.JTextField carreraNombre;
    private javax.swing.JTextField carreraResolucion;
    private javax.swing.JComboBox<String> carreras;
    private javax.swing.JComboBox<String> correlativaSeleccionada;
    private javax.swing.JButton eliminarMateria;
    private javax.swing.JButton inicio;
    private javax.swing.JCheckBox isOptativa;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel labelCdescripcion;
    private javax.swing.JLabel labelCnombre;
    private javax.swing.JLabel labelCresolucion;
    private javax.swing.JPanel main;
    private javax.swing.JComboBox<String> materiaAborrar;
    private java.awt.TextField materiaCuatrimestre;
    private java.awt.TextField materiaNombre;
    private java.awt.TextField materiaPrograma;
    private javax.swing.JComboBox<String> materiaSeleccionada;
    private javax.swing.JPanel nav;
    private javax.swing.JPanel panelCarreras;
    private javax.swing.JComboBox<String> planesCarrera;
    private javax.swing.JComboBox<String> planesEstudio;
    private javax.swing.JScrollPane scrollMaterias;
    private javax.swing.JScrollPane scrollPlan;
    private javax.swing.JLabel subtitulo1;
    private javax.swing.JLabel subtitulo2;
    private javax.swing.JTable tablaMaterias;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}