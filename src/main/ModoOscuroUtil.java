package main;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class ModoOscuroUtil {
	
	// Mapa para guardar los colores originales de los componentes
    private static final Map<JComponent, Color[]> coloresOriginales = new HashMap<>();

    // Método para aplicar transición suave al modo oscuro
    public static void aplicarModoOscuro(JFrame frame, boolean activar) {
        Color inicio = activar ? Color.WHITE : Color.DARK_GRAY;
        Color fin = activar ? Color.DARK_GRAY : Color.WHITE;
        
     // Guardar colores originales antes de aplicar el modo oscuro
        if (activar) {
            guardarColoresOriginales(frame.getContentPane());
        }


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
            
         // Restaurar colores originales si se desactiva el modo oscuro
            if (!activar) {
                restaurarColoresOriginales(frame.getContentPane());
            }
        }).start();
    }
    
 // Método para guardar los colores originales de los componentes
    private static void guardarColoresOriginales(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JComponent) {
                JComponent jComponent = (JComponent) component;
                if (!coloresOriginales.containsKey(jComponent)) {
                    coloresOriginales.put(jComponent, new Color[]{
                        jComponent.getBackground(),
                        jComponent.getForeground()
                    });
                }
                if (component instanceof Container) {
                    guardarColoresOriginales((Container) component);
                }
            }
        }
    }

    // Método para restaurar los colores originales de los componentes
    private static void restaurarColoresOriginales(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JComponent) {
                JComponent jComponent = (JComponent) component;
                Color[] colores = coloresOriginales.get(jComponent);
                if (colores != null) {
                    jComponent.setBackground(colores[0]);
                    jComponent.setForeground(colores[1]);
                }
                if (component instanceof Container) {
                    restaurarColoresOriginales((Container) component);
                }
            }
        }
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