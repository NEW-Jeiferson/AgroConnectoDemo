// ================================
// ACTIVIDAD 2 - Primera Conexión con la API
// ================================
async function obtenerProductos() {
  try {
    const respuesta = await fetch('https://fakestoreapi.com/products');
    const datos = await respuesta.json();
    console.log('Productos recibidos:', datos);
  } catch (error) {
    console.error('Error al conectar con la API:', error);
  }
}

obtenerProductos();