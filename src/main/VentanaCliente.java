package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import domain.Cliente;
import domain.Seguro;
import domain.TipoSeguro;

public class VentanaCliente extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tablaSeguros;
    private DefaultTableModel modeloTablaSeguros;
    private JLabel lblCostoTotal;
    private JButton  btnReportarSiniestro, btnChatAtencion, btnMiPerfil;
    
    public VentanaCliente(String nombreCliente, List<Seguro> segurosCliente, Bdd bd, String dni) {
        // Configuración básica de la ventana
        setTitle("Aseguradora Bilbao - Cliente");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                bd.cerrarConexion();
                System.out.println("La conexión a la base de datos se ha cerrado.");
            }
        });

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

        // Llenar tabla con datos de seguros
        for (Seguro s : segurosCliente) {
        	Object[] o = new Object[] {
        			s.getTipo().toString(),
        			s.getFechaContratacionFormato(),
        			s.getCostoMensual(),
        			s.getEstado()
        	};
            modeloTablaSeguros.addRow(o);;
        }

        add(panelCentro, BorderLayout.CENTER);

        // ESTE (Botones de acción)
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelOpciones.setBackground(Color.WHITE);
        
        btnMiPerfil = new JButton("Mi perfil");
        btnReportarSiniestro = new JButton("Reportar Siniestro");
        btnChatAtencion = new JButton("Atención al Cliente");

        // Estilo de los botones
        JButton[] botones = {btnMiPerfil, btnReportarSiniestro, btnChatAtencion};
        for (JButton boton : botones) {
            boton.setFont(new Font("Arial", Font.PLAIN, 14));
            boton.setBackground(new Color(0, 102, 204)); // Azul
            boton.setForeground(Color.WHITE);
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.setMaximumSize(new Dimension(200, 40));
            boton.setFocusPainted(false);
            panelOpciones.add(boton);
            panelOpciones.add(Box.createVerticalStrut(10)); // Espacio entre botones
        }
        Image usuario = (new ImageIcon("fotos/users.png")).getImage().getScaledInstance(35, 35, DO_NOTHING_ON_CLOSE);
        ImageIcon iconoUsuario = new ImageIcon(usuario);
        btnMiPerfil.setIcon(iconoUsuario);
        add(panelOpciones, BorderLayout.EAST);
        
        btnMiPerfil.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Cliente c = bd.obtenerCLiente(dni);
				
				new VentanaPerfilCliente(c.getNombre(), c.getDni(), c.getDirección(), c.getEmail(), c.getnTelefono()+"", bd, segurosCliente);
				
			}
		});
        btnChatAtencion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChatBotVentana(segurosCliente, bd, dni);
				
			}
		});

        // SUR (Resumen financiero)
        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelResumen.setBackground(Color.LIGHT_GRAY);
        lblCostoTotal = new JLabel("Costo Total: 0 €");
        lblCostoTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelResumen.add(lblCostoTotal);
        add(panelResumen, BorderLayout.SOUTH);

        // Actualizar el costo total
        actualizarCostoTotal(segurosCliente);

        setVisible(true);
    }

    private void actualizarCostoTotal(List<Seguro> segurosCliente) {
        double totalCosto = 0;
        for (Seguro s : segurosCliente) {
            if ("Activo".equals(s.getEstado())) {
                totalCosto += s.getCostoMensual(); // Asumimos que el costo anual está en la posición 2
            }
        }
        lblCostoTotal.setText("Costo Total: " + totalCosto + " €");
    }
    
    public static void main(String[] args) {
    	List<Seguro> seguros = new ArrayList<Seguro>();
    	for (int i = 0; i < 3; i++) {
			Seguro s = new Seguro(TipoSeguro.COCHE, LocalDate.now(), 100.0, "Activo");
			seguros.add(s);
		}
    	
		new VentanaCliente("Nerea Ramirez Mendez", seguros, new Bdd("resources/db/aseguradora.db"), "79000259C");
	}
}
