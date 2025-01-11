package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
import domain.Seguro;
import gui.BarraProgreso;

public class InicioSesion extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
        panelCentral.add(crearPanelSeleccion(), "Seleccion");
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
        
        iniciarAnimacionColor(etiquetaTitulo);

        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 10));
        botones.setBackground(COLOR_PRINCIPAL);

        JButton btnEmpleado = new JButton("Soy un empleado");
        btnEmpleado.setBackground(COLOR_PRINCIPAL);
        btnEmpleado.setForeground(COLOR_CONTRASTE);
        btnEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                new Thread(() -> {
                    for (int i = 0; i <= 255; i += 5) {
                        int intensidad = i; // Necesario para la lambda
                        SwingUtilities.invokeLater(() -> btnEmpleado.setBackground(new Color(
                            COLOR_NORMAL.getRed() + (COLOR_RESALTADO.getRed() - COLOR_NORMAL.getRed()) * intensidad / 255,
                            COLOR_NORMAL.getGreen() + (COLOR_RESALTADO.getGreen() - COLOR_NORMAL.getGreen()) * intensidad / 255,
                            COLOR_NORMAL.getBlue() + (COLOR_RESALTADO.getBlue() - COLOR_NORMAL.getBlue()) * intensidad / 255
                        )));
                        try {
                            Thread.sleep(5); // Velocidad de la animación
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                new Thread(() -> {
                    for (int i = 255; i >= 0; i -= 5) {
                        int intensidad = i; // Necesario para la lambda
                        SwingUtilities.invokeLater(() -> btnEmpleado.setBackground(new Color(
                            COLOR_NORMAL.getRed() + (COLOR_RESALTADO.getRed() - COLOR_NORMAL.getRed()) * intensidad / 255,
                            COLOR_NORMAL.getGreen() + (COLOR_RESALTADO.getGreen() - COLOR_NORMAL.getGreen()) * intensidad / 255,
                            COLOR_NORMAL.getBlue() + (COLOR_RESALTADO.getBlue() - COLOR_NORMAL.getBlue()) * intensidad / 255
                        )));
                        try {
                            Thread.sleep(5); // Velocidad de la animación
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
                new Thread(() -> {
                    for (int i = 0; i <= 255; i += 5) {
                        int intensidad = i;
                        SwingUtilities.invokeLater(() -> btnCliente.setBackground(new Color(
                            COLOR_NORMAL.getRed() + (COLOR_RESALTADO.getRed() - COLOR_NORMAL.getRed()) * intensidad / 255,
                            COLOR_NORMAL.getGreen() + (COLOR_RESALTADO.getGreen() - COLOR_NORMAL.getGreen()) * intensidad / 255,
                            COLOR_NORMAL.getBlue() + (COLOR_RESALTADO.getBlue() - COLOR_NORMAL.getBlue()) * intensidad / 255
                        )));
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                new Thread(() -> {
                    for (int i = 255; i >= 0; i -= 5) {
                        int intensidad = i;
                        SwingUtilities.invokeLater(() -> btnCliente.setBackground(new Color(
                            COLOR_NORMAL.getRed() + (COLOR_RESALTADO.getRed() - COLOR_NORMAL.getRed()) * intensidad / 255,
                            COLOR_NORMAL.getGreen() + (COLOR_RESALTADO.getGreen() - COLOR_NORMAL.getGreen()) * intensidad / 255,
                            COLOR_NORMAL.getBlue() + (COLOR_RESALTADO.getBlue() - COLOR_NORMAL.getBlue()) * intensidad / 255
                        )));
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(COLOR_PRINCIPAL);

        // Etiqueta y campo de usuario
        JLabel etiquetaUsuario = new JLabel("Usuario:");
        etiquetaUsuario.setForeground(COLOR_CONTRASTE);
        campoUsuario = new JTextField();

        // Etiqueta y campo de contraseña
        JLabel etiquetaContraseña = new JLabel("Contraseña:");
        etiquetaContraseña.setForeground(COLOR_CONTRASTE);
        campoContraseña = new JPasswordField();
        campoUsuario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    campoContraseña.requestFocus();
                }
            }
        });

        // Panel para la contraseña y botón de mostrar contraseña
        JPanel panelContraseña = new JPanel(new BorderLayout());
        panelContraseña.setBackground(COLOR_PRINCIPAL);
        JButton btnVerContraseña = new JButton("👁");
        btnVerContraseña.setPreferredSize(new Dimension(30, campoContraseña.getPreferredSize().height));
        btnVerContraseña.setFocusPainted(false);
        btnVerContraseña.setMargin(new Insets(0, 0, 0, 0));
        btnVerContraseña.addActionListener(e -> {
            if (campoContraseña.getEchoChar() == '\u2022') {
                campoContraseña.setEchoChar((char) 0);
            } else {
                campoContraseña.setEchoChar('\u2022');
            }
        });
        panelContraseña.add(campoContraseña, BorderLayout.CENTER);
        panelContraseña.add(btnVerContraseña, BorderLayout.EAST);

        // Captcha
        JLabel etiquetaCaptcha = new JLabel("Ingrese el Captcha:");
        etiquetaCaptcha.setForeground(COLOR_CONTRASTE);
        JTextField campoCaptcha = new JTextField();
        JLabel labelCaptcha = new JLabel();
        labelCaptcha.setHorizontalAlignment(SwingConstants.CENTER);
        generarCaptcha(labelCaptcha);

        JButton btnRecargarCaptcha = new JButton("⟳");
        btnRecargarCaptcha.setPreferredSize(new Dimension(40, campoCaptcha.getPreferredSize().height));
        btnRecargarCaptcha.addActionListener(e -> generarCaptcha(labelCaptcha));

        JPanel panelCaptcha = new JPanel(new BorderLayout());
        panelCaptcha.add(labelCaptcha, BorderLayout.CENTER);
        panelCaptcha.add(btnRecargarCaptcha, BorderLayout.EAST);

        // Botón de iniciar sesión
        JButton btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setBackground(new Color(51, 153, 255));
        btnIniciarSesion.setForeground(COLOR_CONTRASTE);
        btnIniciarSesion.addActionListener(e -> {
            if (verificarCaptcha(campoCaptcha, labelCaptcha.getText())) {
                iniciarSesion();
            } else {
                JOptionPane.showMessageDialog(this, "Captcha incorrecto. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                generarCaptcha(labelCaptcha);
            }
        });

        campoContraseña.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnIniciarSesion.doClick();
                }
            }
        });

        // Botón de recuperar contraseña
        JButton btnRecuperarContraseña = new JButton("Recuperar Contraseña");
        btnRecuperarContraseña.setForeground(COLOR_CONTRASTE);
        btnRecuperarContraseña.setBackground(Color.GRAY);
        btnRecuperarContraseña.setFocusPainted(false);
        btnRecuperarContraseña.setMargin(new Insets(5, 15, 5, 15));
        btnRecuperarContraseña.addActionListener(e -> {
            VentanaRecuperarContraseña ventanaRecuperar = new VentanaRecuperarContraseña(baseDeDatos);
            ventanaRecuperar.setVisible(true);
        });

        // Botón de regresar
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(Color.RED);
        btnRegresar.setForeground(COLOR_CONTRASTE);
        btnRegresar.addActionListener(e -> cardLayout.show(panelCentral, "Seleccion"));
        campoContraseña.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    btnRegresar.doClick();
                }
            }
        });

        // Añadir componentes al panel
        panel.add(etiquetaUsuario);
        panel.add(campoUsuario);
        panel.add(etiquetaContraseña);
        panel.add(panelContraseña);
        panel.add(etiquetaCaptcha);
        panel.add(panelCaptcha);
        panel.add(btnIniciarSesion);
        panel.add(btnRegresar);
        panel.add(btnRecuperarContraseña);

        return panel;
    }

    private void iniciarSesion() {
        String usuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());
        
        if (esEmpleado) {
        	try {
        	    ResultSet rs = baseDeDatos.obtenerEmpleados();
        	    boolean encontrado = false;
        	    while (rs.next()) {
        	        String dni = rs.getString("dni");
        	        String nombre = rs.getString("nombre");
        	        String apellidos = rs.getString("apellidos");

        	        // Verificar si el usuario y contraseña están configurados
        	        if (baseDeDatos.cargarContraseñaDesdeBDempleados(dni) == null) {
        	            if (baseDeDatos.cargarUsuarioDesdeBDempleados(dni) == null) {
        	                // Caso 1: Usuario y contraseña no configurados, usar nombre_apellidos y dni como credenciales
        	                if ((nombre + "_" + apellidos).equals(usuario) && dni.equals(contraseña)) {
        	                	
        	                
        	                	if(baseDeDatos.obtenerGeneroEmpleado(dni).equals("H")) {
        	                		// Mostrar ventana de Captcha
        	                	    VentanaVerificacionCaptcha ventanaCaptcha = new VentanaVerificacionCaptcha();
        	                	    if (!ventanaCaptcha.isCaptchaResuelto()) {
        	                	        JOptionPane.showMessageDialog(this, "Debe resolver el Captcha para continuar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        	                	        return;
        	                	    }
        	                	    // Mostrar barra de progreso
        	                	    BarraProgreso progressBarDialog = new BarraProgreso(this);
        	                	    progressBarDialog.setVisible(true);
        	                	    SwingUtilities.invokeLater(() -> {
        	                	        JOptionPane.showMessageDialog(this, "Bienvenido, " + nombre + ".");
        	                	        dispose();
        	                	        new VentanaPrincipalEmpleado(baseDeDatos, nombre);
        	                	    });
        	                	    encontrado = true;
        	                	    break;
            	                    

            	                    
            	                
        	                	}else {
        	                		// Mostrar ventana de Captcha
        	                	    VentanaVerificacionCaptcha ventanaCaptcha = new VentanaVerificacionCaptcha();
        	                	    if (!ventanaCaptcha.isCaptchaResuelto()) {
        	                	        JOptionPane.showMessageDialog(this, "Debe resolver el Captcha para continuar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        	                	        return;
        	                	    }
        	                	 // Mostrar barra de progreso
        	                	    BarraProgreso progressBarDialog = new BarraProgreso(this);
        	                	    progressBarDialog.setVisible(true);
        	                	    SwingUtilities.invokeLater(() -> {
        	                	        JOptionPane.showMessageDialog(this, "Bienvenida, " + nombre + ".");
        	                	        dispose();
        	                	        new VentanaPrincipalEmpleado(baseDeDatos, nombre);
        	                	    });
        	                	    encontrado = true;
        	                	    break;
            	                    

        	                	}
        	                	
        	                }
        	            } else {
        	                // Caso 2: Usuario configurado pero contraseña no configurada
        	                if (baseDeDatos.cargarUsuarioDesdeBDempleados(dni).equals(usuario) && dni.equals(contraseña)) {
        	                	if(baseDeDatos.obtenerGeneroEmpleado(dni).equals("H")) {
        	                        // Mostrar la barra de progreso
        	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
        	                        progressBarDialog.setVisible(true); // Muestra la barra
        	                        // Al completar, abrir la ventana principal
        	                        SwingUtilities.invokeLater(() -> {
            	                		JOptionPane.showMessageDialog(this, "Bienvenido, " + nombre + ".");
        	                        	dispose();
        	                        	new VentanaPrincipalEmpleado(baseDeDatos, nombre);
        	                        	});
        	                        encontrado = true;
            	                    break;
        	                	}else {
        	                        // Mostrar la barra de progreso
        	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
        	                        progressBarDialog.setVisible(true); // Muestra la barra
        	                        // Al completar, abrir la ventana principal
        	                        SwingUtilities.invokeLater(() -> {
            	                		JOptionPane.showMessageDialog(this, "Bienvenida, " + nombre + ".");
        	                        	dispose();
        	                        	new VentanaPrincipalEmpleado(baseDeDatos, nombre);
        	                        	});
        	                        encontrado = true;
            	                    break;
        	                	}
        	                }
        	            }
        	        } else {
        	            // Caso 3: Usuario y contraseña configurados
        	            if (baseDeDatos.cargarUsuarioDesdeBDempleados(dni) != null) {
        	                if (baseDeDatos.cargarUsuarioDesdeBDempleados(dni).equals(usuario) &&
        	                        baseDeDatos.cargarContraseñaDesdeBDempleados(dni).equals(contraseña)) {
        	                	if(baseDeDatos.obtenerGeneroEmpleado(dni).equals("H")) {
        	                	// Mostrar la barra de progreso
        	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
        	                        progressBarDialog.setVisible(true); // Muestra la barra
        	                        // Al completar, abrir la ventana principal
        	                        SwingUtilities.invokeLater(() -> {
            	                		JOptionPane.showMessageDialog(this, "Bienvenido, " + nombre + ".");
        	                        	dispose();
        	                        	new VentanaPrincipalEmpleado(baseDeDatos, nombre);
        	                        	});
        	                        encontrado = true;
            	                    break;
        	                        
        	                	}else {
        	                        // Mostrar la barra de progreso
        	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
        	                        progressBarDialog.setVisible(true); // Muestra la barra
        	                        // Al completar, abrir la ventana principal
        	                        SwingUtilities.invokeLater(() -> {
            	                		JOptionPane.showMessageDialog(this, "Bienvenida, " + nombre + ".");
        	                        	dispose();
        	                        	new VentanaPrincipalEmpleado(baseDeDatos, nombre);
        	                        	});
        	                        encontrado = true;
            	                    break;
        	                	}
        	                }
        	            } else {
        	                // Caso 4: Usuario no configurado pero contraseña configurada
        	                if ((nombre + "_" + apellidos).equals(usuario) &&
        	                        baseDeDatos.cargarContraseñaDesdeBDempleados(dni).equals(contraseña)) {
        	                	if(baseDeDatos.obtenerGeneroEmpleado(dni).equals("H")) {
        	                		// Mostrar la barra de progreso
        	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
        	                        progressBarDialog.setVisible(true); // Muestra la barra
        	                        // Al completar, abrir la ventana principal
        	                        SwingUtilities.invokeLater(() -> {
            	                		JOptionPane.showMessageDialog(this, "Bienvenido, " + nombre + ".");
        	                        	dispose();
        	                        	new VentanaPrincipalEmpleado(baseDeDatos, nombre);
        	                        	});
        	                        encontrado = true;
            	                    break;
        	                	}else {
        	                        // Mostrar la barra de progreso
        	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
        	                        progressBarDialog.setVisible(true); // Muestra la barra
        	                        // Al completar, abrir la ventana principal
        	                        SwingUtilities.invokeLater(() -> {
            	                		JOptionPane.showMessageDialog(this, "Bienvenida, " + nombre + ".");
        	                        	dispose();
        	                        	new VentanaPrincipalEmpleado(baseDeDatos, nombre);
        	                        	});
        	                        encontrado = true;
            	                    break;
        	                	}
        	                }
        	            }
        	        }
        	    }

        	    if (!encontrado) {
        	        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        	    }
        	} catch (SQLException e) {
        	    JOptionPane.showMessageDialog(this, "Error al validar el usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        	    e.printStackTrace();
        	}

        } else {
            try {
                ResultSet rs = baseDeDatos.obtenerClientes();
                boolean encontrado = false;
                while (rs.next()) {
                    String dni = rs.getString("dni");
                    String nombre = rs.getString("nombre");
                    String apellidos = rs.getString("apellidos");
                    if(baseDeDatos.cargarContraseñaDesdeBDclientes(dni) == null) {
                    	if (baseDeDatos.cargarUsuarioDesdeBDclientes(dni) == null) {
                    		if ((nombre + "_" + apellidos).equals(usuario) && dni.equals(contraseña)) {
                    			if(baseDeDatos.obtenerGeneroCliente(dni).equals("H")) {
                    				// Mostrar ventana de Captcha
                    			    VentanaVerificacionCaptcha ventanaCaptcha = new VentanaVerificacionCaptcha();
                    			    if (!ventanaCaptcha.isCaptchaResuelto()) {
                    			        JOptionPane.showMessageDialog(this, "Debe resolver el Captcha para continuar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    			        return;
                    			    }
        	                        // Mostrar la barra de progreso
                    			    String ultimoInicio = baseDeDatos.obtenerUltimoInicioCliente(dni);
                    			    BarraProgreso progressBarDialog = new BarraProgreso(this);
                    			    progressBarDialog.setVisible(true);
                    			    SwingUtilities.invokeLater(() -> {
                    			        baseDeDatos.actualizarUltimoInicioCliente(dni, LocalDate.now().toString());
                    			        JOptionPane.showMessageDialog(this, "Bienvenido, " + nombre + ".");
                    			        dispose();
                    			        new VentanaCliente(nombre, (ArrayList<Seguro>) baseDeDatos.obtenerSeguros(contraseña), baseDeDatos, dni, "H");
                    			    });
                    			    encontrado = true;
                    			    break;
                    			
                    			    
                                    
                                    
                    			}else {
                    				String ultimoInicio = baseDeDatos.obtenerUltimoInicioCliente(dni);
                    				// Mostrar ventana de Captcha
                    			    VentanaVerificacionCaptcha ventanaCaptcha = new VentanaVerificacionCaptcha();
                    			    if (!ventanaCaptcha.isCaptchaResuelto()) {
                    			        JOptionPane.showMessageDialog(this, "Debe resolver el Captcha para continuar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    			        return;
                    			    }
        	                        // Mostrar la barra de progreso
        	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
        	                        progressBarDialog.setVisible(true); // Muestra la barra
        	                        // Al completar, abrir la ventana principal
        	                        SwingUtilities.invokeLater(() -> {
        	                        	baseDeDatos.actualizarUltimoInicioCliente(dni, LocalDate.now().toString());
            	                		JOptionPane.showMessageDialog(this, "Bienvenida, " + nombre + ".");
        	                        	dispose();
                                        new VentanaCliente(nombre, (ArrayList<Seguro>) baseDeDatos.obtenerSeguros(contraseña), baseDeDatos, dni, "H");
        	                        	});
                                    encontrado = true;
                                    break;
                    			}
                                
                            }
                    	}else {
                    		if(baseDeDatos.cargarUsuarioDesdeBDclientes(dni).equals(usuario) && dni.equals(contraseña)) {
                        			if(baseDeDatos.obtenerGeneroCliente(dni).equals("H")) {
                        				String ultimoInicio = baseDeDatos.obtenerUltimoInicioCliente(dni);
            	                        // Mostrar la barra de progreso
            	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
            	                        progressBarDialog.setVisible(true); // Muestra la barra
            	                        // Al completar, abrir la ventana principal
            	                        SwingUtilities.invokeLater(() -> {
            	                        	baseDeDatos.actualizarUltimoInicioCliente(dni, LocalDate.now().toString());
                	                		JOptionPane.showMessageDialog(this, "Bienvenido, " + nombre + ".");
            	                        	dispose();
                                            new VentanaCliente(nombre, (ArrayList<Seguro>) baseDeDatos.obtenerSeguros(contraseña), baseDeDatos, dni, "H");
            	                        	});
                                        encontrado = true;
                                        break;
                        			}else {
                        				// Mostrar ventana de Captcha
                        			    VentanaVerificacionCaptcha ventanaCaptcha = new VentanaVerificacionCaptcha();
                        			    if (!ventanaCaptcha.isCaptchaResuelto()) {
                        			        JOptionPane.showMessageDialog(this, "Debe resolver el Captcha para continuar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        			        return;
                        			    }
                        				String ultimoInicio = baseDeDatos.obtenerUltimoInicioCliente(dni);
            	                        // Mostrar la barra de progreso
            	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
            	                        progressBarDialog.setVisible(true); // Muestra la barra
            	                        // Al completar, abrir la ventana principal
            	                        SwingUtilities.invokeLater(() -> {
            	                        	baseDeDatos.actualizarUltimoInicioCliente(dni, LocalDate.now().toString());
                	                		JOptionPane.showMessageDialog(this, "Bienvenida, " + nombre + ".");
            	                        	dispose();
                                            new VentanaCliente(nombre, (ArrayList<Seguro>) baseDeDatos.obtenerSeguros(contraseña), baseDeDatos, dni, "H");
            	                        	});
                                        encontrado = true;
                                        break;
                        			}
                    		}
                    	}
                    	
                    }else {
                    	if(baseDeDatos.cargarUsuarioDesdeBDclientes(dni) != null) {
                    		if(baseDeDatos.cargarUsuarioDesdeBDclientes(dni).equals(usuario) && baseDeDatos.cargarContraseñaDesdeBDclientes(dni).equals(contraseña)) {
                    		if((nombre + "_" + apellidos).equals(usuario) && baseDeDatos.cargarContraseñaDesdeBDclientes(dni).equals(contraseña)) {
                        			if(baseDeDatos.obtenerGeneroCliente(dni).equals("H")) {
                        				String ultimoInicio = baseDeDatos.obtenerUltimoInicioCliente(dni);
                        				// Mostrar la barra de progreso
            	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
            	                        progressBarDialog.setVisible(true); // Muestra la barra
            	                        // Al completar, abrir la ventana principal
            	                        SwingUtilities.invokeLater(() -> {
            	                        	baseDeDatos.actualizarUltimoInicioCliente(dni, LocalDate.now().toString());
                	                		JOptionPane.showMessageDialog(this, "Bienvenido, " + nombre + ".");
            	                        	dispose();
                                            new VentanaCliente(nombre, (ArrayList<Seguro>) baseDeDatos.obtenerSeguros(contraseña), baseDeDatos, dni, "H");
            	                        	});
                                        encontrado = true;
                                        break;
                        			}else {
                        				String ultimoInicio = baseDeDatos.obtenerUltimoInicioCliente(dni);
                        				// Mostrar la barra de progreso
            	                		BarraProgreso progressBarDialog = new BarraProgreso(this);
            	                        progressBarDialog.setVisible(true); // Muestra la barra
            	                        // Al completar, abrir la ventana principal
            	                        SwingUtilities.invokeLater(() -> {
            	                        	baseDeDatos.actualizarUltimoInicioCliente(dni, LocalDate.now().toString());
                	                		JOptionPane.showMessageDialog(this, "Bienvenida, " + nombre + ".");
            	                        	dispose();
                                            new VentanaCliente(nombre, (ArrayList<Seguro>) baseDeDatos.obtenerSeguros(contraseña), baseDeDatos, dni, "H");
            	                        	});
                                        encontrado = true;
                                        break;
                        			}
                    		}
                    	}
                    	
                    	
                    }
                    
                }
                if (!encontrado) {
                    JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }} catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al iniciar sesión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        campoUsuario.setText("");
        campoContraseña.setText("");
    }
    
    private void iniciarAnimacionColor(JLabel label) {
        new Thread(() -> {
            boolean aumentando = true; // Controla si aumenta o disminuye la intensidad
            int intensidad = 0;        // Intensidad inicial para el componente rojo (R)

            while (true) {
                // Ajustar intensidad (R)
                if (aumentando) {
                    intensidad += 5;
                    if (intensidad >= 255) aumentando = false; // Límite superior
                } else {
                    intensidad -= 5;
                    if (intensidad <= 0) aumentando = true; // Límite inferior
                }

                // Actualizar el color en el hilo de la interfaz gráfica
                int finalIntensidad = intensidad;
                SwingUtilities.invokeLater(() -> label.setForeground(
                    new Color(finalIntensidad, 255, 255) // Gradiente de cian a blanco
                ));

                // Pausa para suavizar la animación
                try {
                    Thread.sleep(15); // Ajusta la velocidad del cambio de color
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


   

    private void generarCaptcha(JLabel labelCaptcha) {
        String captcha = generarCodigoAleatorio(5);
        BufferedImage imagenCaptcha = new BufferedImage(150, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagenCaptcha.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 150, 50);
        g2d.setFont(new Font("Verdana", Font.BOLD, 24));
        g2d.setColor(Color.BLACK);
        g2d.drawString(captcha, 20, 35);
        g2d.dispose();
        labelCaptcha.setIcon(new ImageIcon(imagenCaptcha));
        labelCaptcha.setText(captcha); // Almacenar texto del captcha para verificación
        labelCaptcha.setVisible(true); // Mostrar el Captcha
    }


    private String generarCodigoAleatorio(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            captcha.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return captcha.toString();
    }

    private boolean verificarCaptcha(JTextField campoCaptcha, String captchaCorrecto) {
        String captchaUsuario = campoCaptcha.getText().toUpperCase().trim();
        return captchaUsuario.equals(captchaCorrecto);
    }



    public static void main(String[] args) {
        new InicioSesion();
    }
}




















