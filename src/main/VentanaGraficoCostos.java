package main;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class VentanaGraficoCostos extends JFrame {

	
    public VentanaGraficoCostos(DefaultCategoryDataset dataset) {
        
    	
    	// Configuración de la ventana
        setTitle("Gráfico Comparativo de Costos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        
        
        // Crear el gráfico
        JFreeChart chart = ChartFactory.createBarChart(
                "Comparación de Costos por Tipo de Seguro", // Título del gráfico
                "Tipo de Seguro",                          // Etiqueta del eje X
                "Costo (€)",                               // Etiqueta del eje Y
                dataset                                    // Datos del gráfico
        );

        
        
        // Crear un panel para mostrar el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // Permitir zoom con la rueda del ratón
        chartPanel.setPreferredSize(new Dimension(800, 600));

        
        // Añadir el gráfico al contenedor principal de la ventana
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
    }
}
