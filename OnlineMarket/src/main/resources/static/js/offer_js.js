document.addEventListener("DOMContentLoaded", function () {
    const errorElement = document.getElementById("toastMessage");
    const successElement = document.getElementById("toastSuccess");

    if (errorElement) {
        showToast(errorElement.getAttribute("data-message"), "error");
    }

    if (successElement) {
        showToast(successElement.getAttribute("data-message"), "success");
    }

    //on change productSelected
    document.getElementById("productId").addEventListener("change", function() {
        var productId = this.value;
        if(productId!=="-1"){
            loadDataProduct(productId);
        }else{
             document.getElementById("productInfo").style.display = "none";
             document.getElementById("productInfo").innerHTML = "";
             document.getElementById("priceInfo").style.display = "none";
             document.getElementById("priceInfo").innerHTML = "";
         }
    });

    //if there was an error and we recover an offer with a product already selected
    var productId = document.getElementById("productId").value;

    if(productId!=="-1"){
        loadDataProduct(productId);
    }else{
        document.getElementById("productInfo").style.display = "none";
        document.getElementById("productInfo").innerHTML = "";
        document.getElementById("priceInfo").style.display = "none";
        document.getElementById("priceInfo").innerHTML = "";
    }

    //if there was an error an we recover an offer with a selected dates:
    if(document.getElementById("offerStartDate").value!="" &&
               document.getElementById("offerEndDate").value!=""){

        calculateMaximumDiscount();

        if(!isStartDateLowerThanEnd()){
            showToast("The offer end date cannot be later than the offer start date", "error");
        }
    }
    var startDateInput = document.getElementById("offerStartDate");
    var endDateInput = document.getElementById("offerEndDate");

    //limit de ranges of days of the input date
    var today = new Date();
    var day = ("0" + today.getDate()).slice(-2);
    var month = ("0" + (today.getMonth() + 1)).slice(-2);
    var year = today.getFullYear();

    var todayDate = year + "-" + month + "-" + day;

    startDateInput.setAttribute("min", todayDate);
    endDateInput.setAttribute("min", todayDate);

    //to limit the days in case there was already a date included
    if (startDateInput.value) {
        endDateInput.min = startDateInput.value;
    }
    if (endDateInput.value) {
         startDateInput.max = endDateInput.value;
    }


    document.querySelectorAll('input[type="date"]').forEach(input => {
        input.addEventListener('click', function() {
            this.showPicker();
        });
    });

    document.getElementById("offerStartDate").addEventListener("change", function(event) {
        var startDateInput = document.getElementById("offerStartDate");
        var endDateInput = document.getElementById("offerEndDate");

        if (startDateInput.value) {
            endDateInput.min = startDateInput.value;
        }

        if (startDateInput.value !== "" && endDateInput.value !== "") {
            calculateMaximumDiscount();

            if (!isStartDateLowerThanEnd()) {
                showToast("The offer end date cannot be later than the offer start date", "error");
            }
        }

        checkDiscountValid()
    });

    document.getElementById("offerEndDate").addEventListener("change", function(event) {
        var startDateInput = document.getElementById("offerStartDate");
        var endDateInput = document.getElementById("offerEndDate");

        if (endDateInput.value) {
            startDateInput.max = endDateInput.value;
        }

        if (startDateInput.value !== "" && endDateInput.value !== "") {
            calculateMaximumDiscount();

            if (!isStartDateLowerThanEnd()) {
                showToast("The offer end date cannot be later than the offer start date", "error");
            }
        }

        checkDiscountValid()
    });

    //discount
    document.getElementById("discount").addEventListener("input", function(event) {
        checkDiscountValid()
    });
});

function validateForm() {
    //validation of product
    const productId = document.getElementById("productId").value;
    if (productId === "-1") {
        showToast("Please select a product.", "error");
        return false;
    }

    //validation of dates
    const startDate = document.getElementById("offerStartDate").value;
    const endDate = document.getElementById("offerEndDate").value;
    if (!startDate || !endDate) {
        showToast("Please fill in both start and end dates.", "error");
        return false;
    }

    // vvalidate that the start date is not later than the end date
    if (!isStartDateLowerThanEnd()) {
        showToast("The offer end date cannot be earlier than the offer start date.", "error");
        return false;
    }

    // validation of discount
    const discount = document.getElementById("discount").value;
    const discountAllowedElement = document.querySelector("#discountInfo .discount");
    if (discountAllowedElement) {
        const discountAllowed = parseInt(discountAllowedElement.textContent.replace('%', ''));
        if (discount > discountAllowed) {
            showToast("The discount exceeds the maximum allowed discount for the selected date range.", "error");
            return false;
        }
    } else {
        showToast("Please select a valid date range to calculate the maximum discount.", "error");
        return false;
    }

    return true;
}

