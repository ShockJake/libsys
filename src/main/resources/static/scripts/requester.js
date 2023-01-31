import {handleError, resolveCSRFToken, serverURL} from "./util/utils.js";

async function doRequest(requestType) {
    const url = `${serverURL}/request_management/create_request?type=${requestType}`;
    const response = await fetch(url, {
        method: 'POST', headers: {
            'X-CSRF-TOKEN': resolveCSRFToken().token
        }
    });
    if (!await handleError(response)) {
        alert('Writer role was successfully requested');
    }
}

async function requestWriterRole() {
    await doRequest('WRITER_ROLE')
}

export function initializeRequester() {
    const requestWriterRoleButton = document.getElementById('request_writer_role_button');
    const requestWriterRoleButtonListener = () => {
        requestWriterRole().then();
    }
    requestWriterRoleButton.addEventListener('click', requestWriterRoleButtonListener);
}