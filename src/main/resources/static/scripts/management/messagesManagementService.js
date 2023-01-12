import {handleError, serverURL, resolveElementID} from "../util/utils.js";

export function setEventListeners() {
    const readButtons = document.getElementsByClassName("mark-read-button");
    const unreadButtons = document.getElementsByClassName("mark-unread-button");
    const deleteButtons = document.getElementsByClassName("delete-button");

    const readButtonsListener = e => {
        markMessage(resolveElementID(e.target.id), 'READ').then(
            () => window.location.reload());
    }
    const unreadButtonsListener = e => {
        markMessage(resolveElementID(e.target.id), 'UNREAD')
            .then(() => window.location.reload());
    }
    const deleteButtonsListener = e => {
        deleteMessage(resolveElementID(e.target.id))
            .then(() => window.location.reload());
    }

    for (let button of readButtons) {
        button.addEventListener("click", readButtonsListener);
    }
    for (let button of unreadButtons) {
        button.addEventListener("click", unreadButtonsListener);
    }
    for (let button of deleteButtons) {
        button.addEventListener("click", deleteButtonsListener);
    }
}

async function markMessage(id, status) {
    const url = `${serverURL}/messages_management/${id}?status=${status}`;
    const response = await fetch(url, {method: 'PUT'});
    if (!await handleError(response)) {
        alert(`Message was marked as "${status}" successfully`);
    }
}

async function deleteMessage(id) {
    const url = `${serverURL}/messages_management/${id}`;
    if (confirm('Are you sure you want to delete this message?')) {
        const response = await fetch(url, {method: 'DELETE'});
        if (!await handleError(response)) {
            alert(`Message was deleted successfully`);
        }
    }
}

