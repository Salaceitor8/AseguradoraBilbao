package main;
import javax.swing.*;

import domain.TipoSeguro;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class VentanaReportarSiniestros extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboBoxTipoSeguro;
    private JComboBox<String> comboBoxPrecioSeguro;
    private JTextArea textAreaResumen;
    private HashMap<TipoSeguro, ArrayList<Double>> preciosPorSeguro;

    public VentanaReportarSiniestros(HashMap<TipoSeguro, ArrayList<Double>> preciosPorSeguro, Bdd bd, String dni) {
        this.preciosPorSeguro = preciosPorSeguro;

        setTitle("Reportar Siniestro");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Colores
        Color fondo = new Color(240, 240, 240); // Color claro de fondo
        Color texto = new Color(50, 50, 50); // Color de texto
        Color botones = new Color(70, 130, 180); // Color azul para botones

        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(fondo);
        panelSuperior.setLayout(new FlowLayout());

        // Etiquetas y ComboBoxes
        JLabel labelTipoSeguro = new JLabel("Tipo de Seguro:");
        labelTipoSeguro.setForeground(texto);
        comboBoxTipoSeguro = new JComboBox<>(
        	    preciosPorSeguro.keySet().stream().map(TipoSeguro::toString).toArray(String[]::new)
        	);
        comboBoxTipoSeguro.addItem("No seleccionado");
        comboBoxTipoSeguro.setSelectedItem("No seleccionado");
        comboBoxTipoSeguro.setEditable(true);
        
        iniciarAnimacionColor(comboBoxTipoSeguro);


        JLabel labelPrecioSeguro = new JLabel("Precio del Seguro:");
        labelPrecioSeguro.setForeground(texto);
        comboBoxPrecioSeguro = new JComboBox<>();
        comboBoxTipoSeguro.addActionListener(e -> filtrarPreciosSeguros());
        comboBoxPrecioSeguro.setEditable(true);
        
        iniciarAnimacionColor(comboBoxPrecioSeguro);

        panelSuperior.add(labelTipoSeguro);
        panelSuperior.add(comboBoxTipoSeguro);
        panelSuperior.add(labelPrecioSeguro);
        panelSuperior.add(comboBoxPrecioSeguro);

        // Panel Central (Resumen del siniestro)
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(fondo);
        panelCentral.setLayout(new BorderLayout());

        JLabel labelResumen = new JLabel("Resumen del Siniestro:");
        labelResumen.setForeground(texto);
        textAreaResumen = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(textAreaResumen);

        panelCentral.add(labelResumen, BorderLayout.NORTH);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // Panel Inferior (Botón Enviar)
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(fondo);
        panelInferior.setLayout(new FlowLayout());

        JButton botonEnviar = new JButton("Enviar");
        botonEnviar.setBackground(botones);
        botonEnviar.setForeground(Color.WHITE);
        botonEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarSiniestro(bd, dni);
            }
        });

        panelInferior.add(botonEnviar);

        // Añadir paneles a la ventana
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // Inicializar ComboBox de precios
        filtrarPreciosSeguros();
    }

    private void filtrarPreciosSeguros() {
        // Lógica para filtrar los precios según el tipo de seguro seleccionado
        String tipoSeleccionado = (String) comboBoxTipoSeguro.getSelectedItem();
        comboBoxPrecioSeguro.removeAllItems();
//        System.out.println(tipoSeleccionado);
//        System.out.println(preciosPorSeguro);
//        System.out.println(TipoSeguro.valueOf(tipoSeleccionado));
//        System.out.println(preciosPorSeguro.containsKey(TipoSeguro.valueOf(tipoSeleccionado)));

        if (tipoSeleccionado != "No seleccionado" && preciosPorSeguro.containsKey(TipoSeguro.valueOf(tipoSeleccionado))) {
            for (Double precio : preciosPorSeguro.get(TipoSeguro.valueOf(tipoSeleccionado))) {
                comboBoxPrecioSeguro.addItem(precio + "€");
            }
            comboBoxPrecioSeguro.addItem("No seleccionado");
            comboBoxPrecioSeguro.setSelectedItem("No seleccionado");
            
        }
    }

    private void enviarSiniestro(Bdd bd, String dni) {
        String tipoSeguro = (String) comboBoxTipoSeguro.getSelectedItem();
        String precioSeguro = (String) comboBoxPrecioSeguro.getSelectedItem();
        String resumen = textAreaResumen.getText();
        
        if(tipoSeguro.equals("No seleccionado")) {
        	JOptionPane.showMessageDialog(this, "Por favor, elije un tipo de seguro.", "Error", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
        if(precioSeguro.equals("No seleccionado")) {
        	JOptionPane.showMessageDialog(this, "Por favor, elije el precio del seguro del que quiere reportar el siniestro.", "Error", JOptionPane.ERROR_MESSAGE);
        	return;
        }

        if (resumen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, escribe un resumen del siniestro.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String confirmacion = String.format(
        	    "Tipo de Seguro: %s\nPrecio: %s\nResumen: %s\n\n¿Confirma que desea reportar este siniestro?",
        	    tipoSeguro, precioSeguro, resumen
        	);
        int resultado = JOptionPane.showConfirmDialog(
        	    this,
        	    confirmacion,
        	    "Confirmar Siniestro",
        	    JOptionPane.YES_NO_OPTION
        );

        if (resultado == JOptionPane.YES_OPTION) {
        	bd.insertarSiniestro(dni, resumen, TipoSeguro.valueOf(tipoSeguro), "PENDIENTE");
            JOptionPane.showMessageDialog(this, "El siniestro ha sido enviado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Cierra la ventana
        }
    }
    
    private void iniciarAnimacionColor(JComboBox<String> comboBox) {
        new Thread(() -> {
            boolean aumentando = true; // Controla si aumenta o disminuye la intensidad
            int intensidad = 0;        // Intensidad inicial para el componente rojo (R)

            while (true) {
                // Comprobar si está seleccionada la opción "No seleccionado"
                if ("No seleccionado".equals(comboBox.getSelectedItem())) {
                    // Ajustar intensidad (R)
                    if (aumentando) {
                        intensidad += 5;
                        if (intensidad >= 255) aumentando = false; // Límite superior
                    } else {
                        intensidad -= 5;
                        if (intensidad <= 0) aumentando = true; // Límite inferior
                    }

                    // Actualizar el color en el hilo de la interfaz gráfica
                    int finalIntensidad = intensidad;
                    SwingUtilities.invokeLater(() -> {
                        Component editorComponent = comboBox.getEditor().getEditorComponent();
                        if (editorComponent instanceof JTextField) {
                            JTextField textField = (JTextField) editorComponent;
                            textField.setForeground(new Color(finalIntensidad, 102, 102)); // Gradiente de rojo claro
                        }
                    });
                } else {
                    // Resetear el color si cambia la selección
                    SwingUtilities.invokeLater(() -> {
                        Component editorComponent = comboBox.getEditor().getEditorComponent();
                        if (editorComponent instanceof JTextField) {
                            JTextField textField = (JTextField) editorComponent;
                            textField.setForeground(Color.BLACK); // Color por defecto
                        }
                    });
                }

                // Pausa para suavizar la animación
                try {
                    Thread.sleep(15); // Ajusta la velocidad del cambio de color
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}