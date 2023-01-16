import {handleError, serverURL, resolveElementID, setEventListenerToObjects} from "../util/utils.js";

export function initializeRequestManagementService() {
    setEventListenerToObjects('accept-button',
        e => approveRequest(resolveElementID(e.target.id)).then(() => window.location.reload()));
    setEventListenerToObjects('reject-button',
        e => rejectRequest(resolveElementID(e.target.id)).then(() => window.location.reload()));
}

async function approveRequest(id) {
    const url = `${serverURL}/request_management/${id}?action=APPROVE`;
    const response = await fetch(url, {method: 'PATCH'});
    if (!await handleError(response)) {
        alert(`Request was approved successfully`);
    }
}

async function rejectRequest(id) {
    const url = `${serverURL}/request_management/${id}?action=REJECT`;
    const response = await fetch(url, {method: 'PATCH'});
    if (!await handleError(response)) {
        alert('Request was rejected successfully');
    }
}