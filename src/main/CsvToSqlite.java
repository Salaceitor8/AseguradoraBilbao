package main;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CsvToSqlite {

    private static final String DB_NAME = "aseguradora.db";
    private static final String CLIENTES_CSV = "clientes.csv";
    private static final String MENSAJES_CSV = "mensajes.csv";
    private static final String SEGUROS_CSV = "seguros.csv";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME)) {
//            System.out.println("Conexión establecida con SQLite.");

            // Crear tablas si no existen
            crearTablas(connection);

            // Importar datos desde los CSV
            importarClientes(connection, CLIENTES_CSV);
            importarMensajes(connection, MENSAJES_CSV);
            importarSeguros(connection, SEGUROS_CSV);

//            System.out.println("Importación completada.");
        } catch (SQLException e) {
//            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    private static void crearTablas(Connection connection) throws SQLException {
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

        try (PreparedStatement stmt = connection.prepareStatement(createClientes)) {
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement(createMensajes)) {
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement(createSeguros)) {
            stmt.executeUpdate();
        }

//        System.out.println("Tablas creadas correctamente.");
    }

    private static void importarClientes(Connection connection, String csvFile) {
        String insertCliente = "INSERT INTO clientes (nombre, apellidos, dni, telefono, email) VALUES (?, ?, ?, ?, ?)";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             PreparedStatement pstmt = connection.prepareStatement(insertCliente)) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    pstmt.setString(1, datos[0].trim()); // nombre
                    pstmt.setString(2, datos[1].trim()); // apellidos
                    pstmt.setString(3, datos[2].trim()); // dni
                    pstmt.setString(4, datos[3].trim()); // telefono
                    pstmt.setString(5, datos[4].trim()); // email
                    pstmt.executeUpdate();
                }
            }

//            System.out.println("Clientes importados correctamente.");
        } catch (IOException | SQLException e) {
//            System.err.println("Error al importar clientes: " + e.getMessage());
        }
    }

    private static void importarMensajes(Connection connection, String csvFile) {
        String insertMensaje = "INSERT INTO mensajes (dni_cliente, remitente, mensaje, fecha_hora) VALUES (?, ?, ?, ?)";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             PreparedStatement pstmt = connection.prepareStatement(insertMensaje)) {

            String linea;
            boolean primeraLinea = true; // Saltar encabezado
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split(";");
                if (datos.length == 4) {
                    pstmt.setString(1, datos[0].trim()); // dni_cliente
                    pstmt.setString(2, datos[1].trim()); // remitente
                    pstmt.setString(3, datos[2].trim()); // mensaje
                    pstmt.setString(4, datos[3].trim()); // fecha_hora
                    pstmt.executeUpdate();
                }
            }

//            System.out.println("Mensajes importados correctamente.");
        } catch (IOException | SQLException e) {
//            System.err.println("Error al importar mensajes: " + e.getMessage());
        }
    }

    private static void importarSeguros(Connection connection, String csvFile) {
        String insertSeguro = "INSERT INTO seguros (dni_cliente, tipo_seguro, fecha_inicio, costo, estado) VALUES (?, ?, ?, ?, ?)";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             PreparedStatement pstmt = connection.prepareStatement(insertSeguro)) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    pstmt.setString(1, datos[0].trim()); // dni_cliente
                    pstmt.setString(2, datos[1].trim()); // tipo_seguro
                    pstmt.setString(3, datos[2].trim()); // fecha_inicio
                    pstmt.setDouble(4, Double.parseDouble(datos[3].trim())); // costo
                    pstmt.setString(5, datos[4].trim()); // estado
                    pstmt.executeUpdate();
                }
            }

//            System.out.println("Seguros importados correctamente.");
        } catch (IOException | SQLException e) {
//            System.err.println("Error al importar seguros: " + e.getMessage());
        }
    }
}

