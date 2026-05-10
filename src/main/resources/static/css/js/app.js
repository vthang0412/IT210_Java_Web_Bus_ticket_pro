function toggleMenu(){

    const menu = document.getElementById("dropdownMenu");

    if(menu){
        menu.classList.toggle("show-menu");
    }
}

window.onclick = function(e){

    if(!e.target.closest(".user-dropdown")){

        let menu =
            document.getElementById("dropdownMenu");

        if(menu && menu.classList.contains("show-menu")){

            menu.classList.remove("show-menu");
        }
    }
}
