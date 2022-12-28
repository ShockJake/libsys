async function checkAuthentication() {
    let isAuthenticated = false;
    await fetch('http://localhost:8080/auth')
        .then(async response => isAuthenticated = await resolveAuthenticationStatus(response));
    return isAuthenticated;
}

async function resolveAuthenticationStatus(response) {
    if (response.headers.get("Content-Type") === 'application/json') {
        const body = await response.text();
        return body === 'true';
    }
    return false
}

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