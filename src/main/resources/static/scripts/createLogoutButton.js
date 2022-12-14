async function checkAuthentication() {
    let isAuthenticated = false;
    await fetch('http://localhost:8080/auth')
        .then(response => isAuthenticated = resolveAuthenticationStatus(response));
    console.log('Fetched authentication status - ' + isAuthenticated);
    return isAuthenticated;
}

function resolveAuthenticationStatus(response) {
    return response.headers.get("Content-Type") === 'application/json';
}

function createLogoutButton(isAuthenticated) {
    console.log(isAuthenticated)
    const logoutButton = document.getElementById("logout_button");
    if (isAuthenticated === true) {
        logoutButton.setAttribute("style", "display: inline;");
        console.log("Changed logout button attribute to inline display")
    } else {
        logoutButton.setAttribute("style", "display: none;");
        console.log("Changed logout button attribute to none display")
    }
}

function insertLogoutButton() {
    checkAuthentication()
        .then(result => createLogoutButton(result))
        .catch(err => console.log(err))
}