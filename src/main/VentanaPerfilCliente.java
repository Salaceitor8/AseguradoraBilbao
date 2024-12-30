package main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class VentanaPerfilCliente extends JFrame {

    private JTextField campoNombre, campoDNI, campoEmail, campoTelefono;
    private JButton btnCambiarFoto, btnEditar, btnGuardar, btnCambiarContraseña, btnVerHistorial;
    private JLabel lblFotoPerfil;

    public VentanaPerfilCliente(String nombreCliente, String dni, String direccion, String email, String telefono) {
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

        btnCambiarFoto = new JButton("Cambiar Foto");
        btnCambiarFoto.addActionListener(e -> cambiarFoto());

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
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Estadísticas del Cliente"));
        JLabel lblEstadisticas = new JLabel("Aquí se mostrarán las estadísticas en el futuro", SwingConstants.CENTER);
        lblEstadisticas.setFont(new Font("Arial", Font.ITALIC, 16));
        panelDerecho.add(lblEstadisticas, BorderLayout.CENTER);

        // Añadir los paneles izquierdo y derecho al centro
        panelCentral.add(panelIzquierdo);
        panelCentral.add(panelDerecho);
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnEditar = new JButton("Editar");
        btnGuardar = new JButton("Guardar Cambios");
        btnCambiarContraseña = new JButton("Cambiar Contraseña");
        btnVerHistorial = new JButton("Ver Historial");

        btnEditar.addActionListener(e -> habilitarEdicion());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCambiarContraseña.addActionListener(e -> mostrarDialogoCambiarContraseña());
        btnVerHistorial.addActionListener(e -> mostrarHistorial());

        panelBotones.add(btnEditar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCambiarContraseña);
        panelBotones.add(btnVerHistorial);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
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

    private void guardarCambios() {
        if (campoEmail.isEditable() && campoTelefono.isEditable()) {
            String nuevoEmail = campoEmail.getText();
            String nuevoTelefono = campoTelefono.getText();

            // Lógica para actualizar en la base de datos
            boolean actualizado = actualizarDatosEnBD(campoDNI.getText(), nuevoEmail, nuevoTelefono);

            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Cambios guardados correctamente en la base de datos.");
                campoEmail.setEditable(false);
                campoTelefono.setEditable(false);
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar los cambios en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
    
    private boolean actualizarDatosEnBD(String dni, String email, String telefono) {
        try {
            // Llamada a la clase de conexión y ejecución de la consulta
            String sql = "UPDATE clientes SET email = ?, telefono = ? WHERE dni = ?";
            PreparedStatement stmt = DriverManager.getConnection("jdbc:sqlite:resources/db/aseguradora.db").prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, telefono);
            stmt.setString(3, dni);

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0; // Devuelve true si se actualizó correctamente
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private void cambiarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona una foto de perfil");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try {
                // Crear la carpeta si no existe
                Files.createDirectories(Paths.get("resources/imagenes"));

                // Copiar el archivo al destino, sobrescribiendo si ya existe
                String destino = "resources/imagenes/" + archivoSeleccionado.getName();
                Files.copy(archivoSeleccionado.toPath(), Paths.get(destino), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Actualizar la etiqueta de la foto
                lblFotoPerfil.setText("");
                lblFotoPerfil.setIcon(new ImageIcon(new ImageIcon(destino).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                JOptionPane.showMessageDialog(this, "Foto actualizada correctamente.");
            } catch (Exception ex) {
                // Mostrar un mensaje más detallado del error
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // Imprime el error en la consola para depuración
            }
        }
    }



    public static void main(String[] args) {
        new VentanaPerfilCliente("Nerea Ramírez", "12345678A", "Calle Falsa 123", "nerea@email.com", "600123456");
    }
}

