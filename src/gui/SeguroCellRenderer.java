package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SeguroCellRenderer extends DefaultTableCellRenderer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
        String estadoSeguro = table.getValueAt(row, table.getColumnCount() - 2).toString();

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
        
//        if(table.getValueAt(row, table.getColumnCount()-1).toString() == "ESTANDAR") {
//        	System.out.println(table.getValueAt(row, table.getColumnCount()-1).toString());
//        	table.setValueAt("Cobertura Estandar", row, table.getColumnCount()-1);
//        }else if(table.getValueAt(row, table.getColumnCount()-1).toString() == "PLUS") {
//        	table.setValueAt("Cobertura Plus", row, table.getColumnCount()-1);
//        }else if(table.getValueAt(row, table.getColumnCount()-1).toString() == "FALLECIMIENTO") {
//        	table.setValueAt("Fallecimiento", row, table.getColumnCount()-1);
//        }else if(table.getValueAt(row, table.getColumnCount()-1).toString() == "FYINVALIDEZ") {
//        	table.setValueAt("Fallecimiento y Invalidez", row, table.getColumnCount()-1);
//        }else if(table.getValueAt(row, table.getColumnCount()-1).toString() == "TERCEROS") {
//        	table.setValueAt("A Terceros", row, table.getColumnCount()-1);
//        }else {
//        	System.out.println(table.getValueAt(row, table.getColumnCount()-1).toString());
//
//        	table.setValueAt("A Todo Riesgo", row, table.getColumnCount()-1);
//        }

        return cellComponent;
    }
}
