// VALIDACIONES - Formulario de Registro AgroConnect

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registerForm');
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