document.addEventListener("DOMContentLoaded", function() {
    const container = document.getElementById("toastContainer");
    if (!container) {
        console.error("Error: Toast container not found.");
        return;
    }

    function showToast(message, type = "success", duration = 3000) {
        const toast = document.createElement("div");
        toast.classList.add("toast", type === "success" ? "success" : "error");
        toast.innerText = message;
        container.appendChild(toast);

        setTimeout(() => toast.classList.add("show"), 100);
        setTimeout(() => hideToast(toast), duration);

        toast.addEventListener("click", () => hideToast(toast));
    }

    function hideToast(toast) {
        toast.classList.add("hide");
        setTimeout(() => toast.remove(), 500);
    }

    const errorElement = document.getElementById("toastMessage");
    const successElement = document.getElementById("toastSuccess");

    if (errorElement) {
        showToast(errorElement.getAttribute("data-message"), "error");
    }

    if (successElement) {
        showToast(successElement.getAttribute("data-message"), "success");
    }
});
