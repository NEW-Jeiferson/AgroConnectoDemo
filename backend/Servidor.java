import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

// Importar nuestras nuevas rutas
import routes.ProductoRoutes;

public class Servidor {
    public static void main(String[] args) throws Exception {
        // Punto de entrada que enciende todo el sistema (SERVER.JS equivalente)
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
        
        // Conectar la ruta al nuevo archivo de rutas
        HttpContext context = server.createContext("/api/productos", new ProductoRoutes());
        
        // Mantener el middleware
        context.getFilters().add(new LoggerMiddleware());
        
        server.setExecutor(null); 
        server.start();
        System.out.println("Servidor de la API corriendo con estructura MVC en el puerto 3000");
    }

    // Middleware de registro se queda en el servidor principal
    static class LoggerMiddleware extends Filter {
        @Override
        public void doFilter(HttpExchange t, Chain chain) throws IOException {
            String method = t.getRequestMethod();
            String path = t.getRequestURI().getPath();
            System.out.println("[MIDDLEWARE LOG] Petición entrante -> Método: " + method + " | Ruta: " + path);
            chain.doFilter(t);
        }
        @Override
        public String description() { return "Logging"; }
    }
}
