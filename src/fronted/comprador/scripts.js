// VALIDACIONES - Formulario de Registro AgroConnect

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registerForm');

    // Si esta página no tiene el formulario de registro, no ejecutamos
    // nada de la lógica de validación (evita el error "Cannot read
    // properties of null").
    if (!form) return;

    const successMessage = document.getElementById('successMessage');
    const provinceGroup = document.getElementById('provinceGroup');
    const provinceInput = document.getElementById('province');

    // Referencias a los inputs
    const fullNameInput = document.getElementById('fullName');
    const emailInput = document.getElementById('email');
    const phoneInput = document.getElementById('phone');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const termsInput = form.querySelector('input[name="terms"]');
    const userTypeRadios = form.querySelectorAll('input[name="userType"]');

    // Mostrar / ocultar la provincia según el tipo de usuario
    // (solo es obligatoria para productores)

    function updateProvinceVisibility() {
        const selected = form.querySelector('input[name="userType"]:checked');
        if (selected && selected.value === 'productor') {
            provinceGroup.style.display = 'flex';
            provinceInput.required = true;
        } else {
            provinceGroup.style.display = 'none';
            provinceInput.required = false;
            provinceInput.value = '';
            hideError('provinceError');
        }
    }
    userTypeRadios.forEach(radio => {
        radio.addEventListener('change', updateProvinceVisibility);
    });
    updateProvinceVisibility(); // estado inicial


    // Helpers para mostrar/ocultar mensajes de error

    function showError(id, message) {
        const el = document.getElementById(id);
        if (!el) return;
        el.textContent = message;
        el.classList.add('show');
    }

    function hideError(id) {
        const el = document.getElementById(id);
        if (!el) return;
        el.textContent = '';
        el.classList.remove('show');
    }

    // ----------------------------------------
    // Validadores individuales
    // Cada uno devuelve true si es válido, false si no
    // ----------------------------------------
    function validateUserType() {
        const selected = form.querySelector('input[name="userType"]:checked');
        if (!selected) {
            showError('userTypeError', 'Selecciona si eres productor o consumidor.');
            return false;
        }
        hideError('userTypeError');
        return true;
    }

    function validateFullName() {
        const value = fullNameInput.value.trim();
        const nameRegex = /^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]{3,}$/;
        if (value === '') {
            showError('fullNameError', 'El nombre es obligatorio.');
            return false;
        }
        if (!nameRegex.test(value)) {
            showError('fullNameError', 'Ingresa un nombre válido (mínimo 3 letras, sin números).');
            return false;
        }
        hideError('fullNameError');
        return true;
    }

    function validateEmail() {
        const value = emailInput.value.trim();
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (value === '') {
            showError('emailError', 'El email es obligatorio.');
            return false;
        }
        if (!emailRegex.test(value)) {
            showError('emailError', 'Ingresa un email válido (ej: tu@email.com).');
            return false;
        }
        hideError('emailError');
        return true;
    }

    function validatePhone() {
        const value = phoneInput.value.trim();
        // Acepta formatos como: +1 809 000-0000, 8090000000, 809-000-0000, etc.
        const phoneRegex = /^\+?[0-9]{1,3}?[\s-]?\(?[0-9]{3}\)?[\s-]?[0-9]{3}[\s-]?[0-9]{4}$/;
        if (value === '') {
            showError('phoneError', 'El teléfono es obligatorio.');
            return false;
        }
        if (!phoneRegex.test(value)) {
            showError('phoneError', 'Ingresa un teléfono válido (ej: +1 809 000-0000).');
            return false;
        }
        hideError('phoneError');
        return true;
    }

    function validateProvince() {
        // Solo se valida si el campo está visible/requerido (productor)
        if (!provinceInput.required) {
            hideError('provinceError');
            return true;
        }
        const value = provinceInput.value.trim();
        if (value === '') {
            showError('provinceError', 'La provincia es obligatoria para productores.');
            return false;
        }
        if (value.length < 3) {
            showError('provinceError', 'Ingresa una provincia válida.');
            return false;
        }
        hideError('provinceError');
        return true;
    }

    function validatePassword() {
        const value = passwordInput.value;
        // Mínimo 8 caracteres, al menos una letra y un número
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d).{8,}$/;
        if (value === '') {
            showError('passwordError', 'La contraseña es obligatoria.');
            return false;
        }
        if (!passwordRegex.test(value)) {
            showError('passwordError', 'Mínimo 8 caracteres, con al menos una letra y un número.');
            return false;
        }
        hideError('passwordError');
        return true;
    }

    function validateConfirmPassword() {
        const value = confirmPasswordInput.value;
        if (value === '') {
            showError('confirmPasswordError', 'Confirma tu contraseña.');
            return false;
        }
        if (value !== passwordInput.value) {
            showError('confirmPasswordError', 'Las contraseñas no coinciden.');
            return false;
        }
        hideError('confirmPasswordError');
        return true;
    }

    function validateTerms() {
        if (!termsInput.checked) {
            showError('termsError', 'Debes aceptar los términos y condiciones.');
            return false;
        }
        hideError('termsError');
        return true;
    }


    // Validación en tiempo real (al salir del campo)

    fullNameInput.addEventListener('blur', validateFullName);
    emailInput.addEventListener('blur', validateEmail);
    phoneInput.addEventListener('blur', validatePhone);
    provinceInput.addEventListener('blur', validateProvince);
    passwordInput.addEventListener('blur', validatePassword);
    confirmPasswordInput.addEventListener('blur', validateConfirmPassword);
    confirmPasswordInput.addEventListener('input', validateConfirmPassword);
    termsInput.addEventListener('change', validateTerms);
    userTypeRadios.forEach(radio => radio.addEventListener('change', validateUserType));


    // Validación al enviar el formulario

    form.addEventListener('submit', (event) => {
        event.preventDefault();

        const validations = [
            validateUserType(),
            validateFullName(),
            validateEmail(),
            validatePhone(),
            validateProvince(),
            validatePassword(),
            validateConfirmPassword(),
            validateTerms()
        ];

        const isFormValid = validations.every(v => v === true);

        if (isFormValid) {
            successMessage.style.display = 'block';
            form.reset();
            updateProvinceVisibility();

            // Oculta el mensaje de éxito después de unos segundos
            setTimeout(() => {
                successMessage.style.display = 'none';
            }, 4000);
        } else {
            successMessage.style.display = 'none';
            // Enfoca el primer campo con error para mejor experiencia de usuario
            const firstError = form.querySelector('.error.show');
            if (firstError) {
                firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        }
    });
});


