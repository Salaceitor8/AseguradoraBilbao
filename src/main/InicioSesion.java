package main;

import javax.swing.*;

import gui.PlaceholderPasswordField;
import gui.PlaceholderTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InicioSesion extends JFrame {

    public InicioSesion() {
        // Configuración de la ventana
        setTitle("Aseguradoras Bilbao - Inicio de Sesión");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con color de fondo azul oscuro
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(0, 51, 102)); 
        panelPrincipal.setLayout(null); 

        // Añadir el logo
        JLabel logoLabel = new JLabel();
        ImageIcon logo = new ImageIcon("fotos/logo.png");
        Image logoImage = logo.getImage();
        Image logoEscalado = logoImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logo.setImage(logoEscalado);
        logoLabel.setIcon(logo);
        logoLabel.setBounds(150, 10, 100, 100);

        // Etiqueta de título
        JLabel titulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBounds(100, 120, 200, 30);

        // Campo de texto para el usuario
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setForeground(Color.WHITE);
        usuarioLabel.setBounds(25, 170, 100, 25);
        usuarioLabel.setFont(usuarioLabel.getFont().deriveFont(Font.BOLD, 18F));
        PlaceholderTextField usuarioField = new PlaceholderTextField("Introduce el usuario");
        usuarioField.setBounds(150, 170, 200, 25);

        // Campo de texto para la contraseña
        JLabel contraseñaLabel = new JLabel("Contraseña:");
        contraseñaLabel.setForeground(Color.WHITE);
        contraseñaLabel.setBounds(25, 210, 150, 25);
        contraseñaLabel.setFont(contraseñaLabel.getFont().deriveFont(Font.BOLD, 18F));
        PlaceholderPasswordField contraseñaField = new PlaceholderPasswordField("Introduce la contraseña");
        contraseñaField.setBounds(150, 210, 200, 25);

        // Botón de iniciar sesión
        JButton iniciarSesionButton = new JButton("Iniciar Sesión");
        iniciarSesionButton.setBounds(150, 260, 200, 30);
        iniciarSesionButton.setBackground(new Color(51, 153, 255)); // Azul más claro para destacar
        iniciarSesionButton.setForeground(Color.WHITE);
        iniciarSesionButton.setFocusPainted(false);

        // Acción para el botón de inicio de sesión
        iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                String contraseña = new String(contraseñaField.getPassword());

                if (usuario.isEmpty() || contraseña.isEmpty() || 
                    usuario.equals("Introduce el usuario") || 
                    contraseña.equals("Introduce la contraseña")) {
                    JOptionPane.showMessageDialog(null, "Por favor, rellene ambos campos", "Error", JOptionPane.ERROR_MESSAGE);
                } else if(usuario.equals("Grupo14") && contraseña.equals("hola")) {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso", "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                    new VentanaPrincipalEmpleado();
                }
            }
        });

        // Añadir los componentes al panel principal
        panelPrincipal.add(logoLabel);
        panelPrincipal.add(titulo);
        panelPrincipal.add(usuarioLabel);
        panelPrincipal.add(usuarioField);
        panelPrincipal.add(contraseñaLabel);
        panelPrincipal.add(contraseñaField);
        panelPrincipal.add(iniciarSesionButton);

        // Añadir el panel a la ventana
        add(panelPrincipal);

        // Hacer visible la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        new InicioSesion();
    }
}
