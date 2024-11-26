package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import domain.Seguro;
import domain.TipoSeguro;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaAltaSeguro extends JFrame {

    private JComboBox<TipoSeguro> comboTipoSeguro;
    private JTextField campoFechaContratacion;
    private JTextField campoCostoAnual;
    private JComboBox<String> comboEstado;
    private JButton btnGuardar, btnCancelar;
    
    private DefaultTableModel modeloTablaSeguros;
    private String dniCliente;
    private String archivoCSVSeguros;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Constructor de la ventana
    public VentanaAltaSeguro(DefaultTableModel modeloTablaSeguros, String dniCliente, String archivoCSVSeguros) {
        this.modeloTablaSeguros = modeloTablaSeguros;
        this.dniCliente = dniCliente;
        this.archivoCSVSeguros = archivoCSVSeguros;

        // Configuración básica de la ventana
        setTitle("Alta de Seguro");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana
        setResizable(false); // No se puede redimensionar

        // Colores personalizados
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorContraste = new Color(255, 255, 255); // Blanco

        // Panel principal
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(colorPrincipal);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Etiqueta y combo box para tipo de seguro
        JLabel etiquetaTipo = new JLabel("Tipo de Seguro:");
        etiquetaTipo.setForeground(colorContraste);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(etiquetaTipo, gbc);

        comboTipoSeguro = new JComboBox<>(TipoSeguro.values());
        gbc.gridx = 1;
        panelPrincipal.add(comboTipoSeguro, gbc);

        // Etiqueta y campo para la fecha de contratación
        JLabel etiquetaFecha = new JLabel("Fecha de Contratación (yyyy-MM-dd):");
        etiquetaFecha.setForeground(colorContraste);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(etiquetaFecha, gbc);

        campoFechaContratacion = new JTextField(10);
        campoFechaContratacion.setText(LocalDate.now().toString()); // Fecha actual por defecto
        campoFechaContratacion.setEditable(false); // No editable porque es la fecha actual
        gbc.gridx = 1;
        panelPrincipal.add(campoFechaContratacion, gbc);

        // Etiqueta y campo para el costo anual
        JLabel etiquetaCosto = new JLabel("Costo Anual:");
        etiquetaCosto.setForeground(colorContraste);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(etiquetaCosto, gbc);

        campoCostoAnual = new JTextField(10);
        gbc.gridx = 1;
        panelPrincipal.add(campoCostoAnual, gbc);

        // Etiqueta y combo box para el estado del seguro
        JLabel etiquetaEstado = new JLabel("Estado:");
        etiquetaEstado.setForeground(colorContraste);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelPrincipal.add(etiquetaEstado, gbc);

        comboEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        comboEstado.setSelectedItem("Activo"); // Siempre por defecto "Activo"
        comboEstado.setEnabled(false); // No se puede cambiar, siempre activo al crear
        gbc.gridx = 1;
        panelPrincipal.add(comboEstado, gbc);

        // Botones de Guardar y Cancelar
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(colorPrincipal);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(51, 153, 255)); // Azul claro
        btnGuardar.setForeground(Color.WHITE);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.WHITE);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelPrincipal.add(panelBotones, gbc);

        // Acción del botón Cancelar
        btnCancelar.addActionListener(e -> dispose());

        // Acción del botón Guardar
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarSeguro();
            }
        });

        // Añadir el panel a la ventana
        add(panelPrincipal);
        setVisible(true);
    }

    // Método para guardar el nuevo seguro
    private void guardarSeguro() {
        try {
            TipoSeguro tipo = (TipoSeguro) comboTipoSeguro.getSelectedItem();
            LocalDate fechaContratacion = LocalDate.now(); // Usar la fecha actual
            double costo = Double.parseDouble(campoCostoAnual.getText());
            String estado = "Activo"; // Siempre activo al crear el seguro

            // Crear un nuevo objeto Seguro y añadirlo a la tabla
            Seguro nuevoSeguro = new Seguro(tipo, fechaContratacion, costo, estado);

            // Añadir el nuevo seguro al modelo de la tabla
            modeloTablaSeguros.addRow(new Object[]{
                    nuevoSeguro.getTipo().name(),
                    nuevoSeguro.getFechaContratacion().toString(),
                    nuevoSeguro.getCostoMensual(),
                    nuevoSeguro.getEstado()
            });

            // Guardar el nuevo seguro en el archivo CSV
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCSVSeguros, true))) {
                bw.write(dniCliente + "," + tipo.name() + "," + fechaContratacion + "," + costo + "," + estado);
                bw.newLine();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el seguro en el archivo CSV: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, "Seguro guardado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Cerrar la ventana
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un costo válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
