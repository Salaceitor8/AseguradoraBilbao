package main;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import domain.Seguro;

public class VentanaPerfilCliente extends JFrame {

    private JTextField campoNombre, campoDNI, campoEmail, campoTelefono;
    private JButton btnCambiarFoto, btnEditar, btnGuardar, btnCambiarContraseña, btnCambiarUsuario, btnDescargarContrato;
    private JLabel lblFotoPerfil;
    private JPanel panelGraficos;
    private JProgressBar barraProgreso;

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

        // Panel derecho con barra de progreso inicialmente
        panelGraficos = new JPanel(new BorderLayout());
        panelGraficos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
        	    "Cargando Estadísticas", 
        	    TitledBorder.DEFAULT_JUSTIFICATION, 
        	    TitledBorder.DEFAULT_POSITION, 
        	    new Font("Arial", Font.BOLD, 16))); // Cambiar a un tamaño más grande


        // Crear un panel contenedor para centrar la barra de progreso
        JPanel panelProgreso = new JPanel(new GridBagLayout());
        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true); // Habilitar texto sobre la barra
        barraProgreso.setString("Calculando estadísticas..."); // Texto inicial
        barraProgreso.setPreferredSize(new Dimension(300, 30)); // Tamaño más pequeño


        // Añadir la barra de progreso al panel contenedor
        panelProgreso.add(barraProgreso);

        // Añadir el panel contenedor al panel principal
        panelGraficos.add(panelProgreso, BorderLayout.CENTER);

        // Añadir los paneles izquierdo y derecho al centro
        panelCentral.add(panelIzquierdo);
        panelCentral.add(panelGraficos);
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnEditar = new JButton("Editar");
        btnGuardar = new JButton("Guardar Cambios");
        btnCambiarUsuario = new JButton("Cambiar Usuario");
        btnCambiarContraseña = new JButton("Cambiar Contraseña");
        

        btnEditar.addActionListener(e -> habilitarEdicion());
        btnGuardar.addActionListener(e -> guardarCambios(bd));
        btnCambiarUsuario.addActionListener(e -> new VentanaCambiarUsuario(dni, bd));
        btnCambiarContraseña.addActionListener(e -> new VentanaCambiarContraseña(campoDNI.getText(), bd));
        

        panelBotones.add(btnEditar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCambiarUsuario);
        panelBotones.add(btnCambiarContraseña);
        add(panelBotones, BorderLayout.SOUTH);

        // Iniciar la simulación del cálculo de estadísticas
        simularCalculoEstadisticas(seguros, bd);

        setVisible(true);
    }
    
    



    private void simularCalculoEstadisticas(List<Seguro> seguros, Bdd bd) {
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    final int progreso = i;
                    SwingUtilities.invokeLater(() -> barraProgreso.setValue(progreso));
                    Thread.sleep(10); // Simula el tiempo de cálculo
                }
                // Una vez completado, mostrar los gráficos
                SwingUtilities.invokeLater(() -> mostrarGraficos(seguros, bd));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void mostrarGraficos(List<Seguro> seguros, Bdd bd) {
        panelGraficos.removeAll();
        panelGraficos.setLayout(new GridLayout(3, 1, 10, 10));
        panelGraficos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
        	    "Estadísticas del cliente", 
        	    TitledBorder.DEFAULT_JUSTIFICATION, 
        	    TitledBorder.DEFAULT_POSITION, 
        	    new Font("Arial", Font.BOLD, 16))); // Cambiar a un tamaño más grande


        // Generar gráficos
        Map<Integer, Double> gastosAnuales = obtenerGastosAnuales(seguros);
        JFreeChart graficoGastos = crearGraficoGastosAnuales(gastosAnuales);
        ChartPanel panelGastos = new ChartPanel(graficoGastos);

        Map<String, Integer> tiposSeguro = obtenerTiposSeguro(seguros);
        JFreeChart graficoTipos = crearGraficoTiposSeguro(tiposSeguro);
        ChartPanel panelTipos = new ChartPanel(graficoTipos);

        Map<String, Integer> estadoSeguros = obtenerEstadoSeguros(seguros);
        JFreeChart graficoEstados = crearGraficoEstadoSeguros(estadoSeguros);
        ChartPanel panelEstados = new ChartPanel(graficoEstados);

        // Añadir gráficos al panel
        panelGraficos.add(panelGastos);
        panelGraficos.add(panelTipos);
        panelGraficos.add(panelEstados);

        // Refrescar el panel
        panelGraficos.revalidate();
        panelGraficos.repaint();
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

    private Map<Integer, Double> obtenerGastosAnuales(List<Seguro> seguros) {
        Map<Integer, Double> gastos = new HashMap<>();
        for (Seguro seguro : seguros) {
            gastos.merge(seguro.getFechaContratacion().getYear(), seguro.getCostoMensual(), Double::sum);
        }
        return new TreeMap<>(gastos); // Ordenar por año
    }

    private Map<String, Integer> obtenerTiposSeguro(List<Seguro> seguros) {
        Map<String, Integer> tipos = new HashMap<>();
        for (Seguro seguro : seguros) {
            tipos.merge(seguro.getTipo().toString(), 1, Integer::sum);
        }
        return tipos;
    }

    private Map<String, Integer> obtenerEstadoSeguros(List<Seguro> seguros) {
        Map<String, Integer> estados = new HashMap<>();
        estados.put("Activo", 0);
        estados.put("Inactivo", 0);
        for (Seguro seguro : seguros) {
            estados.merge(seguro.getEstado(), 1, Integer::sum);
        }
        return estados;
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

    private void cambiarFoto(Bdd bd) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona una foto de perfil");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            java.io.File archivoSeleccionado = fileChooser.getSelectedFile();
            try {
                // Crear la carpeta si no existe
                java.nio.file.Files.createDirectories(java.nio.file.Paths.get("resources/imagenes"));

                // Copiar el archivo a la carpeta de destino
                String destino = "resources/imagenes/" + archivoSeleccionado.getName();
                java.nio.file.Files.copy(archivoSeleccionado.toPath(), java.nio.file.Paths.get(destino), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

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
}
