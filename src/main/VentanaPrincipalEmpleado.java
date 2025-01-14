package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import domain.*;
import gui.SeguroCellRenderer;

public class VentanaPrincipalEmpleado extends JFrame {

    private static final long serialVersionUID = 1L;
    private JList<String> listaClientes;
    private DefaultListModel<String> modeloListaClientes;
    private JTextField campoBusqueda;
    private JPanel panelInfoCliente;
    private JTable tablaSeguros;
    private DefaultTableModel modeloTablaSeguros;
    @SuppressWarnings("unused")
	private Map<String, java.util.List<Seguro>> segurosPorCliente;
    private JLabel totalCostoSeguros;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    


    // Constructor
	public VentanaPrincipalEmpleado(Bdd baseDeDatos, String nombre) {
        setTitle("Aseguradoras Bilbao - Panel de Empleado");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setLocationRelativeTo(null);
        setResizable(true);
        
     // Añadir un WindowListener para cerrar la conexión al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                baseDeDatos.cerrarConexion();
                System.out.println("La conexión a la base de datos se ha cerrado.");
            }
        });

        Color colorPrincipal = new Color(0, 51, 102);
        Color colorContraste = new Color(255, 255, 255);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(colorPrincipal);

        // Lista de clientes
        modeloListaClientes = new DefaultListModel<>();
        listaClientes = new JList<>(modeloListaClientes);
        listaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaClientes.setBackground(Color.WHITE);
        listaClientes.setForeground(Color.BLACK);
        listaClientes.setBorder(BorderFactory.createLineBorder(colorPrincipal, 2));
        listaClientes.setFont(new Font("Arial", Font.PLAIN, 14));

     // Campo de búsqueda
        campoBusqueda = new JTextField(20);
        campoBusqueda.setBackground(Color.WHITE);
        campoBusqueda.setForeground(Color.BLACK);
        campoBusqueda.setBorder(BorderFactory.createLineBorder(colorPrincipal, 1));
        campoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarClientes(campoBusqueda.getText());
            }
        });

        // ComboBox para seleccionar criterio de orden
        String[] opcionesOrden = {"Ordenar por Nombre", "Ordenar por DNI","Ordenar por Apellido"};
        JComboBox<String> comboOrden = new JComboBox<>(opcionesOrden);
        comboOrden.setBackground(Color.WHITE);
        comboOrden.setForeground(Color.BLACK);
        comboOrden.addActionListener(e -> {
            String seleccion = (String) comboOrden.getSelectedItem();
            ordenarClientes(seleccion); // Llama al método de ordenación
        });

        JLabel etiquetaBuscar = new JLabel("Buscar cliente:");
        etiquetaBuscar.setForeground(colorContraste);

        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.add(etiquetaBuscar, BorderLayout.WEST);
        panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelBusqueda.add(comboOrden, BorderLayout.EAST); // Añade el ComboBox
        panelBusqueda.setBackground(colorPrincipal);
        panelBusqueda.setForeground(colorContraste);

        // Botones de alta y baja de clientes
        JPanel panelBotonesClientes = new JPanel(new FlowLayout());
        panelBotonesClientes.setBackground(colorPrincipal);

        JButton btnAltaCliente = new JButton("Dar de Alta Cliente");
        JButton btnBajaCliente = new JButton("Dar de Baja Cliente");
        btnAltaCliente.setBackground(new Color(51, 153, 255));
        btnAltaCliente.setForeground(Color.WHITE);
        btnBajaCliente.setBackground(Color.RED);
        btnBajaCliente.setForeground(Color.WHITE);

        panelBotonesClientes.add(btnAltaCliente);
        panelBotonesClientes.add(btnBajaCliente);
        
