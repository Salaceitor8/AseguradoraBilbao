package main;

import java.awt.*;
import javax.swing.*;

public class ModoOscuroUtil {

    // Método para aplicar transición suave al modo oscuro
    public static void aplicarModoOscuro(JFrame frame, boolean activar) {
        Color inicio = activar ? Color.WHITE : Color.DARK_GRAY;
        Color fin = activar ? Color.DARK_GRAY : Color.WHITE;

        new Thread(() -> {
            for (int i = 0; i <= 100; i++) { // 100 pasos de transición
                float ratio = i / 100.0f;

                // Interpolar colores
                int red = (int) (inicio.getRed() * (1 - ratio) + fin.getRed() * ratio);
                int green = (int) (inicio.getGreen() * (1 - ratio) + fin.getGreen() * ratio);
                int blue = (int) (inicio.getBlue() * (1 - ratio) + fin.getBlue() * ratio);
                Color intermedio = new Color(red, green, blue);

                // Aplicar color interpolado al fondo y texto
                SwingUtilities.invokeLater(() -> {
                    frame.getContentPane().setBackground(intermedio);
                    for (Component component : frame.getContentPane().getComponents()) {
                        if (component instanceof JComponent) {
                            aplicarEstiloComponente((JComponent) component, intermedio, activar ? Color.WHITE : Color.BLACK);
                        }
                    }
                    frame.repaint();
                });

                // Retraso para la transición
                try {
                    Thread.sleep(10); // 10 ms por paso (ajustable)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    // Método auxiliar para aplicar estilos a un componente y sus subcomponentes
    private static void aplicarEstiloComponente(JComponent component, Color fondo, Color texto) {
        component.setBackground(fondo);
        component.setForeground(texto);

        for (Component subComponent : component.getComponents()) {
            if (subComponent instanceof JComponent) {
                aplicarEstiloComponente((JComponent) subComponent, fondo, texto);
            }
        }
    }
}