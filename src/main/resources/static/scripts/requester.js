import {handleError} from "./util/utils.js";

async function doRequest(requestType) {
    const url = `http://localhost:8080/request_management/create_request?type=${requestType}`;
    const response = await fetch(url, {method: 'POST'});
    if (!await handleError(response)) {
        alert('Writer role was successfully requested');
    }
}

async function requestWriterRole() {
    await doRequest('WRITER_ROLE')
}

export function setEventListeners() {
    const requestWriterRoleButton = document.getElementById('request_writer_role_button');
    const requestWriterRoleButtonListener = () => {
        requestWriterRole().then();
    }
    requestWriterRoleButton.addEventListener('click', requestWriterRoleButtonListener);
}