package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VentanaCliente extends JFrame{
	private JTable tablaSeguros;
    private DefaultTableModel modeloTablaSeguros;
    private JLabel lblCostoTotal;
    private JButton btnActualizarDatos, btnReportarSiniestro, btnChatAtencion;
	
	public VentanaCliente(String nombreCliente, List<Object[]> segurosCliente) {
		 // Configuración básica de la ventana
        setTitle("Aseguradora Bilbao - Cliente");
//        setSize(800, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Layout principal
        setLayout(new BorderLayout());

        // Colores base
        Color colorPrincipal = new Color(0, 51, 102); // Azul oscuro
        Color colorContraste = Color.WHITE;           // Blanco

        // HEADER (NORTE)
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelHeader.setBackground(colorPrincipal);
        JLabel lblBienvenida = new JLabel("Bienvenido, " + nombreCliente);
        lblBienvenida.setForeground(colorContraste);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        panelHeader.add(lblBienvenida);
        add(panelHeader, BorderLayout.NORTH);

        // CENTRO (Tabla de seguros)
        JPanel panelCentro = new JPanel(new BorderLayout());
        String[] columnas = {"Tipo de Seguro", "Fecha de Contratación", "Costo Anual (€)", "Estado"};
        modeloTablaSeguros = new DefaultTableModel(columnas, 0);
        tablaSeguros = new JTable(modeloTablaSeguros);
        tablaSeguros.setRowHeight(25);
        tablaSeguros.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaSeguros.setSelectionBackground(new Color(51, 153, 255)); // Azul claro
        JScrollPane scrollTabla = new JScrollPane(tablaSeguros);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);
	}
	
	

}