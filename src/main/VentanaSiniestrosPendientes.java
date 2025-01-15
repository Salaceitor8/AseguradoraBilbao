package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import domain.Siniestro;
import java.awt.*;
import java.util.List;

public class VentanaSiniestrosPendientes extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tablaSiniestros;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboFiltroEstado;
    private JTextField campoFiltroDNI;
    private JButton btnActualizar, btnResolver, btnRechazar, btnPendiente;

    public VentanaSiniestrosPendientes(Bdd bd) {
        // Configuración básica de la ventana
        setTitle("Gestión de Siniestros");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Colores
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorContraste = Color.WHITE;           // Blanco
        Color colorBotonActualizar = new Color(0, 102, 204); // Azul claro
        Color colorBotonResolver = new Color(0, 153, 76);     // Verde suave
        Color colorBotonRechazar = new Color(204, 0, 0);      // Rojo suave

        // Panel superior combinado
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(colorPrincipal);

        // Subpanel 1: Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(colorPrincipal);
        JLabel titulo = new JLabel("Gestión de Siniestros");
        titulo.setForeground(colorContraste);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titulo);

        // Subpanel 2: Barra de herramientas (filtros)
        JPanel barraHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barraHerramientas.setBackground(colorPrincipal);

        comboFiltroEstado = new JComboBox<>(new String[]{"PENDIENTE", "RESUELTO", "RECHAZADO", "TODOS"});
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

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnActualizar.setBackground(colorBotonActualizar);
        btnActualizar.setForeground(colorContraste);
        btnActualizar.addActionListener(e -> filtrarSiniestros(bd));

        barraHerramientas.add(new JLabel("Estado: "));
        barraHerramientas.add(comboFiltroEstado);
        barraHerramientas.add(new JLabel("DNI: "));
        barraHerramientas.add(campoFiltroDNI);
        barraHerramientas.add(btnActualizar);

        // Agregar subpaneles al panel superior
        panelSuperior.add(headerPanel, BorderLayout.NORTH);
        panelSuperior.add(barraHerramientas, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // Centro (Tabla de siniestros)
        String[] columnas = {"Cliente (DNI)", "Tipo de Seguro", "Precio", "Resumen", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición de las celdas
            }
        };
        tablaSiniestros = new JTable(modeloTabla);
        tablaSiniestros.setRowHeight(30);
        tablaSiniestros.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaSiniestros.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaSiniestros.getTableHeader().setBackground(colorPrincipal);
        tablaSiniestros.getTableHeader().setForeground(colorContraste);
        tablaSiniestros.setSelectionBackground(new Color(51, 153, 255)); // Azul claro para selección
        JScrollPane scrollTabla = new JScrollPane(tablaSiniestros);
        add(scrollTabla, BorderLayout.CENTER);

        // Panel de acciones (Sur)
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelAcciones.setBackground(colorContraste);

        btnResolver = new JButton("Resolver");
        btnResolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnResolver.setBackground(colorBotonResolver);
        btnResolver.setForeground(colorContraste);
        btnResolver.addActionListener(e -> resolverSiniestro(bd));

        btnRechazar = new JButton("Rechazar");
        btnRechazar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRechazar.setBackground(colorBotonRechazar);
        btnRechazar.setForeground(colorContraste);
        btnRechazar.addActionListener(e -> rechazarSiniestro(bd));

        btnPendiente = new JButton("Dejar en Pendiente");
        btnPendiente.setFont(new Font("Arial", Font.BOLD, 14));
        btnPendiente.setBackground(colorBotonActualizar);
        btnPendiente.setForeground(colorContraste);
        btnPendiente.addActionListener(e -> dejarEnPendiente(bd));

        panelAcciones.add(btnResolver);
        panelAcciones.add(btnRechazar);
        panelAcciones.add(btnPendiente);
        add(panelAcciones, BorderLayout.SOUTH);

        // Cargar datos iniciales
        cargarSiniestrosPendientes(bd);

        setVisible(true);
    }

    private void cargarSiniestrosPendientes(Bdd bd) {
        modeloTabla.setRowCount(0); // Limpiar la tabla
        List<Siniestro> siniestros = bd.obtenerSiniestrosPendientes();
        for (Siniestro s : siniestros) {
            modeloTabla.addRow(new Object[]{s.getDni_cliente(), s.getTipoSeguro(), s.getPrecio(), s.getResumen(), s.getEstado()});
        }
    }
    private void filtrarSiniestros(Bdd bd) {
        String estadoSeleccionado = (String) comboFiltroEstado.getSelectedItem();
        String dniFiltro = campoFiltroDNI.getText().trim();

        modeloTabla.setRowCount(0); // Limpiar la tabla
        List<Siniestro> siniestros = bd.obtenerSiniestrosPorFiltro(estadoSeleccionado, dniFiltro);
        for (Siniestro s : siniestros) {
        	modeloTabla.addRow(new Object[]{s.getDni_cliente(), s.getTipoSeguro(), s.getPrecio(), s.getResumen(), s.getEstado()});
        }
    }

    private void resolverSiniestro(Bdd bd) {
        int selectedRow = tablaSiniestros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un siniestro para resolver.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(selectedRow, 0);
        String tipoSeguro = modeloTabla.getValueAt(selectedRow, 1).toString();
        bd.resolverSiniestro(dni, tipoSeguro);
        cargarSiniestrosPendientes(bd);
    }

    private void rechazarSiniestro(Bdd bd) {
        int selectedRow = tablaSiniestros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un siniestro para rechazar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(selectedRow, 0);
        String tipoSeguro = modeloTabla.getValueAt(selectedRow, 1).toString();
        bd.rechazarSiniestro(dni, tipoSeguro);
        cargarSiniestrosPendientes(bd);
    }

    private void dejarEnPendiente(Bdd bd) {
        int selectedRow = tablaSiniestros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un siniestro para dejar en pendiente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(selectedRow, 0);
        String tipoSeguro = modeloTabla.getValueAt(selectedRow, 1).toString();
        bd.dejarEnPendienteSiniestro(dni, tipoSeguro);
        cargarSiniestrosPendientes(bd);
    }


}
