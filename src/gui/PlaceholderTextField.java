package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PlaceholderTextField extends JTextField {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String placeholder;

    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;
        setForeground(Color.GRAY);  // Color del placeholder

        // Añadir el FocusListener para el comportamiento del placeholder
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                handleFocusGained();
            }

            @Override
            public void focusLost(FocusEvent e) {
                handleFocusLost();
            }
        });

        // Inicialmente mostrar el placeholder
        setText(placeholder);
    }

    // Método para manejar cuando el campo obtiene el foco
    private void handleFocusGained() {
        if (isPlaceholderDisplayed()) {
            setText("");
            setForeground(Color.BLACK);  // Cambiar el color del texto al escribir
        }
    }

    // Método para manejar cuando el campo pierde el foco
    private void handleFocusLost() {
        if (getText().isEmpty()) {
            setPlaceholderVisible();
        }
    }

    // Verifica si el placeholder está siendo mostrado
    private boolean isPlaceholderDisplayed() {
        return getText().equals(placeholder);
    }

    // Muestra el placeholder y lo estiliza adecuadamente
    private void setPlaceholderVisible() {
        setForeground(Color.GRAY);
        setText(placeholder);
    }

    // Método opcional para cambiar el placeholder en cualquier momento
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (getText().isEmpty() || isPlaceholderDisplayed()) {
            setPlaceholderVisible();
        }
    }
}
