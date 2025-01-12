// Clase VentanaSeguros con ajuste automático al contenido
package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import domain.Seguro;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VentanaSeguros extends JFrame {

    public VentanaSeguros(int id, String dniCliente,String tipo, String fecha, double costo, String estado, String cobertura, Bdd bd) {
        setTitle("Tipos de Seguros");

        // Configurar el CardLayout
        JPanel panelPrincipal = new JPanel(new CardLayout());
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 51, 102));

        JLabel labelTitulo;
        JComboBox<String> comboBox;
        if(tipo == "VIDA"){
        	// Panel Seguro de Vida
            labelTitulo = new JLabel("Seguro de Vida");
            comboBox = new JComboBox<>(new String[]{"Fallecimiento", "Fallecimiento y Invalidez"});
        }else if(tipo == "COCHE") {
        	// Panel Seguro de Coche
        	labelTitulo = new JLabel("Seguro de Coche");
            comboBox = new JComboBox<>(new String[]{"A Terceros", "A Todo Riesgo"});
        }else {
        	// Panel Seguro de Hogar
        	labelTitulo = new JLabel("Seguro de Vivienda");
            comboBox = new JComboBox<>(new String[]{"Cobertura Estándar", "Cobertura Plus"});
        }
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(labelTitulo, BorderLayout.NORTH);

        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(0, 51, 102));
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setPreferredSize(new Dimension(200, 30)); 
        JPanel comboPanel = new JPanel();
        comboPanel.setBackground(new Color(0, 51, 102));
        comboPanel.add(comboBox);
        panel.add(comboPanel, BorderLayout.CENTER);
        panelPrincipal.add(panel);


        // Panel de Selección
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSeleccion.setBackground(new Color(0, 51, 102));

//        JButton botonVida = crearBotonGrande("Vida", e -> mostrarPanel(panelPrincipal, "Vida"));
//        JButton botonCoche = crearBotonGrande("Coche", e -> mostrarPanel(panelPrincipal, "Coche"));
//        JButton botonHogar = crearBotonGrande("Vivienda", e -> mostrarPanel(panelPrincipal, "Vivienda"));
        
        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(0, 51, 102));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(51, 153, 255));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.addActionListener(e -> {
        	System.out.println(id);
        	System.out.println(tipo);
        	System.out.println(fecha);
        	System.out.println(costo);
        	System.out.println(estado);
        	System.out.println(comboBox.getSelectedItem());
        	
        	String mensajeConfirmacion = String.format(
                    "Por favor, confirma que quieres hacer el cambio"
                );
        	
        	// Mostrar cuadro de diálogo de confirmación
            int confirmacion = JOptionPane.showConfirmDialog(
                panelPrincipal, mensajeConfirmacion, "Confirmación de Datos", JOptionPane.YES_NO_OPTION
            );
            
            if(confirmacion == JOptionPane.YES_OPTION) {
            	try {
            		if(comboBox.getSelectedItem().equals("Fallecimiento")) {
                		bd.actualizarSeguro(id, tipo, fecha, costo, estado, "FALLECIMIENTO");
                		dispose();
                	}else if(comboBox.getSelectedItem().equals("Fallecimiento y Invalidez")) {
                		bd.actualizarSeguro(id, tipo, fecha, costo, estado, "FYINVALIDEZ");
                		dispose();
                	}else if(comboBox.getSelectedItem().equals("Cobertura Plus")) {
                		bd.actualizarSeguro(id, tipo, fecha, costo, estado, "PLUS");
                		dispose();
                	}else if(comboBox.getSelectedItem().equals("Cobertura Estándar")) {
                		bd.actualizarSeguro(id, tipo, fecha, costo, estado, "ESTANDAR");
                		dispose();
                	}else if(comboBox.getSelectedItem().equals("A Terceros")) {
                		bd.actualizarSeguro(id, tipo, fecha, costo, estado, "TERCEROS");
                		dispose();
                	}else{
                		bd.actualizarSeguro(id, tipo, fecha, costo, estado, "TODORIESGO");
                		dispose();
                		
                	}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(panelPrincipal, "Error al guardar el cliente: " + e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
            }
        	
        	
        	
        	
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelPrincipal);
        setVisible(true);

//        panelSeleccion.add(botonVida);
//        panelSeleccion.add(botonCoche);
//        panelSeleccion.add(botonHogar);

        // Configurar el Layout principal
        setLayout(new BorderLayout());
        add(panelSeleccion, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        pack(); // Ajustar el tamaño de la ventana al contenido
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
    }

    private JPanel crearPanelConComboBox(String titulo, String[] opciones) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 51, 102));

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(labelTitulo, BorderLayout.NORTH);

        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(0, 51, 102));
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setPreferredSize(new Dimension(200, 30)); 
        JPanel comboPanel = new JPanel();
        comboPanel.setBackground(new Color(0, 51, 102));
        comboPanel.add(comboBox);
        panel.add(comboPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton crearBotonGrande(String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(0, 51, 102));
        boton.setFont(new Font("Arial", Font.BOLD, 18)); 
        boton.setPreferredSize(new Dimension(150, 50)); 
        boton.addActionListener(accion);
        return boton;
    }

    private void mostrarPanel(JPanel panelPrincipal, String nombrePanel) {
        CardLayout cl = (CardLayout) panelPrincipal.getLayout();
        cl.show(panelPrincipal, nombrePanel);
        pack(); // Ajustar la ventana al contenido del panel actual
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            new VentanaSeguros().setVisible(true);
        });
    }
}