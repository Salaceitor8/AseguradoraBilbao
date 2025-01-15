package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ServiciosEmergencia extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
    private JPanel panelCentral;
    final Color COLOR_NORMAL = COLOR_PRINCIPAL;
    final Color COLOR_RESALTADO = new Color(51, 102, 204); // Azul más claro
    private static final Color COLOR_PRINCIPAL = new Color(0, 51, 102); // Azul oscuro

    public ServiciosEmergencia() {
        setTitle("Servicios de Emergencia");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración principal
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        // Crear las tarjetas
        JPanel panelOpciones = crearPanelOpciones();
        JPanel panelUbicacion = crearPanelUbicacion();

        // Añadir tarjetas al panel central
        panelCentral.add(panelOpciones, "Opciones");
        panelCentral.add(panelUbicacion, "Ubicacion");

        add(panelCentral);
        setVisible(true);
    }

    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(0, 51, 102));

        // Botones para las opciones
        JButton btnFontanero = new JButton("Fontanero");
        btnFontanero.setBackground(new Color(0, 51, 102));
        btnFontanero.setForeground(Color.WHITE);
        btnFontanero.setBorder(null);
        Image fontanero = (new ImageIcon("fotos/fontanero.png")).getImage().getScaledInstance(50, 50, DO_NOTHING_ON_CLOSE);
        ImageIcon iconoFontanero = new ImageIcon(fontanero);
        btnFontanero.setIcon(iconoFontanero);
        btnFontanero.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                new Thread(() -> {
                    for (int i = 0; i <= 255; i += 5) {
                        int intensidad = i;
                        SwingUtilities.invokeLater(() -> btnFontanero.setBackground(new Color(
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
                        SwingUtilities.invokeLater(() -> btnFontanero.setBackground(new Color(
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
        JButton btnCerrajero = new JButton("Cerrajero");
        btnCerrajero.setBackground(new Color(0, 51, 102));
        btnCerrajero.setForeground(Color.WHITE);
        btnCerrajero.setBorder(null);
        Image cerrajero = (new ImageIcon("fotos/cerrajero.png")).getImage().getScaledInstance(50, 50, DO_NOTHING_ON_CLOSE);
        ImageIcon iconoCerrajero = new ImageIcon(cerrajero);
        btnCerrajero.setIcon(iconoCerrajero);
        btnCerrajero.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                new Thread(() -> {
                    for (int i = 0; i <= 255; i += 5) {
                        int intensidad = i;
                        SwingUtilities.invokeLater(() -> btnCerrajero.setBackground(new Color(
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
                        SwingUtilities.invokeLater(() -> btnCerrajero.setBackground(new Color(
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
        JButton btnElectricista = new JButton("Electricista");
        btnElectricista.setBackground(new Color(0, 51, 102));
        btnElectricista.setForeground(Color.WHITE);
        btnElectricista.setBorder(null);
        Image electricista = (new ImageIcon("fotos/electricista.png")).getImage().getScaledInstance(50, 50, DO_NOTHING_ON_CLOSE);
        ImageIcon iconoElectricista = new ImageIcon(electricista);
        btnElectricista.setIcon(iconoElectricista);
        btnElectricista.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                new Thread(() -> {
                    for (int i = 0; i <= 255; i += 5) {
                        int intensidad = i;
                        SwingUtilities.invokeLater(() -> btnElectricista.setBackground(new Color(
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
                        SwingUtilities.invokeLater(() -> btnElectricista.setBackground(new Color(
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
        JButton btnGrua = new JButton("Grúa");
        btnGrua.setBackground(new Color(0, 51, 102));
        btnGrua.setForeground(Color.WHITE);
        btnGrua.setBorder(null);
        Image grua = (new ImageIcon("fotos/grua.png")).getImage().getScaledInstance(50, 50, DO_NOTHING_ON_CLOSE);
        ImageIcon iconoGrua = new ImageIcon(grua);
        btnGrua.setIcon(iconoGrua);
        btnGrua.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                new Thread(() -> {
                    for (int i = 0; i <= 255; i += 5) {
                        int intensidad = i;
                        SwingUtilities.invokeLater(() -> btnGrua.setBackground(new Color(
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
                        SwingUtilities.invokeLater(() -> btnGrua.setBackground(new Color(
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

        // Listener para pasar al formulario de ubicación
        ActionListener listener = e -> cardLayout.show(panelCentral, "Ubicacion");
        
        btnFontanero.addActionListener(listener);
        btnCerrajero.addActionListener(listener);
        btnElectricista.addActionListener(listener);
        btnGrua.addActionListener(listener);

        // Agregar botones al panel
        panel.add(btnFontanero);
        panel.add(btnCerrajero);
        panel.add(btnElectricista);
        panel.add(btnGrua);

        return panel;
    }

    private JPanel crearPanelUbicacion() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(0, 51, 102));

        // Etiquetas y campos de texto
        JLabel lblCalle = new JLabel("Calle:");
        lblCalle.setForeground(Color.WHITE);
        JTextField txtCalle = new JTextField();


        JLabel lblPortal = new JLabel("Portal:");
        lblPortal.setForeground(Color.WHITE);
        JTextField txtPortal = new JTextField();

        JLabel lblPiso = new JLabel("Piso:");
        lblPiso.setForeground(Color.WHITE);
        JTextField txtPiso = new JTextField();

        JLabel lblComentarios = new JLabel("Comentarios:");
        lblComentarios.setForeground(Color.WHITE);
        JTextField txtComentarios = new JTextField();
        

        
        // Botón para enviar la información
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setBackground(new Color(51, 153, 255));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.addActionListener(e -> {
        	if (txtCalle.getText().trim().isEmpty()) {
        	    JOptionPane.showMessageDialog(this, "El campo 'Calle' es obligatorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        	    return;
        	}
            String ubicacion = "Calle: " + txtCalle.getText() + "\n" +
                               "Portal: " + txtPortal.getText() + "\n" +
                               "Piso: " + txtPiso.getText() + "\n" +
                               "Comentarios: " + txtComentarios.getText() + "\n" +
            					"Un especialista va de camino";
            JOptionPane.showMessageDialog(this, "Ubicación enviada:\n" + ubicacion);
            dispose();
        });
        
//        Hacer que al darle al enter salte al siguiente campo
        txtCalle.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					txtPortal.requestFocus();
				}
			}
        	
		});
        txtPortal.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					txtPiso.requestFocus();
				}
			}
        	
		});
        txtPiso.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					txtComentarios.requestFocus();
				}
			}
        	
		});
        txtComentarios.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnEnviar.doClick();
				}
			}
        	
		});
        
        // Botón para regresar a las opciones
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBackground(Color.RED);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(e -> cardLayout.show(panelCentral, "Opciones"));

        // Agregar componentes al panel
        panel.add(lblCalle);
        panel.add(txtCalle);
        panel.add(lblPortal);
        panel.add(txtPortal);
        panel.add(lblPiso);
        panel.add(txtPiso);
        panel.add(lblComentarios);
        panel.add(txtComentarios);
        panel.add(btnVolver);
        panel.add(btnEnviar);

        return panel;
    }


}

