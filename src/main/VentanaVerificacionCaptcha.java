package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VentanaVerificacionCaptcha extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int intentosFallidos = 0;
    private String captchaCorrecto;
    private JTextField campoCaptcha;
    private JButton botonVerificar;
    private JLabel labelCaptcha;
    private JProgressBar progressBar;
    private boolean captchaResuelto = false;
    private Timer timer;  // Temporizador para deshabilitar el botón temporalmente

    public VentanaVerificacionCaptcha() {
        // Configuración de la ventana de CAPTCHA
        setTitle("Verificación CAPTCHA");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centrar la ventana

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Crear el JLabel para mostrar el CAPTCHA
        labelCaptcha = new JLabel();
        panel.add(labelCaptcha, BorderLayout.CENTER);

        // Campo para que el usuario ingrese el CAPTCHA
        campoCaptcha = new JTextField(10);
        panel.add(campoCaptcha, BorderLayout.EAST);

        // Botón de verificación
        botonVerificar = new JButton("Verificar");
        botonVerificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarCaptcha();
            }
        });
        panel.add(botonVerificar, BorderLayout.SOUTH);

        // Crear el ProgressBar (con valor máximo de 10 segundos)
        progressBar = new JProgressBar(0, 10);
        progressBar.setStringPainted(true);  // Mostrar el texto del progreso
        progressBar.setString("Tiempo restante: 10s");
        progressBar.setVisible(false);  // Inicialmente oculto
        panel.add(progressBar, BorderLayout.NORTH);

        // Añadir el panel al frame
        add(panel);

        // Generar un CAPTCHA aleatorio
        generarCaptcha();

        setVisible(true);
    }

    private void generarCaptcha() {
        // Generar el texto aleatorio para el CAPTCHA (puedes personalizar la longitud)
        captchaCorrecto = generarCodigoAleatorio(4);
        
        // Crear una imagen con el código CAPTCHA
        BufferedImage imagenCaptcha = new BufferedImage(90, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagenCaptcha.createGraphics();

        // Dibujar el fondo
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 100, 50);

        // Establecer la fuente y el color del texto
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.setColor(Color.BLACK);

        // Dibujar el código CAPTCHA aleatorio
        g2d.drawString(captchaCorrecto, 10, 35);

        // Finalizar el dibujo
        g2d.dispose();

        // Mostrar la imagen en el JLabel
        labelCaptcha.setIcon(new ImageIcon(imagenCaptcha));
    }

    private String generarCodigoAleatorio(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(caracteres.length());
            captcha.append(caracteres.charAt(indice));
        }

        return captcha.toString();
    }

    private void verificarCaptcha() {
        String captchaUsuario = campoCaptcha.getText().toUpperCase().trim();

        // Verificar si el CAPTCHA es correcto
        if (captchaUsuario.equals(captchaCorrecto)) {
        	captchaResuelto = true;
            JOptionPane.showMessageDialog(this, "Captcha correcto, acceso permitido.");
            dispose();  // Cerrar la ventana de CAPTCHA
        } else {
            intentosFallidos++;
            if (intentosFallidos >= 3) {
                JOptionPane.showMessageDialog(this, "Intento fallido. Has alcanzado el número máximo de intentos.");
                System.exit(0);  // Salir de la aplicación
            } else {
                campoCaptcha.setText("");
                JOptionPane.showMessageDialog(this, "Captcha incorrecto. Intentos restantes: " + (3 - intentosFallidos));
                // Generar un nuevo CAPTCHA
                generarCaptcha();
                // Deshabilitar el botón y usar el temporizador para habilitarlo de nuevo
                botonVerificar.setEnabled(false);
                iniciarTemporizador();
                
            }
        }
    }
    public boolean isCaptchaResuelto() {
        
		return captchaResuelto;
    }
    private void iniciarTemporizador() {
        // Temporizador que deshabilita el botón de verificación por 10 segundos
        timer = new Timer(1000, new ActionListener() {
            private int tiempoRestante = 10; // Tiempo inicial de 10 segundos

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tiempoRestante > 0) {
                    // Actualizar el ProgressBar
                    progressBar.setValue(tiempoRestante);
                    progressBar.setString("Tiempo restante: " + tiempoRestante + "s");
                    tiempoRestante--;
                } else {
                    // Cuando el tiempo termine, habilitar el botón y ocultar el ProgressBar
                    botonVerificar.setEnabled(true);
                    progressBar.setVisible(false);  // Ocultar el ProgressBar
                    JOptionPane.showMessageDialog(VentanaVerificacionCaptcha.this, "Puedes intentar de nuevo.");
                    timer.stop();  // Detener el temporizador
                }
            }
        });
        timer.setRepeats(true);  // El temporizador debe repetirse cada segundo
        timer.start();  // Iniciar el temporizador

        // Mostrar el ProgressBar
        progressBar.setVisible(true);
    }

    public static void main(String[] args) {
        new VentanaVerificacionCaptcha();
    }
}
