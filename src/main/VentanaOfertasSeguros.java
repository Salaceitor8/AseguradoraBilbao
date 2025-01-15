package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaOfertasSeguros extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VentanaOfertasSeguros() {
        setTitle("Ofertas de Seguros - Aseguradoras Bilbao");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de encabezado
        JLabel titulo = new JLabel("Ofertas Exclusivas de Seguros", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(0, 51, 102));
        add(titulo, BorderLayout.NORTH);

        // Panel de contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new GridLayout(0, 1, 10, 10)); // Una columna con filas dinámicas
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JScrollPane scrollPane = new JScrollPane(panelContenido);
        add(scrollPane, BorderLayout.CENTER);

        // Ofertas de seguros
        String[][] ofertas = {
            {"Seguro de Hogar", "Protege tu hogar desde 15€/mes"},
            {"Seguro de Coche", "Cobertura completa desde 25€/mes"},
            {"Seguro de Vida", "Tranquilidad para tu familia desde 10€/mes"},
        };

        // Agregar ofertas al panel
        for (String[] oferta : ofertas) {
            JPanel ofertaPanel = new JPanel(new BorderLayout());
            ofertaPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 102), 2));
            ofertaPanel.setBackground(Color.WHITE);

            JLabel tituloOferta = new JLabel(oferta[0]);
            tituloOferta.setFont(new Font("Arial", Font.BOLD, 16));
            tituloOferta.setForeground(new Color(0, 51, 102));

            JLabel descripcionOferta = new JLabel(oferta[1]);
            descripcionOferta.setFont(new Font("Arial", Font.PLAIN, 14));

            ofertaPanel.add(tituloOferta, BorderLayout.NORTH);
            ofertaPanel.add(descripcionOferta, BorderLayout.CENTER);

            panelContenido.add(ofertaPanel);
        }
        JButton contratar = new JButton("Contratar");
        contratar.setBackground(new Color(0, 51, 102));
        contratar.setForeground(Color.WHITE);
        contratar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
        		JOptionPane.showMessageDialog(panelContenido, "Tu asesor se llama Carlos, llama al 900-123-456 y el te atenderá");
        		dispose();
			}
		});
        panelContenido.add(contratar);

        setVisible(true);
    }


}
