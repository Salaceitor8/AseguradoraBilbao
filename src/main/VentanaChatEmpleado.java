package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import domain.*;

public class VentanaChatEmpleado extends JFrame {

    private JList<String> listaClientes;
    private DefaultListModel<String> modeloListaClientes;
    private JPanel panelChat; 
    private JTextArea campoMensaje; // Cambiar a JTextArea para permitir múltiples líneas
    private JButton btnEnviar;
    private Map<String, List<Mensaje>> mensajesPorCliente;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String archivoMensajes = "mensajes.csv"; // Archivo de mensajes
    private JScrollPane scrollChat;

    public VentanaChatEmpleado() {
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
        cargarClientesDesdeCSV("clientes.csv");
        cargarMensajesDesdeCSV(archivoMensajes);

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

    public void cargarClientesDesdeCSV(String archivoCSV) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    String nombre = datos[0].trim();
                    String apellidos = datos[1].trim();
                    String dni = datos[2].trim();
                    String cliente = nombre + " " + apellidos + " - DNI: " + dni;
                    modeloListaClientes.addElement(cliente);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los clientes desde el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarMensajesDesdeCSV(String archivoCSV) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            boolean esPrimeraLinea = true;  // Bandera para saltar el encabezado
            while ((linea = br.readLine()) != null) {
                if (esPrimeraLinea) {
                    esPrimeraLinea = false;
                    continue;  // Saltar la primera línea (encabezado)
                }
                String[] datos = linea.split(";");
                if (datos.length == 4) {
                    String dniCliente = datos[0].trim();
                    String remitente = datos[1].trim();
                    String mensaje = datos[2].trim();
                    LocalDateTime fechaHora = LocalDateTime.parse(datos[3].trim(), dateFormatter);
                    Mensaje nuevoMensaje = new Mensaje(remitente, mensaje, fechaHora);
                    mensajesPorCliente.computeIfAbsent(dniCliente, k -> new ArrayList<>()).add(nuevoMensaje);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los mensajes desde el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMensajesCliente(String cliente) {
        panelChat.removeAll();  // Limpiar el panel de chat
        if (mensajesPorCliente.containsKey(cliente)) {
            List<Mensaje> mensajes = mensajesPorCliente.get(cliente);
            for (Mensaje mensaje : mensajes) {
                agregarBurbujaMensaje(mensaje);
            }
        } else {
            panelChat.add(new JLabel("No hay mensajes para este cliente."));
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
        
        // Ancho preferido más grande para las burbujas de chat
        contenido.setMaximumSize(new Dimension(500, Integer.MAX_VALUE)); 

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
        if (mensaje.getRemitente().equalsIgnoreCase("cliente")) {
        	hora.setHorizontalAlignment(SwingConstants.LEFT);
        } else {
        	hora.setHorizontalAlignment(SwingConstants.RIGHT);
        }

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

            // Añadir el mensaje a la lista de mensajes del cliente
            mensajesPorCliente.computeIfAbsent(dniCliente, k -> new ArrayList<>()).add(nuevoMensaje);

            // Actualizar el panel de chat con el nuevo mensaje
            agregarBurbujaMensaje(nuevoMensaje);

            // Limpiar el campo de texto después de enviar
            campoMensaje.setText("");

            // Guardar el mensaje en el CSV
            guardarMensajeEnCSV(dniCliente, nuevoMensaje);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un cliente antes de enviar el mensaje.",
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void guardarMensajeEnCSV(String dniCliente, Mensaje mensaje) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoMensajes, true))) {
            String linea = dniCliente + ";" + mensaje.getRemitente() + ";" + mensaje.getContenido() + ";" +
                    mensaje.getFechaHora().format(dateFormatter);
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el mensaje en el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class Mensaje {
        private String remitente;
        private String contenido;
        private LocalDateTime fechaHora;

        public Mensaje(String remitente, String contenido, LocalDateTime fechaHora) {
            this.remitente = remitente;
            this.contenido = contenido;
            this.fechaHora = fechaHora;
        }

        public String getRemitente() {
            return remitente;
        }

        public String getContenido() {
            return contenido;
        }

        public LocalDateTime getFechaHora() {
            return fechaHora;
        }
    }

}
