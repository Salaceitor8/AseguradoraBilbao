package main;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;



public class VentanaSimuladorCostos extends JFrame {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtEdad, txtMonto;
    private JComboBox<String> cmbTipoSeguro;
    private JComboBox<Integer> cmbDuracion;
    private JLabel lblResultado, lblRecomendacion;
    private DefaultCategoryDataset dataset;
    private ChartPanel chartPanel; // Panel que muestra el gráfico
    
    
    public VentanaSimuladorCostos() {
        // Configuración de la ventana principal
        setTitle("Simulador de Costos de Seguros");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        
        // Inicializar el dataset para el gráfico
        dataset = new DefaultCategoryDataset();

        
        // Configuración de los controles visuales
        JLabel lblEdad = new JLabel("Edad:");
        txtEdad = new JTextField(10);
        JLabel lblTipoSeguro = new JLabel("Tipo de Seguro:");
        cmbTipoSeguro = new JComboBox<>(new String[]{"Coche", "Vida", "Hogar"});
        JLabel lblMonto = new JLabel("Monto Asegurado:");
        txtMonto = new JTextField(10);
        JLabel lblDuracion = new JLabel("Duración (años):");
        cmbDuracion = new JComboBox<>(new Integer[]{1, 2, 5, 10});
        JButton btnCalcular = new JButton("Calcular");
        lblResultado = new JLabel("Costo Total: $0.00");
        lblRecomendacion = new JLabel("Recomendación: ");
        JButton btnMostrarGrafico = new JButton("Mostrar Gráfico");

        
        // Crear el gráfico inicial
        chartPanel = crearGraficoInicial();

        
        // Crear el panel principal con diseño
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10)); // 7 filas, 2 columnas
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen alrededor del panel

        
        // Añadir controles al panel
        panel.add(lblEdad);
        panel.add(txtEdad);
        panel.add(lblTipoSeguro);
        panel.add(cmbTipoSeguro);
        panel.add(lblMonto);
        panel.add(txtMonto);
        panel.add(lblDuracion);
        panel.add(cmbDuracion);
        panel.add(btnCalcular);
        panel.add(lblResultado);

        
        // Layout principal de la ventana
        setLayout(new BorderLayout());
        add(panel, BorderLayout.WEST);        // Panel de controles a la izquierda
        add(chartPanel, BorderLayout.CENTER); // Gráfico al centro
        add(lblRecomendacion, BorderLayout.SOUTH); // Recomendación en la parte inferior

        
        // Acción del botón Calcular
        btnCalcular.addActionListener(e -> actualizarDatosYGrafico());

        
        // Acción del botón Mostrar Gráfico
        btnMostrarGrafico.addActionListener(e -> {
            VentanaGraficoCostos grafico = new VentanaGraficoCostos(dataset);
            grafico.setVisible(true);
        });
    

        
     // Llamar a pruebaInicial() para cargar datos en el gráfico
        pruebaInicial();
    }
    
    
    

    // Método para actualizar los datos y el gráfico
    private void actualizarDatosYGrafico() {
        if (!validarEntradas()) {
            return; // Detener el proceso si las entradas no son válidas
        }

        try {
            // Obtener los valores ingresados
            int edad = Integer.parseInt(txtEdad.getText());
            double monto = Double.parseDouble(txtMonto.getText());
            int duracion = (Integer) cmbDuracion.getSelectedItem();

            
            // Calcular los costos para diferentes tipos de seguros
            double costoCoche = calcularCosto("Coche", edad, monto, duracion);
            double costoVida = calcularCosto("Vida", edad, monto, duracion);
            double costoHogar = calcularCosto("Hogar", edad, monto, duracion);

            
            // Validar que al menos uno de los costos sea válido
            if (costoCoche <= 0 && costoVida <= 0 && costoHogar <= 0) {
                JOptionPane.showMessageDialog(this, "Los valores calculados no son válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            // Actualizar el dataset del gráfico
            dataset.clear();
            dataset.addValue(costoCoche, "Costo (€)", "Coche");
            dataset.addValue(costoVida, "Costo (€)", "Vida");
            dataset.addValue(costoHogar, "Costo (€)", "Hogar");

            
            // Redibujar el gráfico en la ventana principal
            chartPanel.revalidate();
            chartPanel.repaint();

            // Actualizar la recomendación
            actualizarRecomendacion(costoCoche, costoVida, costoHogar);

            
            // Mostrar el resultado en el lblResultado
            lblResultado.setText(String.format("Costo Total (Coche): $%.2f", costoCoche));

        } catch (Exception ex) {
            ex.printStackTrace(); // Mostrar el error en consola para depuración
            JOptionPane.showMessageDialog(this, "Ocurrió un error al calcular los costos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    
    
    // Método para actualizar las recomendaciones
    
    private void actualizarRecomendacion(double costoCoche, double costoVida, double costoHogar) {
        String recomendacion;

        // Introducir una lógica aleatoria para diversificar recomendaciones
        double[] costos = {costoCoche, costoVida, costoHogar};
        String[] tipos = {"Coche", "Vida", "Hogar"};

        int indiceRecomendado = obtenerIndiceRecomendacion(costos, tipos);

        switch (indiceRecomendado) {
            case 0:
                recomendacion = "Recomendación: Contratar un seguro de Coche por ser económico y útil para conductores frecuentes.";
                break;
            case 1:
                recomendacion = "Recomendación: Contratar un seguro de Vida por ofrecer protección financiera a largo plazo.";
                break;
            case 2:
                recomendacion = "Recomendación: Contratar un seguro de Hogar por brindar seguridad al patrimonio familiar.";
                break;
            default:
                recomendacion = "Recomendación: Evalúa las opciones disponibles para encontrar la mejor cobertura.";
                break;
        }

        lblRecomendacion.setText(recomendacion);
    }

    private int obtenerIndiceRecomendacion(double[] costos, String[] tipos) {
        // Lógica para decidir la recomendación

        // Por ejemplo, elegir una recomendación aleatoria entre los costos más bajos
        double costoMinimo = Double.MAX_VALUE;
        int indiceMinimo = -1;

        for (int i = 0; i < costos.length; i++) {
            if (costos[i] < costoMinimo && costos[i] > 0) {
                costoMinimo = costos[i];
                indiceMinimo = i;
            }
        }

        // Si tienes más criterios, puedes introducirlos aquí
        // Por ejemplo, aleatorizar entre seguros con costos similares
        if (indiceMinimo != -1) {
            return indiceMinimo; // Retorna el índice del más barato como fallback
        }

        // Valor predeterminado si no hay una recomendación clara
        return (int) (Math.random() * costos.length);
    }



    
    
    // Método para crear el gráfico inicial
    private ChartPanel crearGraficoInicial() {
        JFreeChart chart = ChartFactory.createBarChart(
                "Comparación de Costos por Tipo de Seguro",
                "Tipo de Seguro",
                "Costo (€)",
                dataset
        );
        
        

        // Crear el ChartPanel con las opciones de interacción habilitadas
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Permitir zoom con la rueda del ratón
        chartPanel.setPreferredSize(new Dimension(400, 300));
        return chartPanel;
    }

    
    
    // Método para calcular el costo del seguro
    private double calcularCosto(String tipoSeguro, int edad, double monto, int duracion) {
        double base = obtenerCostoBase(tipoSeguro); // Obtener costo base según el tipo de seguro
        if (base <= 0) {
            return 0; // Si el costo base no es válido, devolver 0
        }

        double ajusteEdad = (edad > 50) ? 1.2 : 1.0; // Aumentar 20% si la edad es mayor a 50
        double ajusteMonto = monto / 10000; // Ajustar por bloques de 10,000
        double ajusteDuracion;
        if (duracion == 1) {
            ajusteDuracion = 1.0; // Sin descuento
        } else if (duracion == 2) {
            ajusteDuracion = 0.95; // 5% de descuento
        } else if (duracion == 5) {
            ajusteDuracion = 0.90; // 10% de descuento
        } else if (duracion == 10) {
            ajusteDuracion = 0.85; // 15% de descuento
        } else {
            ajusteDuracion = 1.0; // Sin descuento para valores fuera del rango
        }

        
        double costoFinal = base * ajusteEdad * ajusteMonto * ajusteDuracion;
        return Math.max(costoFinal, 0); // Asegurar que nunca devuelva un valor negativo
    }

    
    
    private boolean validarEntradas() {
        try {
            // Verificar que la edad es un número válido
            int edad = Integer.parseInt(txtEdad.getText());
            if (edad <= 0) {
                JOptionPane.showMessageDialog(this, "La edad debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            
            // Verificar que el monto asegurado es un número válido
            double monto = Double.parseDouble(txtMonto.getText());
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto asegurado debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Verificar que se selecciona una duración válida
            if (cmbDuracion.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una duración válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }



    // Método simulado para obtener el costo base según el tipo de seguro
    private double obtenerCostoBase(String tipoSeguro) {
        switch (tipoSeguro) {
            case "Coche":
                return 400.0;
            case "Vida":
                return 300.0;
            case "Hogar":
                return 200.0;
            default:
                return 0.0;
        }
    }
    
 
    // Método de prueba inicial para verificar el gráfico
    private void pruebaInicial() {
        dataset.clear();
        dataset.addValue(400, "Costo (€)", "Coche");
        dataset.addValue(300, "Costo (€)", "Vida");
        dataset.addValue(200, "Costo (€)", "Hogar");
    }

    


}
