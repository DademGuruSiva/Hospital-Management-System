/*document.addEventListener("DOMContentLoaded", function () {

    const menuItems = document.querySelectorAll(".menu-item");

    menuItems.forEach(item => {
        item.addEventListener("click", function () {

            const submenu = this.nextElementSibling;

            // Close all other submenus
            document.querySelectorAll(".submenu").forEach(menu => {
                if (menu !== submenu) {
                    menu.classList.remove("show");
                }
            });

            // Toggle current submenu
            submenu.classList.toggle("show");
        });
    });

});*/


/*document.addEventListener("DOMContentLoaded", function () {

    const menuItems = document.querySelectorAll(".menu-item");

    menuItems.forEach(item => {
        item.addEventListener("click", function () {

            const submenu = this.nextElementSibling;

            // Close others
            document.querySelectorAll(".submenu").forEach(menu => {
                if (menu !== submenu) {
                    menu.classList.remove("show");
                }
            });

            // Toggle current
            submenu.classList.toggle("show");
        });
    });

});*/

document.addEventListener("DOMContentLoaded", function () {

    // Select all main menu items
    const menuItems = document.querySelectorAll(".menu-item");

    // Loop through each menu item
    menuItems.forEach(function(item) {

        item.addEventListener("click", function() {

            // Get next submenu
            const submenu = this.nextElementSibling;

            // Safety check (if submenu exists)
            if (submenu && submenu.classList.contains("submenu")) {

                // Toggle show class (open/close only this menu)
                submenu.classList.toggle("show");
            }
        });

    });

});