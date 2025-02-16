document.addEventListener("DOMContentLoaded", function () {
    const errorElement = document.getElementById("toastMessage");
    const successElement = document.getElementById("toastSuccess");

    if (errorElement) {
        showToast(errorElement.getAttribute("data-message"), "error");
    }

    if (successElement) {
        showToast(successElement.getAttribute("data-message"), "success");
    }

    if(document.getElementsByClassName("product-page").length > 0){
        const categorySelect = document.getElementById("categoryId");
        const productSelect = document.getElementById("productId");
        const formInputs = document.querySelectorAll("#addProductForm input, #addProductForm select, #addProductForm textarea, #addProductForm button");

        formInputs.forEach(input => {
            if (input.id !== "categoryId") {
                input.disabled = true;
            }
        });

        if (categorySelect.options.length <= 1) {
            categorySelect.disabled = true;
        }

        categorySelect.addEventListener("change", function () {
            const isCategorySelected = categorySelect.value !== "";

            if (!isCategorySelected) {
                productSelect.innerHTML = '<option value="">Select a product</option>';
                productSelect.disabled = true;
            } else {
                productSelect.innerHTML = '<option value="">Select a product</option>';
                productSelect.disabled = false;
                loadProducts(categorySelect.value);
            }

            formInputs.forEach(input => {
                if (input.id !== "categoryId" && input.id !== "productId") {
                    input.disabled = !isCategorySelected;
                }
            });
        });
    }else if(document.getElementsByClassName("offer-page").length > 0){
        //OFFER.HTML
        document.getElementById("productId").addEventListener("change", function() {
            var productId = this.value;
            if(productId!="-1"){
                loadDataProduct(productId);
            }else{
                document.getElementById("productInfo").style.display = "none";
                document.getElementById("productInfo").innerHTML = "";
            }
        });
        var productId = document.getElementById("productId").value;
        if(productId!="-1"){
            loadDataProduct(productId);
        }else{
            document.getElementById("productInfo").style.display = "none";
            document.getElementById("productInfo").innerHTML = "";
        }
    }
    document.querySelectorAll('input[type="date"]').forEach(input => {
        input.addEventListener('click', function() {
            this.showPicker();
        });
    });
});

function loadDataProduct(productId) {
    fetch(`/api-rest/offer/json?productId=${productId}`)
        .then(response => response.json())
        .then(data => {
            if (data.length === 0) {
                showToast("Error while recovering data from product.", "error");
            } else {

                for (let key in data) {
                    if (data.hasOwnProperty(key)) {
                        console.log(key + ": " + data[key]);  // Muestra la clave y el valor
                    }
                }

                var select = document.getElementById("productId");
                var productName = select.options[select.selectedIndex].text;

                var toAdd = "<strong>Product</strong>: "+productName+"<br>";

                if (data.offerStartDate === null) {
                    toAdd+= "- Currently has no active offers<br>"
                } else {
                    var days = calculateDays(data.offerStartDate, data.offerEndDate);
                    var formattedStartDate = new Date(data.offerStartDate).toLocaleDateString();
                    var formattedEndDate = new Date(data.offerEndDate).toLocaleDateString();

                    toAdd+="- Has an active offer from <span class='offer-dates'>"+formattedStartDate+
                    "</span> to <span class='offer-dates'>"+formattedEndDate+"</span> ("+days+" days) allowing <span class='discount'>"+getMaxDiscount(days)+"%</span> maximum discount<br>"
                }

                if(data.offerPrice === null){
                    toAdd+= "- Price: <span class='price'>"+data.price+"$</span>, with no current discounts<br>"
                }else{
                    var discountPercentage = ((data.price - data.offerPrice) / data.price) * 100
                    toAdd+= "- Price: <span class='price'>"+data.offerPrice+"$</span>, after applying <span class='discount'>"+
                    Math.round(discountPercentage)+"%</span> discount (original price was <span class='price'>"+data.price+"$</span>)"
                }

                document.getElementById("productInfo").innerHTML = toAdd;
                document.getElementById("productInfo").style.display = "block";
            }
        })
        .catch(error => {
            document.getElementById("productInfo").style.display = "none";
            showToast("Error loading data for product", "error");
        });
}

function getMaxDiscount(daysBetween){
    if (daysBetween >= 30) {
        return 10;
    } else if (daysBetween >= 15) {
        return 15;
    } else if (daysBetween >= 7) {
        return 20;
    } else if (daysBetween >= 3) {
        return 30;
    } else if (daysBetween >= 1) {
        return 50;
    } else {
        return 0;
    }
}

function calculateDays(dateStart, dateEnd) {
    var date1 = new Date(dateStart);
    var date2 = new Date(dateEnd);

    var differenceM = date2.getTime() - date1.getTime();
    var differenceDays = differenceM / (1000 * 60 * 60 * 24);

    return Math.round(differenceDays);
}

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

    fetch(`/api-rest/products/jsonProducts?categoryId=${categoryId}`)
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
