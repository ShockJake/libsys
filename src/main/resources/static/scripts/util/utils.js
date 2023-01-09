// export const serverURL = 'http://localhost:8080';
export const serverURL = 'http://192.168.137.1:8080';

async function resolveAuthenticationStatus(response) {
    if (response.headers.get("Content-Type") === 'application/json') {
        const body = await response.text();
        return body === 'true';
    }
    return false
}

export async function handleError(response) {
    if (response.status !== 200) {
        const text = await response.json();
        alert('Failure: ' + text.message);
        return true;
    }
    return false;
}

export async function checkAuthentication() {
    let isAuthenticated = false;
    await fetch(`${serverURL}/auth`)
        .then(async response => isAuthenticated = await resolveAuthenticationStatus(response));
    return isAuthenticated;
}

export function resolveElementID(fullID) {
    return fullID.split("-")[1];
}