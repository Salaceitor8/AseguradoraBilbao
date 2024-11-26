package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PlaceholderPasswordField extends JPasswordField {

    private String placeholder;

    public PlaceholderPasswordField(String placeholder) {
        this.placeholder = placeholder;
        setForeground(Color.GRAY);  // Color del placeholder
        setEchoChar((char) 0); // Mostrar texto normal mientras es el placeholder

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

    private void handleFocusGained() {
        if (isPlaceholderDisplayed()) {
            setText("");
            setForeground(Color.BLACK);  // Cambiar el color del texto al escribir
            setEchoChar('*'); // Cambiar a modo de contraseña (asteriscos)
        }
    }

    private void handleFocusLost() {
        if (getPassword().length == 0) {
            setPlaceholderVisible();
        }
    }

    private boolean isPlaceholderDisplayed() {
        return new String(getPassword()).equals(placeholder);
    }

    private void setPlaceholderVisible() {
        setForeground(Color.GRAY);
        setText(placeholder);
        setEchoChar((char) 0); // Mostrar texto normal sin asteriscos
    }

    // Método opcional para cambiar el placeholder en cualquier momento
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        if (getPassword().length == 0) {
            setPlaceholderVisible();
        }
    }
}
