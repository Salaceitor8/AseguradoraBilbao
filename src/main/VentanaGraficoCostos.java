package main;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class VentanaGraficoCostos extends JFrame {

    public VentanaGraficoCostos(double costoCoche, double costoVida, double costoHogar) {
        // Configuración de la ventana
        setTitle("Gráfico Comparativo de Costos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el conjunto de datos para el gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(costoCoche, "Costo (€)", "Coche");
        dataset.addValue(costoVida, "Costo (€)", "Vida");
        dataset.addValue(costoHogar, "Costo (€)", "Hogar");

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createBarChart(
                "Comparación de Costos por Tipo de Seguro", // Título del gráfico
                "Tipo de Seguro",                          // Etiqueta del eje X
                "Costo (€)",                               // Etiqueta del eje Y
                dataset                                    // Datos del gráfico
        );

        // Crear un panel para mostrar el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setMouseWheelEnabled(true); // Permitir zoom con la rueda del ratón

        // Agregar el gráfico a la ventana
        add(chartPanel, BorderLayout.CENTER);
    }

    // Método principal para probar el gráfico
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Ejemplo de costos para probar
            VentanaGraficoCostos ventana = new VentanaGraficoCostos(400.0, 300.0, 200.0);
            ventana.setVisible(true);
        });
    }
}
