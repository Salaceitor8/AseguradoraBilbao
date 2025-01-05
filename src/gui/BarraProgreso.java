package gui;

import javax.swing.*;
import java.awt.*;

public class BarraProgreso extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BarraProgreso(JFrame parent) {
        super(parent, "Cargando Seguros...", true); // true para que sea modal
        setSize(400, 120);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.CENTER);

        Thread progressThread = new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int progress = i;
                SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
            }
            dispose(); // Cierra el diálogo al completar
        });

        progressThread.start();
    }
}
