package main;

import java.awt.*;
import javax.swing.*;

public class ModoOscuroUtil {
    public static void aplicarModoOscuro(JFrame frame, boolean activar) {
        // Cambiar colores del JFrame
        if (activar) {
            frame.getContentPane().setBackground(Color.DARK_GRAY);
        } else {
            frame.getContentPane().setBackground(Color.WHITE);
        }

        // Cambiar colores de los componentes dentro del JFrame
        for (Component component : frame.getContentPane().getComponents()) {
            if (component instanceof JComponent) {
                aplicarEstiloComponente((JComponent) component, activar);
            }
        }

        // Actualizar la ventana
        frame.repaint();
        frame.revalidate();
    }

    private static void aplicarEstiloComponente(JComponent component, boolean activar) {
        if (activar) {
            component.setBackground(Color.DARK_GRAY);
            component.setForeground(Color.WHITE);
        } else {
            component.setBackground(Color.WHITE);
            component.setForeground(Color.BLACK);
        }

        // Recursivamente, aplica estilo a los subcomponentes
        for (Component subComponent : component.getComponents()) {
            if (subComponent instanceof JComponent) {
                aplicarEstiloComponente((JComponent) subComponent, activar);
            }
        }
    }
}