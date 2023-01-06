import {handleError, serverURL, resolveElementID} from "../util/utils.js";

export function setEventListeners() {
    const acceptButtons = document.getElementsByClassName('accept-button');
    const rejectButtons = document.getElementsByClassName('reject-button');

    const acceptButtonListener = e => {
        approveRequest(resolveElementID(e.target.id))
            .then(() => window.location.reload());
    }

    const rejectButtonListener = e => {
        rejectRequest(resolveElementID(e.target.id))
            .then(() => window.location.reload());
    }

    for (let button of acceptButtons) {

        button.addEventListener('click', acceptButtonListener);
    }
    for (let button of rejectButtons) {
        button.addEventListener('click', rejectButtonListener);
    }
}

async function approveRequest(id) {
    const url = `${serverURL}/request_manager/${id}?action=APPROVE`;
    const response = await fetch(url, {method: 'PATCH'});
    if (!await handleError(response)) {
        alert(`Request was approved successfully`);
    }
}

async function rejectRequest(id) {
    const url = `${serverURL}/request_manager/${id}?action=REJECT`;
    const response = await fetch(url, {method: 'PATCH'});
    if (!await handleError(response)) {
        alert('Request was rejected successfully');
    }
}