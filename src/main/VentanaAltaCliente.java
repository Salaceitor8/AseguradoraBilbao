package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaAltaCliente extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public VentanaAltaCliente(DefaultListModel<String> modeloListaClientes, Bdd baseDeDatos) {

        // Configuración básica de la ventana
        setTitle("Dar de Alta Cliente");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Solo cerrar esta ventana
        setLocationRelativeTo(null); // Centrar la ventana

        // Crear el panel principal
        JPanel panelAltaCliente = new JPanel(new GridBagLayout());
        panelAltaCliente.setBackground(new Color(0, 51, 102)); // Azul oscuro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font fuenteGeneral = new Font("Arial", Font.PLAIN, 14);
        Color colorTexto = Color.WHITE;

        // Crear campos
        JTextField campoNombre = new JTextField(15);
        JTextField campoApellidos = new JTextField(15);
        JTextField campoDNI = new JTextField(10);
        JTextField campoTelefono = new JTextField(10);
        JTextField campoEmail = new JTextField(15);
        JComboBox<String> campoGenero = new JComboBox<String>(new String[]{"Hombre", "Mujer"});

        // Añadir los KeyListeners para mover el cursor con ENTER
        setNavigationWithEnter(campoNombre, campoApellidos);
        setNavigationWithEnter(campoApellidos, campoDNI);
        setNavigationWithEnter(campoDNI, campoTelefono);
        setNavigationWithEnter(campoTelefono, campoEmail);

        // Etiqueta y campo de nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel etiquetaNombre = new JLabel("Nombre:");
        etiquetaNombre.setForeground(colorTexto);
        etiquetaNombre.setFont(fuenteGeneral);
        panelAltaCliente.add(etiquetaNombre, gbc);

        gbc.gridx = 1;
        panelAltaCliente.add(campoNombre, gbc);

        // Etiqueta y campo de apellidos
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel etiquetaApellidos = new JLabel("Apellidos:");
        etiquetaApellidos.setForeground(colorTexto);
        etiquetaApellidos.setFont(fuenteGeneral);
        panelAltaCliente.add(etiquetaApellidos, gbc);

        gbc.gridx = 1;
        panelAltaCliente.add(campoApellidos, gbc);

        // Etiqueta y campo de DNI
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel etiquetaDNI = new JLabel("DNI:");
        etiquetaDNI.setForeground(colorTexto);
        etiquetaDNI.setFont(fuenteGeneral);
        panelAltaCliente.add(etiquetaDNI, gbc);

        gbc.gridx = 1;
        panelAltaCliente.add(campoDNI, gbc);

        // Etiqueta y campo de teléfono
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel etiquetaTelefono = new JLabel("Teléfono:");
        etiquetaTelefono.setForeground(colorTexto);
        etiquetaTelefono.setFont(fuenteGeneral);
        panelAltaCliente.add(etiquetaTelefono, gbc);

        gbc.gridx = 1;
        panelAltaCliente.add(campoTelefono, gbc);

        // Etiqueta y campo de email
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel etiquetaEmail = new JLabel("Email:");
        etiquetaEmail.setForeground(colorTexto);
        etiquetaEmail.setFont(fuenteGeneral);
        panelAltaCliente.add(etiquetaEmail, gbc);

        gbc.gridx = 1;
        panelAltaCliente.add(campoEmail, gbc);
        
        // Etiqueta y campo genero
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel etiquetaGenero = new JLabel("Genero:");
        etiquetaGenero.setForeground(colorTexto);
        etiquetaGenero.setFont(fuenteGeneral);
        panelAltaCliente.add(etiquetaGenero, gbc);

        gbc.gridx = 1;
        panelAltaCliente.add(campoGenero, gbc);

        // Botón para guardar cliente
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnGuardarCliente = new JButton("Guardar Cliente");
        btnGuardarCliente.setBackground(new Color(51, 153, 255)); // Azul claro
        btnGuardarCliente.setForeground(Color.WHITE);
        panelAltaCliente.add(btnGuardarCliente, gbc);

        // Añadir un KeyListener al campo email para simular clic en "Guardar" con ENTER
        campoEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnGuardarCliente.doClick(); // Simula el clic en el botón "Guardar"
                }
            }
        });

        // Acción del botón Guardar Cliente
        btnGuardarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = campoNombre.getText();
                String apellidos = campoApellidos.getText();
                String dni = campoDNI.getText();
                String telefono = campoTelefono.getText();
                String email = campoEmail.getText();
                String genero = campoGenero.getSelectedItem().toString();

                // Validaciones básicas
                if (nombre.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(panelAltaCliente, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verificar que los apellidos tienen exactamente dos palabras
                if (apellidos.split("\\s+").length != 2) {
                    JOptionPane.showMessageDialog(panelAltaCliente, "El campo Apellidos debe contener exactamente dos apellidos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verificar el formato del DNI (8 números seguidos de una letra)
                if (!dni.matches("[0-9]{8}[A-Za-z]")) {
                    JOptionPane.showMessageDialog(panelAltaCliente, "El DNI debe tener 8 números seguidos de una letra.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verificar que el teléfono tiene 9 dígitos
                if (!telefono.matches("[0-9]{9}")) {
                    JOptionPane.showMessageDialog(panelAltaCliente, "El teléfono debe tener 9 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verificar formato del email
                if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                    JOptionPane.showMessageDialog(panelAltaCliente, "El formato del email no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear mensaje de confirmación con los datos del cliente
                String mensajeConfirmacion = String.format(
                    "Por favor, confirma los datos del cliente:\n\n" +
                    "Nombre: %s\nApellidos: %s\nDNI: %s\nTeléfono: %s\nEmail: %s\nGenero: %s\n\n¿Son estos datos correctos?",
                    nombre, apellidos, dni, telefono, email, genero
                );

                // Mostrar cuadro de diálogo de confirmación
                int confirmacion = JOptionPane.showConfirmDialog(
                    panelAltaCliente, mensajeConfirmacion, "Confirmación de Datos", JOptionPane.YES_NO_OPTION
                );

                // Si el usuario confirma los datos
                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Guardar el cliente en la base de datos
                    try {
                    	if(genero.equals("Hombre")) {
                    		baseDeDatos.insertarCliente(nombre, apellidos, dni, telefono, email, genero);
                            JOptionPane.showMessageDialog(panelAltaCliente, "Cliente guardado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
                            modeloListaClientes.addElement(nombre + " " + apellidos + " - DNI: " + dni);
                            dispose(); // Cerrar la ventana
                    	}else {
                        baseDeDatos.insertarCliente(nombre, apellidos, dni, telefono, email, genero);
                        JOptionPane.showMessageDialog(panelAltaCliente, "Cliente guardado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        modeloListaClientes.addElement(nombre + " " + apellidos + " - DNI: " + dni);
                        dispose(); // Cerrar la ventana
                    	}
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panelAltaCliente, "Error al guardar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Añadir el panel a la ventana
        add(panelAltaCliente);

        // Hacer visible la ventana
        setVisible(true);
    }

    // Método auxiliar para mover el cursor con ENTER
    private void setNavigationWithEnter(JTextField actualCampo, JTextField siguienteCampo) {
        actualCampo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    siguienteCampo.requestFocus(); // Cambiar el foco al siguiente campo
                }
            }
        });
    }
}
