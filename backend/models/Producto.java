package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import config.Database;

public class Producto {
    // 1. Definición de campos y reglas (Tipos de dato)
    public Integer id;           // Auto-generado por la BD
    public String nombre;        // Obligatorio
    public double precio;        // Obligatorio
    public String descuento;     

    // Constructor principal para crear nuevos registros en código
    public Producto(String nombre, double precio, String descuento) {
        this.nombre = nombre;
        this.precio = precio;
        this.descuento = descuento;
    }

    // 2. Conectar el modelo a la base de datos (Método para guardarse a sí mismo)
    public void guardar() {
        // Validar obligatorios en el código
        if (this.nombre == null || this.nombre.isEmpty()) {
            System.out.println("Error del Modelo: El nombre es obligatorio.");
            return;
        }

        String dbUrl = Database.getEnvVariable("DB_URL");
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            // Usamos PreparedStatement para evitar inyecciones SQL
            String sql = "INSERT INTO productos (nombre, precio) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, this.nombre);
            pstmt.setDouble(2, this.precio);
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("--- Modelo guardado exitosamente en BD: " + this.nombre + " ---");
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar el modelo Producto:");
            System.out.println(e.getMessage());
        }
    }

    // 3. Prueba directa para verificar que el modelo funciona
    public static void main(String[] args) {
        System.out.println("Iniciando prueba del Modelo Producto...");
        
        // Creamos un registro de ejemplo
        Producto productoPrueba = new Producto("Zanahorias Frescas", 12.50, null);
        
        // Verificamos que funcione insertándolo
        productoPrueba.guardar();
    }
}
