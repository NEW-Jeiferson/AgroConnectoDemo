# API AgroConnect - Backend

Este es el backend oficial del marketplace AgroConnect, desarrollado en Java utilizando la estructura MVC (Model-View-Controller) adaptada a `HttpServer`.

## Estructura del Proyecto

*   **/models**: Define la estructura de los datos (Ej. `Producto.java`).
*   **/controllers**: Contiene la lógica de negocio y las respuestas JSON (Ej. `ProductoController.java`).
*   **/routes**: Define qué endpoints existen, lee las peticiones HTTP y las envía al controlador correspondiente (Ej. `ProductoRoutes.java`).
*   **Servidor.java**: Es el archivo principal (equivalente a `server.js`). Inicia el servidor, aplica el middleware y conecta las rutas.

---

## Documentación de Rutas Disponibles

La API corre de manera predeterminada en `http://localhost:3000`.

### 1. Obtener Todos los Productos
*   **Ruta:** `/api/productos`
*   **Método HTTP:** `GET`
*   **Descripción:** Retorna la lista completa de todos los productos agrícolas disponibles en el marketplace.
*   **Respuesta Exitosa (200 OK):**
    ```json
    [
      {"id": 1, "nombre": "Tomates Orgánicos", "precio": 24.99},
      {"id": 2, "nombre": "Manzanas", "precio": 15.50}
    ]
    ```

### 2. Obtener Productos en Oferta
*   **Ruta:** `/api/productos/ofertas`
*   **Método HTTP:** `GET`
*   **Descripción:** Retorna únicamente aquellos productos que tienen un descuento aplicado.
*   **Respuesta Exitosa (200 OK):**
    ```json
    [
      {"id": 1, "nombre": "Tomates Orgánicos", "descuento": "20%"}
    ]
    ```

### 3. Crear un Nuevo Producto
*   **Ruta:** `/api/productos`
*   **Método HTTP:** `POST`
*   **Descripción:** Simula la inserción de un nuevo producto en la base de datos del marketplace.
*   **Respuesta Exitosa (201 Created):**
    ```json
    {
      "mensaje": "Producto creado exitosamente",
      "estado": "OK"
    }
    ```
