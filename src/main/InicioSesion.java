package main;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import javax.swing.*;

import domain.Seguro;

public class InicioSesion extends JFrame {

    private static final Color COLOR_PRINCIPAL = new Color(0, 51, 102); // Azul oscuro
    private static final Color COLOR_CONTRASTE = Color.WHITE;
    private CardLayout cardLayout;
    private JPanel panelCentral;
    private JTextField campoUsuario;
    private JPasswordField campoContraseña;
    private boolean esEmpleado; // Para determinar si es empleado o cliente
    private final Bdd baseDeDatos; // Instancia de la base de datos
    final Color COLOR_NORMAL = COLOR_PRINCIPAL;
    final Color COLOR_RESALTADO = new Color(51, 102, 204); // Azul más claro

    public InicioSesion() {
        baseDeDatos = new Bdd("resources/db/aseguradora.db"); // Conecta a la base de datos SQLite

        setTitle("Inicio de Sesión - Aseguradoras Bilbaaaao");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración principal
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        // Crear las tarjetas
        JPanel panelSeleccion = crearPanelSeleccion();
        JPanel panelLogin = crearPanelLogin();

        // Añadir tarjetas al panel central
        panelCentral.add(panelSeleccion, "Seleccion");
        panelCentral.add(panelLogin, "Login");

        add(panelCentral);
        setVisible(true);
    }

    private JPanel crearPanelSeleccion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRINCIPAL);

        JLabel etiquetaTitulo = new JLabel("Haz click en su tipo de usuario", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaTitulo.setForeground(COLOR_CONTRASTE);
        panel.add(etiquetaTitulo, BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 10));
        botones.setBackground(COLOR_PRINCIPAL);

        JButton btnEmpleado = new JButton("Soy un empleado");
        btnEmpleado.setBackground(COLOR_PRINCIPAL);
        btnEmpleado.setForeground(COLOR_CONTRASTE);
        btnEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEmpleado.setBackground(COLOR_RESALTADO);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEmpleado.setBackground(COLOR_NORMAL);
            }
        });
        Image empleado = (new ImageIcon("fotos/empleado.png")).getImage().getScaledInstance(50, 50, DO_NOTHING_ON_CLOSE);
        ImageIcon iconoEmpleado = new ImageIcon(empleado);
        btnEmpleado.setIcon(iconoEmpleado);
        btnEmpleado.setBorderPainted(false);
        btnEmpleado.addActionListener(e -> {
            esEmpleado = true;
            limpiarCampos();
            cardLayout.show(panelCentral, "Login");
        });

        JButton btnCliente = new JButton("Soy un cliente");
        btnCliente.setBackground(COLOR_PRINCIPAL);
        btnCliente.setForeground(COLOR_CONTRASTE);
        btnCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCliente.setBackground(COLOR_RESALTADO);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCliente.setBackground(COLOR_NORMAL);
            }
        });
        Image cliente = (new ImageIcon("fotos/cliente.png")).getImage().getScaledInstance(50, 50, DO_NOTHING_ON_CLOSE);
        ImageIcon iconoCliente = new ImageIcon(cliente);
        btnCliente.setIcon(iconoCliente);
        btnCliente.setBorderPainted(false);
        btnCliente.addActionListener(e -> {
            esEmpleado = false;
            limpiarCampos();
            cardLayout.show(panelCentral, "Login");
        });

        botones.add(btnEmpleado);
        botones.add(btnCliente);
        panel.add(botones, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(COLOR_PRINCIPAL);

        JLabel etiquetaUsuario = new JLabel("Usuario:");
        etiquetaUsuario.setForeground(COLOR_CONTRASTE);
        campoUsuario = new JTextField();

        JLabel etiquetaContraseña = new JLabel("Contraseña:");
        etiquetaContraseña.setForeground(COLOR_CONTRASTE);
        campoContraseña = new JPasswordField();
        campoUsuario.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					campoContraseña.requestFocus();
				}
			}
        	
		});

        JButton btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setBackground(new Color(51, 153, 255));
        btnIniciarSesion.setForeground(COLOR_CONTRASTE);
        btnIniciarSesion.addActionListener(e -> iniciarSesion());
        campoContraseña.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnIniciarSesion.doClick();
				}
			}
        	
		});

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(Color.RED);
        btnRegresar.setForeground(COLOR_CONTRASTE);
        btnRegresar.addActionListener(e -> cardLayout.show(panelCentral, "Seleccion"));
        campoContraseña.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					btnRegresar.doClick();
				}
			}
        	
        	
		});
        campoUsuario.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					btnRegresar.doClick();
				}
			}
        	
        	
		});

        panel.add(etiquetaUsuario);
        panel.add(campoUsuario);
        panel.add(etiquetaContraseña);
        panel.add(campoContraseña);
        panel.add(btnIniciarSesion);
        panel.add(btnRegresar);
        return panel;
    }

    private void iniciarSesion() {
        String usuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());

        if (esEmpleado) {
            if ("Empleado1234".equals(usuario) && "hola".equals(contraseña)) {
                JOptionPane.showMessageDialog(this, "Bienvenido, empleado.");
                dispose();
                new VentanaPrincipalEmpleado(baseDeDatos);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try {
                ResultSet rs = baseDeDatos.obtenerClientes();
                boolean encontrado = false;
                while (rs.next()) {
                    String dni = rs.getString("dni");
                    String nombre = rs.getString("nombre");
                    String apellidos = rs.getString("apellidos");
                    if ((nombre + "_" + apellidos).equals(usuario) && dni.equals(contraseña)) {
                        JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario + ".");
                        dispose();
                        new VentanaCliente(usuario, (ArrayList<Seguro>) baseDeDatos.obtenerSeguros(contraseña));
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al iniciar sesión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        campoUsuario.setText("");
        campoContraseña.setText("");
    }

    public static void main(String[] args) {
        new InicioSesion();
    }
}
