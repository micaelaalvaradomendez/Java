/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.micaela.view;

import com.micaela.controller.ControladorGestion;
import com.micaela.controller.ControladorInscripcion;
import com.micaela.model.Alumno;
import com.micaela.model.Carrera;
import com.micaela.model.Facultad;
import com.micaela.view.GestionCarreras;
import com.micaela.view.Principal;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class GestionAlumnos extends javax.swing.JFrame {

    /**
     * Creates new form GestionAlumnos
     */
    
    private Facultad facultad;
    
    private ControladorInscripcion controlador;
    private JLabel labelresultado;
    
    private static GestionAlumnos instance = null;

    private GestionAlumnos() {
        initComponents();
        configuracion();
        facultad = Facultad.getInstance();
        controlador = ControladorInscripcion.getInstance();
        
        inicio.addActionListener(e -> {
            Principal.getInstance(true);
            this.dispose();
        });
        carreras.addActionListener(e -> {
            GestionCarreras.getInstance(true);
            this.dispose();
        });
        
        labelresultado = new JLabel();
        labelresultado.setHorizontalTextPosition(JLabel.CENTER);
        resultadoLegajo.add(labelresultado, BorderLayout.CENTER);
        
        altaAlumno.addActionListener(e -> altaAlumno());
        // Mucho cuidado con el orden de llamada de estos métodos
        
        actualizarAlumnos();
        actualizarCarrera();
        cargarMaterias();
        actualizarHistoria();
        cargarMateriasNotas();
        elegirMateria();
        
        comboDniBuscar.addActionListener(e -> sincronizarBusqueda(e));
        comboNombreBuscar.addActionListener(e -> sincronizarBusqueda(e));
        comboLegajoBuscar.addActionListener(e -> sincronizarBusqueda(e));
        inscribirMateria.addActionListener(e -> inscribirAMateria());
        comboMateriaNota.addActionListener(e -> elegirMateria());
        agregarNota.addActionListener(e -> agregarNota());
        graduacion.addActionListener(e -> graduarAlumno());
    }
    
    private void cargarMateriasNotas(){
        comboMateriaNota.removeAllItems();
        String legajoAlumno = (String) comboLegajoBuscar.getSelectedItem();
        if (legajoAlumno!=null) {
            int legajo = Integer.parseInt(legajoAlumno);
            var historia = controlador.obtenerHistoriaAcademica(legajo);
            historia.forEach((dato) -> {
                String nombre = dato.getMateria().getNombre();
                comboMateriaNota.addItem(nombre);
            });
        }
    }
    
    private void elegirMateria(){
        String materia = (String) comboMateriaNota.getSelectedItem();
        if (materia==null) return;
        String legajoAlumno = (String) comboLegajoBuscar.getSelectedItem();
        if (legajoAlumno==null) return;
        int legajo = Integer.parseInt(legajoAlumno);
        String nota = controlador.getTipoNota(legajo, materia);
        if (nota!=null)
            tipoNota.setText(nota);
        else
            tipoNota.setText("Sin nota");
    }
    
    private void agregarNota(){
        String materia = (String) comboMateriaNota.getSelectedItem();
        if (materia==null) return;
        String legajoAlumno = (String) comboLegajoBuscar.getSelectedItem();
        if (legajoAlumno==null) return;
        int legajo = Integer.parseInt(legajoAlumno);
        
        int nota = Integer.parseInt((String) comboNota.getSelectedItem());
        
        
        String tipo = tipoNota.getText();
        
        if (tipo.equals("nota Parcial") || tipo.equals("nota Final")) {
            facultad.cargarNota(legajo, materia, nota);
        }
        actualizarHistoria();
        elegirMateria();
        cargarMaterias();
    }
    
    private void actualizarHistoria(){
        DefaultTableModel model = (DefaultTableModel) historiaAcademica.getModel();
        model.setRowCount(0);
        String legajoSeleccionado = (String) comboLegajoBuscar.getSelectedItem();
        if (legajoSeleccionado!=null){
            int legajo = Integer.parseInt(legajoSeleccionado);
            var historia = controlador.obtenerHistoriaAcademica(legajo);
            
            historia.forEach((dato) -> {
                var materia = dato.getMateria();
                String nombre = materia.getNombre();
                int cuatrimestre = materia.getCuatrimestre();
                String estado = dato.getNombreEstado();
                int nota = dato.getNota();
                model.addRow(new Object[]{nombre, cuatrimestre, estado, nota});
            });
        }
        historiaAcademica.revalidate();
        historiaAcademica.repaint();
    }
    
    private void sincronizarBusqueda(ActionEvent e) {
        // Determinar cuál ComboBox activó el evento
        String tipoBusqueda = "";
        String valorSeleccionado = "";

        if (e.getSource() == comboNombreBuscar) {
            tipoBusqueda = "nombre";
            valorSeleccionado = (String) comboNombreBuscar.getSelectedItem();
        } else if (e.getSource() == comboDniBuscar) {
            tipoBusqueda = "dni";
            valorSeleccionado = (String) comboDniBuscar.getSelectedItem();
        } else if (e.getSource() == comboLegajoBuscar) {
            tipoBusqueda = "legajo";
            valorSeleccionado = (String) comboLegajoBuscar.getSelectedItem();
        }
        
        // Buscar el alumno correspondiente
        
        Alumno alumno = facultad.buscarAlumno(tipoBusqueda, valorSeleccionado);

        if (alumno != null) {
            // Sincronizar los otros ComboBox
            if (!tipoBusqueda.equals("nombre")) {
                comboNombreBuscar.setSelectedItem(alumno.getNombre());
            }
            if (!tipoBusqueda.equals("dni")) {
                comboDniBuscar.setSelectedItem(String.valueOf(alumno.getDni()));
            }
            if (!tipoBusqueda.equals("legajo")) {
                comboLegajoBuscar.setSelectedItem(String.valueOf(alumno.getLegajo()));
            }
        }
        
        actualizarCarrera();
        actualizarHistoria();
        cargarMateriasNotas();
        elegirMateria();
    }
    
    private void actualizarCarrera() {
        String legajoBuscar = (String) comboLegajoBuscar.getSelectedItem();
        if (legajoBuscar!=null){
            int alumno = Integer.parseInt(legajoBuscar);
            var carreras = facultad.getCarreras();
            for (var carrera: carreras) {
                if (carrera.getAlumnos().containsKey(alumno)) {
                    carreraAlumno.setText(carrera.getNombre());
                    cargarMaterias();
                    return;
                }
            }
        }
    }
    
    private void cargarCarreras() {
        comboCarreras.removeAllItems();
        for (Carrera carrera : facultad.getCarreras()) {
            comboCarreras.addItem(carrera.getNombre());
        }
    }
    
    private void cargarMaterias() {
        String nombreCarrera = carreraAlumno.getText();
        if (nombreCarrera == null) return;
        String legajoAlumno = (String) comboLegajoBuscar.getSelectedItem();
        comboMaterias.removeAllItems();
        if (legajoAlumno==null) return;
        int legajo = Integer.parseInt(legajoAlumno);
        var materias = controlador.obtenerMateriasParaCursar(legajo, nombreCarrera);
        materias.forEach(nombre -> comboMaterias.addItem(nombre));
    }
    
    public void altaAlumno(){
        String nombre = alumnoNombre.getText();
        int dni = Integer.parseInt(alumnoDNI.getText());
        var fechaNacimiento = LocalDate.parse(alumnoFecha.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        

        if (!nombre.isEmpty() && dni!=0 && fechaNacimiento!=null) {
            int legajo = facultad.altaAlumno(nombre, dni, fechaNacimiento, true);
            labelresultado.setText("" + legajo);
            if( legajo>=0 ) {
                JOptionPane.showMessageDialog(this, "Alumno dado de alta con éxito.");
                String carrera = (String) comboCarreras.getSelectedItem();
                controlador.inscripcionAlumnoCarrera(legajo, carrera);
                actualizarAlumnos();
            }
            else
                JOptionPane.showMessageDialog(this, "El alumno ya esta en el sistema!");
        } else {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
        }
    }
    
    public void actualizarAlumnos() {
        
        comboLegajoBuscar.removeAllItems();
        comboDniBuscar.removeAllItems();
        comboNombreBuscar.removeAllItems();
        
        var alumnosLegajo = facultad.getAlumnos("legajo");
        var alumnosDni = facultad.getAlumnos("dni");
        var alumnosNombre = facultad.getAlumnos("nombre");

        for (int i = 0; i<alumnosDni.size(); i++){
            comboLegajoBuscar.addItem(alumnosLegajo.get(i));
            comboNombreBuscar.addItem(alumnosNombre.get(i));
            comboDniBuscar.addItem(alumnosDni.get(i));
        }
    }
    
    public void configuracion(){
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    public static GestionAlumnos getInstance(boolean mostrar){
        if (instance==null)
            instance = new GestionAlumnos();
        instance.cargarCarreras();
        instance.setVisible(mostrar);
        return instance;
    }
    
    private void inscribirAMateria() {
        String carreraSeleccionada = (String) comboCarreras.getSelectedItem();
        String legajoSeleccionado = (String) comboLegajoBuscar.getSelectedItem();
        String materiaSeleccionada = (String) comboMaterias.getSelectedItem();
        if (carreraSeleccionada==null || legajoSeleccionado==null || materiaSeleccionada==null) return;
        int legajo = Integer.parseInt(legajoSeleccionado);
        facultad.inscripcionAlumnoMateria(carreraSeleccionada, legajo, materiaSeleccionada);
        actualizarHistoria();
        cargarMateriasNotas();
        elegirMateria();
        cargarMaterias();
    }
    
    private void graduarAlumno() {
        String legajoAlumno = (String) comboLegajoBuscar.getSelectedItem();
        if (legajoAlumno==null) return;
        int legajo = Integer.parseInt(legajoAlumno);
        boolean graduado = facultad.isGraduado(legajo);
        if (graduado){
             JOptionPane.showMessageDialog(null, "¡Se ha graduado!", "Graduación", JOptionPane.INFORMATION_MESSAGE);
        } else {
             JOptionPane.showMessageDialog(null, "Todavía no puede graduarse", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new javax.swing.JPanel();
        barra = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        inicio = new javax.swing.JButton();
        carreras = new javax.swing.JButton();
        centro = new javax.swing.JPanel();
        PanelAlumno = new javax.swing.JPanel();
        subtitulo1 = new javax.swing.JLabel();
        labelAnombre = new javax.swing.JLabel();
        labelADNI = new javax.swing.JLabel();
        labelAFecha = new javax.swing.JLabel();
        resultadoLegajo = new javax.swing.JPanel();
        comboCarreras = new javax.swing.JComboBox<>();
        comboMaterias = new javax.swing.JComboBox<>();
        inscribirMateria = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        alumnoNombre = new javax.swing.JTextField();
        alumnoDNI = new javax.swing.JTextField();
        alumnoFecha = new javax.swing.JTextField();
        altaAlumno = new javax.swing.JButton();
        PanelHistoria = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        historiaAcademica = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        mostrarCarreraDeAlumno = new javax.swing.JPanel();
        carreraAlumno = new javax.swing.JLabel();
        PanelGestion = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        comboLegajoBuscar = new javax.swing.JComboBox<>();
        comboDniBuscar = new javax.swing.JComboBox<>();
        comboNombreBuscar = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        comboMateriaNota = new javax.swing.JComboBox<>();
        agregarNota = new javax.swing.JButton();
        graduacion = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        comboNota = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        tipoNota = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        main.setBackground(new java.awt.Color(25, 42, 81));
        main.setSize(getWidth(), 80);

        barra.setBackground(new java.awt.Color(150, 122, 161));
        barra.setPreferredSize(new java.awt.Dimension(938, 80));

        titulo.setBackground(new java.awt.Color(255, 255, 255));
        titulo.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Gestion Alumnos");
        titulo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        inicio.setBackground(new java.awt.Color(245, 230, 232));
        inicio.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        inicio.setText("Inicio");

        carreras.setBackground(new java.awt.Color(245, 230, 232));
        carreras.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        carreras.setText("Gestion Carreras");

        javax.swing.GroupLayout barraLayout = new javax.swing.GroupLayout(barra);
        barra.setLayout(barraLayout);
        barraLayout.setHorizontalGroup(
            barraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(149, 149, 149)
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(carreras)
                .addContainerGap())
        );
        barraLayout.setVerticalGroup(
            barraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barraLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(barraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inicio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(barraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(carreras, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        centro.setBackground(new java.awt.Color(150, 122, 161));

        PanelAlumno.setBackground(new java.awt.Color(213, 198, 224));

        subtitulo1.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        subtitulo1.setText("Alta Alumno");

        labelAnombre.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        labelAnombre.setText("Nombre");

        labelADNI.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        labelADNI.setText("DNI");

        labelAFecha.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        labelAFecha.setText("Fecha Nacimiento");

        resultadoLegajo.setBackground(new java.awt.Color(245, 230, 232));
        resultadoLegajo.setLayout(new java.awt.BorderLayout());

        comboCarreras.setBackground(new java.awt.Color(170, 161, 200));
        comboCarreras.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        comboCarreras.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboCarreras.setToolTipText("Seleccionar carrera");
        comboCarreras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCarrerasActionPerformed(evt);
            }
        });

        comboMaterias.setBackground(new java.awt.Color(170, 161, 200));
        comboMaterias.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        comboMaterias.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboMaterias.setToolTipText("Seleccionar materia");

        inscribirMateria.setBackground(new java.awt.Color(245, 230, 232));
        inscribirMateria.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        inscribirMateria.setText("Inscrbir a Materia");
        inscribirMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inscribirMateriaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jLabel10.setText("Seleccionar Materia");

        jLabel11.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jLabel11.setText("Seleccionar Carrera");

        altaAlumno.setBackground(new java.awt.Color(245, 230, 232));
        altaAlumno.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        altaAlumno.setText("Alta Alumno");

        javax.swing.GroupLayout PanelAlumnoLayout = new javax.swing.GroupLayout(PanelAlumno);
        PanelAlumno.setLayout(PanelAlumnoLayout);
        PanelAlumnoLayout.setHorizontalGroup(
            PanelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAlumnoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(PanelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(PanelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel11)
                        .addComponent(subtitulo1)
                        .addComponent(inscribirMateria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(PanelAlumnoLayout.createSequentialGroup()
                            .addComponent(labelAFecha)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(alumnoFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                        .addComponent(comboCarreras, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboMaterias, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(PanelAlumnoLayout.createSequentialGroup()
                            .addGroup(PanelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelAnombre)
                                .addComponent(labelADNI))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(PanelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(alumnoNombre)
                                .addComponent(alumnoDNI))))
                    .addGroup(PanelAlumnoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(resultadoLegajo, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(altaAlumno)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        PanelAlumnoLayout.setVerticalGroup(
            PanelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAlumnoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(subtitulo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelAnombre)
                    .addComponent(alumnoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(PanelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelADNI)
                    .addComponent(alumnoDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(PanelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelAFecha)
                    .addComponent(alumnoFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboCarreras, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resultadoLegajo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(altaAlumno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(inscribirMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        PanelHistoria.setBackground(new java.awt.Color(213, 198, 224));
        PanelHistoria.setPreferredSize(new java.awt.Dimension(300, 488));

        historiaAcademica.setBackground(new java.awt.Color(245, 230, 232));
        historiaAcademica.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        historiaAcademica.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                " Nombre", "Cuatrimestre", "Estado", "Nota"
            }
        ));
        jScrollPane2.setViewportView(historiaAcademica);
        if (historiaAcademica.getColumnModel().getColumnCount() > 0) {
            historiaAcademica.getColumnModel().getColumn(0).setHeaderValue(" Nombre");
            historiaAcademica.getColumnModel().getColumn(1).setHeaderValue("Cuatrimestre");
            historiaAcademica.getColumnModel().getColumn(2).setHeaderValue("Estado");
            historiaAcademica.getColumnModel().getColumn(3).setHeaderValue("Nota");
        }

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Historia Academica");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        mostrarCarreraDeAlumno.setBackground(new java.awt.Color(245, 230, 232));

        carreraAlumno.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        carreraAlumno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        carreraAlumno.setText("jLabel2");

        javax.swing.GroupLayout mostrarCarreraDeAlumnoLayout = new javax.swing.GroupLayout(mostrarCarreraDeAlumno);
        mostrarCarreraDeAlumno.setLayout(mostrarCarreraDeAlumnoLayout);
        mostrarCarreraDeAlumnoLayout.setHorizontalGroup(
            mostrarCarreraDeAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(carreraAlumno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
        );
        mostrarCarreraDeAlumnoLayout.setVerticalGroup(
            mostrarCarreraDeAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(carreraAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PanelHistoriaLayout = new javax.swing.GroupLayout(PanelHistoria);
        PanelHistoria.setLayout(PanelHistoriaLayout);
        PanelHistoriaLayout.setHorizontalGroup(
            PanelHistoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelHistoriaLayout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(PanelHistoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mostrarCarreraDeAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelHistoriaLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(56, 56, 56))
            .addGroup(PanelHistoriaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelHistoriaLayout.setVerticalGroup(
            PanelHistoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelHistoriaLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mostrarCarreraDeAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanelGestion.setBackground(new java.awt.Color(213, 198, 224));

        jLabel8.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabel8.setText("Buscar Alumno");

        comboLegajoBuscar.setBackground(new java.awt.Color(170, 161, 200));
        comboLegajoBuscar.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        comboLegajoBuscar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboLegajoBuscar.setToolTipText("Lista de Legajos");

        comboDniBuscar.setBackground(new java.awt.Color(170, 161, 200));
        comboDniBuscar.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        comboDniBuscar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboDniBuscar.setToolTipText("Lista de DNI");

        comboNombreBuscar.setBackground(new java.awt.Color(170, 161, 200));
        comboNombreBuscar.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        comboNombreBuscar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboNombreBuscar.setToolTipText("Lista de Nombres");
        comboNombreBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboNombreBuscarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        jLabel9.setText("Agregar nota");

        comboMateriaNota.setBackground(new java.awt.Color(170, 161, 200));
        comboMateriaNota.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        comboMateriaNota.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboMateriaNota.setToolTipText("Seleccionar materia");
        comboMateriaNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMateriaNotaActionPerformed(evt);
            }
        });

        agregarNota.setBackground(new java.awt.Color(245, 230, 232));
        agregarNota.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        agregarNota.setText("Agregar Nota");

        graduacion.setBackground(new java.awt.Color(245, 230, 232));
        graduacion.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        graduacion.setText("Graduacion");

        jLabel1.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jLabel1.setText("Legajo :");

        jLabel3.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jLabel3.setText("DNI :");

        jLabel4.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jLabel4.setText("Nombre : ");

        jLabel5.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jLabel5.setText("Materia:");

        comboNota.setBackground(new java.awt.Color(170, 161, 200));
        comboNota.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        comboNota.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        comboNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboNotaActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jLabel6.setText("Asignar nota :");

        tipoNota.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        tipoNota.setText("Sin nota");

        javax.swing.GroupLayout PanelGestionLayout = new javax.swing.GroupLayout(PanelGestion);
        PanelGestion.setLayout(PanelGestionLayout);
        PanelGestionLayout.setHorizontalGroup(
            PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelGestionLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9)
                        .addComponent(comboNombreBuscar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(graduacion, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(PanelGestionLayout.createSequentialGroup()
                            .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(comboLegajoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(PanelGestionLayout.createSequentialGroup()
                                    .addGap(16, 16, 16)
                                    .addComponent(jLabel8))
                                .addComponent(jLabel1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(comboDniBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(agregarNota, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelGestionLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(comboMateriaNota, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelGestionLayout.createSequentialGroup()
                        .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelGestionLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(33, 33, 33))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelGestionLayout.createSequentialGroup()
                                .addComponent(tipoNota, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(comboNota, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );
        PanelGestionLayout.setVerticalGroup(
            PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelGestionLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboLegajoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboDniBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboNombreBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboMateriaNota, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelGestionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboNota, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tipoNota, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(agregarNota, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(graduacion, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout centroLayout = new javax.swing.GroupLayout(centro);
        centro.setLayout(centroLayout);
        centroLayout.setHorizontalGroup(
            centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(PanelGestion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelHistoria, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );
        centroLayout.setVerticalGroup(
            centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(centroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelHistoria, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addGroup(centroLayout.createSequentialGroup()
                        .addGroup(centroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(PanelGestion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
        main.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, 1036, Short.MAX_VALUE)
                    .addComponent(centro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(centro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
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
            .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inscribirMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inscribirMateriaActionPerformed
    
    }//GEN-LAST:event_inscribirMateriaActionPerformed

    private void comboNombreBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboNombreBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboNombreBuscarActionPerformed

    private void comboCarrerasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCarrerasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCarrerasActionPerformed

    private void comboMateriaNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMateriaNotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboMateriaNotaActionPerformed

    private void comboNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboNotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboNotaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelAlumno;
    private javax.swing.JPanel PanelGestion;
    private javax.swing.JPanel PanelHistoria;
    private javax.swing.JButton agregarNota;
    private javax.swing.JButton altaAlumno;
    private javax.swing.JTextField alumnoDNI;
    private javax.swing.JTextField alumnoFecha;
    private javax.swing.JTextField alumnoNombre;
    private javax.swing.JPanel barra;
    private javax.swing.JLabel carreraAlumno;
    private javax.swing.JButton carreras;
    private javax.swing.JPanel centro;
    private javax.swing.JComboBox<String> comboCarreras;
    private javax.swing.JComboBox<String> comboDniBuscar;
    private javax.swing.JComboBox<String> comboLegajoBuscar;
    private javax.swing.JComboBox<String> comboMateriaNota;
    private javax.swing.JComboBox<String> comboMaterias;
    private javax.swing.JComboBox<String> comboNombreBuscar;
    private javax.swing.JComboBox<String> comboNota;
    private javax.swing.JButton graduacion;
    private javax.swing.JTable historiaAcademica;
    private javax.swing.JButton inicio;
    private javax.swing.JButton inscribirMateria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelADNI;
    private javax.swing.JLabel labelAFecha;
    private javax.swing.JLabel labelAnombre;
    private javax.swing.JPanel main;
    private javax.swing.JPanel mostrarCarreraDeAlumno;
    private javax.swing.JPanel resultadoLegajo;
    private javax.swing.JLabel subtitulo1;
    private javax.swing.JLabel tipoNota;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
