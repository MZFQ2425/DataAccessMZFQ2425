document.addEventListener("DOMContentLoaded", function () {
    const categorySelect = document.getElementById("categoryId");
    const productSelect = document.getElementById("productId");
    const formInputs = document.querySelectorAll("#addProductForm input, #addProductForm select, #addProductForm textarea, #addProductForm button");
    const errorElement = document.getElementById("toastMessage");
    const successElement = document.getElementById("toastSuccess");



    if (errorElement) {
        showToast(errorElement.getAttribute("data-message"), "error");
    }

    if (successElement) {
        showToast(successElement.getAttribute("data-message"), "success");
    }

    // Desactivar todos los inputs excepto el select de categorías al inicio
    formInputs.forEach(input => {
        if (input.id !== "categoryId") {
            input.disabled = true;
        }
    });

    // Si no hay categorías disponibles, desactivar también el select de categorías y mostrar un toast
    if (categorySelect.options.length <= 1) {
        categorySelect.disabled = true;
        showToast("No categories available for your products.", "error");
    }

    // Evento para habilitar los inputs cuando se seleccione una categoría
    categorySelect.addEventListener("change", function () {
        const isCategorySelected = categorySelect.value !== "";

        // Si la categoría seleccionada es la opción vacía, desactivar el selector de productos y limpiar el contenido
        if (!isCategorySelected) {
            productSelect.innerHTML = '<option value="">Select a product</option>'; // Limpiar los productos
            productSelect.disabled = true; // Deshabilitar el selector de productos
        } else {
            productSelect.innerHTML = '<option value="">Select a product</option>'; // Reset productos
            productSelect.disabled = false;// Habilitar el selector de productos
            console.log(categorySelect.value);
            loadProducts(categorySelect.value); // Cargar los productos de la categoría seleccionada
        }

        // Habilitar o deshabilitar los inputs basados en si hay una categoría seleccionada
        formInputs.forEach(input => {
            if (input.id !== "categoryId" && input.id !== "productId") {
                input.disabled = !isCategorySelected;
            }
        });
    });


});

// Función para cargar las categorías disponibles para el vendedor
function loadCategories() {
    const categorySelect = document.getElementById("categoryId");

    fetch('/api-rest/products/json')  // URL correcta para obtener las categorias
        .then(response => response.json())
        .then(data => {
            if (!Array.isArray(data) || data.length === 0) {
                showToast("This seller doesn't have any categories available to add products.", "error");
                categorySelect.disabled = true;
                toggleFormInputs(true);
            } else {
                // Agregar las categorías al select
                data.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.categoryId;
                    option.textContent = category.categoryName;
                    categorySelect.appendChild(option);
                });
                categorySelect.disabled = false;
            }
        })
        .catch(error => {
            showToast("Error loading categories", "error");
        });
}


// Función para cargar los productos de una categoría seleccionada
function loadProducts(categoryId) {
    const productSelect = document.getElementById("productId");

    fetch(`/api-rest/products/jsonProducts?categoryId=${categoryId}`)  // URL con el contexto correcto
        .then(response => response.json())
        .then(data => {
            if (!Array.isArray(data) || data.length === 0) {
                showToast("No products available in this category.", "error");
                productSelect.disabled = true;
                return;
            }

            data.forEach(product => {
                const option = document.createElement('option');
                option.value = product.productId;
                option.textContent = product.productName;
                productSelect.appendChild(option);
            });

            productSelect.disabled = false;
        })
        .catch(error => {
            showToast("Error loading products. Try again.", "error");
        });
}

// Función para habilitar o deshabilitar los inputs del formulario
function toggleFormInputs(disable = false) {
    const formInputs = document.querySelectorAll("#addProductForm input, #addProductForm select, #addProductForm textarea");

    formInputs.forEach(input => {
        input.disabled = disable;
    });
}

//Función para mostrar el valor del stock


// Función para mostrar toasts
function showToast(message, type = "success", duration = 3000) {
    const container = document.getElementById("toastContainer");

    const toast = document.createElement("div");
    toast.classList.add("toast", type === "success" ? "success" : "error");
    toast.innerText = message;
    container.appendChild(toast);

    setTimeout(() => toast.classList.add("show"), 100);
    setTimeout(() => hideToast(toast), duration);

    toast.addEventListener("click", () => hideToast(toast));
}

// Función para ocultar toasts
function hideToast(toast) {
    toast.classList.remove("show");
    setTimeout(() => toast.remove(), 300);
}
