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

    // Variables estáticas para la Caché en memoria
    private static String cacheProductos = null;
    private static long cacheExpiracion = 0;

    // 1. READ: Obtener todos los productos (CON CACHÉ)
    public String obtenerTodos() {
        long tiempoActual = System.currentTimeMillis();
        
        // Verificar si el caché existe y no ha expirado (TTL)
        if (cacheProductos != null && tiempoActual < cacheExpiracion) {
            System.out.println("Respondiendo desde la Caché en memoria");
            return cacheProductos;
        }
        
        System.out.println("Caché vacío o expirado. Consultando la base de datos...");
        StringBuilder jsonArray = new StringBuilder("[");
        String dbUrl = Database.getEnvVariable("DB_URL");
        
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            // Usamos ORDER BY nombre para aprovechar el nuevo índice de la base de datos
            String sql = "SELECT * FROM productos ORDER BY nombre";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            
            boolean primero = true;
            while (rs.next()) {
                if (!primero) jsonArray.append(",");
                jsonArray.append(String.format("{\"id\": %d, \"nombre\": \"%s\", \"precio\": %.2f}", 
                    rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio")));
                primero = false;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR BD] Falló al consultar productos con url: " + dbUrl);
            e.printStackTrace();
            return "{\"error\": \"Error al consultar BD: " + e.getMessage() + "\"}";
        }
        
        jsonArray.append("]");
        
        // Guardar en la caché y definir el TTL (Time To Live) a 30 segundos
        cacheProductos = jsonArray.toString();
        cacheExpiracion = tiempoActual + 30000; // 30,000 ms = 30 seg
        
        return cacheProductos;
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
