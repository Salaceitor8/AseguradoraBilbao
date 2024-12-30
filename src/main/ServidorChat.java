package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class ServidorChat extends JFrame {

    private static final int PUERTO = 12345;
    private static Set<ClienteHandler> clientesConectados = new HashSet<>();
    private DefaultListModel<String> modeloListaClientes;
    private JTextArea areaLog;
    private JButton btnIniciar, btnDetener;

    private ServerSocket serverSocket;
    private boolean ejecutando = false;

    public ServidorChat() {
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("Servidor de Chat - Administración");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de clientes conectados
        JPanel panelClientes = new JPanel(new BorderLayout());
        modeloListaClientes = new DefaultListModel<>();
        JList<String> listaClientes = new JList<>(modeloListaClientes);
        listaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelClientes.setBorder(BorderFactory.createTitledBorder("Clientes Conectados"));
        panelClientes.add(new JScrollPane(listaClientes), BorderLayout.CENTER);
        panelClientes.setPreferredSize(new Dimension(200, 0));

        // Área de logs
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Logs del Servidor"));

        // Botones de control
        JPanel panelControles = new JPanel(new FlowLayout());
        btnIniciar = new JButton("Iniciar Servidor");
        btnDetener = new JButton("Detener Servidor");
        btnDetener.setEnabled(false);
        panelControles.add(btnIniciar);
        panelControles.add(btnDetener);

        // Añadir componentes a la ventana
        add(panelClientes, BorderLayout.WEST);
        add(scrollLog, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);

        // Listeners de botones
        btnIniciar.addActionListener(e -> iniciarServidor());
        btnDetener.addActionListener(e -> detenerServidor());
    }

    private void iniciarServidor() {
        ejecutando = true;
        btnIniciar.setEnabled(false);
        btnDetener.setEnabled(true);
        areaLog.append("Iniciando el servidor en el puerto " + PUERTO + "...\n");

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PUERTO);
                areaLog.append("Servidor iniciado en el puerto " + PUERTO + "\n");

                while (ejecutando) {
                    Socket socket = serverSocket.accept();
                    areaLog.append("Nuevo cliente conectado: " + socket.getInetAddress().getHostAddress() + "\n");

                    ClienteHandler cliente = new ClienteHandler(socket, this);
                    synchronized (clientesConectados) {
                        clientesConectados.add(cliente);
                    }
                    modeloListaClientes.addElement(socket.getInetAddress().getHostAddress());
                    new Thread(cliente).start();
                }
            } catch (IOException e) {
                if (ejecutando) {
                    areaLog.append("Error en el servidor: " + e.getMessage() + "\n");
                }
            }
        }).start();
    }

    private void detenerServidor() {
        ejecutando = false;
        btnIniciar.setEnabled(true);
        btnDetener.setEnabled(false);

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            areaLog.append("Servidor detenido.\n");
        } catch (IOException e) {
            areaLog.append("Error al detener el servidor: " + e.getMessage() + "\n");
        }

        synchronized (clientesConectados) {
            for (ClienteHandler cliente : clientesConectados) {
                cliente.cerrarConexion();
            }
            clientesConectados.clear();
            modeloListaClientes.clear();
        }
    }

    public void enviarMensajeATodos(String mensaje, ClienteHandler remitente) {
        synchronized (clientesConectados) {
            for (ClienteHandler cliente : clientesConectados) {
                if (cliente != remitente) {
                    cliente.enviarMensaje(mensaje);
                }
            }
        }
        areaLog.append("Mensaje enviado a todos: " + mensaje + "\n");
    }

    public void eliminarCliente(ClienteHandler cliente) {
        synchronized (clientesConectados) {
            clientesConectados.remove(cliente);
            modeloListaClientes.removeElement(cliente.getDireccion());
            areaLog.append("Cliente desconectado: " + cliente.getDireccion() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServidorChat().setVisible(true));
    }
}

// Clase ClienteHandler
class ClienteHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ServidorChat servidor;

    public ClienteHandler(Socket socket, ServidorChat servidor) {
        this.socket = socket;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                servidor.enviarMensajeATodos(mensaje, this);
            }
        } catch (IOException e) {
            servidor.eliminarCliente(this);
        } finally {
            cerrarConexion();
        }
    }

    public void enviarMensaje(String mensaje) {
        if (out != null) {
            out.println(mensaje);
        }
    }

    public void cerrarConexion() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDireccion() {
        return socket.getInetAddress().getHostAddress();
    }
}

