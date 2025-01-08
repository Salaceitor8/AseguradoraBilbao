package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.Seguro;
import domain.Siniestro;
import domain.Solicitud;
import domain.TipoSeguro;
import domain.Cliente;
import domain.Encuesta;
import domain.EstadoSiniestro;
import domain.EstadoSolicitud;
import domain.Mensaje;
import domain.Notificacion;


public class Bdd {

    private Connection connection;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor para inicializar la conexión
    public Bdd(String dbName) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            System.out.println("Conexión establecida con la base de datos: " + dbName);

            crearTablas();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    // Método para crear las tablas en SQLite
    private void crearTablas() {
        String createClientes = "CREATE TABLE IF NOT EXISTS clientes (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "nombre TEXT NOT NULL, " +
                                "apellidos TEXT NOT NULL, " +
                                "dni TEXT UNIQUE NOT NULL, " +
                                "telefono TEXT NOT NULL, " +
                                "email TEXT NOT NULL);";

        String createMensajes = "CREATE TABLE IF NOT EXISTS mensajes (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "dni_cliente TEXT NOT NULL, " +
                                "remitente TEXT CHECK(remitente IN ('cliente', 'empleado')), " +
                                "mensaje TEXT NOT NULL, " +
                                "fecha_hora DATETIME NOT NULL, " +
                                "FOREIGN KEY (dni_cliente) REFERENCES clientes(dni));";

        String createSeguros = "CREATE TABLE IF NOT EXISTS seguros (" +
                               "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                               "dni_cliente TEXT NOT NULL, " +
                               "tipo_seguro TEXT NOT NULL, " +
                               "fecha_inicio DATE NOT NULL, " +
                               "costo REAL NOT NULL, " +
                               "estado TEXT CHECK(estado IN ('Activo', 'Inactivo')), " +
                               "FOREIGN KEY (dni_cliente) REFERENCES clientes(dni));";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createClientes);
            stmt.execute(createMensajes);
            stmt.execute(createSeguros);
            System.out.println("Tablas creadas correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear las tablas: " + e.getMessage());
        }
    }
    
    


    // Métodos CRUD para Clientes
    public void insertarCliente(String nombre, String apellidos, String dni, String telefono, String email) {
        String sql = "INSERT INTO clientes (nombre, apellidos, dni, telefono, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, dni);
            pstmt.setString(4, telefono);
            pstmt.setString(5, email);
            pstmt.executeUpdate();
            System.out.println("Cliente insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
        }
    }
    
    public ResultSet obtenerEmpleados() {
        String sql = "SELECT * FROM empleados";
        try {
            return connection.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados: " + e.getMessage());
            return null;
        }
    }


    public ResultSet obtenerClientes() {
        String sql = "SELECT * FROM clientes";
        try {
            return connection.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes: " + e.getMessage());
            return null;
        }
    }
    
    public ArrayList<Notificacion> obtenerNotificacionesPorCliente(String dniCliente) {
        String sql = "SELECT * FROM notificacion WHERE dni_cliente = ?";
        ArrayList<Notificacion> lista = new ArrayList<Notificacion>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, dniCliente); // Establecer el parámetro
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	int id = rs.getInt("id");
            	String dni_cliente = rs.getString("dni_cliente");
            	String resumen = rs.getString("resumen");
            	Notificacion n = new Notificacion(id, dni_cliente, resumen);
            	lista.add(n);
            }
            return lista; // Ejecutar la consulta
        } catch (SQLException e) {
            System.err.println("Error al obtener notificaciones: " + e.getMessage());
            return null;
        }
    }

    
    public Cliente obtenerCLiente(String dni) {
    	String sql = "SELECT * FROM clientes WHERE dni = ?";
    	Cliente c = null;
    	try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    	    pstmt.setString(1, dni);
    	    ResultSet rs = pstmt.executeQuery();
    	    while (rs.next()) {
    	        String nombre = rs.getString("nombre");
    	        String apellidos = rs.getString("apellidos");
    	        int telefono = rs.getInt("telefono");
    	        String email = rs.getString("email");
    	        c = new Cliente(nombre, apellidos, dni, email, telefono);
    	        return c;
    	    }
    	    
    	} catch (SQLException e) {
    	    e.printStackTrace();
    	}
		return c;

    	
    }
    
    public String obtenerGeneroCliente(String dni) {
    	String sql = "SELECT genero FROM clientes WHERE dni = ?";
    	
    	try(PreparedStatement stmt = connection.prepareStatement(sql)){
    		stmt.setString(1, dni);
    		try(ResultSet rs = stmt.executeQuery()){
    			if(rs.next()){
    				return rs.getString("genero");
    			}
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
		return null;
    }
    public String obtenerGeneroEmpleado(String dni) {
    	String sql = "SELECT genero FROM empleados WHERE dni = ?";
    	
    	try(PreparedStatement stmt = connection.prepareStatement(sql)){
    		stmt.setString(1, dni);
    		try(ResultSet rs = stmt.executeQuery()){
    			if(rs.next()){
    				return rs.getString("genero");
    			}
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
		return null;
    }
    
    public String cargarUsuarioDesdeBDempleados(String dni) {
        String sql = "SELECT usuario FROM empleados WHERE dni = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("usuario"); // Devuelve la contraseña
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Muestra errores en la consola
        }
        return null; // Devuelve null si no se encontró el cliente o hubo un error
    }
    
    public String cargarContraseñaDesdeBDempleados(String dni) {
        String sql = "SELECT contraseña FROM empleados WHERE dni = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("contraseña"); // Devuelve la contraseña
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Muestra errores en la consola
        }
        return null; // Devuelve null si no se encontró el cliente o hubo un error
    }
    
    public String cargarUsuarioDesdeBDclientes(String dni) {
        String sql = "SELECT usuario FROM clientes WHERE dni = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("usuario"); // Devuelve la contraseña
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Muestra errores en la consola
        }
        return null; // Devuelve null si no se encontró el cliente o hubo un error
    }
    
    public String cargarContraseñaDesdeBDclientes(String dni) {
        String sql = "SELECT contraseña FROM clientes WHERE dni = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("contraseña"); // Devuelve la contraseña
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Muestra errores en la consola
        }
        return null; // Devuelve null si no se encontró el cliente o hubo un error
    }
    
    public boolean existeUsuarioEnBD(String usuario) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Devuelve true si el usuario ya existe
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Devuelve false si no hay coincidencias o hay un error
    }

    
    public boolean cambiarUsuarioEnBD(String dni, String nuevoUsuario) {
        String sql = "UPDATE clientes SET usuario = ? WHERE dni = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevoUsuario); // Establecer la nueva contraseña
            stmt.setString(2, dni); // Establecer el DNI del cliente

            int filasActualizadas = stmt.executeUpdate(); // Ejecutar la actualización
            return filasActualizadas > 0; // Devuelve true si se actualizó al menos una fila
        } catch (Exception e) {
            e.printStackTrace(); // Imprime cualquier error en la consola
        }
        return false; // Devuelve false si ocurre un error
    }
    
    public boolean cambiarContraseñaEnBD(String dni, String nuevaContraseña) {
        String sql = "UPDATE clientes SET contraseña = ? WHERE dni = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevaContraseña); // Establecer la nueva contraseña
            stmt.setString(2, dni); // Establecer el DNI del cliente

            int filasActualizadas = stmt.executeUpdate(); // Ejecutar la actualización
            return filasActualizadas > 0; // Devuelve true si se actualizó al menos una fila
        } catch (Exception e) {
            e.printStackTrace(); // Imprime cualquier error en la consola
        }
        return false; // Devuelve false si ocurre un error
    }
    
    public String obtenerDNIporCorreo(String correo) {
        String sql = "SELECT dni FROM clientes WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("dni");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	public void actualizarUltimoInicioCliente(String dni, String fecha) {
        String sql = "UPDATE clientes SET ultimo_inicio = ? WHERE dni = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni); // Establecer el DNI del cliente
            stmt.setString(2, fecha);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	public String obtenerUltimoInicioCliente(String dni) {
        String sql = "SELECT ultimo_inicio FROM clientes WHERE dni = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni); 
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String s = rs.getString("ultimo_inicio");
                if (s != null) {
                    return s.toString(); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Nunca";
    }


    
    public String cargarRutaFotoDesdeBD(String dni) {
    	String sql = "SELECT foto FROM clientes WHERE dni = ?";
        
            
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
            	if (rs.next()) {
            		return rs.getString("foto");
            	}
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actualizarCliente(String dni, String nombre, String apellidos, String telefono, String email) {
        String sql = "UPDATE clientes SET nombre = ?, apellidos = ?, telefono = ?, email = ? WHERE dni = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellidos);
            pstmt.setString(3, telefono);
            pstmt.setString(4, email);
            pstmt.setString(5, dni);
            pstmt.executeUpdate();
            System.out.println("Cliente actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        }
    }
    
    public void guardarRutaFotoEnBD(String dni, String rutaFoto) {
    	String sql = "UPDATE clientes SET foto = ? WHERE dni = ?";
        
            
    	try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    		pstmt.setString(1, rutaFoto);
    		pstmt.setString(2, dni);
    		pstmt.executeUpdate();
            pstmt.executeUpdate();
            System.out.println("foto actualizado");
        } catch (Exception e) {
        	System.err.println("Error al actualizar: " + e.getMessage());
        }
    }
    
    public void actualizarDatosEnBD(String dni, String email, String telefono) {
    	String sql = "UPDATE clientes SET email = ?, telefono = ? WHERE dni = ?";
    	try (PreparedStatement pstmt = connection.prepareStatement(sql);){
            pstmt.setString(1, email);
            pstmt.setString(2, telefono);
            pstmt.setString(3, dni);
            pstmt.executeUpdate();
            System.out.println("cliente actualizado");
        } catch (Exception e) {
        	System.err.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarCliente(String dni) {
        String sql = "DELETE FROM clientes WHERE dni = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            pstmt.executeUpdate();
            System.out.println("Cliente eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    // Métodos CRUD para Mensajes
    public void insertarMensaje(String dniCliente, String remitente, String mensaje, String fechaHora) {
        String sql = "INSERT INTO mensajes (dni_cliente, remitente, mensaje, fecha_hora) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dniCliente);
            pstmt.setString(2, remitente);
            pstmt.setString(3, mensaje);
            pstmt.setString(4, fechaHora);
            pstmt.executeUpdate();
            System.out.println("Mensaje insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar mensaje: " + e.getMessage());
        }
    }

    public ArrayList<Mensaje> obtenerMensajes(String dniCliente) {
        String sql = "SELECT * FROM mensajes WHERE dni_cliente ='" + dniCliente + "'";
        ArrayList<Mensaje> mensajes = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String remitente = rs.getString("remitente");
                String mensaje = rs.getString("mensaje");
                LocalDateTime fechaHora = LocalDateTime.parse(rs.getString("fecha_hora"), dateFormatter);
                Mensaje m = new Mensaje(remitente, mensaje, fechaHora);
                mensajes.add(m);
            }
            return mensajes;
        } catch (SQLException e) {
            System.err.println("Error al obtener mensajes: " + e.getMessage());
            return null;
        }
    }

    public void eliminarMensaje(int id) {
        String sql = "DELETE FROM mensajes WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Mensaje eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar mensaje: " + e.getMessage());
        }
    }

    // Métodos CRUD para Seguros
    public void insertarSeguro(String dniCliente, String tipoSeguro, String fechaInicio, double costo, String estado) {
        String sql = "INSERT INTO seguros (dni_cliente, tipo_seguro, fecha_inicio, costo, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dniCliente);
            pstmt.setString(2, tipoSeguro);
            pstmt.setString(3, fechaInicio);
            pstmt.setDouble(4, costo);
            pstmt.setString(5, estado);
            pstmt.executeUpdate();
            System.out.println("Seguro insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar seguro: " + e.getMessage());
        }
    }
    public void insertarSiniestro(String dniCliente, String resumen, TipoSeguro tipoSeguro, String estado) {
        String sql = "INSERT INTO siniestros (dni_cliente, resumen, tipo_seguro, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dniCliente); // DNI del cliente
            pstmt.setString(2, resumen); // Resumen del siniestro
            pstmt.setString(3, tipoSeguro.toString()); // Tipo de seguro
            pstmt.setString(4, estado); // Estado del siniestro

            pstmt.executeUpdate();
            System.out.println("Siniestro insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar siniestro: " + e.getMessage());
        }
    }
    
    public List<Siniestro> obtenerSiniestros(String estado, String dni) {
        StringBuilder sql = new StringBuilder("SELECT * FROM siniestros");
        boolean hasCondition = false;

        // Construir condiciones dinámicas
        if (!"TODOS".equalsIgnoreCase(estado)) {
            sql.append(" WHERE estado = ?");
            hasCondition = true;
        }

        if (dni != null && !dni.trim().isEmpty() && !dni.equals("Buscar por DNI...")) {
            sql.append(hasCondition ? " AND " : " WHERE ");
            sql.append("dni_cliente = ?");
        }

        List<Siniestro> siniestros = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            int parameterIndex = 1;

            // Asignar parámetros a la consulta
            if (!"TODOS".equalsIgnoreCase(estado)) {
                pstmt.setString(parameterIndex++, estado);
            }

            if (dni != null && !dni.trim().isEmpty() && !dni.equals("Buscar por DNI...")) {
                pstmt.setString(parameterIndex++, dni);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Siniestro siniestro = new Siniestro(
                            rs.getInt("id"),
                            rs.getString("dni_cliente"),
                            rs.getString("resumen"),
                            TipoSeguro.valueOf(rs.getString("tipo_seguro")),
                            rs.getDouble("precio"),

                            EstadoSiniestro.valueOf(rs.getString("estado"))
                    );
                    siniestros.add(siniestro);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener siniestros: " + e.getMessage());
        }
        return siniestros;
    }
    
    public void dejarEnPendienteSiniestro(String dni, String tipoSeguro) {
        String sql = "UPDATE siniestros SET estado = 'PENDIENTE' WHERE dni_cliente = ? AND tipo_seguro = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            pstmt.setString(2, tipoSeguro);

            int filasActualizadas = pstmt.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Siniestro marcado como PENDIENTE.");
            } else {
                System.out.println("No se encontró ningún siniestro para dejar en pendiente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al dejar siniestro en pendiente: " + e.getMessage());
        }
    }

    
    public void rechazarSiniestro(String dni, String tipoSeguro) {
        String sql = "UPDATE siniestros SET estado = 'RECHAZADO' WHERE dni_cliente = ? AND tipo_seguro = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            pstmt.setString(2, tipoSeguro);

            int filasActualizadas = pstmt.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Siniestro marcado como RECHAZADO.");
            } else {
                System.out.println("No se encontró ningún siniestro para rechazar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al rechazar siniestro: " + e.getMessage());
        }
    }

    
    public void resolverSiniestro(String dni, String tipoSeguro) {
        String sql = "UPDATE siniestros SET estado = 'RESUELTO' WHERE dni_cliente = ? AND tipo_seguro = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            pstmt.setString(2, tipoSeguro);

            int filasActualizadas = pstmt.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Siniestro marcado como RESUELTO.");
            } else {
                System.out.println("No se encontró ningún siniestro para resolver.");
            }
        } catch (SQLException e) {
            System.err.println("Error al resolver siniestro: " + e.getMessage());
        }
    }




    
    public ArrayList<Seguro> obtenerSeguros(String dniCliente) {
    	ArrayList<Seguro> lista = new ArrayList<>();
        String sql = "SELECT * FROM seguros WHERE dni_cliente = '" + dniCliente + "'"; // Consulta directa
        try (Statement stmt = connection.createStatement()) {
//            System.out.println("Consulta directa ejecutada: " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
//                System.out.println("Tipo: " + rs.getString("tipo_seguro"));
//                System.out.println("Fecha: " + rs.getString("fecha_inicio"));
//                System.out.println("Costo: " + rs.getDouble("costo"));
//                System.out.println("Estado: " + rs.getString("estado"));

                TipoSeguro tipo = TipoSeguro.valueOf(rs.getString("tipo_seguro"));
                LocalDate fecha = LocalDate.parse(rs.getString("fecha_inicio"), formatter);
                double costo = rs.getDouble("costo");
                String estado = rs.getString("estado");
                Seguro seguro = new Seguro(tipo, fecha, costo, estado);
                lista.add(seguro);

               
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error al obtener seguros (consulta directa): " + e.getMessage());
            return null;
        }
    }




    public void actualizarSeguro(int id, String tipoSeguro, String fechaInicio, double costo, String estado) {
        String sql = "UPDATE seguros SET tipo_seguro = ?, fecha_inicio = ?, costo = ?, estado = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tipoSeguro);
            pstmt.setString(2, fechaInicio);
            pstmt.setDouble(3, costo);
            pstmt.setString(4, estado);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            System.out.println("Seguro actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar seguro: " + e.getMessage());
        }
    }

    public void eliminarSeguro(int id) {
        String sql = "DELETE FROM seguros WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Seguro eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar seguro: " + e.getMessage());
        }
    }
    
    public int obtenerIdSeguro(String dniCliente, String tipoSeguro, String fechaInicio, double costo, String estado) {
        String sql = "SELECT id FROM seguros WHERE dni_cliente = ? AND tipo_seguro = ? AND fecha_inicio = ? AND costo = ? AND estado = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dniCliente);
            pstmt.setString(2, tipoSeguro);
            pstmt.setString(3, fechaInicio);
            pstmt.setDouble(4, costo);
            pstmt.setString(5, estado);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id"); // Devuelve el ID si existe
            } else {
                System.out.println("No se encontró ningún seguro que coincida con los criterios.");
                return -1; // ID no encontrado
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID del seguro: " + e.getMessage());
            return -1;
        }
    }

    

    public void actualizarSeguroEstado(String dniCliente, int filaSeleccionada, String estado) {
        String sql = "UPDATE seguros SET estado = ? WHERE id = ? AND dni_cliente = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            pstmt.setInt(2, filaSeleccionada);
            pstmt.setString(3, dniCliente);
            pstmt.executeUpdate();
            System.out.println("Estado del seguro actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado del seguro: " + e.getMessage());
        }
    }
    
    
 // Método para añadir una solicitud
    public void añadirSolicitud(String dniCliente, String pregunta) {
        String sql = "INSERT INTO solicitudes_preguntas (dni_cliente, pregunta, estado) VALUES (?, ?, 'PENDIENTE')";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dniCliente);
            pstmt.setString(2, pregunta);
            pstmt.executeUpdate();
            System.out.println("Solicitud añadida correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al añadir solicitud: " + e.getMessage());
        }
    }

    // Método para obtener solicitudes pendientes
    public List<Solicitud> obtenerSolicitudesPendientes() {
        String sql = "SELECT * FROM solicitudes_preguntas WHERE estado = 'PENDIENTE'";
        List<Solicitud> solicitudes = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Solicitud solicitud = new Solicitud(
                        rs.getInt("id"),
                        rs.getString("dni_cliente"),
                        rs.getString("pregunta"),
                        EstadoSolicitud.valueOf(rs.getString("estado")),
                        rs.getString("respuesta")
                );
                solicitudes.add(solicitud);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes pendientes: " + e.getMessage());
        }
        return solicitudes;
    }

    // Método para aceptar una solicitud
    public void aceptarSolicitud(int id, String respuesta) {
        String sql = "UPDATE solicitudes_preguntas SET estado = 'ACEPTADA', respuesta = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, respuesta);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Solicitud aceptada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al aceptar solicitud: " + e.getMessage());
        }
    }

    // Método para rechazar una solicitud
    public void rechazarSolicitud(int id) {
        String sql = "UPDATE solicitudes_preguntas SET estado = 'RECHAZADA', respuesta = NULL WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Solicitud rechazada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al rechazar solicitud: " + e.getMessage());
        }
    }
    
    public List<Solicitud> obtenerSolicitudesPorFiltro(String estado, String dniFiltro) {
    	System.out.println(dniFiltro);
        List<Solicitud> solicitudes = new ArrayList<>();
        String sql = "SELECT * FROM solicitudes_preguntas WHERE 1=1";
        if (!estado.equals("TODAS")) {
            sql += " AND UPPER(estado) = ?";
        }
        if (!dniFiltro.equals("Buscar por DNI...")) {
            sql += " AND dni_cliente LIKE ?";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int paramIndex = 1;

            if (!estado.equals("TODAS")) {
                pstmt.setString(paramIndex, estado.toUpperCase());
            }
            if (!dniFiltro.equals("Buscar por DNI...") && !estado.equals("TODAS")) {
                pstmt.setString(paramIndex+1, "%" + dniFiltro.trim() + "%");
            }
            if(estado.equals("TODAS") && !dniFiltro.equals("Buscar por DNI...")) {
            	pstmt.setString(paramIndex, "%" + dniFiltro.trim() + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    solicitudes.add(new Solicitud(
                            rs.getInt("id"),
                            rs.getString("dni_cliente"),
                            rs.getString("pregunta"),
                            EstadoSolicitud.valueOf(rs.getString("estado").toUpperCase()),
                            rs.getString("respuesta")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes filtradas: " + e.getMessage());
        }

        return solicitudes;
    }
    
    public HashMap<String, String> obtenerSolicitudesAceptadas() {
        HashMap<String, String> solicitudesAceptadas = new HashMap<>();
        String sql = "SELECT pregunta, respuesta FROM solicitudes_preguntas WHERE estado = 'ACEPTADA'";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String pregunta = rs.getString("pregunta");
                String respuesta = rs.getString("respuesta");
                solicitudesAceptadas.put(pregunta, respuesta);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes aceptadas: " + e.getMessage());
        }

        return solicitudesAceptadas;
    }


    // Método para obtener el ID de una solicitud en base al DNI y la pregunta
    public int obtenerIdSolicitud(String dniCliente, String pregunta) {
        String sql = "SELECT id FROM solicitudes_preguntas WHERE dni_cliente = ? AND pregunta = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dniCliente);
            pstmt.setString(2, pregunta);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de la solicitud: " + e.getMessage());
        }
        return -1; // Retorna -1 si no se encuentra el ID
    }

    // Método para dejar una solicitud en pendiente
    public void dejarEnPendiente(int id) {
        String sql = "UPDATE solicitudes_preguntas SET estado = 'PENDIENTE', respuesta = NULL WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Solicitud cambiada a pendiente correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al cambiar solicitud a pendiente: " + e.getMessage());
        }
    }
    
    public List<Siniestro> obtenerSiniestrosPorFiltro(String estado, String dniFiltro) {
        System.out.println("Filtro DNI: " + dniFiltro);
        List<Siniestro> siniestros = new ArrayList<>();
        String sql = "SELECT * FROM siniestros WHERE 1=1";
        
        if (!estado.equalsIgnoreCase("TODOS")) {
            sql += " AND UPPER(estado) = ?";
        }
        if (!dniFiltro.equals("Buscar por DNI...") && !dniFiltro.trim().isEmpty()) {
            sql += " AND dni_cliente LIKE ?";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int paramIndex = 1;

            if (!estado.equalsIgnoreCase("TODOS")) {
                pstmt.setString(paramIndex++, estado.toUpperCase());
            }
            if (!dniFiltro.equals("Buscar por DNI...") && !dniFiltro.trim().isEmpty()) {
                pstmt.setString(paramIndex, "%" + dniFiltro.trim() + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    siniestros.add(new Siniestro(
                            rs.getInt("id"),
                            rs.getString("dni_cliente"),
                            rs.getString("resumen"),
                            TipoSeguro.valueOf(rs.getString("tipo_seguro")),
                            rs.getDouble("precio"),
                            EstadoSiniestro.valueOf(rs.getString("estado").toUpperCase())
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener siniestros filtrados: " + e.getMessage());
        }

        return siniestros;
    }
    
    public List<Siniestro> obtenerSiniestrosPendientes() {
        String sql = "SELECT * FROM siniestros WHERE estado = 'PENDIENTE'";
        List<Siniestro> siniestros = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	siniestros.add(new Siniestro(
                        rs.getInt("id"),
                        rs.getString("dni_cliente"),
                        rs.getString("resumen"),
                        TipoSeguro.valueOf(rs.getString("tipo_seguro")),
                        rs.getDouble("precio"),
                        EstadoSiniestro.valueOf(rs.getString("estado").toUpperCase())
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener solicitudes pendientes: " + e.getMessage());
        }
        return siniestros;
    }
    
    public ArrayList<String> obtenerNombresEmpleados() {
        ArrayList<String> nombresEmpleados = new ArrayList<>();
        String sql = "SELECT nombre FROM empleados WHERE departamento = 'Atención al Cliente y Ventas'";
        
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                nombresEmpleados.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombres de empleados: " + e.getMessage());
        }
        
        return nombresEmpleados;
    }
    
    public void insertarEncuesta(String dniCliente, String fecha, String satisfecho, String aspectoFavorito, int valoracion, String comentario) {
        String sql = "INSERT INTO encuestas (dni_cliente, fecha, satisfecho, aspecto_favorito, valoracion, comentario) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dniCliente);
            pstmt.setString(2, fecha);
            pstmt.setString(3, satisfecho);
            pstmt.setString(4, aspectoFavorito);
            pstmt.setInt(5, valoracion);
            pstmt.setString(6, comentario);
            pstmt.executeUpdate();
            System.out.println("Encuesta insertada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al insertar encuesta: " + e.getMessage());
        }
    }
    
    public List<Encuesta> obtenerEncuestas() {
        List<Encuesta> encuestas = new ArrayList<>();
        String sql = "SELECT * FROM encuestas";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Encuesta encuesta = new Encuesta(
                    rs.getInt("id"),
                    rs.getString("dni_cliente"),
                    rs.getString("fecha"),
                    rs.getString("satisfecho"),
                    rs.getString("aspecto_favorito"),
                    rs.getInt("valoracion"),
                    rs.getString("comentario")
                );
                encuestas.add(encuesta);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener encuestas: " + e.getMessage());
        }
        return encuestas;
    }







    // Cerrar la conexión
    public void cerrarConexion() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

	
}

