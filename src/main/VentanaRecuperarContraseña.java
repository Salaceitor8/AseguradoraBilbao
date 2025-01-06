package main;

	import javax.swing.*;
	import java.awt.*;
	import java.util.Random;

	public class VentanaRecuperarContraseña extends JFrame {

	    private static final long serialVersionUID = 1L;
	    private JTextField campoCorreo, campoCodigo;
	    private JPasswordField campoNuevaContraseña, campoConfirmarContraseña;
	    private String codigoGenerado;
	    private final Bdd baseDeDatos;

	    public VentanaRecuperarContraseña(Bdd baseDeDatos) {
	        this.baseDeDatos = baseDeDatos;

	        setTitle("Recuperar Contraseña");
	        setSize(400, 300);
	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setLayout(new CardLayout());

	        // Paneles
	        JPanel panelCorreo = crearPanelCorreo();
	        JPanel panelCodigo = crearPanelCodigo();
	        JPanel panelNuevaContraseña = crearPanelNuevaContraseña();

	        add(panelCorreo, "Correo");
	        add(panelCodigo, "Codigo");
	        add(panelNuevaContraseña, "NuevaContraseña");

	        setVisible(true);
	    }

	    private JPanel crearPanelCorreo() {
	        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

	        campoCorreo = new JTextField();

	        JButton btnEnviarCodigo = new JButton("Enviar Código");
	        btnEnviarCodigo.addActionListener(e -> {
	            String correo = campoCorreo.getText().trim();
	            if (correo.isEmpty() || !correo.contains("@")) {
	                JOptionPane.showMessageDialog(this, "Correo inválido.");
	            } else {
	                generarCodigoRecuperacion();
	                JOptionPane.showMessageDialog(this, "Código generado. Ingrese el código para continuar.");
	                ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Codigo");
	            }
	        });

	        panel.add(new JLabel("Ingrese su correo electrónico:"));
	        panel.add(campoCorreo);
	        panel.add(btnEnviarCodigo);

	        return panel;
	    }

	    private JPanel crearPanelCodigo() {
	        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

	        campoCodigo = new JTextField();

	        JButton btnValidarCodigo = new JButton("Validar Código");
	        btnValidarCodigo.addActionListener(e -> {
	            if (campoCodigo.getText().equals(codigoGenerado)) {
	                JOptionPane.showMessageDialog(this, "Código válido. Proceda a cambiar su contraseña.");
	                ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "NuevaContraseña");
	            } else {
	                JOptionPane.showMessageDialog(this, "Código incorrecto.");
	            }
	        });

	        panel.add(new JLabel("Ingrese el código recibido:"));
	        panel.add(campoCodigo);
	        panel.add(btnValidarCodigo);

	        return panel;
	    }

	    private JPanel crearPanelNuevaContraseña() {
	        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

	        campoNuevaContraseña = new JPasswordField();
	        campoConfirmarContraseña = new JPasswordField();

	        JButton btnRestablecer = new JButton("Restablecer Contraseña");
	        btnRestablecer.addActionListener(e -> {
	            String correo = campoCorreo.getText().trim(); // Correo proporcionado por el usuario
	            String nuevaContraseña = new String(campoNuevaContraseña.getPassword()).trim();
	            String confirmarContraseña = new String(campoConfirmarContraseña.getPassword()).trim();

	            if (nuevaContraseña.isEmpty() || !nuevaContraseña.equals(confirmarContraseña)) {
	                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden o son inválidas.");
	                return;
	            }

	            try {
	                // Obtener el DNI desde la base de datos usando el correo
	                String dni = baseDeDatos.obtenerDNIporCorreo(correo);
	                if (dni == null) {
	                    JOptionPane.showMessageDialog(this, "No se encontró un usuario con el correo proporcionado.");
	                    return;
	                }

	                // Cambiar la contraseña usando el DNI
	                boolean actualizado = baseDeDatos.cambiarContraseñaEnBD(dni, nuevaContraseña);
	                if (actualizado) {
	                    JOptionPane.showMessageDialog(this, "Contraseña restablecida exitosamente.");
	                    dispose(); // Cerrar la ventana
	                } else {
	                    JOptionPane.showMessageDialog(this, "Error al actualizar la contraseña.");
	                }
	            } catch (Exception ex) {
	                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
	            }
	        });

	        panel.add(new JLabel("Nueva contraseña:"));
	        panel.add(campoNuevaContraseña);
	        panel.add(new JLabel("Confirmar contraseña:"));
	        panel.add(campoConfirmarContraseña);
	        panel.add(btnRestablecer);

	        return panel;
	    }

	    private void generarCodigoRecuperacion() {
	        codigoGenerado = String.format("%06d", new Random().nextInt(999999));
	    }
	}

