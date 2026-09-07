package routes;

import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import controllers.ProductoController;

public class ProductoRoutes implements HttpHandler {
    // Instanciamos el controlador que tiene la lógica
    private ProductoController controller = new ProductoController();

    @Override
    public void handle(HttpExchange t) throws IOException {
        t.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        
        String method = t.getRequestMethod();
        String path = t.getRequestURI().getPath();
        
        String response = "";
        int statusCode = 200;

        // Define qué endpoints existen y apunta al controlador adecuado
        if (method.equals("GET")) {
            if (path.equals("/api/productos")) {
                response = controller.obtenerTodos();
            } else if (path.equals("/api/productos/ofertas")) {
                response = controller.obtenerOfertas();
            } else {
                response = "{\"error\": \"Ruta GET no encontrada\"}";
                statusCode = 404;
            }
        } else if (method.equals("POST") && path.equals("/api/productos")) {
            response = controller.crearProducto();
            statusCode = 201;
        } else {
            response = "{\"error\": \"Método no soportado\"}";
            statusCode = 405;
        }

        byte[] bytes = response.getBytes("UTF-8");
        t.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
