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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
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
    private JButton  btnReportarSiniestro, btnTablonNotificaciones, btnChatAtencion, btnMiPerfil, btnOfertas, btnSolicitarEspecialista, btnEncuesta, btnModoOscuro, btnPresupuestos, btnResultadosSorteos, btnSegurosGratuitos;
    private boolean activado = false;
    private Bdd baseDeDatos; // Conexión a la base de datos
    private String dniCliente; // DNI del cliente actual
    
    public VentanaCliente(String nombreCliente, List<Seguro> segurosCliente, Bdd bd, String dni, String genero, String fecha) {
        this.baseDeDatos = bd; // Asignar la base de datos
        this.dniCliente = dni; // Asignar el DNI del cliente

        
        // Configuración básica de la ventana
        setTitle("Aseguradora Bilbao - Cliente");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
//        String ultimoInicio = bd.obtenerUltimoInicioCliente(dni);
        
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
        if(genero.equals("H")) {
        	JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelHeader.setBackground(colorPrincipal);
            JLabel lblBienvenida = new JLabel("Bienvenido, " + nombreCliente);
            lblBienvenida.setForeground(colorContraste);
            lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
            panelHeader.add(lblBienvenida);
            add(panelHeader, BorderLayout.NORTH);
        }else {
        	JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelHeader.setBackground(colorPrincipal);
            JLabel lblBienvenida = new JLabel("Bienvenida, " + nombreCliente);
            lblBienvenida.setForeground(colorContraste);
            lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
            panelHeader.add(lblBienvenida);
            add(panelHeader, BorderLayout.NORTH);
        }
        

        // CENTRO (Tabla de seguros)
        JPanel panelCentro = new JPanel(new BorderLayout());
        String[] columnas = {"Tipo de Seguro", "Fecha de Contratación", "Costo Anual (€)", "Estado", "Cobertura"};
        modeloTablaSeguros = new DefaultTableModel(columnas, 0);
        tablaSeguros = new JTable(modeloTablaSeguros);
        tablaSeguros.getTableHeader().setReorderingAllowed(false);
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
        			s.getEstado(), 
        			s.getCobertura()
        	};
            modeloTablaSeguros.addRow(o);;
        }

        add(panelCentro, BorderLayout.CENTER);

        // ESTE (Botones de acción)
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelOpciones.setBackground(Color.WHITE);
        
        
     // Etiqueta para mostrar el último inicio de sesión
        JLabel lblUltimoInicio = new JLabel("Último inicio: " + fecha);
        lblUltimoInicio.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 16)); // Fuente mejorada
        lblUltimoInicio.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUltimoInicio.setForeground(new Color(128, 0, 128)); // Color púrpura oscuro
        panelOpciones.add(lblUltimoInicio);
        panelOpciones.add(Box.createVerticalStrut(10)); // Espacio entre el label y los botones
        
        btnMiPerfil = new JButton("Mi perfil");
        btnModoOscuro = new JButton("Modo oscuro");
        btnTablonNotificaciones = new JButton("Tablón de Notificaciones");
        btnReportarSiniestro = new JButton("Reportar Siniestro");
        btnChatAtencion = new JButton("Atención al Cliente");
        btnOfertas = new JButton("Ver Ofertas");
        btnSolicitarEspecialista = new JButton("Solicitar Especialista");
        btnEncuesta = new JButton("Rellena la encuesta");
        btnPresupuestos=new JButton("Presupuestos");
        btnResultadosSorteos = new JButton("Resultados de sorteos");
        // Estilo de los botones
        JButton[] botones = {btnMiPerfil, btnReportarSiniestro, btnChatAtencion, btnOfertas, btnSolicitarEspecialista, btnEncuesta, btnModoOscuro, btnTablonNotificaciones, btnPresupuestos, btnResultadosSorteos};
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
        
        btnModoOscuro.addActionListener(e -> {
        	
        	if(activado == false) {
        		ModoOscuroUtil.aplicarModoOscuro(this, true); // Llama a la transición
        		activado = true;
        	}else {
        		ModoOscuroUtil.aplicarModoOscuro(this, false); // Llama a la transición
        		activado = false;
        	}
        	
        });
        


        
        btnResultadosSorteos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    mostrarResultadosSorteos(baseDeDatos, dniCliente); // Llamada al método
                });
            }
        });

        
         
        btnPresupuestos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    VentanaSimuladorCostos simulador = new VentanaSimuladorCostos();
                    simulador.setVisible(true);
                });
            }
        });
        
        
        btnChatAtencion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChatBotVentana(segurosCliente, bd, dni);
				
			}
		});
        
        btnTablonNotificaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TablonNotificaciones(dni,bd);
            }
        });
        
        btnOfertas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new VentanaOfertasSeguros();
				
			}
		});
        btnSolicitarEspecialista.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ServiciosEmergencia();
				
			}
		});
        btnReportarSiniestro.addActionListener(new ActionListener() {
			
			@SuppressWarnings("unlikely-arg-type")
			@Override
			public void actionPerformed(ActionEvent e) {
				HashMap<TipoSeguro, ArrayList<Double>> preciosPorSeguro = new HashMap<>();
				ArrayList<Seguro> seguros = bd.obtenerSeguros(dni);
				for (Seguro s : seguros) {
					if(s.getEstado().equals("Activo")) {
						if(preciosPorSeguro.values().contains(TipoSeguro.valueOf(s.getTipo().toString()))) {
							preciosPorSeguro.get(TipoSeguro.valueOf(s.getTipo().toString())).add(s.getCostoMensual());
						}else {
							preciosPorSeguro.put(TipoSeguro.valueOf(s.getTipo().toString()), new ArrayList<Double>(List.of(s.getCostoMensual())));
						}
					}
					
				}
//				System.out.println(preciosPorSeguro);
				SwingUtilities.invokeLater(() -> {
					VentanaReportarSiniestros ventana = new VentanaReportarSiniestros(preciosPorSeguro, bd, dni);
		            ventana.setVisible(true);
		        });
				
			}
		});
        
        btnEncuesta.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new VentanaEncuesta(bd, dni);
				
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
    
    public VentanaCliente(Bdd baseDeDatos, String dniCliente) {
        this.baseDeDatos = baseDeDatos; // Inicializa la variable con el valor recibido
        this.dniCliente = dniCliente;   // Inicializa el DNI del cliente

        // Configuración de la ventana y componentes
    }

    
    private void mostrarResultadosSorteos(Bdd baseDeDatos, String dniCliente) {
        try {
            // Consulta para verificar si el cliente tiene resultados de sorteos
            ResultSet rs = baseDeDatos.obtenerResultadosSorteos(dniCliente);

            StringBuilder mensaje = new StringBuilder("Tus resultados de sorteos:\n");
            boolean tieneResultados = false;
            

            while (rs.next()) {
                String premio = rs.getString("premio");
                String fecha = rs.getString("fecha");
                mensaje.append("- Ganaste un ").append(premio).append(" el ").append(fecha).append("\n");
                tieneResultados = true;

                // Abrir ventana de selección para convertir el seguro básico
                if (premio.equalsIgnoreCase("Seguro Básico")) {
                    SwingUtilities.invokeLater(() -> {
                        new VentanaSeleccionSeguro(this, dniCliente, baseDeDatos, modeloTablaSeguros);
                    });
                }
            }

            if (!tieneResultados) {
                mensaje.append("No tienes resultados de sorteos pendientes.");
            }

            // Mostrar resultados en un cuadro de diálogo
            JOptionPane.showMessageDialog(this, mensaje.toString(), "Resultados de Sorteos", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los resultados de sorteos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

}