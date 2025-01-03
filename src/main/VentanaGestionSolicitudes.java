package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import domain.Solicitud;
import java.awt.*;
import java.util.List;

public class VentanaGestionSolicitudes extends JFrame {

    private JTable tablaSolicitudes;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboFiltroEstado;
    private JTextField campoFiltroDNI;
    private JButton btnAceptar, btnRechazar, btnPendiente;

    public VentanaGestionSolicitudes(Bdd bd) {
        // Configuración básica de la ventana
        setTitle("Gestión de Solicitudes de Preguntas Predefinidas");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Colores
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorContraste = Color.WHITE;           // Blanco
        Color colorBotonAceptar = new Color(0, 153, 76); // Verde suave
        Color colorBotonRechazar = new Color(204, 0, 0); // Rojo suave
        Color colorBotonPendiente = new Color(0, 102, 204); // Azul claro

        // Panel superior combinado
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(colorPrincipal);

        // Subpanel 1: Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(colorPrincipal);
        JLabel titulo = new JLabel("Gestión de Solicitudes de Preguntas Predefinidas");
        titulo.setForeground(colorContraste);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titulo);

        // Subpanel 2: Barra de herramientas (filtros)
        JPanel barraHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barraHerramientas.setBackground(colorPrincipal);

        comboFiltroEstado = new JComboBox<>(new String[]{"PENDIENTE", "ACEPTADA", "RECHAZADA", "TODAS"});
        comboFiltroEstado.setFont(new Font("Arial", Font.PLAIN, 14));
        comboFiltroEstado.setBackground(colorContraste);
        comboFiltroEstado.setForeground(colorPrincipal);

        campoFiltroDNI = new JTextField(20);
        campoFiltroDNI.setFont(new Font("Arial", Font.PLAIN, 14));
        campoFiltroDNI.setForeground(Color.GRAY);
        campoFiltroDNI.setText("Buscar por DNI...");
        campoFiltroDNI.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campoFiltroDNI.getText().equals("Buscar por DNI...")) {
                    campoFiltroDNI.setText("");
                    campoFiltroDNI.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (campoFiltroDNI.getText().isBlank()) {
                    campoFiltroDNI.setText("Buscar por DNI...");
                    campoFiltroDNI.setForeground(Color.GRAY);
                }
            }
        });

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnFiltrar.setBackground(colorBotonPendiente);
        btnFiltrar.setForeground(colorContraste);
        btnFiltrar.addActionListener(e -> filtrarSolicitudes(bd));

        barraHerramientas.add(new JLabel("Estado: "));
        barraHerramientas.add(comboFiltroEstado);
        barraHerramientas.add(new JLabel("DNI: "));
        barraHerramientas.add(campoFiltroDNI);
        barraHerramientas.add(btnFiltrar);

        // Agregar subpaneles al panel superior
        panelSuperior.add(headerPanel, BorderLayout.NORTH);
        panelSuperior.add(barraHerramientas, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // Centro (Tabla de solicitudes)
        String[] columnas = {"Cliente (DNI)", "Pregunta", "Estado", "Respuesta"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición de las celdas
            }
        };
        tablaSolicitudes = new JTable(modeloTabla);
        tablaSolicitudes.setRowHeight(30);
        tablaSolicitudes.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaSolicitudes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaSolicitudes.getTableHeader().setBackground(colorPrincipal);
        tablaSolicitudes.getTableHeader().setForeground(colorContraste);
        tablaSolicitudes.setSelectionBackground(new Color(51, 153, 255)); // Azul claro para selección
        JScrollPane scrollTabla = new JScrollPane(tablaSolicitudes);
        add(scrollTabla, BorderLayout.CENTER);

        // Panel de acciones (Sur)
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelAcciones.setBackground(colorContraste);

        btnAceptar = new JButton("Aceptar Solicitud");
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAceptar.setBackground(colorBotonAceptar);
        btnAceptar.setForeground(colorContraste);

        btnRechazar = new JButton("Rechazar Solicitud");
        btnRechazar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRechazar.setBackground(colorBotonRechazar);
        btnRechazar.setForeground(colorContraste);

        btnPendiente = new JButton("Dejar en Pendiente");
        btnPendiente.setFont(new Font("Arial", Font.BOLD, 14));
        btnPendiente.setBackground(colorBotonPendiente);
        btnPendiente.setForeground(colorContraste);

        panelAcciones.add(btnAceptar);
        panelAcciones.add(btnRechazar);
        panelAcciones.add(btnPendiente);
        add(panelAcciones, BorderLayout.SOUTH);

        // Cargar datos iniciales
        cargarSolicitudesPendientes(bd);

        // Listeners
        btnAceptar.addActionListener(e -> aceptarSolicitud(bd));
        btnRechazar.addActionListener(e -> rechazarSolicitud(bd));
        btnPendiente.addActionListener(e -> dejarEnPendiente(bd));

        setVisible(true);
    }

    private void cargarSolicitudesPendientes(Bdd bd) {
        modeloTabla.setRowCount(0); // Limpiar la tabla
        List<Solicitud> solicitudes = bd.obtenerSolicitudesPendientes();
        for (Solicitud s : solicitudes) {
            modeloTabla.addRow(new Object[]{s.getDniCliente(), s.getPregunta(), s.getEstado(), s.getRespuesta()});
        }
    }

    private void filtrarSolicitudes(Bdd bd) {
        String estadoSeleccionado = (String) comboFiltroEstado.getSelectedItem();
        String dniFiltro = campoFiltroDNI.getText().trim();

        modeloTabla.setRowCount(0); // Limpiar la tabla
        List<Solicitud> solicitudes = bd.obtenerSolicitudesPorFiltro(estadoSeleccionado, dniFiltro);
        for (Solicitud s : solicitudes) {
            modeloTabla.addRow(new Object[]{s.getDniCliente(), s.getPregunta(), s.getEstado(), s.getRespuesta()});
        }
    }

    private void aceptarSolicitud(Bdd bd) {
        int selectedRow = tablaSolicitudes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una solicitud para aceptar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String respuesta = JOptionPane.showInputDialog(this, "Ingrese la respuesta para la solicitud:");
        if (respuesta != null && !respuesta.isBlank()) {
            String dni = (String) modeloTabla.getValueAt(selectedRow, 0);
            String pregunta = (String) modeloTabla.getValueAt(selectedRow, 1);
            int id = bd.obtenerIdSolicitud(dni, pregunta);
            bd.aceptarSolicitud(id, respuesta);
            cargarSolicitudesPendientes(bd);
        }
    }

    private void rechazarSolicitud(Bdd bd) {
        int selectedRow = tablaSolicitudes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una solicitud para rechazar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(selectedRow, 0);
        String pregunta = (String) modeloTabla.getValueAt(selectedRow, 1);
        int id = bd.obtenerIdSolicitud(dni, pregunta);
        bd.rechazarSolicitud(id);
        cargarSolicitudesPendientes(bd);
    }

    private void dejarEnPendiente(Bdd bd) {
        int selectedRow = tablaSolicitudes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una solicitud para dejar en pendiente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(selectedRow, 0);
        String pregunta = (String) modeloTabla.getValueAt(selectedRow, 1);
        int id = bd.obtenerIdSolicitud(dni, pregunta);
        bd.dejarEnPendiente(id);
        cargarSolicitudesPendientes(bd);
    }

    public static void main(String[] args) {
        new VentanaGestionSolicitudes(new Bdd("aseguradora.db"));
    }
}
