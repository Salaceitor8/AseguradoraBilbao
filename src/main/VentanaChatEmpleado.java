package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import domain.*;

public class VentanaChatEmpleado extends JFrame {

    private JList<String> listaClientes;
    private DefaultListModel<String> modeloListaClientes;
    private JPanel panelChat;
    private JTextArea campoMensaje; // Cambiar a JTextArea para permitir múltiples líneas
    private JButton btnEnviar;
    private Map<String, List<Mensaje>> mensajesPorCliente;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Bdd baseDeDatos;

    private JScrollPane scrollChat;

    public VentanaChatEmpleado(Bdd baseDeDatos) {
        this.baseDeDatos = baseDeDatos;

        // Configuración básica de la ventana
        setTitle("Atención al Cliente - Chat");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana
        setResizable(true);

        // Layout principal
        setLayout(new BorderLayout());

        // Crear el modelo de lista y JList para los clientes
        modeloListaClientes = new DefaultListModel<>();
        listaClientes = new JList<>(modeloListaClientes);
        listaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Panel de la lista de clientes a la izquierda
        JPanel panelListaClientes = new JPanel(new BorderLayout());
        panelListaClientes.setBorder(BorderFactory.createTitledBorder("Clientes"));
        panelListaClientes.add(new JScrollPane(listaClientes), BorderLayout.CENTER);
        panelListaClientes.setPreferredSize(new Dimension(200, 0)); // Ancho de 200 px

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

        mensajesPorCliente = new HashMap<>();

        // Añadir todo al panel principal
        add(panelListaClientes, BorderLayout.WEST);  // Lista de clientes a la izquierda
        add(scrollChat, BorderLayout.CENTER);        // Panel de chat en el centro
        add(panelMensaje, BorderLayout.SOUTH);       // Campo de mensaje en la parte inferior

        // Cargar la lista de clientes
        cargarClientesDesdeBaseDeDatos();
        
        // Listener para cambiar el chat al seleccionar un cliente
        listaClientes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String clienteSeleccionado = listaClientes.getSelectedValue();
                if (clienteSeleccionado != null) {
                    String dniCliente = clienteSeleccionado.split("- DNI: ")[1].trim();
                    cargarMensajesCliente(dniCliente);
                }
            }
        });

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

        setVisible(true);
    }

    public void cargarClientesDesdeBaseDeDatos() {
        try {
            java.sql.ResultSet rs = baseDeDatos.obtenerClientes();
            while (rs.next()) {
                String nombre = rs.getString("nombre").trim();
                String apellidos = rs.getString("apellidos").trim();
                String dni = rs.getString("dni").trim();
                String cliente = nombre + " " + apellidos + " - DNI: " + dni;
                modeloListaClientes.addElement(cliente);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los clientes desde la base de datos: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMensajesCliente(String cliente) {
        panelChat.removeAll();  // Limpiar el panel de chat
        String clienteSeleccionado = listaClientes.getSelectedValue();
        if(clienteSeleccionado == null) return;
        String dniCliente = clienteSeleccionado.split("- DNI: ")[1];
        try {
            ArrayList<Mensaje> mensajes = baseDeDatos.obtenerMensajes(dniCliente);
            for (Mensaje mensaje : mensajes) {
				agregarBurbujaMensaje(mensaje);
			}
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los mensajes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        panelChat.revalidate();  // Asegurar que el panel se actualice
        panelChat.repaint();
    }

    private void agregarBurbujaMensaje(Mensaje mensaje) {
        JPanel burbuja = new JPanel();
        burbuja.setLayout(new BorderLayout());
        burbuja.setBorder(new EmptyBorder(10, 10, 10, 10)); // Espaciado alrededor del mensaje
        JTextArea contenido = new JTextArea(mensaje.getContenido());
        contenido.setLineWrap(true); // Ajustar texto para evitar desplazamiento horizontal
        contenido.setWrapStyleWord(true);
        contenido.setEditable(false);
        contenido.setBorder(new EmptyBorder(5, 5, 5, 5)); // Espaciado interno
        contenido.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente más grande

        // Color de fondo y alineación según remitente
        if (mensaje.getRemitente().equalsIgnoreCase("cliente")) {
            contenido.setBackground(new Color(220, 220, 220)); // Gris claro para clientes
            burbuja.add(contenido, BorderLayout.WEST); // Alinear a la izquierda
        } else {
            contenido.setBackground(new Color(180, 180, 180)); // Gris más oscuro para empleados
            burbuja.add(contenido, BorderLayout.EAST); // Alinear a la derecha
        }

        JLabel hora = new JLabel(mensaje.getFechaHora().format(dateFormatter));
        hora.setFont(new Font("Arial", Font.PLAIN, 10)); // Hora más pequeña
        hora.setForeground(Color.GRAY); // Letra gris para la hora

        burbuja.add(hora, BorderLayout.SOUTH); // Añadir la hora debajo del mensaje
        burbuja.setOpaque(false); // Hacer transparente la burbuja
        panelChat.add(burbuja);
        panelChat.add(Box.createVerticalStrut(10)); // Espacio entre mensajes
    }

    private void enviarMensaje() {
        String texto = campoMensaje.getText();
        if (texto.isEmpty()) {
            return; // No enviar mensajes vacíos
        }

        String clienteSeleccionado = listaClientes.getSelectedValue();
        if (clienteSeleccionado != null) {
            String dniCliente = clienteSeleccionado.split("- DNI: ")[1].trim();
            Mensaje nuevoMensaje = new Mensaje("empleado", texto, LocalDateTime.now());

            // Añadir el mensaje a la base de datos
            try {
                baseDeDatos.insertarMensaje(dniCliente, nuevoMensaje.getRemitente(), nuevoMensaje.getContenido(), nuevoMensaje.getFechaHora().format(dateFormatter));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el mensaje: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Actualizar el panel de chat con el nuevo mensaje
            agregarBurbujaMensaje(nuevoMensaje);

            // Limpiar el campo de texto después de enviar
            campoMensaje.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un cliente antes de enviar el mensaje.",
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
