package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SeguroCellRenderer extends DefaultTableCellRenderer {

    // Formato para mostrar la fecha en la tabla
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Verificar si el valor es una fecha y formatearla
        if (value instanceof LocalDate) {
            LocalDate fecha = (LocalDate) value;
            setText(fecha.format(dateFormatter));  // Formatear la fecha como dd/MM/yyyy
        } else {
            setText(value != null ? value.toString() : "");  // Dejar otros valores sin cambios
        }

        // Obtener el estado del seguro (asumiendo que está en la última columna de la tabla)
        String estadoSeguro = table.getValueAt(row, table.getColumnCount() - 1).toString();

        // Si el estado es "Inactivo", poner el texto en rojo
        if (estadoSeguro.equalsIgnoreCase("Inactivo")) {
            cellComponent.setForeground(Color.RED);
        } else {
            cellComponent.setForeground(Color.BLACK);  // Devolver el color normal si está activo
        }

        // Aplicar color de fondo según selección o alternado
        if (isSelected) {
            cellComponent.setBackground(Color.LIGHT_GRAY); // Color al seleccionar
        } else {
            // Colores alternados para las filas
            if (row % 2 == 0) {
                cellComponent.setBackground(Color.WHITE); // Fondo para filas pares
            } else {
                cellComponent.setBackground(new Color(240, 240, 240)); // Fondo para filas impares
            }
        }

        return cellComponent;
    }
}
