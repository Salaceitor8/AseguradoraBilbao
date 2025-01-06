package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class VentanaRecuperarContraseña extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField campoCorreo, campoCodigo;
    private JPasswordField campoNuevaContraseña, campoConfirmarContraseña;
    private String codigoGenerado;
    private final Bdd baseDeDatos;
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public VentanaRecuperarContraseña(Bdd baseDeDatos) {
        this.baseDeDatos = baseDeDatos;

        setTitle("Recuperar Contraseña");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar panel principal con CardLayout
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.setBackground(new Color(0, 51, 102)); // Azul inicial

        // Añadir los paneles al CardLayout
        panelPrincipal.add(crearPanelCorreo(), "Correo");
        panelPrincipal.add(crearPanelCodigo(), "Codigo");
        panelPrincipal.add(crearPanelNuevaContraseña(), "NuevaContraseña");

        add(panelPrincipal);

        setVisible(true);
    }

    private JPanel crearPanelCorreo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 51, 102)); // Azul vibrante
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel etiquetaTitulo = crearEtiquetaTitulo("Recuperar Contraseña");

        JLabel etiquetaCorreo = new JLabel("Introduce tu correo electrónico:");
        etiquetaCorreo.setFont(new Font("Arial", Font.PLAIN, 14));
        etiquetaCorreo.setForeground(new Color(255, 204, 0)); // Amarillo

        campoCorreo = new JTextField();
        campoCorreo.setPreferredSize(new Dimension(300, 30));

        JButton btnEnviarCodigo = crearBoton("Enviar Código", e -> {
            String correo = campoCorreo.getText().trim();
            if (correo.isEmpty() || !correo.contains("@")) {
                mostrarError("Correo inválido.");
            } else {
                generarCodigoRecuperacion();
                mostrarMensaje("Código generado. Ingrese el código para continuar.");
                
                // Mostrar el código generado en una ventana de mensaje
                JOptionPane.showMessageDialog(this, 
                    "El código generado es: " + codigoGenerado, 
                    "Código Generado", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                cardLayout.show(panelPrincipal, "Codigo");
            }
        });

        JPanel panelCentral = new JPanel(new GridLayout(3, 1, 10, 10));
        panelCentral.setBackground(new Color(0, 51, 102));
        panelCentral.add(etiquetaCorreo);
        panelCentral.add(campoCorreo);
        panelCentral.add(btnEnviarCodigo);

        panel.add(etiquetaTitulo, BorderLayout.NORTH);
        panel.add(panelCentral, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelCodigo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(102, 0, 102)); // Morado
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel etiquetaTitulo = crearEtiquetaTitulo("Validar Código");

        JLabel etiquetaCodigo = new JLabel("Introduce el código enviado:");
        etiquetaCodigo.setFont(new Font("Arial", Font.PLAIN, 14));
        etiquetaCodigo.setForeground(new Color(255, 204, 0)); // Amarillo

        campoCodigo = new JTextField();
        campoCodigo.setPreferredSize(new Dimension(300, 30));

        JButton btnValidarCodigo = crearBoton("Validar Código", e -> {
            if (campoCodigo.getText().equals(codigoGenerado)) {
                mostrarMensaje("Código válido. Proceda a cambiar su contraseña.");
                cardLayout.show(panelPrincipal, "NuevaContraseña");
            } else {
                mostrarError("Código incorrecto.");
            }
        });

        JPanel panelCentral = new JPanel(new GridLayout(3, 1, 10, 10));
        panelCentral.setBackground(new Color(102, 0, 102));
        panelCentral.add(etiquetaCodigo);
        panelCentral.add(campoCodigo);
        panelCentral.add(btnValidarCodigo);

        panel.add(etiquetaTitulo, BorderLayout.NORTH);
        panel.add(panelCentral, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelNuevaContraseña() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 51, 102)); // Azul
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel etiquetaTitulo = crearEtiquetaTitulo("Nueva Contraseña");

        JLabel etiquetaNuevaContraseña = new JLabel("Nueva Contraseña:");
        etiquetaNuevaContraseña.setForeground(new Color(255, 204, 0)); // Amarillo
        JLabel etiquetaConfirmarContraseña = new JLabel("Confirmar Contraseña:");
        etiquetaConfirmarContraseña.setForeground(new Color(255, 204, 0)); // Amarillo

        campoNuevaContraseña = new JPasswordField();
        campoNuevaContraseña.setPreferredSize(new Dimension(300, 30));
        campoConfirmarContraseña = new JPasswordField();
        campoConfirmarContraseña.setPreferredSize(new Dimension(300, 30));

        JButton btnRestablecer = crearBoton("Restablecer Contraseña", e -> {
            String nuevaContraseña = new String(campoNuevaContraseña.getPassword()).trim();
            String confirmarContraseña = new String(campoConfirmarContraseña.getPassword()).trim();

            if (nuevaContraseña.isEmpty() || !nuevaContraseña.equals(confirmarContraseña)) {
                mostrarError("Las contraseñas no coinciden.");
                return;
            }

            try {
                String dni = baseDeDatos.obtenerDNIporCorreo(campoCorreo.getText().trim());
                if (dni == null) {
                    mostrarError("No se encontró un usuario con el correo proporcionado.");
                    return;
                }

                mostrarBarraDeProgreso(() -> {
                    boolean actualizado = baseDeDatos.cambiarContraseñaEnBD(dni, nuevaContraseña);
                    if (actualizado) {
                        mostrarMensaje("Contraseña restablecida exitosamente.");
                        dispose();
                    } else {
                        mostrarError("Error al actualizar la contraseña.");
                    }
                });
            } catch (Exception ex) {
                mostrarError("Error: " + ex.getMessage());
            }
        });

        JPanel panelCentral = new JPanel(new GridLayout(4, 1, 10, 10));
        panelCentral.setBackground(new Color(0, 51, 102)); // Azul
        panelCentral.add(etiquetaNuevaContraseña);
        panelCentral.add(campoNuevaContraseña);
        panelCentral.add(etiquetaConfirmarContraseña);
        panelCentral.add(campoConfirmarContraseña);
        panelCentral.add(btnRestablecer);

        panel.add(etiquetaTitulo, BorderLayout.NORTH);
        panel.add(panelCentral, BorderLayout.CENTER);

        return panel;
    }

    private void mostrarBarraDeProgreso(Runnable tareaFinal) {
        JDialog dialog = new JDialog(this, "Actualizando contraseña", true);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(this);

        JProgressBar barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        dialog.add(barraProgreso, BorderLayout.CENTER);

        Thread hilo = new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i += 10) {
                    Thread.sleep(300); // Simula tiempo de espera
                    barraProgreso.setValue(i);
                }
                dialog.dispose(); // Cierra el diálogo cuando finaliza
                tareaFinal.run(); // Ejecuta la tarea después del progreso
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        hilo.start();

        dialog.setVisible(true);
    }

    private JLabel crearEtiquetaTitulo(String texto) {
        JLabel etiquetaTitulo = new JLabel(texto, SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        etiquetaTitulo.setForeground(new Color(255, 204, 0)); // Amarillo
        return etiquetaTitulo;
    }

    private JButton crearBoton(String texto, ActionListener actionListener) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 14));
        boton.setBackground(new Color(102, 0, 102)); // Morado
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(150, 40));
        boton.addActionListener(actionListener);
        return boton;
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void generarCodigoRecuperacion() {
        codigoGenerado = String.format("%06d", new Random().nextInt(999999));
    }
}