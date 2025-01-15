package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VentanaSeleccionSeguro extends JDialog {
    private String seguroSeleccionado;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public VentanaSeleccionSeguro(VentanaCliente parent, String dniCliente, Bdd baseDeDatos, DefaultTableModel t) {
        super(parent, "Seleccionar Seguro", true); // Llama al constructor de JDialog
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Opciones de seguros
        String[] opcionesSeguros = {"Seguro de Vida", "Seguro de Vivienda", "Seguro de Coche"};

        // Layout y componentes
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JLabel label = new JLabel("Selecciona el tipo de seguro que deseas:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);

        JComboBox<String> comboSeguros = new JComboBox<>(opcionesSeguros);
        panel.add(comboSeguros);

        JButton btnConfirmar = new JButton("Confirmar");
        JButton btnCancelar = new JButton("Cancelar");
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        panel.add(panelBotones);

        add(panel);

        // Acción del botón confirmar
        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seguroSeleccionado = (String) comboSeguros.getSelectedItem();
                guardarSeguroGratis(dniCliente, seguroSeleccionado, baseDeDatos, t);
                JOptionPane.showMessageDialog(VentanaSeleccionSeguro.this,
                        "Has seleccionado: " + seguroSeleccionado + "\nEste seguro será completamente gratuito.",
                        "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
            }
        });
        

        // Acción del botón cancelar
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    
    
    private void guardarSeguroGratis(String dniCliente, String seguroSeleccionado, Bdd baseDeDatos, DefaultTableModel t) {
        try {
            String query = "INSERT INTO seguros (dni_cliente, tipo_seguro, fecha_inicio, costo, estado, cobertura) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = baseDeDatos.connection.prepareStatement(query); // Usa connection directamente
            if(seguroSeleccionado.equals("Seguro de Vida")) {
            	seguroSeleccionado = "VIDA";
            }else if(seguroSeleccionado.equals("Seguro de Coche")) {
            	seguroSeleccionado = "COCHE";
            }else {
            	seguroSeleccionado = "VIVIENDA";
            }
            stmt.setString(1, dniCliente);
            stmt.setString(2, seguroSeleccionado);
            stmt.setString(3, LocalDate.now().format(formatter));          
            stmt.setDouble(4, 0.0); // Precio gratuito
            stmt.setString(5, "Activo");
            stmt.setString(6, "BASICO");
            stmt.executeUpdate();
            Object[] o = new Object[] {
        			seguroSeleccionado,
        			LocalDate.now().format(formatter),
        			0.0,
        			"Activo", 
        			"BASICO"
        	};
            t.addRow(o);;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar el seguro seleccionado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public String getSeguroSeleccionado() {
        return seguroSeleccionado;
    }
}


