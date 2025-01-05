package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ServiciosEmergencia extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelCentral;

    public ServiciosEmergencia() {
        setTitle("Servicios de Emergencia");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración principal
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        // Crear las tarjetas
        JPanel panelOpciones = crearPanelOpciones();
        JPanel panelUbicacion = crearPanelUbicacion();

        // Añadir tarjetas al panel central
        panelCentral.add(panelOpciones, "Opciones");
        panelCentral.add(panelUbicacion, "Ubicacion");

        add(panelCentral);
        setVisible(true);
    }

    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(0, 51, 102));

        // Botones para las opciones
        JButton btnFontanero = new JButton("Fontanero");
        btnFontanero.setBackground(new Color(0, 51, 102));
        btnFontanero.setForeground(Color.WHITE);
        JButton btnCerrajero = new JButton("Cerrajero");
        btnCerrajero.setBackground(new Color(0, 51, 102));
        btnCerrajero.setForeground(Color.WHITE);
        JButton btnElectricista = new JButton("Electricista");
        btnElectricista.setBackground(new Color(0, 51, 102));
        btnElectricista.setForeground(Color.WHITE);
        JButton btnGrua = new JButton("Grúa");
        btnGrua.setBackground(new Color(0, 51, 102));
        btnGrua.setForeground(Color.WHITE);

        // Listener para pasar al formulario de ubicación
        ActionListener listener = e -> cardLayout.show(panelCentral, "Ubicacion");

        btnFontanero.addActionListener(listener);
        btnCerrajero.addActionListener(listener);
        btnElectricista.addActionListener(listener);
        btnGrua.addActionListener(listener);

        // Agregar botones al panel
        panel.add(btnFontanero);
        panel.add(btnCerrajero);
        panel.add(btnElectricista);
        panel.add(btnGrua);

        return panel;
    }

    private JPanel crearPanelUbicacion() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(0, 51, 102));

        // Etiquetas y campos de texto
        JLabel lblCalle = new JLabel("Calle:");
        lblCalle.setForeground(Color.WHITE);
        JTextField txtCalle = new JTextField();

        JLabel lblPortal = new JLabel("Portal:");
        lblPortal.setForeground(Color.WHITE);
        JTextField txtPortal = new JTextField();

        JLabel lblPiso = new JLabel("Piso:");
        lblPiso.setForeground(Color.WHITE);
        JTextField txtPiso = new JTextField();

        JLabel lblComentarios = new JLabel("Comentarios:");
        lblComentarios.setForeground(Color.WHITE);
        JTextField txtComentarios = new JTextField();

        // Botón para enviar la información
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setBackground(new Color(51, 153, 255));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.addActionListener(e -> {
        	if (txtCalle.getText().trim().isEmpty()) {
        	    JOptionPane.showMessageDialog(this, "El campo 'Calle' es obligatorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        	    return;
        	}
            String ubicacion = "Calle: " + txtCalle.getText() + "\n" +
                               "Portal: " + txtPortal.getText() + "\n" +
                               "Piso: " + txtPiso.getText() + "\n" +
                               "Comentarios: " + txtComentarios.getText() + "\n" +
            					"Un especialista va de camino";
            JOptionPane.showMessageDialog(this, "Ubicación enviada:\n" + ubicacion);
            dispose();
        });

        // Botón para regresar a las opciones
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBackground(Color.RED);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.addActionListener(e -> cardLayout.show(panelCentral, "Opciones"));

        // Agregar componentes al panel
        panel.add(lblCalle);
        panel.add(txtCalle);
        panel.add(lblPortal);
        panel.add(txtPortal);
        panel.add(lblPiso);
        panel.add(txtPiso);
        panel.add(lblComentarios);
        panel.add(txtComentarios);
        panel.add(btnVolver);
        panel.add(btnEnviar);

        return panel;
    }

    public static void main(String[] args) {
        new ServiciosEmergencia();
    }
}

