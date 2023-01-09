import {checkAuthentication} from "../util/utils.js";

function createLogoutButton(isAuthenticated) {
    const logoutButton = document.getElementById("logout_button");
    if (isAuthenticated === true) {
        logoutButton.setAttribute("style", "display: inline;");
    } else {
        logoutButton.setAttribute("style", "display: none;");
    }
}

export function insertLogoutButton() {
    checkAuthentication()
        .then(result => createLogoutButton(result))
        .catch(err => console.log(err))
}