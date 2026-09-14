package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Database {

    // Método para leer el archivo .env de forma manual (ya que no usamos librerías externas para esto)
    public static String getEnvVariable(String key) {
        try (BufferedReader br = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(key + "=")) {
                    return line.substring(key.length() + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo .env");
        }
        return null;
    }

    // Método principal para probar la conexión
    public static void main(String[] args) {
        String dbUrl = getEnvVariable("DB_URL");
        
        if (dbUrl == null) {
            System.out.println("Error: No se encontro la variable DB_URL en el archivo .env");
            return;
        }

        try {
            // Conectar a la base de datos
            Connection conn = DriverManager.getConnection(dbUrl);
            if (conn != null) {
                System.out.println("--- Conexion a la base de datos exitosa! ---");
                
                // 1. Crear la tabla de productos (SQL Básico)
                String sqlCrearTabla = "CREATE TABLE IF NOT EXISTS productos ("
                                     + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                     + "nombre TEXT NOT NULL, "
                                     + "precio REAL NOT NULL"
                                     + ");";
                
                java.sql.Statement stmt = conn.createStatement();
                stmt.execute(sqlCrearTabla);
                System.out.println("Tabla 'productos' verificada/creada.");

                // 2. Insertar un par de productos de prueba SOLO si la tabla está vacía
                java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM productos");
                if (rs.next() && rs.getInt("total") == 0) {
                    String sqlInsertar = "INSERT INTO productos (nombre, precio) VALUES "
                                       + "('Tomates Organicos', 24.99), "
                                       + "('Manzanas Rojas', 15.50);";
                    stmt.execute(sqlInsertar);
                    System.out.println("Se insertaron 2 productos de prueba.");
                } else {
                    System.out.println("La tabla ya tiene datos, no se insertaron nuevos.");
                }

                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos:");
            System.out.println(e.getMessage());
        }
    }
}
