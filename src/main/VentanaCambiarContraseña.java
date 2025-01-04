package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaCambiarContraseña extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public VentanaCambiarContraseña(String dniCliente, Bdd bd) {

        // Configuración básica de la ventana
        setTitle("Cambiar Contraseña");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Colores personalizados
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorContraste = Color.WHITE; // Blanco
        Color colorBoton = new Color(51, 153, 255); // Azul claro

        // Crear los campos de contraseña
        JPasswordField campoContraseñaActual = new JPasswordField(15);
        JPasswordField campoNuevaContraseña = new JPasswordField(15);
        JPasswordField campoConfirmarContraseña = new JPasswordField(15);

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
        JLabel lblContraseñaActual = new JLabel("Contraseña Actual:");
        lblContraseñaActual.setForeground(colorContraste);
        panel.add(lblContraseñaActual, gbc);

        gbc.gridx = 1;
        panel.add(campoContraseñaActual, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblNuevaContraseña = new JLabel("Nueva Contraseña:");
        lblNuevaContraseña.setForeground(colorContraste);
        panel.add(lblNuevaContraseña, gbc);

        gbc.gridx = 1;
        panel.add(campoNuevaContraseña, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblConfirmarContraseña = new JLabel("Confirmar Contraseña:");
        lblConfirmarContraseña.setForeground(colorContraste);
        panel.add(lblConfirmarContraseña, gbc);

        gbc.gridx = 1;
        panel.add(campoConfirmarContraseña, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(colorPrincipal);
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        // Añadir panel principal y de botones a la ventana
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Foco inicial en el primer campo
        campoContraseñaActual.requestFocusInWindow();

        // Configurar movimiento del foco con Enter
        configurarMovimientoConEnter(campoContraseñaActual, campoNuevaContraseña);
        configurarMovimientoConEnter(campoNuevaContraseña, campoConfirmarContraseña);

        // Acción en el último campo (simular clic en "Aceptar")
        campoConfirmarContraseña.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnAceptar.doClick(); // Simula el clic en el botón "Aceptar"
                }
            }
        });

        // Acción del botón "Aceptar"
        btnAceptar.addActionListener(e -> {
            String contraseñaActual = new String(campoContraseñaActual.getPassword());
            String nuevaContraseña = new String(campoNuevaContraseña.getPassword());
            String confirmarContraseña = new String(campoConfirmarContraseña.getPassword());

            // Validaciones
            if (contraseñaActual.isEmpty() || nuevaContraseña.isEmpty() || confirmarContraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!nuevaContraseña.equals(confirmarContraseña)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar la contraseña actual desde la base de datos
            String contraseñaGuardada = bd.cargarContraseñaDesdeBDclientes(dniCliente);
            if (!contraseñaActual.equals(contraseñaGuardada)) {
                JOptionPane.showMessageDialog(this, "La contraseña actual no es correcta.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cambiar la contraseña en la base de datos
            if (bd.cambiarContraseñaEnBD(dniCliente, nuevaContraseña)) {
                JOptionPane.showMessageDialog(this, "Contraseña actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Cerrar la ventana
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción del botón "Cancelar"
        btnCancelar.addActionListener(e -> dispose()); // Cerrar la ventana

        // Hacer visible la ventana
        setVisible(true);
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
