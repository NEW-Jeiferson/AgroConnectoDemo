import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ActualizarTablaUsuarios {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:agroconnect.db";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            
            // Agregar la columna 'rol' a la tabla existente (si no existe, fallará limpiamente)
            String sql = "ALTER TABLE usuarios ADD COLUMN rol TEXT DEFAULT 'cliente';";
            stmt.execute(sql);
            System.out.println("Columna 'rol' agregada exitosamente a la tabla usuarios.");
            
        } catch (Exception e) {
            System.out.println("Nota: " + e.getMessage());
        }
    }
}
