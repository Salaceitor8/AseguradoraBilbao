package main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaSolicitarPregunta extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea campoPregunta;
    private JButton btnEnviar;
    private JLabel lblContador;
    private final int LIMITE_CARACTERES = 35;
    private Bdd baseDeDatos;
    private String dniCliente;

    public VentanaSolicitarPregunta(Bdd baseDeDatos, String dniCliente) {
        this.baseDeDatos = baseDeDatos;
        this.dniCliente = dniCliente;

        // Configuración básica de la ventana
        setTitle("Solicitar Pregunta Predefinida");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior con el título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 51, 102));
        JLabel lblTitulo = new JLabel("Solicitar Pregunta");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Área central con el campo de texto para la pregunta
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        campoPregunta = new JTextArea();
        campoPregunta.setLineWrap(true);
        campoPregunta.setWrapStyleWord(true);
        campoPregunta.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollCampoPregunta = new JScrollPane(campoPregunta);
        panelCentral.add(scrollCampoPregunta, BorderLayout.CENTER);

        // Contador de caracteres
        lblContador = new JLabel("Carácteres: 0/" + LIMITE_CARACTERES);
        lblContador.setFont(new Font("Arial", Font.PLAIN, 12));
        lblContador.setHorizontalAlignment(SwingConstants.RIGHT);
        panelCentral.add(lblContador, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con el botón de enviar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnEnviar = new JButton("Enviar");
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEnviar.setBackground(new Color(0, 102, 204));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setEnabled(false); // Deshabilitado por defecto
        panelBotones.add(btnEnviar);

        add(panelBotones, BorderLayout.SOUTH);

        // Agregar DocumentListener para el contador
        campoPregunta.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarContador();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarContador();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarContador();
            }
        });

        // Acción del botón Enviar
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarSolicitud();
            }
        });

        setVisible(true);
    }

    private void actualizarContador() {
        int caracteres = campoPregunta.getText().length();
        lblContador.setText("Carácteres: " + caracteres + "/" + LIMITE_CARACTERES);

        if (caracteres > LIMITE_CARACTERES) {
            lblContador.setForeground(Color.RED);
            btnEnviar.setEnabled(false);
        } else {
            lblContador.setForeground(Color.BLACK);
            btnEnviar.setEnabled(caracteres > 0); // Habilita solo si hay texto
        }
    }

    private void enviarSolicitud() {
        String pregunta = campoPregunta.getText().trim();

        if (pregunta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, escribe una pregunta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas enviar esta solicitud?\nRecuerda que no será anónima.",
                "Confirmar Envío",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                baseDeDatos.añadirSolicitud(dniCliente, pregunta);
                JOptionPane.showMessageDialog(this, "Solicitud enviada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Cerrar la ventana después de enviar
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al enviar la solicitud: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
