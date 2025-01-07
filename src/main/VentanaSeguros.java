// Clase VentanaSeguros con ajuste automático al contenido
package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaSeguros extends JFrame {

    public VentanaSeguros() {
        setTitle("Tipos de Seguros");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Configurar el CardLayout
        JPanel panelPrincipal = new JPanel(new CardLayout());

        // Panel Seguro de Vida
        JPanel panelVida = crearPanelConComboBox("Seguro de Vida", new String[]{"Fallecimiento", "Fallecimiento e Invalidez"});
        panelPrincipal.add(panelVida, "Vida");

        // Panel Seguro de Coche
        JPanel panelCoche = crearPanelConComboBox("Seguro de Coche", new String[]{"A Terceros", "A Todo Riesgo"});
        panelPrincipal.add(panelCoche, "Coche");

        // Panel Seguro de Hogar
        JPanel panelHogar = crearPanelConComboBox("Seguro de Hogar", new String[]{"Cobertura Estándar", "Cobertura Plus"});
        panelPrincipal.add(panelHogar, "Hogar");

        // Panel de Selección
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSeleccion.setBackground(new Color(0, 51, 102));

        JButton botonVida = crearBotonGrande("Vida", e -> mostrarPanel(panelPrincipal, "Vida"));
        JButton botonCoche = crearBotonGrande("Coche", e -> mostrarPanel(panelPrincipal, "Coche"));
        JButton botonHogar = crearBotonGrande("Hogar", e -> mostrarPanel(panelPrincipal, "Hogar"));

        panelSeleccion.add(botonVida);
        panelSeleccion.add(botonCoche);
        panelSeleccion.add(botonHogar);

        // Configurar el Layout principal
        setLayout(new BorderLayout());
        add(panelSeleccion, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);

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
            new VentanaSeguros().setVisible(true);
        });
    }
}
