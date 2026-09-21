package routes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import controllers.ProductoController;

public class ProductoRoutes implements HttpHandler {
    private ProductoController controller = new ProductoController();

    @Override
    public void handle(HttpExchange t) throws IOException {
        t.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        
        String method = t.getRequestMethod();
        
        // Handle Preflight OPTIONS request for CORS
        if (method.equals("OPTIONS")) {
            t.sendResponseHeaders(204, -1);
            return;
        }
        
        String path = t.getRequestURI().getPath();
        
        String response = "";
        int statusCode = 200;

        // Leer el body (para POST y PUT)
        InputStream is = t.getRequestBody();
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String body = s.hasNext() ? s.next() : "";
        s.close();

        // Extraer ID de la ruta si existe (ej. /api/productos/5)
        int id = -1;
        if (path.startsWith("/api/productos/") && !path.equals("/api/productos/ofertas")) {
            try {
                id = Integer.parseInt(path.substring("/api/productos/".length()));
            } catch(Exception e){}
        }

        // ENRUTADOR CRUD
        if (method.equals("GET")) {
            if (path.equals("/api/productos")) {
                response = controller.obtenerTodos(); // READ ALL
            } else {
                response = "{\"error\": \"Ruta GET no encontrada\"}";
                statusCode = 404;
            }
        } 
        else if (method.equals("POST") && path.equals("/api/productos")) {
            response = controller.crearProducto(body); // CREATE
            statusCode = 201;
        } 
        else if (method.equals("PUT") && id != -1) {
            response = controller.actualizarProducto(id, body); // UPDATE
        } 
        else if (method.equals("DELETE") && id != -1) {
            response = controller.eliminarProducto(id); // DELETE
        } 
        else {
            response = "{\"error\": \"Metodo o ruta no soportada\"}";
            statusCode = 405;
        }

        byte[] bytes = response.getBytes("UTF-8");
        t.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
