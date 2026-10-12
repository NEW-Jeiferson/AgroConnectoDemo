package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;
import config.Database;
import utils.JwtUtil;

public class UsuarioController {
    
    // Función auxiliar para extraer datos del JSON (sin librerias externas)
    private String extraerString(String json, String clave) {
        Matcher m = Pattern.compile("\"" + clave + "\"\\s*:\\s*\"([^\"]+)\"").matcher(json);
        return m.find() ? m.group(1) : null;
    }

    // REGISTRO
    public String registro(String body) {
        String email = extraerString(body, "email");
        String plainPassword = extraerString(body, "password");
        String rol = extraerString(body, "rol");
        if (rol == null) rol = "cliente"; // rol por defecto

        if (email == null || plainPassword == null) {
            return "{\"error\": \"Faltan datos obligatorios (email, password)\"}";
        }

        // 1. Encriptar contraseña con jBcrypt
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));

        // 2. Guardar en Base de Datos
        String dbUrl = Database.getEnvVariable("DB_URL");
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String sql = "INSERT INTO usuarios (email, password, rol) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, rol);
            pstmt.executeUpdate();
            
            return "{\"mensaje\": \"Usuario registrado exitosamente como " + rol + "\"}";
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                return "{\"error\": \"El email ya está registrado\"}";
            }
            return "{\"error\": \"Error al registrar usuario\"}";
        }
    }

    // LOGIN
    public String login(String body) {
        String email = extraerString(body, "email");
        String plainPassword = extraerString(body, "password");

        if (email == null || plainPassword == null) {
            return "{\"error\": \"Faltan credenciales\"}";
        }

        String dbUrl = Database.getEnvVariable("DB_URL");
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            // Buscar al usuario
            String sql = "SELECT * FROM usuarios WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbHashedPassword = rs.getString("password");
                String dbRol = rs.getString("rol");
                if (dbRol == null) dbRol = "cliente";
                
                // 1. Validar la contraseña desencriptada vs encriptada
                if (BCrypt.checkpw(plainPassword, dbHashedPassword)) {
                    // 2. Credenciales válidas -> Generar JWT con el ROL
                    String token = JwtUtil.generarToken(email, dbRol);
                    return "{\"mensaje\": \"Login exitoso\", \"token\": \"" + token + "\"}";
                } else {
                    return "{\"error\": \"Contraseña incorrecta\"}";
                }
            } else {
                return "{\"error\": \"Usuario no encontrado\"}";
            }
        } catch (SQLException e) {
            return "{\"error\": \"Error en la base de datos\"}";
        }
    }
}
