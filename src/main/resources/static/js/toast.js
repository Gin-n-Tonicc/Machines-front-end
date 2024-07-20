document.addEventListener('DOMContentLoaded', (event) => {
        // Check if there's an error message
        const errorElement = document.getElementById('error-message-hidden');
        if (errorElement && errorElement.textContent.trim().length > 0) {
            showToast(errorElement.textContent);
        }
    });

    function showToast(message) {
        const toast = document.getElementById("toast");
        toast.textContent = message;
        toast.className = "toast show";
        setTimeout(() => {
            toast.className = toast.className.replace("show", "");
        }, 6000); // Total display time adjusted to match the animation duration
    }