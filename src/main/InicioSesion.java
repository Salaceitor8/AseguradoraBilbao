package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class InicioSesion extends JFrame{
	
	private static final Color COLOR_PRINCIPAL = new Color(0, 51, 102); // Azul oscuro
    private static final Color COLOR_CONTRASTE = Color.WHITE;
    private CardLayout cardLayout;
    private JPanel panelCentral;
    private JTextField campoUsuario;
    private JPasswordField campoContraseña;

    
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

        
    }
    
    private JPanel crearPanelSeleccion() {
    	JPanel panel = new JPanel(new BorderLayout());
    	return panel;
        
    }
    
    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        return panel;
    }
    
    public static void main(String[] args) {
		new InicioSesion();
	}

}