// ================================
// ACTIVIDAD 2 - Primera Conexión con la API
// ACTIVIDAD 3 - Mostrar los Datos en tarjetas
// ================================

// Crea el HTML de UNA tarjeta a partir de un producto de la API.
// Si el producto no trae imagen, usamos un placeholder gris para
// que la tarjeta no se vea rota.
function crearTarjetaProducto(producto) {
    const imagen = producto.image || 'https://placehold.co/400x300?text=Sin+imagen';
    const titulo = producto.title || 'Producto sin nombre';
    const categoria = producto.category || 'General';
    const precio = typeof producto.price === 'number' ? producto.price.toFixed(2) : '0.00';
    const rating = producto.rating && producto.rating.rate ? producto.rating.rate : '—';

    return `
        <div class="bg-white border border-outline-variant rounded-xl overflow-hidden group shadow-sm flex flex-col">
            <div class="relative h-48 overflow-hidden bg-surface-container-high">
                <img class="w-full h-full object-contain p-4 group-hover:scale-105 transition-transform duration-500"
                     alt="${titulo}" src="${imagen}"
                     onerror="this.src='https://placehold.co/400x300?text=Sin+imagen'">
            </div>
            <div class="p-4 flex-1 flex flex-col">
                <div class="flex justify-between items-start mb-2 gap-2">
                    <h3 class="font-bold text-on-surface leading-tight line-clamp-2">${titulo}</h3>
                    <div class="flex items-center gap-1 bg-surface-container-high px-2 py-0.5 rounded text-xs shrink-0">
                        <span class="material-symbols-outlined filled-icon text-tertiary text-[14px]">star</span>
                        <span class="font-bold">${rating}</span>
                    </div>
                </div>
                <p class="text-sm text-on-surface-variant mb-4 capitalize">${categoria}</p>
                <div class="mt-auto flex justify-between items-center">
                    <span class="text-xl font-bold text-primary">$${precio}</span>
                    <button class="w-10 h-10 bg-secondary-container text-on-secondary-container rounded-lg flex items-center justify-center hover:bg-primary-container transition-colors active:scale-90">
                        <span class="material-symbols-outlined">add</span>
                    </button>
                </div>
            </div>
        </div>
    `;
}

