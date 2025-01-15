package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class VentanaSimulacionAtencion extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel relojLabel;
    private JLabel[] empleadosLabels;
    private JLabel[] contadoresLabels;
    private JButton btnEmpezar;
    private AtomicBoolean ejecutando;
    private Thread hiloSimulacion;
    private final int numEmpleados = 10;

    public VentanaSimulacionAtencion(Bdd bd) {
        setTitle("Simulación de Atención al Cliente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Colores
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorContraste = Color.WHITE;           // Blanco
        Color colorBoton = new Color(0, 153, 76);     // Verde suave
        Color colorFondo = new Color(240, 240, 240);  // Fondo claro

        // Panel superior: Reloj
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(colorPrincipal);
        relojLabel = new JLabel("09:00");
        relojLabel.setFont(new Font("Arial", Font.BOLD, 24));
        relojLabel.setForeground(colorContraste);
        panelSuperior.add(relojLabel);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central: Empleados
        JPanel panelEmpleados = new JPanel(new GridLayout(2, 5, 10, 10));
        panelEmpleados.setBackground(colorFondo);
        empleadosLabels = new JLabel[numEmpleados];
        contadoresLabels = new JLabel[numEmpleados];

     // Obtener los nombres de los empleados desde la base de datos
        ArrayList<String> nombresEmpleados = bd.obtenerNombresEmpleados();

        for (int i = 0; i < numEmpleados; i++) {
            JPanel empleadoPanel = new JPanel(new BorderLayout());
            empleadoPanel.setBackground(Color.WHITE);
            empleadoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            try {
                // Cargar y redimensionar la imagen
                ImageIcon iconoOriginal = new ImageIcon("fotos/trabajar.png");
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Ajusta el tamaño
                JLabel empleadoIcono = new JLabel(new ImageIcon(imagenEscalada));
                empleadoIcono.setHorizontalAlignment(SwingConstants.CENTER);
                empleadoPanel.add(empleadoIcono, BorderLayout.CENTER);
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen: " + e.getMessage());
                JLabel empleadoIcono = new JLabel("Sin imagen");
                empleadoIcono.setHorizontalAlignment(SwingConstants.CENTER);
                empleadoPanel.add(empleadoIcono, BorderLayout.CENTER);
            }

            // Asignar nombres desde la lista de la base de datos
            String nombreEmpleado = (i < nombresEmpleados.size()) ? nombresEmpleados.get(i) : "Empleado " + (i + 1);
            empleadosLabels[i] = new JLabel(nombreEmpleado, SwingConstants.CENTER);
            empleadosLabels[i].setFont(new Font("Arial", Font.BOLD, 14));
            empleadoPanel.add(empleadosLabels[i], BorderLayout.NORTH);

            contadoresLabels[i] = new JLabel("Atendidos: 0", SwingConstants.CENTER);
            contadoresLabels[i].setFont(new Font("Arial", Font.PLAIN, 14));
            empleadoPanel.add(contadoresLabels[i], BorderLayout.SOUTH);

            panelEmpleados.add(empleadoPanel);
        }



        add(panelEmpleados, BorderLayout.CENTER);

        // Panel inferior: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(colorFondo);

        btnEmpezar = new JButton("Empezar");

        btnEmpezar.setFont(new Font("Arial", Font.BOLD, 14));

        btnEmpezar.setBackground(colorBoton);

        btnEmpezar.setForeground(colorContraste);

        panelBotones.add(btnEmpezar);

        add(panelBotones, BorderLayout.SOUTH);

        // Inicializar estados
        ejecutando = new AtomicBoolean(false);

        // Listeners de botones
        btnEmpezar.addActionListener(e -> {iniciarSimulacion();
        	btnEmpezar.setEnabled(false);
        });
    }

    private void iniciarSimulacion() {
        if (ejecutando.get()) return; // Evitar múltiples inicios
        ejecutando.set(true);

        // Reiniciar los contadores y reloj
        for (JLabel contador : contadoresLabels) {
            contador.setText("Atendidos: 0");
        }
        relojLabel.setText("09:00");

        // Crear el hilo de simulación
        hiloSimulacion = new Thread(() -> {
            Random random = new Random();
            int llamadasTotales = 40 + random.nextInt(21); // Entre 40 y 60 llamadas
            int minutosTotales = 0; // Minutos totales, incluyendo las pausas

            while (ejecutando.get() && minutosTotales < (11 * 60)) { // 8 horas laborales reales
                try {
                    Thread.sleep(10); // Cada 10 ms simula 1 minuto
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                // Calcular hora y minuto actuales (simulados)
                int horasSimuladas = 9 + (minutosTotales / 60);
                int minutos = minutosTotales % 60;

                // Saltar de 13:00 a 16:00
                if (horasSimuladas == 13) {
                    minutosTotales += 3 * 60; // Saltar 3 horas
                    horasSimuladas = 16;     // Ajustar horas simuladas
                    minutos = 0;             // Reiniciar minutos
                }

                // Actualizar el reloj en la interfaz
                int finalHorasSimuladas = horasSimuladas;
                int finalMinutos = minutos;
                SwingUtilities.invokeLater(() -> {
                    relojLabel.setText(String.format("%02d:%02d", finalHorasSimuladas, finalMinutos));
                });

                // Simular llamadas solo en horario laboral
                if ((horasSimuladas >= 9 && horasSimuladas < 13) || (horasSimuladas >= 16 && horasSimuladas < 20)) {
                    if (random.nextDouble() < 0.1 && llamadasTotales > 0) { // 10% de probabilidad por minuto
                        int empleadoIndex = random.nextInt(numEmpleados);
                        JLabel contador = contadoresLabels[empleadoIndex];

                        SwingUtilities.invokeLater(() -> {
                            String textoActual = contador.getText();
                            int atendidos = Integer.parseInt(textoActual.replace("Atendidos: ", "")) + 1;
                            contador.setText("Atendidos: " + atendidos);
                        });

                        llamadasTotales--;
                    }
                }

                // Incrementar minutos totales
                minutosTotales++;
            }

            // Al finalizar la simulación, mostrar el resumen
            mostrarResumen();
        });

        hiloSimulacion.start();

    }
    
    
    
    private void mostrarResumen() {
        StringBuilder resumen = new StringBuilder("Resumen de la Simulación:\n\n");

        for (int i = 0; i < numEmpleados; i++) {
            String nombreEmpleado = empleadosLabels[i].getText();
            String textoContador = contadoresLabels[i].getText();
            resumen.append(nombreEmpleado).append(": ").append(textoContador.replace("Atendidos: ", "")).append(" clientes atendidos\n");
        }

        // Mostrar el resumen en un cuadro de diálogo
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, resumen.toString(), "Resumen de la Simulación", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
    }





}
