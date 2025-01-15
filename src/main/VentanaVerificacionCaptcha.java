package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VentanaVerificacionCaptcha extends JFrame {
    private static final long serialVersionUID = 1L;
    private int intentosFallidos = 0;
    private String captchaCorrecto;
    private JTextField campoCaptcha;
    private JButton botonVerificar;
    private JLabel labelCaptcha;
    private JProgressBar progressBar;
    private Timer timer;
    private boolean captchaResuelto = false;

    public VentanaVerificacionCaptcha() {
        setTitle("Verificación CAPTCHA");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());

        // Captcha label
        labelCaptcha = new JLabel("", SwingConstants.CENTER);
        labelCaptcha.setFont(new Font("Verdana", Font.BOLD, 24));
        panel.add(labelCaptcha, BorderLayout.CENTER);

        // Campo de texto
        campoCaptcha = new JTextField(10);
        campoCaptcha.setFont(new Font("Verdana", Font.PLAIN, 16));
        campoCaptcha.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(campoCaptcha, BorderLayout.EAST);

        // Botón de verificación
        botonVerificar = new JButton("Verificar");
        botonVerificar.setBackground(new Color(51, 153, 255));
        botonVerificar.setForeground(Color.WHITE);
        botonVerificar.setFocusPainted(false);
        botonVerificar.addActionListener(e -> verificarCaptcha());
        panel.add(botonVerificar, BorderLayout.SOUTH);

        // Barra de progreso
        progressBar = new JProgressBar(0, 10);
        progressBar.setForeground(new Color(102, 204, 255));
        progressBar.setBackground(new Color(15, 32, 50));
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);
        progressBar.setString("Tiempo restante: 10s");
        progressBar.setVisible(false);
        panel.add(progressBar, BorderLayout.NORTH);

        add(panel);
        generarCaptcha();
        setVisible(true);
    }

    private void generarCaptcha() {
        captchaCorrecto = generarCodigoAleatorio(5);
        int ancho = 20 + captchaCorrecto.length() * 20;
        BufferedImage imagenCaptcha = new BufferedImage(ancho, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagenCaptcha.createGraphics();

        // Fondo del Captcha
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, ancho, 50);

        // Texto del Captcha
        g2d.setFont(new Font("Verdana", Font.BOLD, 30));
        g2d.setColor(Color.BLACK);
        g2d.drawString(captchaCorrecto, 10, 35);

        // Ruido gráfico
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            int x1 = random.nextInt(ancho);
            int y1 = random.nextInt(50);
            int x2 = random.nextInt(ancho);
            int y2 = random.nextInt(50);
            g2d.drawLine(x1, y1, x2, y2);
        }
        for (int i = 0; i < 100; i++) {
            g2d.fillRect(random.nextInt(ancho), random.nextInt(50), 1, 1);
        }

        g2d.dispose();
        labelCaptcha.setIcon(new ImageIcon(imagenCaptcha));
        labelCaptcha.setText(captchaCorrecto); // Almacenar el texto del Captcha
    }

    private String generarCodigoAleatorio(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            captcha.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return captcha.toString();
    }

    private void verificarCaptcha() {
        String captchaUsuario = campoCaptcha.getText().toUpperCase().trim();
        if (captchaUsuario.equals(captchaCorrecto)) {
            captchaResuelto = true; // Asegúrate de que se asigna correctamente
            JOptionPane.showMessageDialog(this, "Captcha correcto, acceso permitido.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            captchaResuelto = false; // Captcha no resuelto
            intentosFallidos++;
            if (intentosFallidos >= 3) {
                JOptionPane.showMessageDialog(this, "Has alcanzado el número máximo de intentos.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(this, "Captcha incorrecto. Intentos restantes: " + (3 - intentosFallidos), "Error", JOptionPane.ERROR_MESSAGE);
                generarCaptcha();
            }
        }
    }

    
    

    @SuppressWarnings("unused")
	private void iniciarTemporizador() {
        timer = new Timer(1000, new ActionListener() {
            private int tiempoRestante = 10;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tiempoRestante > 0) {
                    progressBar.setValue(tiempoRestante);
                    progressBar.setString("Tiempo restante: " + tiempoRestante + "s");
                    tiempoRestante--;
                } else {
                    botonVerificar.setEnabled(true);
                    progressBar.setVisible(false);
                    timer.stop();
                }
            }
        });
        progressBar.setVisible(true);
        timer.start();
    }

    @SuppressWarnings("unused")
	private void iniciarBloqueo(int segundos) {
        Timer bloqueoTimer = new Timer(1000, new ActionListener() {
            private int tiempoRestante = segundos;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tiempoRestante > 0) {
                    progressBar.setValue(tiempoRestante);
                    progressBar.setString("Desbloqueo en: " + tiempoRestante + "s");
                    tiempoRestante--;
                } else {
                    botonVerificar.setEnabled(true);
                    progressBar.setVisible(false);
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        progressBar.setVisible(true);
        bloqueoTimer.start();
    }

    public boolean isCaptchaResuelto() {
        return captchaResuelto;
    }


}