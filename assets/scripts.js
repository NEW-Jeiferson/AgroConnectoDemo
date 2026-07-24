function increaseQty(btn) {
    const qtyElement = btn.closest('.flex').querySelector('.qty');
    if (!qtyElement) return;
    qtyElement.innerText = parseInt(qtyElement.innerText, 10) + 1;
    updateTotal();
}

function decreaseQty(btn) {
    const qtyElement = btn.closest('.flex').querySelector('.qty');
    if (!qtyElement) return;
    const currentQty = parseInt(qtyElement.innerText, 10);
    if (currentQty > 1) {
        qtyElement.innerText = currentQty - 1;
        updateTotal();
    }
}

function updateTotal() {
    const totalElements = document.querySelectorAll('[data-total-update]');
    totalElements.forEach(el => {
        el.textContent = el.textContent;
    });
    console.log('Total actualizado');
}

function initImageGallery() {
    const thumbnails = document.querySelectorAll('.grid.grid-cols-3 .aspect-square');
    const mainImage = document.querySelector('.product-image-container img');
    if (!thumbnails.length || !mainImage) return;

    thumbnails.forEach(thumb => {
        thumb.addEventListener('click', function() {
            const thumbImg = this.querySelector('img');
            if (!thumbImg) return;
            mainImage.src = thumbImg.src;
            mainImage.alt = thumbImg.alt;
            mainImage.style.opacity = '0.5';
            setTimeout(() => { mainImage.style.opacity = '1'; }, 50);
        });
    });
}

function initFavoriteToggle() {
    const favoriteButtons = Array.from(document.querySelectorAll('[data-icon="favorite"], button')).filter(btn => {
        return btn.querySelector('.material-symbols-outlined')?.textContent.trim() === 'favorite';
    });

    favoriteButtons.forEach(btn => {
        btn.addEventListener('click', event => {
            event.stopPropagation();
            const icon = btn.querySelector('.material-symbols-outlined');
            if (!icon) return;
            icon.classList.toggle('filled-icon');
        });
    });
}

function initGroupHoverEffects() {
    document.querySelectorAll('.group').forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.classList.add('shadow-lg');
        });
        card.addEventListener('mouseleave', () => {
            card.classList.remove('shadow-lg');
        });
    });
}

function initButtonPressEffects() {
    document.querySelectorAll('button').forEach(button => {
        button.addEventListener('mousedown', () => {
            button.style.transform = 'scale(0.95)';
        });
        button.addEventListener('mouseup', () => {
            button.style.transform = 'scale(1)';
        });
        button.addEventListener('mouseleave', () => {
            button.style.transform = 'scale(1)';
        });
    });
}

function initProfileMicroInteractions() {
    document.querySelectorAll('.cursor-pointer').forEach(el => {
        el.addEventListener('mousedown', () => el.style.transform = 'scale(0.98)');
        el.addEventListener('mouseup', () => el.style.transform = 'scale(1)');
        el.addEventListener('mouseleave', () => el.style.transform = 'scale(1)');
    });
}

function initProgressAnimation() {
    const stats = document.querySelectorAll('.font-bold');
    if (!stats.length) return;

    stats.forEach(stat => {
        const target = parseInt(stat.innerText, 10);
        if (!isNaN(target)) {
            let current = 0;
            const increment = target / 20;
            const timer = setInterval(() => {
                current += increment;
                if (current >= target) {
                    stat.innerText = target;
                    clearInterval(timer);
                } else {
                    stat.innerText = Math.floor(current);
                }
            }, 50);
        }
    });
}

function initAddToCartFeedback() {
    const button = document.querySelector('button.bg-primary');
    if (!button) return;

    button.addEventListener('click', function() {
        const originalHTML = this.innerHTML;
        this.innerHTML = '<span class="material-symbols-outlined">check_circle</span> ¡Agregado!';
        this.classList.add('opacity-80');

        setTimeout(() => {
            this.innerHTML = originalHTML;
            this.classList.remove('opacity-80');
        }, 2000);
    });
}

function initSimpleAddToCartLogs() {
    document.querySelectorAll('.material-symbols-outlined[data-icon="add"]').forEach(btn => {
        const wrapper = btn.parentElement;
        if (!wrapper) return;
        wrapper.addEventListener('click', () => {
            console.log('Producto agregado al carrito');
        });
    });
}

function initOrderConfirmation() {
    if (!document.querySelector('body')) return;
    window.addEventListener('DOMContentLoaded', () => {
        if (document.querySelector('.success-checkmark-animation')) {
            console.log('Order success screen initialized with celebration animation');
        }
    });
}

window.addEventListener('DOMContentLoaded', () => {
    initImageGallery();
    initFavoriteToggle();
    initGroupHoverEffects();
    initButtonPressEffects();
    initProfileMicroInteractions();
    initProgressAnimation();
    initAddToCartFeedback();
    initSimpleAddToCartLogs();
    initOrderConfirmation();
});
