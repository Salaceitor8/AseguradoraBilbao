package main;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import domain.Seguro;
import java.util.List;

public class VentanaPerfilCliente extends JFrame {

    private JTextField campoNombre, campoDNI, campoEmail, campoTelefono;
    private JButton btnCambiarFoto, btnEditar, btnGuardar, btnCambiarContraseña, btnVerHistorial;
    private JLabel lblFotoPerfil;

    public VentanaPerfilCliente(String nombreCliente, String dni, String direccion, String email, String telefono, Bdd bd, List<Seguro> seguros) {
        // Configuración básica de la ventana
        setTitle("Mi Perfil - " + nombreCliente);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setResizable(false); // Evitar redimensionar
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior con título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 51, 102));
        JLabel lblTitulo = new JLabel("Mi Perfil");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel central con dos columnas
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel izquierdo dividido en dos secciones (foto y campos)
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Parte superior: foto centrada
        JPanel panelFoto = new JPanel(new GridBagLayout()); // Para centrar el contenido


        lblFotoPerfil = new JLabel("Sin Foto");
        lblFotoPerfil.setPreferredSize(new Dimension(300, 300)); // Tamaño fijo de la foto
        lblFotoPerfil.setOpaque(true);
        lblFotoPerfil.setBackground(Color.WHITE); // Fondo blanco para la foto
        lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoPerfil.setVerticalAlignment(SwingConstants.CENTER);
        lblFotoPerfil.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde negro
        
     // Cargar la foto desde la base de datos
        String rutaFoto = bd.cargarRutaFotoDesdeBD(dni);
        if (rutaFoto != null && !rutaFoto.isEmpty()) {
            lblFotoPerfil.setText("");
            lblFotoPerfil.setIcon(new ImageIcon(new ImageIcon(rutaFoto).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        }

        btnCambiarFoto = new JButton("Cambiar Foto");
        btnCambiarFoto.addActionListener(e -> cambiarFoto(bd));

        // Añadir la foto y el botón al panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado alrededor
        panelFoto.add(lblFotoPerfil, gbc);

        gbc.gridy = 1; // Posición del botón
        panelFoto.add(btnCambiarFoto, gbc);

        panelIzquierdo.add(panelFoto, BorderLayout.NORTH);

        // Parte inferior: información del cliente
        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createTitledBorder("Información del Cliente"));
        campoNombre = crearCampo("Nombre:", nombreCliente, false, panelCampos);
        campoDNI = crearCampo("DNI:", dni, false, panelCampos);
        campoEmail = crearCampo("Email:", email, false, panelCampos);
        campoTelefono = crearCampo("Teléfono:", telefono, false, panelCampos);
        panelIzquierdo.add(panelCampos, BorderLayout.CENTER);

        // Panel derecho (reservado para estadísticas futuras)
        
        JPanel panelGraficos = new JPanel();
        panelGraficos.setLayout(new GridLayout(3, 1, 10, 10));
        panelGraficos.setBorder(BorderFactory.createTitledBorder("Visualización de Datos"));
        panelGraficos.setBorder(BorderFactory.createTitledBorder("Estadísticas del Cliente"));
        
        Map<Integer, Double> gastosAnuales = obtenerGastosAnuales(seguros); // Datos ficticios o desde la BD
        JFreeChart graficoGastos = crearGraficoGastosAnuales(gastosAnuales);
        ChartPanel panelGastos = new ChartPanel(graficoGastos);

        Map<String, Integer> tiposSeguro = obtenerTiposSeguro(seguros); // Datos ficticios o desde la BD
        JFreeChart graficoTipos = crearGraficoTiposSeguro(tiposSeguro);
        ChartPanel panelTipos = new ChartPanel(graficoTipos);

        Map<String, Integer> estadoSeguros = obtenerEstadoSeguros(seguros); // Datos ficticios o desde la BD
        JFreeChart graficoEstados = crearGraficoEstadoSeguros(estadoSeguros);
        ChartPanel panelEstados = new ChartPanel(graficoEstados);

        // Añade los paneles al contenedor
        panelGraficos.add(panelGastos);
        panelGraficos.add(panelTipos);
        panelGraficos.add(panelEstados);



        // Añadir los paneles izquierdo y derecho al centro
        panelCentral.add(panelIzquierdo);
        panelCentral.add(panelGraficos);
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnEditar = new JButton("Editar");
        btnGuardar = new JButton("Guardar Cambios");
        btnCambiarContraseña = new JButton("Cambiar Contraseña");
        btnVerHistorial = new JButton("Ver Historial");

        btnEditar.addActionListener(e -> habilitarEdicion());
        btnGuardar.addActionListener(e -> guardarCambios(bd));
        btnCambiarContraseña.addActionListener(e -> mostrarDialogoCambiarContraseña());
        btnVerHistorial.addActionListener(e -> mostrarHistorial());

        panelBotones.add(btnEditar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCambiarContraseña);
        panelBotones.add(btnVerHistorial);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    
	private Map<Integer, Double> obtenerGastosAnuales(List<Seguro> seguros) {
        Map<Integer, Double> gastos = new HashMap<>();
        for (Seguro seguro : seguros) {
			if(gastos.containsKey(seguro.getFechaContratacion().getYear())) {
				gastos.put(seguro.getFechaContratacion().getYear(), gastos.get(seguro.getFechaContratacion().getYear())+seguro.getCostoMensual());
			}else {
				gastos.put(seguro.getFechaContratacion().getYear(), seguro.getCostoMensual());
			}
		}
        Map<Integer, Double> gastosOrdenados = new TreeMap<>(gastos);
        return gastosOrdenados; // Reemplazar con lógica real
    }

    private Map<String, Integer> obtenerTiposSeguro(List<Seguro> seguros) {
        Map<String, Integer> tipos = new HashMap<>();
        for (Seguro seguro : seguros) {
        	if(tipos.containsKey(seguro.getTipo().toString())) {
        		tipos.put(seguro.getTipo().toString(), tipos.get(seguro.getTipo().toString() + 1));
        	}else {
        		tipos.put(seguro.getTipo().toString(), 1);
        	}
        }
        return tipos; // Reemplazar con lógica real
    }

    private Map<String, Integer> obtenerEstadoSeguros(List<Seguro> seguros) {
        Map<String, Integer> estados = new HashMap<>();
        estados.put("Activo", 0);
        estados.put("Inactivo", 0);
        for (Seguro seguro : seguros) {
        	if(seguro.getEstado().equals("Activo")) {
        		estados.put("Activo", estados.get("Activo")+1);
        	}else {
        		estados.put("Inactivo", estados.get("Inactivo")+1);
        	}
        }
        
        return estados; // Reemplazar con lógica real
    }


    private JTextField crearCampo(String etiqueta, String valor, boolean editable, JPanel panel) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField campo = new JTextField(valor);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setEditable(editable);
        panel.add(lbl);
        panel.add(campo);
        return campo;
    }

    private void habilitarEdicion() {
        campoEmail.setEditable(true);
        campoTelefono.setEditable(true);
        JOptionPane.showMessageDialog(this, "Edición Habilitada");
    }

    private void guardarCambios(Bdd bd) {
        if (campoEmail.isEditable() && campoTelefono.isEditable()) {
            String nuevoEmail = campoEmail.getText();
            String nuevoTelefono = campoTelefono.getText();
         // Verificar que el teléfono tiene 9 dígitos
            if (!nuevoTelefono.matches("[0-9]{9}")) {
                JOptionPane.showMessageDialog(null, "El teléfono debe tener 9 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar formato del email
            if (!nuevoEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                JOptionPane.showMessageDialog(null, "El formato del email no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lógica para actualizar en la base de datos
            bd.actualizarDatosEnBD(campoDNI.getText(), nuevoEmail, nuevoTelefono);

            campoEmail.setEditable(false);
            campoTelefono.setEditable(false);
        } else {
            JOptionPane.showMessageDialog(this, "Habilita la edición antes de guardar cambios.");
        }
    }


    private void mostrarDialogoCambiarContraseña() {
        JOptionPane.showMessageDialog(this, "Función de cambiar contraseña aún no implementada.");
    }

    private void mostrarHistorial() {
        JOptionPane.showMessageDialog(this, "Función de historial aún no implementada.");
    }
    


    private void cambiarFoto(Bdd bd) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona una foto de perfil");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try {
                // Crear la carpeta si no existe
                Files.createDirectories(Paths.get("resources/imagenes"));

                // Copiar el archivo a la carpeta de destino
                String destino = "resources/imagenes/" + archivoSeleccionado.getName();
                Files.copy(archivoSeleccionado.toPath(), Paths.get(destino), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Actualizar la etiqueta de la foto
                lblFotoPerfil.setText("");
                lblFotoPerfil.setIcon(new ImageIcon(new ImageIcon(destino).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));

                // Guardar la ruta en la base de datos
                bd.guardarRutaFotoEnBD(campoDNI.getText(), destino);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private JFreeChart crearGraficoGastosAnuales(Map<Integer, Double> gastosAnuales) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<Integer, Double> entry : gastosAnuales.entrySet()) {
            dataset.addValue(entry.getValue(), "Gastos (€)", entry.getKey());
        }
        return ChartFactory.createLineChart(
                "Gastos Anuales",      // Título
                "Año",                // Eje X
                "Gastos (€)",         // Eje Y
                dataset,
                PlotOrientation.VERTICAL,
                false,                 // Leyenda
                true,                  // Tooltips
                false                  // URLs
        );
    }
    
    private JFreeChart crearGraficoTiposSeguro(Map<String, Integer> tiposSeguro) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : tiposSeguro.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        return ChartFactory.createPieChart(
                "Porcentaje de Tipos de Seguro",
                dataset,
                true,  // Leyenda
                true,  // Tooltips
                false  // URLs
        );
    }
    
    private JFreeChart crearGraficoEstadoSeguros(Map<String, Integer> estadoSeguros) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : estadoSeguros.entrySet()) {
            dataset.addValue(entry.getValue(), "Cantidad", entry.getKey());
        }
        return ChartFactory.createBarChart(
                "Distribución del Estado de los Seguros",
                "Estado",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                false, // Leyenda
                true,  // Tooltips
                false  // URLs
        );
    }







//    public static void main(String[] args) {
//        new VentanaPerfilCliente("Nerea Ramírez", "12345678A", "Calle Falsa 123", "nerea@email.com", "600123456");
//    }
}