//		Boton modificar seguro
        JButton btnModificarSeguro = new JButton("Modificar Seguro");
        btnModificarSeguro.setBackground(new Color(51, 153, 255));
        btnModificarSeguro.setForeground(Color.WHITE);
        btnModificarSeguro.setEnabled(false);
        btnModificarSeguro.addActionListener(e -> {
        	int filaSeleccionada = tablaSeguros.getSelectedRow();
            
            String clienteSeleccionado = listaClientes.getSelectedValue();
            if (clienteSeleccionado != null) {
                String dniCliente = clienteSeleccionado.split("- DNI: ")[1];
                System.out.println(dniCliente);
                String tipo = tablaSeguros.getValueAt(filaSeleccionada, 0).toString();
                System.out.println(tipo);
                String fecha = ((LocalDate)tablaSeguros.getValueAt(filaSeleccionada, 1)).format(formatter);
                Object o = ((LocalDate)tablaSeguros.getValueAt(filaSeleccionada, 1)).format(formatter);
                System.out.println(o);
                System.out.println(fecha);
                double costo = Double.parseDouble(tablaSeguros.getValueAt(filaSeleccionada, 2).toString());
                System.out.println(costo);
                String estado = tablaSeguros.getValueAt(filaSeleccionada, 3).toString();
                System.out.println(estado);
                if(estado.equals("Inactivo")) {
                	JOptionPane.showMessageDialog(
                	        this, // Componente padre (la ventana actual)
                	        "Solo se pueden modificar los seguros que estén activos.", // Mensaje
                	        "Información", // Título del cuadro de diálogo
                	        JOptionPane.INFORMATION_MESSAGE // Tipo de mensaje (información)
                	    );
                	return;
                }
                String cobertura;
                if(tablaSeguros.getValueAt(filaSeleccionada, 4) == null) {
                	cobertura = "";
                }else {
                	cobertura = tablaSeguros.getValueAt(filaSeleccionada, 4).toString();
                }
                
                int Id = baseDeDatos.obtenerIdSeguro(dniCliente, tipo, fecha, costo, estado, cobertura);
                System.out.println(Id);
                System.out.println(dniCliente);
                System.out.println(tipo);
                System.out.println(fecha);
                System.out.println(costo);
                System.out.println(estado);
                
                new VentanaSeguros(Id, dniCliente, tipo, fecha, costo, estado, cobertura, baseDeDatos).setVisible(true);
            }
            List<Seguro> seguros = new ArrayList<>();
            double costoTotal = 0;
//            for (int i = 0; i < modeloTablaSeguros.getRowCount(); i++) {
//            	if(tablaSeguros.getValueAt(i, 3).toString().equals("Activo")) {
//            		costoTotal += Double.parseDouble(tablaSeguros.getValueAt(i, 2).toString());
//            	}
//			}
            for(int i = 0; i < modeloTablaSeguros.getRowCount(); i++) {
            	TipoSeguro tipo = TipoSeguro.valueOf(tablaSeguros.getValueAt(i, 0).toString());
            	LocalDate fecha = LocalDate.parse(tablaSeguros.getValueAt(i, 1).toString());
            	double costo = Double.parseDouble(tablaSeguros.getValueAt(i, 2).toString());
            	String estado = tablaSeguros.getValueAt(i, 3).toString();
            	String cobertura = tablaSeguros.getValueAt(i, 4).toString();
            	Seguro s = new Seguro(tipo, fecha, costo, estado, cobertura);
            	seguros.add(s);
            }
            costoTotal = calcularCostoTotal(seguros, 0);
            totalCostoSeguros.setText("Costo Total de Seguros: "+ costoTotal +" €");
        
            
        });

        panelBotonesClientes.add(btnModificarSeguro);
        
        JPanel panelClientes = new JPanel(new BorderLayout());
        panelClientes.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                "Clientes", 0, 0, new Font("Arial", Font.BOLD, 16), colorContraste));
        panelClientes.setBackground(colorPrincipal);
        panelClientes.add(panelBusqueda, BorderLayout.NORTH);
        panelClientes.add(new JScrollPane(listaClientes), BorderLayout.CENTER);
        panelClientes.add(panelBotonesClientes, BorderLayout.SOUTH);

        panelInfoCliente = new JPanel(new BorderLayout());
        panelInfoCliente.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                "Información del Cliente", 0, 0, new Font("Arial", Font.BOLD, 16), colorContraste));
        panelInfoCliente.setBackground(colorPrincipal);

        String[] columnasSeguros = {"Tipo de seguro", "Fecha de contratación", "Costo anual", "Estado", "Cobertura"};
        modeloTablaSeguros = new DefaultTableModel(columnasSeguros, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaSeguros = new JTable(modeloTablaSeguros);
        tablaSeguros.getTableHeader().setReorderingAllowed(false);
        tablaSeguros.setBackground(Color.WHITE);
        tablaSeguros.setForeground(Color.BLACK);
        tablaSeguros.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaSeguros.setRowHeight(25);
        tablaSeguros.setFillsViewportHeight(true);
        tablaSeguros.setDefaultRenderer(Object.class, new SeguroCellRenderer());
        
        
        

        JPanel panelTablaSeguros = new JPanel(new BorderLayout());
        panelTablaSeguros.add(new JScrollPane(tablaSeguros), BorderLayout.CENTER);

        totalCostoSeguros = new JLabel("Costo Total de Seguros: 0 €");
        totalCostoSeguros.setForeground(Color.BLACK);

        panelTablaSeguros.add(totalCostoSeguros, BorderLayout.SOUTH);
        panelInfoCliente.add(panelTablaSeguros, BorderLayout.CENTER);

        JButton btnNuevoSeguro = new JButton("Nuevo Seguro");
        btnNuevoSeguro.setBackground(new Color(51, 153, 255));
        btnNuevoSeguro.setForeground(Color.WHITE);

        JButton btnBajaSeguro = new JButton("Dar de Baja Seguro");
        btnBajaSeguro.setBackground(Color.RED);
        btnBajaSeguro.setForeground(Color.WHITE);
        btnBajaSeguro.setEnabled(false);

        JPanel panelBotonesSeguros = new JPanel(new FlowLayout());
        panelBotonesSeguros.setBackground(colorPrincipal);
        panelBotonesSeguros.add(btnNuevoSeguro);
        panelBotonesSeguros.add(btnBajaSeguro);
        tablaSeguros.getSelectionModel().addListSelectionListener(e -> {
        	if(tablaSeguros.getSelectedRow() == -1) {
        		btnModificarSeguro.setEnabled(false);
            	btnBajaSeguro.setEnabled(false);
        	}else {
        		btnModificarSeguro.setEnabled(true);
            	btnBajaSeguro.setEnabled(true);
        	}
        });

        panelInfoCliente.add(panelBotonesSeguros, BorderLayout.SOUTH);

        panelPrincipal.add(panelClientes, BorderLayout.WEST);
        panelPrincipal.add(panelInfoCliente, BorderLayout.CENTER);
        add(panelPrincipal);

        cargarClientesDesdeBaseDeDatos(baseDeDatos);

        btnAltaCliente.addActionListener(e -> new VentanaAltaCliente(modeloListaClientes, baseDeDatos));
        btnBajaCliente.addActionListener(e -> darDeBajaCliente(baseDeDatos));
        btnBajaCliente.setEnabled(false);
        btnNuevoSeguro.setEnabled(false);
        listaClientes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Asegurarse de que la selección es definitiva
                @SuppressWarnings("unused")
				String clienteSeleccionado = listaClientes.getSelectedValue();
                cargarSegurosCliente(baseDeDatos);
                btnNuevoSeguro.setEnabled(true);
                btnBajaCliente.setEnabled(true);
            }
            double costoTotal = 0;
            for (int i = 0; i < modeloTablaSeguros.getRowCount(); i++) {
            	if(tablaSeguros.getValueAt(i, 3).toString().equals("Activo")) {
            		costoTotal += Double.parseDouble(tablaSeguros.getValueAt(i, 2).toString());            	
            	}
			}
            totalCostoSeguros.setText("Costo Total de Seguros: "+ costoTotal +" €");
        });
        

        btnNuevoSeguro.addActionListener(e -> {
        	String dniCliente = listaClientes.getSelectedValue().split("- DNI: ")[1];
            new VentanaAltaSeguro(modeloTablaSeguros, dniCliente, baseDeDatos);
        });

        btnBajaSeguro.addActionListener(e -> {
            int filaSeleccionada = tablaSeguros.getSelectedRow();
            
            String clienteSeleccionado = listaClientes.getSelectedValue();
            String dniCliente = clienteSeleccionado.split("- DNI: ")[1];
            System.out.println(dniCliente);
            String tipo = tablaSeguros.getValueAt(filaSeleccionada, 0).toString();
            System.out.println(tipo);
            String fecha = ((LocalDate)tablaSeguros.getValueAt(filaSeleccionada, 1)).format(formatter);
            Object o = ((LocalDate)tablaSeguros.getValueAt(filaSeleccionada, 1)).format(formatter);
            System.out.println(o);
            System.out.println(fecha);
            double costo = Double.parseDouble(tablaSeguros.getValueAt(filaSeleccionada, 2).toString());
            System.out.println(costo);
            String estado = tablaSeguros.getValueAt(filaSeleccionada, 3).toString();
            System.out.println(estado);
            String cobertura = tablaSeguros.getValueAt(filaSeleccionada, 4).toString();
            
            
            
            int Id = baseDeDatos.obtenerIdSeguro(dniCliente, tipo, fecha, costo, estado, cobertura);
            System.out.println(Id);
            System.out.println(dniCliente);
            System.out.println(tipo);
            System.out.println(fecha);
            System.out.println(costo);
            System.out.println(estado);
            try {
            	if(cobertura.equals("Fallecimiento")) {
            		baseDeDatos.actualizarSeguro(Id, tipo, fecha, costo, "Inactivo", "FALLECIMIENTO");
            	}else if(cobertura.equals("Fallecimiento y Invalidez")) {
            		baseDeDatos.actualizarSeguro(Id, tipo, fecha, costo, "Inactivo", "FYINVALIDEZ");
            	}else if(cobertura.equals("Cobertura Plus")) {
            		baseDeDatos.actualizarSeguro(Id, tipo, fecha, costo, "Inactivo", "PLUS");
            	}else if(cobertura.equals("Cobertura Estándar")) {
            		baseDeDatos.actualizarSeguro(Id, tipo, fecha, costo, "Inactivo", "ESTANDAR");
            	}else if(cobertura.equals("A Terceros")) {
            		baseDeDatos.actualizarSeguro(Id, tipo, fecha, costo, "Inactivo", "TERCEROS");
            	}else{
            		baseDeDatos.actualizarSeguro(Id, tipo, fecha, costo, "Inactivo", "TODO RIESGO");
            		
            	}
                baseDeDatos.actualizarSeguro(Id, tipo, fecha, costo, "Inactivo", cobertura);
                cargarSegurosCliente(baseDeDatos);
                JOptionPane.showMessageDialog(this, "El seguro ha sido dado de baja.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al dar de baja el seguro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            List<Seguro> seguros = new ArrayList<>();
            double costoTotal = 0;
//            for (int i = 0; i < modeloTablaSeguros.getRowCount(); i++) {
//            	if(tablaSeguros.getValueAt(i, 3).toString().equals("Activo")) {
//            		costoTotal += Double.parseDouble(tablaSeguros.getValueAt(i, 2).toString());
//            	}
//			}
            for(int i = 0; i < modeloTablaSeguros.getRowCount(); i++) {
            	TipoSeguro tipo1 = TipoSeguro.valueOf(tablaSeguros.getValueAt(i, 0).toString());
            	LocalDate fecha1 = LocalDate.parse(tablaSeguros.getValueAt(i, 1).toString());
            	double costo1 = Double.parseDouble(tablaSeguros.getValueAt(i, 2).toString());
            	String estado1 = tablaSeguros.getValueAt(i, 3).toString();
            	String cobertura1;
                if(tablaSeguros.getValueAt(filaSeleccionada, 4) == null) {
                	cobertura1 = "";
                }else {
                	cobertura1 = tablaSeguros.getValueAt(filaSeleccionada, 4).toString();
                }
            	Seguro s = new Seguro(tipo1, fecha1, costo1, estado1, cobertura1);
            	seguros.add(s);
            }
            costoTotal = calcularCostoTotal(seguros, 0);
            totalCostoSeguros.setText("Costo Total de Seguros: "+ costoTotal +" €");
        });
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Atencion al cliente");
        JMenuItem itemChat = new JMenuItem("Solicitudes");
        JMenu menu1 = new JMenu("Gestion Siniestros");
        JMenuItem itemSin = new JMenuItem("Siniestros");
        JMenu menu2 = new JMenu("Jornada de Trabajo");
        JMenuItem itemSim = new JMenuItem("Jornada");
        JMenu menu3 = new JMenu("Encuestas");
        JMenuItem itemEnc= new JMenuItem("Resultados");
        JMenu menuConfiguracion = new JMenu("Configuración");
        JCheckBoxMenuItem modoOscuroItem = new JCheckBoxMenuItem("Modo oscuro");
        
        itemChat.addActionListener(e -> {new VentanaGestionSolicitudes(baseDeDatos);});
        itemSin.addActionListener(e -> {new VentanaSiniestrosPendientes(baseDeDatos);});
        itemSim.addActionListener(e -> {
        	System.out.println(nombre);
        	if(nombre.equals("Mikel")) {
        		SwingUtilities.invokeLater(() -> {
                    VentanaSimulacionAtencion ventana = new VentanaSimulacionAtencion(baseDeDatos);
                    ventana.setVisible(true);
                });
        	}else {
        		JOptionPane.showMessageDialog(this, "Solo el jefe puede acceder a esta simulación.", "Error", JOptionPane.ERROR_MESSAGE);
        	}
        });
        itemEnc.addActionListener(e -> {new VentanaResultadosEncuestas(baseDeDatos);});
        
     // Añadir funcionalidad al botón
        modoOscuroItem.addActionListener(e -> {
            boolean activar = modoOscuroItem.isSelected(); // Ver si está activado o desactivado
            ModoOscuroUtil.aplicarModoOscuro(this, activar); // Cambiar el tema
            // Opcional: guardar la preferencia para usarla más tarde
            // guardarPreferenciaModoOscuro(activar);
        });

        // Añadir el botón al menú de configuración
        menuConfiguracion.add(modoOscuroItem);

        // Añadir el menú a la barra de menús
        menuBar.add(menuConfiguracion);
        
        menuBar.add(menu);
        menu.add(itemChat);
        menuBar.add(menu1);
        menu1.add(itemSin);
        menuBar.add(menu2);
        menu2.add(itemSim);
        menuBar.add(menu3);
        menu3.add(itemEnc);
        
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private void cargarClientesDesdeBaseDeDatos(Bdd baseDeDatos) {
        try {
        	ResultSet rs = baseDeDatos.obtenerClientes();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellidos");
                String dni = rs.getString("dni");
                modeloListaClientes.addElement(nombre + " " + apellidos + " - DNI: " + dni);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarSegurosCliente(Bdd baseDeDatos) {
        modeloTablaSeguros.setRowCount(0);
        String clienteSeleccionado = listaClientes.getSelectedValue();
        if (clienteSeleccionado == null) return;

        String dniCliente = clienteSeleccionado.split("- DNI: ")[1];
        double totalCosto = 0;
        try {
            ArrayList<Seguro> seguros = baseDeDatos.obtenerSeguros(dniCliente);
            
            for (Seguro s : seguros) {
            	if(s.getCobertura().equals("FALLECIMIENTO")) {
            		modeloTablaSeguros.addRow(new Object[] {s.getTipo(), s.getFechaContratacion(), s.getCostoMensual(), s.getEstado(), "Fallecimiento"});
            	}else if(s.getCobertura().equals("FYINVALIDEZ")) {
            		modeloTablaSeguros.addRow(new Object[] {s.getTipo(), s.getFechaContratacion(), s.getCostoMensual(), s.getEstado(), "Fallecimiento y Invalidez"});
            	}else if(s.getCobertura().equals("ESTANDAR")) {
            		modeloTablaSeguros.addRow(new Object[] {s.getTipo(), s.getFechaContratacion(), s.getCostoMensual(), s.getEstado(), "Cobertura Estandar"});
            	}else if(s.getCobertura().equals("PLUS")) {
            		modeloTablaSeguros.addRow(new Object[] {s.getTipo(), s.getFechaContratacion(), s.getCostoMensual(), s.getEstado(), "Cobertura Plus"});
            	}else if(s.getCobertura().equals("TERCEROS")) {
            		modeloTablaSeguros.addRow(new Object[] {s.getTipo(), s.getFechaContratacion(), s.getCostoMensual(), s.getEstado(), "A Terceros"});
            	}else if(s.getCobertura().equals("TODORIESGO")) {
            		modeloTablaSeguros.addRow(new Object[] {s.getTipo(), s.getFechaContratacion(), s.getCostoMensual(), s.getEstado(), "A Todo Riesgo"});
            	}else {
            		modeloTablaSeguros.addRow(new Object[] {s.getTipo(), s.getFechaContratacion(), s.getCostoMensual(), s.getEstado(), s.getCobertura()});
            	}
				
			}


            totalCostoSeguros.setText("Costo Total de Seguros: " + totalCosto + " €");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los seguros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    

    private void darDeBajaCliente(Bdd baseDeDatos) {
        String clienteSeleccionado = listaClientes.getSelectedValue();
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para dar de baja.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres dar de baja al cliente?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                String dniCliente = clienteSeleccionado.split("- DNI: ")[1];
                baseDeDatos.eliminarCliente(dniCliente);
                modeloListaClientes.removeElement(clienteSeleccionado);
                JOptionPane.showMessageDialog(this, "El cliente ha sido dado de baja.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al dar de baja al cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void filtrarClientes(String filtro) {
        if (filtro.isEmpty()) {
            modeloListaClientes.clear();
            cargarClientesDesdeBaseDeDatos(new Bdd("resources/db/aseguradora.db")); // Asegúrate de usar la base de datos correcta
            return;
        }

        DefaultListModel<String> modeloFiltrado = new DefaultListModel<>();
        for (int i = 0; i < modeloListaClientes.size(); i++) {
            String cliente = modeloListaClientes.getElementAt(i);
            if (cliente.toLowerCase().contains(filtro.toLowerCase())) {
                modeloFiltrado.addElement(cliente);
            }
        }

        listaClientes.setModel(modeloFiltrado);
    }
    
    public double calcularCostoTotal(List<Seguro> seguros, int indice) {
        if (indice >= seguros.size()) return 0; // Caso base

        Seguro seguro = seguros.get(indice);
        double costoActual = seguro.getCostoMensual();

        // Llamada recursiva al siguiente seguro
        return costoActual + calcularCostoTotal(seguros, indice + 1);
    }
    
    private void ordenarClientes(String criterio) {
        List<String> listaClientesTemp = new ArrayList<>();
        for (int i = 0; i < modeloListaClientes.size(); i++) {
            listaClientesTemp.add(modeloListaClientes.getElementAt(i));
        }

        // Ordenar la lista temporal según el criterio
        if (criterio.equals("Ordenar por Nombre")) {
            listaClientesTemp.sort((c1, c2) -> {
                String nombre1 = c1.split(" - DNI: ")[0];
                String nombre2 = c2.split(" - DNI: ")[0];
                return nombre1.compareToIgnoreCase(nombre2);
            });
        } else if (criterio.equals("Ordenar por DNI")) {
            listaClientesTemp.sort((c1, c2) -> {
                String dni1 = c1.split(" - DNI: ")[1];
                String dni2 = c2.split(" - DNI: ")[1];
                return dni1.compareTo(dni2);
            });
        }
        if (criterio.equals("Ordenar por Apellido")) {
            listaClientesTemp.sort((c1, c2) -> {
                String apellido1 = c1.split(" ")[1]; // Asume que el nombre completo está en formato "Nombre Apellido"
                String apellido2 = c2.split(" ")[1];
                return apellido1.compareToIgnoreCase(apellido2);
            });
        }
       
        // Actualizar el modelo de la lista con los datos ordenados
        modeloListaClientes.clear();
        for (String cliente : listaClientesTemp) {
            modeloListaClientes.addElement(cliente);
        }
    }

    
    public static void main(String[] args) {
		new VentanaPrincipalEmpleado(new Bdd("resources/db/aseguradora.db"), "Hola");
	}
  
}