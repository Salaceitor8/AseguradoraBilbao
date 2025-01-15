package main;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import domain.Encuesta;

import java.awt.*;
import java.util.List;

public class VentanaResultadosEncuestas extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tablaEncuestas;
    private DefaultTableModel modeloTabla;
    private JPanel panelGraficos;
    private JButton btnActualizar;
    private JButton btnCerrar;

    public VentanaResultadosEncuestas(Bdd bd) {
        // Configuración básica
        setTitle("Resultados de Encuestas");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Colores
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorFondo = Color.WHITE;

        // Panel superior: Título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(colorPrincipal);
        JLabel titulo = new JLabel("Resultados de Encuestas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        panelTitulo.add(titulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel central: Dividido en dos mitades (tabla y gráficos)
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(1, 2, 10, 10)); // Dividido en dos columnas
        panelCentral.setBackground(colorFondo);

     // Panel izquierdo: Tabla
        String[] columnas = {"DNI Cliente", "Fecha", "Satisfecho", "Aspecto Favorito", "Valoración", "Comentario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición de las celdas
            }
        };
        DefaultTableCellRenderer centradoRenderer = new DefaultTableCellRenderer();
        centradoRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        tablaEncuestas = new JTable(modeloTabla);
        tablaEncuestas.setRowHeight(25);
        tablaEncuestas.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaEncuestas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaEncuestas.getTableHeader().setBackground(colorPrincipal);
        tablaEncuestas.getTableHeader().setForeground(Color.WHITE);
        TableColumnModel columnModel = tablaEncuestas.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10); 
        columnModel.getColumn(0).setCellRenderer(centradoRenderer);
        columnModel.getColumn(1).setPreferredWidth(10);
        columnModel.getColumn(1).setCellRenderer(centradoRenderer);
        columnModel.getColumn(2).setPreferredWidth(10);
        columnModel.getColumn(2).setCellRenderer(centradoRenderer);
        columnModel.getColumn(3).setPreferredWidth(25);
        columnModel.getColumn(3).setCellRenderer(centradoRenderer);
        columnModel.getColumn(4).setPreferredWidth(5); 
        columnModel.getColumn(4).setCellRenderer(centradoRenderer);
        JScrollPane scrollTabla = new JScrollPane(tablaEncuestas);
        tablaEncuestas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tablaEncuestas.rowAtPoint(e.getPoint());
                int columna = tablaEncuestas.columnAtPoint(e.getPoint());

                // Verificar si se hizo clic en la columna de "Comentario"
                if (columna == 5) { // Índice de la columna de comentarios
                    Object comentario = modeloTabla.getValueAt(fila, columna);
                    if (comentario != null) {
                        // Mostrar el comentario completo en un cuadro de diálogo
                        JOptionPane.showMessageDialog(
                            tablaEncuestas,
                            comentario.toString(),
                            "Comentario completo",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });

        panelCentral.add(scrollTabla);

        // Panel derecho: Gráficos
        panelGraficos = new JPanel();
        panelGraficos.setLayout(new GridLayout(2, 1, 10, 10)); // Dos gráficos (barras y pastel)
        panelGraficos.setBackground(colorFondo);
        panelGraficos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentral.add(panelGraficos);

        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(colorFondo);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnActualizar.setBackground(new Color(0, 153, 76)); // Verde
        btnActualizar.setForeground(Color.WHITE);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(204, 0, 0)); // Rojo
        btnCerrar.setForeground(Color.WHITE);

        panelBotones.add(btnActualizar);
        add(panelBotones, BorderLayout.SOUTH);

        // Listeners de botones
        btnActualizar.addActionListener(e -> actualizarResultados(bd));

        // Cargar datos iniciales
        actualizarResultados(bd);

        setVisible(true);
    }

    private void actualizarResultados(Bdd bd) {
        // Limpiar la tabla
        modeloTabla.setRowCount(0);

        // Obtener encuestas desde la base de datos
        List<Encuesta> encuestas = bd.obtenerEncuestas();
        for (Encuesta e : encuestas) {
            modeloTabla.addRow(new Object[]{
                e.getDniCliente(),
                e.getFecha(),
                e.getSatisfecho(),
                e.getAspectoFavorito(),
                e.getValoracion(),
                e.getComentario()
            });
        }

        // Actualizar gráficos
        actualizarGraficos(encuestas);
    }

    private void actualizarGraficos(List<Encuesta> encuestas) {
        panelGraficos.removeAll(); // Limpiar gráficos previos

        // Aquí irían los métodos para agregar gráficos
        JPanel graficoBarras = crearGraficoBarras(encuestas);
        panelGraficos.add(graficoBarras);

        JPanel graficoPastel = crearGraficoPastel(encuestas);
        panelGraficos.add(graficoPastel);

        panelGraficos.revalidate();
        panelGraficos.repaint();
    }

    // Métodos para los gráficos (pendiente implementación)
    private JPanel crearGraficoBarras(List<Encuesta> encuestas) {
        // Crear el dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double sumaValoraciones = 0;
        int totalEncuestas = 0;

        for (Encuesta encuesta : encuestas) {
            sumaValoraciones += encuesta.getValoracion();
            totalEncuestas++;
        }

        double promedioValoracion = totalEncuestas > 0 ? sumaValoraciones / totalEncuestas : 0;

        dataset.addValue(promedioValoracion, "Promedio", "Valoración");

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createBarChart(
                "Promedio de Valoraciones",  // Título
                "Categoría",                // Etiqueta del eje X
                "Promedio",                 // Etiqueta del eje Y
                dataset                     // Dataset
        );

        // Configurar el rango del eje Y
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 5); // Fijar el rango del eje Y entre 0 y 5

        // Opcional: Cambiar el color del fondo
        plot.setBackgroundPaint(Color.WHITE);

        // Crear el panel para mostrar el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearGraficoPastel(List<Encuesta> encuestas) {
        // Crear el dataset
        DefaultPieDataset dataset = new DefaultPieDataset();
        int totalSi = 0;
        int totalNo = 0;

        for (Encuesta encuesta : encuestas) {
            if ("Sí".equalsIgnoreCase(encuesta.getSatisfecho())) {
                totalSi++;
            } else if ("No".equalsIgnoreCase(encuesta.getSatisfecho())) {
                totalNo++;
            }
        }

        dataset.setValue("Sí", totalSi);
        dataset.setValue("No", totalNo);

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createPieChart(
                "Satisfacción de Clientes", // Título
                dataset,                   // Dataset
                true,                      // Leyenda
                true,                      // Tooltips
                false                      // URLs
        );

        // Crear el panel para mostrar el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }


}
