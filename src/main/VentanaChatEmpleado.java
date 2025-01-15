package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class VentanaChatEmpleado extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelChat;
    private JTextArea campoMensaje;
    private JButton btnEnviar;
    private JScrollPane scrollChat;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public VentanaChatEmpleado(String servidor, int puerto) {
        // Configuración básica de la ventana
        setTitle("Atención al Cliente - Chat");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana
        setResizable(true);

        // Layout principal
        setLayout(new BorderLayout());

        // Panel de chat
        panelChat = new JPanel();
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.Y_AXIS)); // Usar BoxLayout para las burbujas
        panelChat.setBackground(Color.WHITE); // Fondo blanco para el chat
        scrollChat = new JScrollPane(panelChat); // Guardar la referencia del JScrollPane
        scrollChat.setBorder(BorderFactory.createTitledBorder("Chat"));

        // Panel de mensajes (campo de texto y botón de enviar)
        JPanel panelMensaje = new JPanel(new BorderLayout());
        campoMensaje = new JTextArea(); // Cambiado a JTextArea para múltiples líneas
        campoMensaje.setRows(3); // Múltiples filas
        campoMensaje.setLineWrap(true); // Ajuste de línea automático
        campoMensaje.setWrapStyleWord(true);
        JScrollPane scrollMensaje = new JScrollPane(campoMensaje); // Usar JScrollPane para manejar el área de texto
        btnEnviar = new JButton("Enviar");
        panelMensaje.add(scrollMensaje, BorderLayout.CENTER);
        panelMensaje.add(btnEnviar, BorderLayout.EAST);

        // Añadir todo al panel principal
        add(scrollChat, BorderLayout.CENTER);        // Panel de chat en el centro
        add(panelMensaje, BorderLayout.SOUTH);       // Campo de mensaje en la parte inferior

        // Acción para enviar mensajes
        btnEnviar.addActionListener(e -> enviarMensaje());

        // Manejo de "Enter" para enviar y "Shift + Enter" para nueva línea
        campoMensaje.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume(); // Prevenir salto de línea al presionar Enter
                    enviarMensaje(); // Enviar mensaje
                }
            }
        });

        // Conectar al servidor
        conectarServidor(servidor, puerto);

        // Hilo para escuchar mensajes del servidor
        new Thread(this::escucharMensajes).start();

        setVisible(true);
    }

    private void conectarServidor(String servidor, int puerto) {
        try {
            socket = new Socket(servidor, puerto);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            agregarMensaje("Conectado al servidor de chat.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void escucharMensajes() {
        try {
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                agregarMensaje(mensaje);
            }
        } catch (IOException e) {
            agregarMensaje("Desconectado del servidor.");
        } finally {
            cerrarConexion();
        }
    }

    private void enviarMensaje() {
        String texto = campoMensaje.getText();
        if (texto.isEmpty()) {
            return; // No enviar mensajes vacíos
        }

        out.println(texto); // Enviar mensaje al servidor
        campoMensaje.setText(""); // Limpiar el campo de texto después de enviar
    }

    private void agregarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            JPanel burbuja = new JPanel();
            burbuja.setLayout(new BorderLayout());
            burbuja.setBorder(new EmptyBorder(10, 10, 10, 10)); // Espaciado alrededor del mensaje

            JTextArea contenido = new JTextArea(mensaje);
            contenido.setLineWrap(true); // Ajustar texto para evitar desplazamiento horizontal
            contenido.setWrapStyleWord(true);
            contenido.setEditable(false);
            contenido.setBorder(new EmptyBorder(5, 5, 5, 5)); // Espaciado interno
            contenido.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente más grande

            // Color de fondo según remitente (simple ejemplo)
            contenido.setBackground(new Color(220, 220, 220)); // Fondo gris claro

            burbuja.add(contenido, BorderLayout.WEST); // Alinear a la izquierda
            panelChat.add(burbuja);
            panelChat.add(Box.createVerticalStrut(10)); // Espacio entre mensajes

            panelChat.revalidate();
            panelChat.repaint();
        });
    }

    private void cerrarConexion() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

