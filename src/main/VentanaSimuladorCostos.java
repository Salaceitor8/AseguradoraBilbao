package main;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaSimuladorCostos extends JFrame {

    public VentanaSimuladorCostos() {
        // Configuración de la ventana principal
        setTitle("Simulador de Costos de Seguros");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        // Crear controles
        JLabel lblEdad = new JLabel("Edad:");
        JTextField txtEdad = new JTextField(10);
        JLabel lblTipoSeguro = new JLabel("Tipo de Seguro:");
        JComboBox<String> cmbTipoSeguro = new JComboBox<>(new String[]{"Coche", "Vida", "Hogar"});
        JLabel lblMonto = new JLabel("Monto Asegurado:");
        JTextField txtMonto = new JTextField(10);
        JLabel lblDuracion = new JLabel("Duración (años):");
        JComboBox<Integer> cmbDuracion = new JComboBox<>(new Integer[]{1, 2, 5, 10});
        JButton btnCalcular = new JButton("Calcular");
        JLabel lblResultado = new JLabel("Costo Total: $0.00");
        JButton btnMostrarGrafico = new JButton("Mostrar Gráfico"); // Nuevo botón

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
        panel.add(new JLabel()); // Espacio vacío
        panel.add(lblResultado);
        panel.add(btnMostrarGrafico); // Añadir el nuevo botón

        // Acción del botón Calcular
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Obtener los valores ingresados
                    int edad = Integer.parseInt(txtEdad.getText());
                    String tipoSeguro = (String) cmbTipoSeguro.getSelectedItem();
                    double monto = Double.parseDouble(txtMonto.getText());
                    int duracion = (Integer) cmbDuracion.getSelectedItem();

                    // Calcular el costo total
                    double costo = calcularCosto(tipoSeguro, edad, monto, duracion);

                    // Mostrar el resultado
                    lblResultado.setText(String.format("Costo Total: $%.2f", costo));
                } catch (Exception ex) {
                    lblResultado.setText("Error en los datos ingresados.");
                }
            }
        });

        // Acción del botón Mostrar Gráfico
        btnMostrarGrafico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int edad = Integer.parseInt(txtEdad.getText());
                    double monto = Double.parseDouble(txtMonto.getText());
                    int duracion = (Integer) cmbDuracion.getSelectedItem();

                    // Calcular costos para diferentes tipos de seguro
                    double costoCoche = calcularCosto("Coche", edad, monto, duracion);
                    double costoVida = calcularCosto("Vida", edad, monto, duracion);
                    double costoHogar = calcularCosto("Hogar", edad, monto, duracion);

                    // Crear y mostrar el gráfico
                    VentanaGraficoCostos grafico = new VentanaGraficoCostos(costoCoche, costoVida, costoHogar);
                    grafico.setVisible(true);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaSimuladorCostos.this, "Error en los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Añadir el panel a la ventana
        add(panel);
    }

    // Método para calcular el costo del seguro
    private double calcularCosto(String tipoSeguro, int edad, double monto, int duracion) {
        double base = obtenerCostoBase(tipoSeguro); // Obtener costo base según el tipo de seguro
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
        return base * ajusteEdad * ajusteMonto * ajusteDuracion;
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



    // Método principal para ejecutar la aplicación
  //  public static void main(String[] args) {
    //    SwingUtilities.invokeLater(() -> {
      //      VentanaSimuladorCostos ventana = new VentanaSimuladorCostos();
        //    ventana.setVisible(true);
      //  });
   // }
}

