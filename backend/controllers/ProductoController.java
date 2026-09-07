package controllers;

public class ProductoController {
    
    // Contiene la lógica de qué hacer con cada petición
    public String obtenerTodos() {
        return "[{\"id\": 1, \"nombre\": \"Tomates Orgánicos\", \"precio\": 24.99}, {\"id\": 2, \"nombre\": \"Manzanas\", \"precio\": 15.50}]";
    }

    public String obtenerOfertas() {
        return "[{\"id\": 1, \"nombre\": \"Tomates Orgánicos\", \"descuento\": \"20%\"}]";
    }

    public String crearProducto() {
        return "{\"mensaje\": \"Producto creado exitosamente\", \"estado\": \"OK\"}";
    }
}
