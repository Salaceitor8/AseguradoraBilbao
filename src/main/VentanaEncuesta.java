package main;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VentanaEncuesta extends JFrame {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> comboSatisfecho;
    private JComboBox<String> comboAspectoFavorito;
    private JSlider sliderValoracion;
    private JTextArea areaComentario;
    private JButton btnEnviar;
    private JButton btnCancelar;

    public VentanaEncuesta(Bdd bd, String dniCliente) {
        // Configuración básica
        setTitle("Encuesta de Satisfacción");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Colores
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorTexto = Color.WHITE; // Texto blanco
        Color colorBotonEnviar = new Color(0, 153, 76); // Verde para "Enviar"
        Color colorBotonCancelar = new Color(204, 0, 0); // Rojo para "Cancelar"

        // Cambiar el fondo general de la ventana
        getContentPane().setBackground(colorPrincipal);

        // Panel superior: Título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(colorPrincipal);
        JLabel titulo = new JLabel("Encuesta de Satisfacción", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(colorTexto);
        panelTitulo.add(titulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel central: Preguntas
        JPanel panelPreguntas = new JPanel();
        panelPreguntas.setLayout(new GridLayout(5, 2, 10, 20));
        panelPreguntas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPreguntas.setBackground(colorPrincipal);

        // Pregunta 1: ¿Está satisfecho con el servicio?
        JLabel lblSatisfecho = new JLabel("¿Está satisfecho con el servicio?");
        lblSatisfecho.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSatisfecho.setForeground(colorTexto);
        comboSatisfecho = new JComboBox<>(new String[]{"", "Sí", "No"});
        comboSatisfecho.setFont(new Font("Arial", Font.PLAIN, 14));
        comboSatisfecho.setPreferredSize(new Dimension(120, 25));
        panelPreguntas.add(lblSatisfecho);
        panelPreguntas.add(comboSatisfecho);

        // Pregunta 2: ¿Qué le gustó más del servicio?
        JLabel lblAspectoFavorito = new JLabel("¿Qué le gustó más del servicio?");
        lblAspectoFavorito.setFont(new Font("Arial", Font.PLAIN, 14));
        lblAspectoFavorito.setForeground(colorTexto);
        comboAspectoFavorito = new JComboBox<>(new String[]{"", "Atención", "Rapidez", "Precio"});
        comboAspectoFavorito.setFont(new Font("Arial", Font.PLAIN, 14));
        comboAspectoFavorito.setPreferredSize(new Dimension(120, 25));
        panelPreguntas.add(lblAspectoFavorito);
        panelPreguntas.add(comboAspectoFavorito);

        // Pregunta 3: Calificación general
        JLabel lblValoracion = new JLabel("Califique el servicio (1 a 5):");
        lblValoracion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblValoracion.setForeground(colorTexto);
        sliderValoracion = new JSlider(1, 5, 3); // Valor inicial 3
        sliderValoracion.setMajorTickSpacing(1);
        sliderValoracion.setPaintTicks(true);
        sliderValoracion.setPaintLabels(true);
        sliderValoracion.setBackground(colorPrincipal); // Fondo azul
        sliderValoracion.setForeground(colorTexto); // Texto blanco en las etiquetas
        panelPreguntas.add(lblValoracion);
        panelPreguntas.add(sliderValoracion);

        // Pregunta 4: Comentarios
        JLabel lblComentario = new JLabel("Comentarios adicionales:");
        lblComentario.setFont(new Font("Arial", Font.PLAIN, 14));
        lblComentario.setForeground(colorTexto);
        areaComentario = new JTextArea(3, 20);
        areaComentario.setFont(new Font("Arial", Font.PLAIN, 14));
        areaComentario.setLineWrap(true);
        areaComentario.setWrapStyleWord(true);
        JScrollPane scrollComentario = new JScrollPane(areaComentario);
        panelPreguntas.add(lblComentario);
        panelPreguntas.add(scrollComentario);

        add(panelPreguntas, BorderLayout.CENTER);

        // Panel inferior: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(colorPrincipal);

        btnEnviar = new JButton("Enviar");
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEnviar.setBackground(colorBotonEnviar);
        btnEnviar.setForeground(colorTexto);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setBackground(colorBotonCancelar);
        btnCancelar.setForeground(colorTexto);

        panelBotones.add(btnEnviar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // Listeners de botones
        btnEnviar.addActionListener(e -> enviarEncuesta(bd, dniCliente));
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void enviarEncuesta(Bdd bd, String dniCliente) {
        String satisfecho = (String) comboSatisfecho.getSelectedItem();
        String aspectoFavorito = (String) comboAspectoFavorito.getSelectedItem();
        int valoracion = sliderValoracion.getValue();
        String comentario = areaComentario.getText().trim();

        // Validar campos
        if (satisfecho.isBlank() || aspectoFavorito.isBlank()) {
            JOptionPane.showMessageDialog(this, "Por favor, responda todas las preguntas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Fecha actual
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Insertar en la base de datos
        bd.insertarEncuesta(dniCliente, fecha, satisfecho, aspectoFavorito, valoracion, comentario);

        JOptionPane.showMessageDialog(this, "Gracias por completar la encuesta.", "Encuesta Enviada", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }


}
