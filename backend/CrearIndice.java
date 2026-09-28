import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CrearIndice {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:agroconnect.db";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            
            // Crear un índice en la columna 'nombre'
            String sql = "CREATE INDEX IF NOT EXISTS idx_productos_nombre ON productos(nombre);";
            stmt.execute(sql);
            System.out.println("Índice 'idx_productos_nombre' creado exitosamente en SQLite.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
