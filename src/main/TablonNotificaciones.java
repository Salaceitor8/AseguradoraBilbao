package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import domain.Notificacion;

public class TablonNotificaciones extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tablaNotificaciones;
    private DefaultTableModel modeloTabla;

    public TablonNotificaciones(String dniCliente, Bdd baseDeDatos) {
        // Configuración básica de la ventana
        setTitle("Tablón de Notificaciones");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Colores
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorContraste = Color.WHITE;           // Blanco
        Color colorTablaHeader = new Color(0, 102, 204); // Azul claro

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(colorPrincipal);
        JLabel titulo = new JLabel("Tablón de Notificaciones");
        titulo.setForeground(colorContraste);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titulo);
        add(headerPanel, BorderLayout.NORTH);

        // Modelo y tabla de notificaciones
        String[] columnas = {"Resumen"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición de las celdas
            }
        };

        tablaNotificaciones = new JTable(modeloTabla);
        tablaNotificaciones.setRowHeight(50); // Ajusta la altura de las filas para que se vea más texto
        tablaNotificaciones.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaNotificaciones.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaNotificaciones.getTableHeader().setBackground(colorTablaHeader);
        tablaNotificaciones.getTableHeader().setForeground(colorContraste);

        // Renderizador para múltiples líneas en las celdas
        tablaNotificaciones.setDefaultRenderer(Object.class, new MultiLineCellRenderer());

        JScrollPane scrollPane = new JScrollPane(tablaNotificaciones);
        add(scrollPane, BorderLayout.CENTER);

        // Cargar notificaciones
        cargarNotificaciones(dniCliente, baseDeDatos);

        // Botón para cerrar
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(colorContraste);
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setBackground(colorPrincipal);
        btnCerrar.setForeground(colorContraste);
        btnCerrar.addActionListener(e -> dispose());
        footerPanel.add(btnCerrar);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarNotificaciones(String dniCliente, Bdd baseDeDatos) {
        ArrayList<Notificacion> notificaciones = baseDeDatos.obtenerNotificacionesPorCliente(dniCliente);
        if (notificaciones != null) {
            for (Notificacion n : notificaciones) {
                modeloTabla.addRow(new Object[]{n.getResumen()});
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar las notificaciones.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Renderizador personalizado para celdas con múltiples líneas
    static class MultiLineCellRenderer extends DefaultTableCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JTextArea textArea = new JTextArea(value != null ? value.toString() : "");
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setFont(new Font("Arial", Font.PLAIN, 14));

            if (isSelected) {
                textArea.setBackground(table.getSelectionBackground());
                textArea.setForeground(table.getSelectionForeground());
            } else {
                textArea.setBackground(table.getBackground());
                textArea.setForeground(table.getForeground());
            }

            return textArea;
        }
    }

    public static void main(String[] args) {
        // Ejemplo de ejecución (necesitarás ajustar la conexión a tu base de datos)
        Bdd baseDeDatos = new Bdd("resources/db/aseguradora.db");
        new TablonNotificaciones("10000001A", baseDeDatos);
    }
}