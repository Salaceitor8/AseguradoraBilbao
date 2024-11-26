package main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.*;
import domain.Seguro;
import domain.TipoSeguro;
import gui.SeguroCellRenderer;

public class VentanaPrincipalEmpleado extends JFrame {

    private static final long serialVersionUID = 1L;
    private JList<String> listaClientes;
    private DefaultListModel<String> modeloListaClientes;
    private JTextField campoBusqueda;
    private JPanel panelInfoCliente;
    private JTable tablaSeguros;
    private DefaultTableModel modeloTablaSeguros;
    private Map<String, List<Seguro>> segurosPorCliente;
    private List<String[]> listaOriginalClientes;
    private JLabel totalCostoSeguros;
    private final String archivoCSVClientes = "clientes.csv";
    private final String archivoCSVSeguros = "seguros.csv";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor
    public VentanaPrincipalEmpleado() {
        setTitle("Aseguradoras Bilbao - Panel de Empleado");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        Color colorPrincipal = new Color(0, 51, 102);
        Color colorContraste = new Color(255, 255, 255);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(colorPrincipal);

        // Lista de clientes
        modeloListaClientes = new DefaultListModel<>();
        listaClientes = new JList<>(modeloListaClientes);
        listaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaClientes.setBackground(Color.WHITE);
        listaClientes.setForeground(Color.BLACK);
        listaClientes.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        listaClientes.setFont(new Font("Arial", Font.PLAIN, 14));

        // Campo de búsqueda
        campoBusqueda = new JTextField(20);
        campoBusqueda.setBackground(Color.WHITE);
        campoBusqueda.setForeground(Color.BLACK);
        campoBusqueda.setBorder(BorderFactory.createLineBorder(colorPrincipal, 1));
        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarClientes(campoBusqueda.getText());
            }
        });

        JLabel etiquetaBuscar = new JLabel("Buscar cliente:");
        etiquetaBuscar.setForeground(colorContraste);

        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.add(etiquetaBuscar, BorderLayout.WEST);
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelBusqueda.setBackground(colorPrincipal);
        panelBusqueda.setForeground(colorContraste);

        // Botones de alta y baja de clientes
        JPanel panelBotonesClientes = new JPanel(new FlowLayout());
        panelBotonesClientes.setBackground(colorPrincipal);

        JButton btnAltaCliente = new JButton("Dar de Alta Cliente");
        JButton btnBajaCliente = new JButton("Dar de Baja Cliente");
        btnAltaCliente.setBackground(new Color(51, 153, 255));
        btnAltaCliente.setForeground(Color.WHITE);
        btnBajaCliente.setBackground(Color.RED);
        btnBajaCliente.setForeground(Color.WHITE);

        panelBotonesClientes.add(btnAltaCliente);
        panelBotonesClientes.add(btnBajaCliente);

        JPanel panelClientes = new JPanel(new BorderLayout());
        panelClientes.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                "Clientes", 0, 0, new Font("Arial", Font.BOLD, 16), colorContraste));
        panelClientes.setBackground(colorPrincipal);
        panelClientes.add(panelBusqueda, BorderLayout.NORTH);
        panelClientes.add(new JScrollPane(listaClientes), BorderLayout.CENTER);
        panelClientes.add(panelBotonesClientes, BorderLayout.SOUTH);

        panelInfoCliente = new JPanel(new BorderLayout());
        panelInfoCliente.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                "Información del Cliente", 0, 0, new Font("Arial", Font.BOLD, 16), colorContraste));
        panelInfoCliente.setBackground(colorPrincipal);

        String[] columnasSeguros = {"Tipo de seguro", "Fecha de contratación", "Costo anual", "Estado"};
        modeloTablaSeguros = new DefaultTableModel(columnasSeguros, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaSeguros = new JTable(modeloTablaSeguros);
        tablaSeguros.setBackground(Color.WHITE);
        tablaSeguros.setForeground(Color.BLACK);
        tablaSeguros.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaSeguros.setRowHeight(25);
        tablaSeguros.setFillsViewportHeight(true);
        tablaSeguros.setDefaultRenderer(Object.class, new SeguroCellRenderer());

        JPanel panelTablaSeguros = new JPanel(new BorderLayout());
        panelTablaSeguros.add(new JScrollPane(tablaSeguros), BorderLayout.CENTER);

        totalCostoSeguros = new JLabel("Costo Total de Seguros: 0 €");
        totalCostoSeguros.setForeground(Color.BLACK);

        panelTablaSeguros.add(totalCostoSeguros, BorderLayout.SOUTH);
        panelInfoCliente.add(panelTablaSeguros, BorderLayout.CENTER);

        JButton btnNuevoSeguro = new JButton("Nuevo Seguro");
        btnNuevoSeguro.setBackground(new Color(51, 153, 255));
        btnNuevoSeguro.setForeground(Color.WHITE);

        JButton btnBajaSeguro = new JButton("Dar de Baja Seguro");
        btnBajaSeguro.setBackground(Color.RED);
        btnBajaSeguro.setForeground(Color.WHITE);

        JPanel panelBotonesSeguros = new JPanel(new FlowLayout());
        panelBotonesSeguros.setBackground(colorPrincipal);
        panelBotonesSeguros.add(btnNuevoSeguro);
        panelBotonesSeguros.add(btnBajaSeguro);

        panelInfoCliente.add(panelBotonesSeguros, BorderLayout.SOUTH);

        panelPrincipal.add(panelClientes, BorderLayout.WEST);
        panelPrincipal.add(panelInfoCliente, BorderLayout.CENTER);
        add(panelPrincipal);

        cargarClientesDesdeCSV(archivoCSVClientes);
        cargarSegurosDesdeCSV(archivoCSVSeguros);

        btnAltaCliente.addActionListener(e -> new VentanaAltaCliente(modeloListaClientes, listaOriginalClientes, archivoCSVClientes));
        btnBajaCliente.addActionListener(e -> darDeBajaCliente());
        listaClientes.addListSelectionListener(e -> cargarSegurosCliente());

        btnNuevoSeguro.addActionListener(e -> {
            if (listaClientes.getSelectedValue() != null) {
                String dniCliente = listaClientes.getSelectedValue().split("- DNI: ")[1];

                List<Seguro> segurosCliente = segurosPorCliente.get(dniCliente);
                if (segurosCliente == null) {
                    segurosCliente = new ArrayList<>();
                    segurosPorCliente.put(dniCliente, segurosCliente);
                }

                new VentanaAltaSeguro(modeloTablaSeguros, dniCliente, archivoCSVSeguros);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un cliente primero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBajaSeguro.addActionListener(e -> {
            int filaSeleccionada = tablaSeguros.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un seguro para dar de baja.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String clienteSeleccionado = listaClientes.getSelectedValue();
            if (clienteSeleccionado != null) {
                String dniCliente = clienteSeleccionado.split("- DNI: ")[1];
                List<Seguro> segurosCliente = segurosPorCliente.get(dniCliente);
                if (segurosCliente != null) {
                    segurosCliente.get(filaSeleccionada).setEstado("Inactivo");
                    actualizarSegurosCSV();
                    cargarSegurosCliente();
                    JOptionPane.showMessageDialog(this, "El seguro ha sido dado de baja.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Chat");
        JMenuItem itemChat = new JMenuItem("Atencion al clietne");
        
        itemChat.addActionListener(e -> {new VentanaChatEmpleado();});
        
        menuBar.add(menu);
        menu.add(itemChat);
        
        setJMenuBar(menuBar);
        
        

        setVisible(true);
    }

    private void cargarSegurosDesdeCSV(String archivoCSV) {
        segurosPorCliente = new HashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datosSeguro = linea.split(",");
                String dniCliente = datosSeguro[0];
                TipoSeguro tipoSeguro = TipoSeguro.valueOf(datosSeguro[1].trim().toUpperCase());

                LocalDate fechaContratacion = LocalDate.parse(datosSeguro[2], dateFormatter);
                double costoMensual = Double.parseDouble(datosSeguro[3]);
                String estado = datosSeguro[4];

                Seguro seguro = new Seguro(tipoSeguro, fechaContratacion, costoMensual, estado);
                segurosPorCliente.computeIfAbsent(dniCliente, k -> new ArrayList<>()).add(seguro);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los seguros desde el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void cargarSegurosCliente() {
        modeloTablaSeguros.setRowCount(0);

        String clienteSeleccionado = listaClientes.getSelectedValue();
        if (clienteSeleccionado == null) return;

        String dniCliente = clienteSeleccionado.split("- DNI: ")[1];
        List<Seguro> segurosCliente = segurosPorCliente.get(dniCliente);
        double totalCosto = 0;

        if (segurosCliente != null) {
            for (Seguro seguro : segurosCliente) {
                modeloTablaSeguros.addRow(new Object[]{
                        seguro.getTipo().name(),
                        seguro.getFechaContratacion().toString(),
                        seguro.getCostoMensual(),
                        seguro.getEstado()
                });
                if (seguro.getEstado().equals("Activo")) {
                    totalCosto += seguro.getCostoMensual();
                }
            }
        }

        totalCostoSeguros.setText("Costo Total de Seguros: " + totalCosto + " €");
    }

    private void actualizarSegurosCSV() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCSVSeguros))) {
            for (Map.Entry<String, List<Seguro>> entry : segurosPorCliente.entrySet()) {
                String dniCliente = entry.getKey();
                for (Seguro seguro : entry.getValue()) {
                    String fechaContratacion = seguro.getFechaContratacion().format(dateFormatter);
                    bw.write(dniCliente + ", " + seguro.getTipo().name() + ", " + fechaContratacion + ", " +
                            seguro.getCostoMensual() + ", " + seguro.getEstado());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el archivo CSV de seguros: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void darDeBajaCliente() {
        String clienteSeleccionado = listaClientes.getSelectedValue();
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para dar de baja.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres dar de baja al cliente?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            listaOriginalClientes.removeIf(cliente -> (cliente[0] + " " + cliente[1] + " - DNI: " + cliente[2]).equals(clienteSeleccionado));
            modeloListaClientes.removeElement(clienteSeleccionado);
            actualizarClientesCSV(archivoCSVClientes);
            JOptionPane.showMessageDialog(this, "El cliente ha sido dado de baja.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarClientesDesdeCSV(String archivoCSV) {
        listaOriginalClientes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datosCliente = linea.split(",");
                if (datosCliente.length == 5) {
                    listaOriginalClientes.add(datosCliente);
                    String clienteEnLista = datosCliente[0] + "" + datosCliente[1] + " - DNI:" + datosCliente[2];
                    modeloListaClientes.addElement(clienteEnLista);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarClientesCSV(String archivoCSV) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCSV))) {
            for (String[] cliente : listaOriginalClientes) {
                bw.write(String.join(",", cliente));
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarClientes(String filtro) {
        if (filtro.isEmpty()) {
            modeloListaClientes.clear();
            for (String[] cliente : listaOriginalClientes) {
                String clienteEnLista = cliente[0] + " " + cliente[1] + " - DNI: " + cliente[2];
                modeloListaClientes.addElement(clienteEnLista);
            }
            return;
        }

        List<String[]> clientesFiltrados = new ArrayList<>();
        for (String[] cliente : listaOriginalClientes) {
            if ((cliente[0] + " " + cliente[1]).toLowerCase().contains(filtro.toLowerCase())) {
                clientesFiltrados.add(cliente);
            }
        }

        modeloListaClientes.clear();
        for (String[] cliente : clientesFiltrados) {
            String clienteEnLista = cliente[0] + " " + cliente[1] + " - DNI: " + cliente[2];
            modeloListaClientes.addElement(clienteEnLista);
        }
    }

    public static void main(String[] args) {
        new VentanaPrincipalEmpleado();
    }
}
