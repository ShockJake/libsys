export const serverURL = 'http://localhost:8080';

// export const serverURL = 'http://192.168.137.1:8080';

async function resolveAuthenticationStatus(response) {
    if (response.headers.get("Content-Type") === 'application/json') {
        const body = await response.text();
        return body === 'true';
    }
    return false
}

export function resolveCSRFToken() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    return {token: token, header: header};
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

export function setFileSizeChecker() {
    const uploadField = document.getElementById('post_photo_input');
    uploadField.onchange = function () {
        if (this.files[0].size > 6000000) {
            alert("File is too big!");
            this.value = "";
        }
    };
}

export function setEventListenerToObjects(objectName, eventListener) {
    const objects = document.getElementsByClassName(objectName);
    for (const object of objects) {
        object.addEventListener('click', eventListener);
    }
}

export function setEventListenerToSingleObject(objectId, eventListener) {
    const object = document.getElementById(objectId);
    object.addEventListener("click", eventListener);
}

export function catchRedirect(response) {
    if (response.redirected === true) {
        window.location.href = response.url;
    }
}

export const reloadWindow = () => window.location.reload();