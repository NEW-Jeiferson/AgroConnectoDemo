// ================================
// AgroConnect - Configuración Global
// ================================
// Este archivo centraliza las URLs y configuraciones del proyecto.
// Las URLs se definen aquí para no tener valores "hardcodeados"
// dispersos por todo el código.
//
// En producción, estas variables vendrán de variables de entorno
// (process.env.PORT, process.env.API_BASE_URL, etc.) cargadas
// por el servidor o por un bundler como Vite/Webpack.

const CONFIG = {
    // Puerto del servidor (lee de process.env.PORT si está disponible, si no usa 8080)
    PORT: (typeof process !== 'undefined' && process.env && process.env.PORT)
        ? process.env.PORT
        : '8080',

    // URL base del servidor (en navegador toma dinámicamente el dominio actual, ej: Railway)
    API_BASE_URL: (typeof window !== 'undefined' && window.location && window.location.origin && window.location.origin.startsWith('http'))
        ? window.location.origin
        : ((typeof process !== 'undefined' && process.env && process.env.API_BASE_URL)
            ? process.env.API_BASE_URL
            : 'http://localhost:3000'),

    // URL de la API externa (FakeStore para desarrollo/pruebas)
    FAKE_STORE_API_URL: (typeof process !== 'undefined' && process.env && process.env.FAKE_STORE_API_URL)
        ? process.env.FAKE_STORE_API_URL
        : 'https://fakestoreapi.com',
};

// Congelar el objeto para evitar modificaciones accidentales
Object.freeze(CONFIG);