// Recorre el arreglo de productos y genera una tarjeta por cada uno,
// insertándolas todas dentro del contenedor del marketplace.
function mostrarProductos(productos) {
    const contenedor = document.getElementById('productosContainer');
    if (!contenedor) return; // esta página no tiene el contenedor, no hacemos nada

    contenedor.innerHTML = productos
        .map(producto => crearTarjetaProducto(producto))
        .join('');
}

// Muestra un mensaje de error claro dentro del marketplace,
// en vez de dejar la pantalla en blanco o solo avisar en consola.
function mostrarError(mensaje) {
    const contenedor = document.getElementById('productosContainer');
    if (!contenedor) return;

    contenedor.innerHTML = `
        <div class="col-span-full flex flex-col items-center justify-center text-center py-16 px-6 bg-error-container/20 border border-error/30 rounded-xl">
            <span class="material-symbols-outlined text-error text-[48px] mb-3">error</span>
            <h3 class="text-lg font-bold text-on-surface mb-1">No pudimos cargar los productos</h3>
            <p class="text-sm text-on-surface-variant mb-4">${mensaje}</p>
            <button onclick="obtenerProductos()" class="px-6 py-2 bg-primary text-on-primary rounded-lg font-medium text-sm hover:opacity-90 transition-colors">
                Intentar de nuevo
            </button>
        </div>
    `;
}

async function obtenerProductos() {
  // Contenedor límite de tiempo: si la API no responde en 8 segundos,
  // cancelamos la petición en vez de dejar al usuario esperando para siempre.
  const controlador = new AbortController();
  const limiteDeTiempo = setTimeout(() => controlador.abort(), 2000);

  try {
    const contenedor = document.getElementById('productosContainer');
    if (contenedor) {
        contenedor.innerHTML = `<p class="col-span-full text-center py-16 text-on-surface-variant">Cargando productos...</p>`;
    }

    const respuesta = await fetch('https://fakestoreapi.com/products', {
        signal: controlador.signal
    });

    // fetch() NO lanza error automáticamente si el servidor responde
    // con un código de error (404, 500, etc.) — hay que revisarlo a mano.
    if (!respuesta.ok) {
        throw new Error(`El servidor respondió con error ${respuesta.status}`);
    }

    const datos = await respuesta.json();
    console.log('Productos recibidos:', datos);
    mostrarProductos(datos);

  } catch (error) {
    console.error('Error al conectar con la API:', error);

    if (error.name === 'AbortError') {
        // Se canceló porque tardó demasiado (timeout)
        mostrarError('La API está tardando demasiado en responder. Revisa tu conexión e intenta de nuevo.');
    } else if (!navigator.onLine) {
        // El navegador detecta que no hay internet
        mostrarError('Parece que no tienes conexión a internet.');
    } else {
        // Cualquier otro error: URL mal escrita, servidor caído, etc.
        mostrarError('Ocurrió un problema al conectar con la API. Intenta de nuevo en unos momentos.');
    }

  } finally {
    clearTimeout(limiteDeTiempo);
  }
}

obtenerProductos();