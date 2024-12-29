package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import domain.Seguro;
import domain.TipoSeguro;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VentanaAltaSeguro extends JFrame {

    private JComboBox<TipoSeguro> comboTipoSeguro;
    private JDateChooser campoFechaContratacion;
    private JTextField campoCostoAnual;
    private JComboBox<String> comboEstado;
    private JButton btnGuardar, btnCancelar;

    private DefaultTableModel modeloTablaSeguros;
    private String dniCliente;
    private final Bdd baseDeDatos;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor de la ventana
    public VentanaAltaSeguro(DefaultTableModel modeloTablaSeguros, String dniCliente, Bdd baseDeDatos) {
        this.modeloTablaSeguros = modeloTablaSeguros;
        this.dniCliente = dniCliente;
        this.baseDeDatos = baseDeDatos;

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
        JLabel etiquetaFecha = new JLabel("Fecha de Contratación (dd/MM/yyyy):");
        etiquetaFecha.setForeground(colorContraste);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(etiquetaFecha, gbc);

        campoFechaContratacion = new JDateChooser();
        campoFechaContratacion.setDateFormatString("dd/MM/yyyy");
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
            String fechaContratacion = LocalDate.now().format(formatter); // Usar la fecha actual
            double costo = Double.parseDouble(campoCostoAnual.getText());
            String estado = "Activo"; // Siempre activo al crear el seguro

            // Guardar el nuevo seguro en la base de datos
            baseDeDatos.insertarSeguro(dniCliente, tipo.name(), fechaContratacion, costo, estado);

            // Añadir el nuevo seguro al modelo de la tabla
            modeloTablaSeguros.addRow(new Object[]{
                tipo.name(),
                fechaContratacion.toString(),
                costo,
                estado
            });

            JOptionPane.showMessageDialog(this, "Seguro guardado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Cerrar la ventana
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un costo válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el seguro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