// add the validation to the form submission event
document.getElementById("addOfferForm").addEventListener("submit", function(event) {
    if (!validateForm()) {
        event.preventDefault(); // prevent on fail
    }
});

function checkDiscountValid(){
    var discount = document.getElementById("discount").value
    var discountAllowed = document.querySelector("#discountInfo .discount")

    if (discountAllowed) {
        console.log("a")
        var discountValue = discountAllowed.textContent;
        var discountNumber = parseInt(discountValue.replace('%', ''))

        if(discount > discountNumber){
            document.getElementById("discount").style.boxSizing = "border-box"
            document.getElementById("discount").style.border = "3px solid red"

            document.getElementById("priceInfo").innerHTML = "";
            document.getElementById("priceInfo").style.display = "none";
        }else{
            document.getElementById("discount").style.removeProperty("box-sizing")
            document.getElementById("discount").style.border = "1px solid #ccc"

            if(document.getElementById("productId").value!=="-1" && discount!==""){
                var originalPrice = document.querySelector(".price").innerText
                var priceNumber = parseFloat(originalPrice.replace('$', ''))
                showLastPrice(priceNumber, discount)
            }
        }
    }else{
        console.log("b")
        document.getElementById("priceInfo").innerHTML = "";
        document.getElementById("priceInfo").style.display = "none";
    }
}

function showLastPrice(price, discount){
    console.log("showLastPrice price: "+price)
    console.log("showLastPrice discount: "+discount)
    var finalPrice = (price - (price * (discount / 100))).toFixed(2);

    console.log("showLastPrice finalPrice: "+finalPrice)
    document.getElementById("priceInfo").innerHTML = "The final price of this product will be <span class='price'>"+finalPrice+"$</span>";
    document.getElementById("priceInfo").style.display = "block";
}

function isStartDateLowerThanEnd() {
    var startDate = new Date(document.getElementById("offerStartDate").value);
    var endDate = new Date(document.getElementById("offerEndDate").value);

    startDate.setHours(0, 0, 0, 0);
    endDate.setHours(0, 0, 0, 0);

    console.log("startDate: " + startDate);
    console.log("endDate: " + endDate);

    return startDate <= endDate;
}


function calculateMaximumDiscount(){
    var days = calculateDays(document.getElementById("offerStartDate").value, document.getElementById("offerEndDate").value);
    var maxDiscount = getMaxDiscount(days);

    document.getElementById("discountInfo").innerHTML = "The maximum allowed discount for this date range is <span class='discount'>"+maxDiscount+"%</span>"
    document.getElementById("discountInfo").style.display = "block";
}

function loadDataProduct(productId) {
    fetch(`/api-rest/offer/json?productId=${productId}`)
        .then(response => response.json())
        .then(data => {
            if (data.length === 0) {
                showToast("Error while recovering data from product.", "error");
            } else {

               // for (let key in data) {
               //     if (data.hasOwnProperty(key)) {
               //          console.log(key + ": " + data[key]);
               //      }
               //  }

                var select = document.getElementById("productId");
                var productName = select.options[select.selectedIndex].text;

                var toAdd = "<b>Product</b>: "+productName+"<br>";

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
                    toAdd+= "- Price: <strong>"+data.offerPrice+"$</strong>, after applying <span class='discount'>"+
                    Math.round(discountPercentage)+"%</span> discount (original price was <span class='price'>"+data.price+"$</span>)"
                }

                document.getElementById("productInfo").innerHTML = toAdd;
                document.getElementById("productInfo").style.display = "block";

                checkDiscountValid()
            }
        })
        .catch(error => {
            document.getElementById("productInfo").style.display = "none";
            document.getElementById("priceInfo").style.display = "none";
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

    return Math.round(differenceDays)+1; //to count the same day as one day
}

function showToast(message, type = "success", duration = 6000) {
    const container = document.getElementById("toastContainer");

    const toast = document.createElement("div");
    toast.classList.add("toast", type === "success" ? "success" : "error");
    toast.innerText = message;
    container.appendChild(toast);

    setTimeout(() => toast.classList.add("show"), 100);
    setTimeout(() => hideToast(toast), duration);

    toast.addEventListener("click", () => hideToast(toast));
}

function hideToast(toast) {
    toast.classList.remove("show");
    setTimeout(() => toast.remove(), 300);
}
