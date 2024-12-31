package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaCambiarUsuario extends JFrame {

    private final String dniCliente; // DNI del cliente
    private final Bdd bd; // Instancia de la base de datos

    public VentanaCambiarUsuario(String dniCliente, Bdd bd) {
        this.dniCliente = dniCliente;
        this.bd = bd;

        // Configuración básica de la ventana
        setTitle("Cambiar Nombre de Usuario");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Colores personalizados
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorContraste = Color.WHITE; // Blanco
        Color colorBoton = new Color(51, 153, 255); // Azul claro

        // Crear los campos de texto
        JTextField campoUsuarioActual = new JTextField(15);
        JTextField campoNuevoUsuario = new JTextField(15);
        JTextField campoConfirmarUsuario = new JTextField(15);

        // Crear botones
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        // Configuración de botones
        btnAceptar.setBackground(colorBoton);
        btnAceptar.setForeground(colorContraste);
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(colorContraste);

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(colorPrincipal);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Añadir componentes al panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUsuarioActual = new JLabel("Usuario Actual:");
        lblUsuarioActual.setForeground(colorContraste);
        panel.add(lblUsuarioActual, gbc);

        gbc.gridx = 1;
        panel.add(campoUsuarioActual, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblNuevoUsuario = new JLabel("Nuevo Usuario:");
        lblNuevoUsuario.setForeground(colorContraste);
        panel.add(lblNuevoUsuario, gbc);

        gbc.gridx = 1;
        panel.add(campoNuevoUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblConfirmarUsuario = new JLabel("Confirmar Usuario:");
        lblConfirmarUsuario.setForeground(colorContraste);
        panel.add(lblConfirmarUsuario, gbc);

        gbc.gridx = 1;
        panel.add(campoConfirmarUsuario, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(colorPrincipal);
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        // Añadir panel principal y de botones a la ventana
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Foco inicial en el primer campo
        campoUsuarioActual.requestFocusInWindow();

        // Configurar movimiento del foco con Enter
        configurarMovimientoConEnter(campoUsuarioActual, campoNuevoUsuario);
        configurarMovimientoConEnter(campoNuevoUsuario, campoConfirmarUsuario);

        // Acción en el último campo (simular clic en "Aceptar")
        campoConfirmarUsuario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnAceptar.doClick(); // Simula el clic en el botón "Aceptar"
                }
            }
        });
        
        btnAceptar.addActionListener(e -> {
            String usuarioActual = campoUsuarioActual.getText();
            String nuevoUsuario = campoNuevoUsuario.getText();
            String confirmarUsuario = campoConfirmarUsuario.getText();

            if (validarNuevoUsuario(nuevoUsuario, confirmarUsuario, usuarioActual, bd)) {
                // Actualizar el usuario en la base de datos
                if (bd.cambiarUsuarioEnBD(dniCliente, nuevoUsuario)) {
                    JOptionPane.showMessageDialog(this, "Nombre de usuario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Cerrar la ventana
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Acción del botón "Cancelar"
        btnCancelar.addActionListener(e -> dispose()); // Cerrar la ventana

        // Hacer visible la ventana
        setVisible(true);
    }
    
    private boolean validarNuevoUsuario(String nuevoUsuario, String confirmarUsuario, String usuarioActual, Bdd bd) {
        // Validar que no esté vacío
        if (nuevoUsuario.isEmpty() || confirmarUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar que el usuario confirmado coincida con el nuevo usuario
        if (!nuevoUsuario.equals(confirmarUsuario)) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario no coincide con la confirmación.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar que el nuevo usuario no sea igual al actual
        if (nuevoUsuario.equals(usuarioActual)) {
            JOptionPane.showMessageDialog(this, "El nuevo nombre de usuario no puede ser igual al actual.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar que el nuevo usuario no esté ya en uso
        if (bd.existeUsuarioEnBD(nuevoUsuario)) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya está en uso. Por favor, elige otro.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validación exitosa
        return true;
    }


    private void configurarMovimientoConEnter(JComponent actual, JComponent siguiente) {
        actual.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    siguiente.requestFocus(); // Mover el foco al siguiente campo
                }
            }
        });
    }
}
