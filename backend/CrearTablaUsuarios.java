import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CrearTablaUsuarios {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:agroconnect.db";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            
            String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "email TEXT UNIQUE NOT NULL," +
                         "password TEXT NOT NULL" +
                         ");";
            stmt.execute(sql);
            System.out.println("Tabla 'usuarios' creada exitosamente.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
