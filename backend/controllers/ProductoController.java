package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import config.Database;

public class ProductoController {
    
    // Funciones auxiliares para extraer datos del JSON (sin librerias externas)
    private String extraerString(String json, String clave) {
        Matcher m = Pattern.compile("\"" + clave + "\"\\s*:\\s*\"([^\"]+)\"").matcher(json);
        return m.find() ? m.group(1) : null;
    }
    
    private String extraerNumero(String json, String clave) {
        Matcher m = Pattern.compile("\"" + clave + "\"\\s*:\\s*([0-9.]+)").matcher(json);
        return m.find() ? m.group(1) : null;
    }

    // 1. READ: Obtener todos los productos de la BD real
    public String obtenerTodos() {
        StringBuilder jsonArray = new StringBuilder("[");
        String dbUrl = Database.getEnvVariable("DB_URL");
        
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String sql = "SELECT * FROM productos";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            
            boolean primero = true;
            while (rs.next()) {
                if (!primero) jsonArray.append(",");
                jsonArray.append(String.format("{\"id\": %d, \"nombre\": \"%s\", \"precio\": %.2f}", 
                    rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio")));
                primero = false;
            }
        } catch (SQLException e) {
            return "{\"error\": \"Error al consultar BD\"}";
        }
        
        jsonArray.append("]");
        return jsonArray.toString();
    }

    // 2. CREATE: Insertar un nuevo producto
    public String crearProducto(String jsonBody) {
        String nombre = extraerString(jsonBody, "nombre");
        String precioStr = extraerNumero(jsonBody, "precio");
        
        if (nombre == null || precioStr == null) {
            return "{\"error\": \"Faltan datos obligatorios (nombre, precio)\"}";
        }
        
        double precio = Double.parseDouble(precioStr);
        String dbUrl = Database.getEnvVariable("DB_URL");
        
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String sql = "INSERT INTO productos (nombre, precio) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            pstmt.executeUpdate();
            return "{\"mensaje\": \"Producto creado exitosamente en SQLite\"}";
        } catch (SQLException e) {
            return "{\"error\": \"Error al guardar en BD\"}";
        }
    }

    // 3. UPDATE: Actualizar producto existente
    public String actualizarProducto(int id, String jsonBody) {
        String nombre = extraerString(jsonBody, "nombre");
        String precioStr = extraerNumero(jsonBody, "precio");
        String dbUrl = Database.getEnvVariable("DB_URL");
        
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String sql = "UPDATE productos SET nombre = ?, precio = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, Double.parseDouble(precioStr));
            pstmt.setInt(3, id);
            
            int filas = pstmt.executeUpdate();
            if (filas > 0) return "{\"mensaje\": \"Producto actualizado\"}";
            else return "{\"error\": \"Producto no encontrado\"}";
        } catch (Exception e) {
            return "{\"error\": \"Error al actualizar BD\"}";
        }
    }

    // 4. DELETE: Eliminar producto
    public String eliminarProducto(int id) {
        String dbUrl = Database.getEnvVariable("DB_URL");
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String sql = "DELETE FROM productos WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int filas = pstmt.executeUpdate();
            if (filas > 0) return "{\"mensaje\": \"Producto eliminado\"}";
            else return "{\"error\": \"Producto no encontrado\"}";
        } catch (Exception e) {
            return "{\"error\": \"Error al eliminar en BD\"}";
        }
    }
}
