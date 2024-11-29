package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.VentanaCliente;
import main.VentanaPrincipalEmpleado;

public class InicioSesion extends JFrame{
	
	private static final Color COLOR_PRINCIPAL = new Color(0, 51, 102); // Azul oscuro
    private static final Color COLOR_CONTRASTE = Color.WHITE;
    private CardLayout cardLayout;
    private JPanel panelCentral;
    private JTextField campoUsuario;
    private JPasswordField campoContraseña;
    private final Map<String, List<Object[]>> segurosPorCliente;

    
    private final Map<String, String> clientes = new HashMap<>();
    private boolean esEmpleado; // Para determinar si es empleado o cliente

    
    public InicioSesion() {
        setTitle("Inicio de Sesión - Aseguradoras Bilbaaaao");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        // Configuración principal
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        // Crear las tarjetas
        JPanel panelSeleccion = crearPanelSeleccion();
        JPanel panelLogin = crearPanelLogin();

        // Añadir tarjetas al panel central
        panelCentral.add(panelSeleccion, "Seleccion");
        panelCentral.add(panelLogin, "Login");

        add(panelCentral);
        setVisible(true);
        
     // Cargar datos de clientes
        cargarDatosClientesDesdeCSV("clientes.csv");
        
        // Cargar seguros de clientes
        
        segurosPorCliente = cargarSeguros("seguros.csv");
        System.out.println(segurosPorCliente);

        
    }
    
    private JPanel crearPanelSeleccion() {
    	JPanel panel = new JPanel(new BorderLayout());
    	panel.setBackground(COLOR_PRINCIPAL);

        JLabel etiquetaTitulo = new JLabel("Seleccione su tipo de usuario", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetaTitulo.setForeground(COLOR_CONTRASTE);
        panel.add(etiquetaTitulo, BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 10));
        botones.setBackground(COLOR_PRINCIPAL);

        JButton btnEmpleado = new JButton("Soy un empleado");
        btnEmpleado.setBackground(COLOR_PRINCIPAL);
        btnEmpleado.setForeground(COLOR_CONTRASTE);
        btnEmpleado.addActionListener(e -> {
            esEmpleado = true;
            cardLayout.show(panelCentral, "Login");
        });

        JButton btnCliente = new JButton("Soy un cliente");
        btnCliente.setBackground(COLOR_PRINCIPAL);
        btnCliente.setForeground(COLOR_CONTRASTE);
        btnCliente.addActionListener(e -> {
            esEmpleado = false;
            cardLayout.show(panelCentral, "Login");
        });

        botones.add(btnEmpleado);
        botones.add(btnCliente);
        panel.add(botones, BorderLayout.CENTER);
    	return panel;
        
    }
    
    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(COLOR_PRINCIPAL);

        JLabel etiquetaUsuario = new JLabel("Usuario:");
        etiquetaUsuario.setForeground(COLOR_CONTRASTE);
        campoUsuario = new JTextField();

        JLabel etiquetaContraseña = new JLabel("Contraseña:");
        etiquetaContraseña.setForeground(COLOR_CONTRASTE);
        campoContraseña = new JPasswordField();

        JButton btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setBackground(new Color(51, 153, 255));
        btnIniciarSesion.setForeground(COLOR_CONTRASTE);
        btnIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(Color.RED);
        btnRegresar.setForeground(COLOR_CONTRASTE);
        btnRegresar.addActionListener(e -> cardLayout.show(panelCentral, "Seleccion"));

        panel.add(etiquetaUsuario);
        panel.add(campoUsuario);
        panel.add(etiquetaContraseña);
        panel.add(campoContraseña);
        panel.add(btnIniciarSesion);
        panel.add(btnRegresar);
        return panel;
    }
    
    private void iniciarSesion() {
        String usuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());

        if (esEmpleado) {
            // Validación para empleados
            if (usuario.equals("Empleado1234") && contraseña.equals("hola")) {
                JOptionPane.showMessageDialog(this, "Bienvenido, empleado.");
                dispose();
                new VentanaPrincipalEmpleado();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Validación para clientes
            if (clientes.containsKey(usuario) && clientes.get(usuario).equals(contraseña)) {
                JOptionPane.showMessageDialog(this, "Bienvenido, " + usuario + ".");
//                System.out.println();
                dispose();
                new VentanaCliente(usuario, segurosPorCliente.get(contraseña)); // Ventana del cliente  //*usuario*//
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void cargarDatosClientesDesdeCSV(String archivoCSV) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Suponemos que el archivo CSV tiene columnas: Nombre, Apellido1, Apellido2, DNI, Dirección
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    String nombre = datos[0].trim();
                    String apellido1 = datos[1].trim();
                    String dni = datos[2].trim();

                    // Formar el usuario como "Nombre_Apellido1"
                    String usuario = nombre + "_" + apellido1;

                    // Añadir al mapa de clientes
                    clientes.put(usuario, dni.toUpperCase());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de clientes desde el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private Map<String, List<Object[]>> cargarSeguros(String archivoCSV) {
        Map<String, List<Object[]>> segurosPorCliente = new HashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 5) { // DNI, Tipo, Fecha, Costo, Estado
                    String dniCliente = datos[0].trim();
                    String tipoSeguro = datos[1].trim();
                    String fechaContratacion = datos[2].trim();
                    double costoAnual = Double.parseDouble(datos[3].trim());
                    String estado = datos[4].trim();

                    // Crear el array de objetos
                    Object[] seguroData = new Object[]{
                        tipoSeguro,
                        LocalDate.parse(fechaContratacion, dateFormatter), // Fecha en formato LocalDate
                        costoAnual,
                        estado
                    };

                    // Añadir al mapa
                    segurosPorCliente.computeIfAbsent(dniCliente, k -> new ArrayList<>()).add(seguroData);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los seguros desde el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error procesando los datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return segurosPorCliente;
    }
    
    public static void main(String[] args) {
		new InicioSesion();
	}

}