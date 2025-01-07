package main;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TablonNotificaciones {

    private JFrame parent;

    public TablonNotificaciones(JFrame parent) {
        this.parent = parent; // Ventana principal para asociar el diálogo
    }

    public void mostrar() {
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:aseguradora.db")) {
            String consulta = """
                SELECT 
                    c.nombre AS cliente, 
                    s.nombre_seguro, 
                    s.fecha_vencimiento, 
                    s.estado_pago
                FROM clientes c
                JOIN seguros s ON c.id_cliente = s.id_cliente
                WHERE s.estado_pago = 'Pendiente' OR DATE(s.fecha_vencimiento) <= DATE('now', '+7 days');
            """;

            PreparedStatement statement = conexion.prepareStatement(consulta);
            ResultSet resultados = statement.executeQuery();

            // Crear ventana emergente
            JDialog dialogo = new JDialog(parent, "Tablón de Notificaciones", true);
            dialogo.setSize(500, 300);
            dialogo.setLayout(new BorderLayout());

            // Panel para mostrar notificaciones
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JScrollPane scrollPane = new JScrollPane(panel);

            // Agregar notificaciones
            boolean hayNotificaciones = false;
            while (resultados.next()) {
                hayNotificaciones = true;
                String cliente = resultados.getString("cliente");
                String seguro = resultados.getString("nombre_seguro");
                String vencimiento = resultados.getString("fecha_vencimiento");
                String estado = resultados.getString("estado_pago");

                String texto = String.format(
                        "Cliente: %s | Seguro: %s | Vence: %s | Estado: %s",
                        cliente, seguro, vencimiento, estado
                );

                JLabel label = new JLabel(texto);
                if ("Pendiente".equals(estado)) {
                    label.setForeground(Color.RED); // Resaltar pagos pendientes en rojo
                }
                panel.add(label);
            }

            if (!hayNotificaciones) {
                panel.add(new JLabel("No hay notificaciones pendientes."));
            }

            dialogo.add(scrollPane, BorderLayout.CENTER);

            // Botón para cerrar el diálogo
            JButton cerrar = new JButton("Cerrar");
            cerrar.addActionListener(e -> dialogo.dispose());
            dialogo.add(cerrar, BorderLayout.SOUTH);

            dialogo.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parent, "Error al consultar la base de datos: " + ex.getMessage());
        }
    }
}