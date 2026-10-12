import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

// Importar nuestras nuevas rutas
import routes.ProductoRoutes;
import routes.UsuarioRoutes;

public class Servidor {
    public static void main(String[] args) throws Exception {
        // Punto de entrada que enciende todo el sistema (SERVER.JS equivalente)
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
        
        // Conectar la ruta al nuevo archivo de rutas
        HttpContext contextProductos = server.createContext("/api/productos", new ProductoRoutes());
        
        // Conectar rutas de Autenticación
        UsuarioRoutes authRoutes = new UsuarioRoutes();
        HttpContext contextRegistro = server.createContext("/api/registro", authRoutes);
        HttpContext contextLogin = server.createContext("/api/login", authRoutes);
        
        // Mantener el middleware para todas las rutas API
        LoggerMiddleware logger = new LoggerMiddleware();
        contextProductos.getFilters().add(logger);
        contextRegistro.getFilters().add(logger);
        contextLogin.getFilters().add(logger);
        
        // Agregar Middleware de Seguridad solo a productos
        contextProductos.getFilters().add(new AuthMiddleware());
        contextProductos.getFilters().add(new RoleMiddleware());
        
        // Servir archivos estáticos del Frontend (HTML, CSS, JS) y la ruta /health
        File publicDir = new File("public");
        if (!publicDir.exists()) {
            publicDir = new File("../src");
            if (!publicDir.exists()) {
                publicDir = new File("src");
            }
        }
        server.createContext("/", new StaticHandler(publicDir));
        
        server.setExecutor(null); 
        server.start();
        System.out.println("Servidor de la API corriendo con estructura MVC en el puerto 3000");
    }

    // Handler para servir el Frontend HTML, CSS, JS
    static class StaticHandler implements HttpHandler {
        private final File baseDir;

        public StaticHandler(File baseDir) {
            this.baseDir = baseDir;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            
            // Redirección o mapeo de rutas amigables
            if (path.equals("/") || path.isEmpty()) {
                path = "/index.html";
            } else if (path.equals("/health")) {
                path = "/health.html";
            }

            File file = new File(baseDir, path).getCanonicalFile();
            // Prevenir Path Traversal y verificar que exista
            if (!file.getPath().startsWith(baseDir.getCanonicalPath()) || !file.exists() || file.isDirectory()) {
                String error = "404 Not Found";
                byte[] errorBytes = error.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
                exchange.sendResponseHeaders(404, errorBytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(errorBytes);
                }
                return;
            }

            // Detectar tipo de contenido MIME
            String mime = "application/octet-stream";
            String lower = file.getName().toLowerCase();
            if (lower.endsWith(".html")) mime = "text/html; charset=UTF-8";
            else if (lower.endsWith(".css")) mime = "text/css; charset=UTF-8";
            else if (lower.endsWith(".js")) mime = "application/javascript; charset=UTF-8";
            else if (lower.endsWith(".json")) mime = "application/json; charset=UTF-8";
            else if (lower.endsWith(".png")) mime = "image/png";
            else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) mime = "image/jpeg";
            else if (lower.endsWith(".svg")) mime = "image/svg+xml";
            else if (lower.endsWith(".ico")) mime = "image/x-icon";

            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().set("Content-Type", mime);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }


    static class AuthMiddleware extends Filter {
        @Override
        public void doFilter(HttpExchange t, Chain chain) throws IOException {
            String method = t.getRequestMethod();
            

            if (method.equals("OPTIONS") || method.equals("GET")) {
                chain.doFilter(t);
                return;
            }


            String authHeader = t.getRequestHeaders().getFirst("Authorization");
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                rechazarPeticion(t, "Falta el token de autorización (Bearer Token)");
                return;
            }

            String token = authHeader.substring(7); // Quitar "Bearer "
            
            if (utils.JwtUtil.validarToken(token)) {

                chain.doFilter(t);
            } else {

                rechazarPeticion(t, "Token inválido o expirado");
            }
        }

        private void rechazarPeticion(HttpExchange t, String mensaje) throws IOException {
            System.out.println("Acceso Denegado: " + mensaje);
            String response = "{\"error\": \"" + mensaje + "\"}";
            byte[] bytes = response.getBytes("UTF-8");
            t.getResponseHeaders().set("Content-Type", "application/json");
            t.sendResponseHeaders(401, bytes.length); // 401 = No Autorizado
            java.io.OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        }

        @Override
        public String description() { return "Autenticación JWT"; }
    }

    // Middleware de registro se queda en el servidor principal
    static class LoggerMiddleware extends Filter {
        @Override
        public void doFilter(HttpExchange t, Chain chain) throws IOException {
            long inicio = System.currentTimeMillis();
            String method = t.getRequestMethod();
            String path = t.getRequestURI().getPath();
            
            chain.doFilter(t); // Pasa al controlador
            
            long fin = System.currentTimeMillis();
            long tiempo = fin - inicio;
            System.out.println("[PERFORMANCE LOG] Petición " + method + " " + path + " tardó: " + tiempo + " ms");
        }
        @Override
        public String description() { return "Logging"; }
    }

    // Middleware de Autorización basado en Roles
    static class RoleMiddleware extends Filter {
        @Override
        public void doFilter(HttpExchange t, Chain chain) throws IOException {
            String method = t.getRequestMethod();
            
            // Opciones y lectura pública
            if (method.equals("OPTIONS") || method.equals("GET")) {
                chain.doFilter(t);
                return;
            }

            // A este punto, el AuthMiddleware ya verificó que el token existe y es válido
            String authHeader = t.getRequestHeaders().getFirst("Authorization");
            String token = authHeader.substring(7);
            
            // Extraer el rol del token (sin necesidad de ir a la BD)
            String rol = utils.JwtUtil.extraerRol(token);
            
            if (rol.equals("administrador")) {
                // Solo administradores pueden crear/editar/borrar
                chain.doFilter(t);
            } else {
                // Clientes normales intentando hacer algo prohibido
                System.out.println("⛔ Permiso Denegado: Un usuario de nivel '" + rol + "' intentó modificar productos.");
                String response = "{\"error\": \"Acceso denegado: Se requieren permisos de administrador.\"}";
                byte[] bytes = response.getBytes("UTF-8");
                t.getResponseHeaders().set("Content-Type", "application/json");
                t.sendResponseHeaders(403, bytes.length); // 403 = Forbidden
                java.io.OutputStream os = t.getResponseBody();
                os.write(bytes);
                os.close();
            }
        }
        @Override
        public String description() { return "Autorización de Roles"; }
    }
}
