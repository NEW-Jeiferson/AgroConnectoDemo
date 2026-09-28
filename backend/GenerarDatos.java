import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class GenerarDatos {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:agroconnect.db";
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            // Desactivar autocommit para que la inserción de miles de datos sea rapidísima
            conn.setAutoCommit(false);
            
            String sql = "INSERT INTO productos (nombre, precio) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            System.out.println("Generando 5,000 productos simulados en la base de datos...");
            for (int i = 1; i <= 5000; i++) {
                pstmt.setString(1, "Producto Simulado #" + i);
                pstmt.setDouble(2, Math.round((Math.random() * 100) * 100.0) / 100.0);
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            conn.commit();
            System.out.println("¡5,000 productos creados exitosamente!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
