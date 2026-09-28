package routes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import controllers.UsuarioController;

public class UsuarioRoutes implements HttpHandler {
    private UsuarioController controller = new UsuarioController();

    @Override
    public void handle(HttpExchange t) throws IOException {
        t.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
        
        String method = t.getRequestMethod();
        
        // Handle Preflight OPTIONS request
        if (method.equals("OPTIONS")) {
            t.sendResponseHeaders(204, -1);
            return;
        }
        
        String path = t.getRequestURI().getPath();
        String response = "";
        int statusCode = 200;

        // Leer el body de la petición
        InputStream is = t.getRequestBody();
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String body = s.hasNext() ? s.next() : "";
        s.close();

        // Enrutador
        if (method.equals("POST") && path.equals("/api/registro")) {
            response = controller.registro(body);
            if(response.contains("error")) statusCode = 400;
            else statusCode = 201;
        } 
        else if (method.equals("POST") && path.equals("/api/login")) {
            response = controller.login(body);
            if(response.contains("error")) statusCode = 401;
        } 
        else {
            response = "{\"error\": \"Ruta de usuario no encontrada\"}";
            statusCode = 404;
        }

        byte[] bytes = response.getBytes("UTF-8");
        t.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
