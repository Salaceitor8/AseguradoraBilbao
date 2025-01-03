package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import domain.Seguro;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatBotVentana extends JFrame {

    private JTextArea areaChat;
    private Map<String, String> respuestasPredefinidas;
    private List<Seguro> segurosCliente; // Lista de seguros del cliente
    private JPanel panelPreguntas;
    private int paginaActual = 0;   // Página actual
    private final int PREGUNTAS_POR_PAGINA = 3; // Máximo de preguntas por página
    private JButton btnArriba, btnAbajo; // Botones de navegación

    public ChatBotVentana(List<Seguro> seguros) {
        this.segurosCliente = seguros; // Cargar la lista de seguros
        inicializarRespuestas(); // Llenar las respuestas predefinidas

        // Configuración básica de la ventana
        setTitle("ChatBot - Atención al Cliente");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Colores consistentes con el diseño
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorContraste = Color.WHITE;           // Blanco
        Color colorBoton = new Color(0, 102, 204);   // Azul claro

        // Área de chat
        areaChat = new JTextArea();
        areaChat.setEditable(false);
        areaChat.setLineWrap(true);
        areaChat.setWrapStyleWord(true);
        areaChat.setBackground(colorContraste); // Fondo blanco
        areaChat.setForeground(colorPrincipal); // Texto azul oscuro
        areaChat.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollChat = new JScrollPane(areaChat);
        add(scrollChat, BorderLayout.CENTER);

        // Panel de preguntas predefinidas
        JPanel panelNavegacion = new JPanel(new BorderLayout());
        panelPreguntas = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelPreguntas.setBackground(colorContraste);
        panelPreguntas.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(colorPrincipal, 1),
                "Preguntas Predefinidas", 0, 0, new Font("Arial", Font.BOLD, 16), colorPrincipal));

        actualizarPreguntas(); // Mostrar las primeras preguntas

        // Botones de navegación
        btnArriba = new JButton("<-");
        btnArriba.setFont(new Font("Arial", Font.BOLD, 18));
        btnArriba.setBackground(colorBoton);
        btnArriba.setForeground(colorContraste);
        btnArriba.addActionListener(e -> cambiarPagina(-1)); // Ir a la página anterior

        btnAbajo = new JButton("->");
        btnAbajo.setFont(new Font("Arial", Font.BOLD, 18));
        btnAbajo.setBackground(colorBoton);
        btnAbajo.setForeground(colorContraste);
        btnAbajo.addActionListener(e -> cambiarPagina(1)); // Ir a la siguiente página

        panelNavegacion.add(btnArriba, BorderLayout.WEST);
        panelNavegacion.add(panelPreguntas, BorderLayout.CENTER);
        panelNavegacion.add(btnAbajo, BorderLayout.EAST);

        add(panelNavegacion, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void inicializarRespuestas() {
        respuestasPredefinidas = new HashMap<>();
        respuestasPredefinidas.put("¿Cuáles son mis seguros activos?", ""); // Respuesta dinámica
        respuestasPredefinidas.put("¿Cómo reportar un siniestro?", "Para reportar un siniestro, llama al 900-123-456 o utiliza la opción 'Reportar Siniestro' en tu perfil.\n\n");
        respuestasPredefinidas.put("¿Cuál es el número de atención al cliente?", "El número de atención al cliente es 900-123-456.\n\n");
        respuestasPredefinidas.put("Gracias", "¡De nada! ¿Hay algo más en lo que pueda ayudarte?\n\n");
        respuestasPredefinidas.put("¿Qué tipos de seguro ofrecen?", "Ofrecemos seguros de vida, coche y hogar. Si quiere mas información llama al 900-123-456.\n\n");
        respuestasPredefinidas.put("¿Cómo puedo cancelar un seguro?", "Para cancelar un seguro, llama al 900-123-456 y solicita la cancelación.\n\n");
        respuestasPredefinidas.put("¿Qué pasa si no pago a tiempo?", "Si no pagas a tiempo, tu seguro será suspendido hasta regularizar el pago. Si quiere mas información llama al 900-123-456.\n\n");
        respuestasPredefinidas.put("¿Cómo actualizo mi información personal?", "Puedes actualizar tu información desde la sección 'Mi Perfil'.\n\n");
        respuestasPredefinidas.put("¿Cómo contacto con un asesor?", "Llama al 900-123-456 para contactar con un asesor.\n\n");
        respuestasPredefinidas.put("¿Cuánto cuesta un seguro de vida?", "El costo del seguro de vida depende de tu edad y cobertura elegida. Si quiere mas información llama al 900-123-456.\n\n");
        respuestasPredefinidas.put("¿Qué cobertura tiene el seguro de coche?", "El seguro de coche cubre daños a terceros, robo y colisiones. Si quiere mas información llama al 900-123-456.\n\n");
        respuestasPredefinidas.put("¿Cuáles son los beneficios del seguro hogar?", "El seguro hogar cubre incendios, robos y daños por agua. Si quiere mas información llama al 900-123-456.\n\n");
    }

    private void actualizarPreguntas() {
        panelPreguntas.removeAll(); // Limpiar el panel
        List<String> preguntas = new ArrayList<>(respuestasPredefinidas.keySet());
        int inicio = paginaActual * PREGUNTAS_POR_PAGINA;
        int fin = Math.min(inicio + PREGUNTAS_POR_PAGINA, preguntas.size());

        for (int i = inicio; i < fin; i++) {
            String pregunta = preguntas.get(i);
            JButton btnPregunta = new JButton(pregunta);
            btnPregunta.setFocusPainted(false);
            btnPregunta.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente mediana
            btnPregunta.setBackground(new Color(0, 102, 204));
            btnPregunta.setForeground(Color.WHITE);
            btnPregunta.setPreferredSize(new Dimension(350, 40)); // Tamaño fijo
            btnPregunta.addActionListener(e -> procesarPreguntaPredefinida(pregunta));
            panelPreguntas.add(btnPregunta);
        }

        panelPreguntas.revalidate();
        panelPreguntas.repaint();
    }

    private void cambiarPagina(int direccion) {
        int totalPaginas = (int) Math.ceil((double) respuestasPredefinidas.size() / PREGUNTAS_POR_PAGINA);
        paginaActual = (paginaActual + direccion + totalPaginas) % totalPaginas; // Moverse circularmente
        actualizarPreguntas();
    }

    private void procesarPreguntaPredefinida(String pregunta) {
        areaChat.append("Tú: " + pregunta + "\n");

        // Procesar la respuesta en un hilo separado
        new Thread(() -> {
            try {
                // Simular tiempo de procesamiento
                Thread.sleep(1000); // 1 segundo

                // Respuesta personalizada para la pregunta específica
                String respuesta;
                if (pregunta.equals("¿Cuáles son mis seguros activos?")) {
                    respuesta = generarRespuestaSegurosActivos();
                } else {
                    respuesta = respuestasPredefinidas.getOrDefault(pregunta, "Lo siento, no entiendo tu pregunta.");
                }

                SwingUtilities.invokeLater(() -> areaChat.append("ChatBot: " + respuesta + "\n"));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private String generarRespuestaSegurosActivos() {
        StringBuilder respuesta = new StringBuilder("Tus seguros activos son:\n");
        boolean tieneSegurosActivos = false;

        for (Seguro seguro : segurosCliente) {
            if (seguro.getEstado().equalsIgnoreCase("Activo")) {
                tieneSegurosActivos = true;
                respuesta.append("- Tipo: ").append(seguro.getTipo()).append("\n")
                        .append("  Costo: ").append(seguro.getCostoMensual()).append(" €\n")
                        .append("  Fecha Contratación: ").append(seguro.getFechaContratacion()).append("\n\n");
            }
        }

        if (!tieneSegurosActivos) {
            respuesta = new StringBuilder("No tienes seguros activos en este momento.");
        }

        return respuesta.toString();
    }

    public static void main(String[] args) {
        new ChatBotVentana(new ArrayList<>());
    }
}
