import {
    handleError,
    serverURL,
    resolveElementID,
    setEventListenerToObjects,
    reloadWindow,
} from "../util/utils.js";

export function initializeMessagesManagementService() {
    setEventListenerToObjects('mark-read-button',
            e => markMessage(resolveElementID(e.target.id), 'READ').then(reloadWindow));
    setEventListenerToObjects('mark-unread-button',
            e => markMessage(resolveElementID(e.target.id), 'UNREAD').then(reloadWindow));
    setEventListenerToObjects('delete-button',
            e => deleteMessage(resolveElementID(e.target.id)).then(reloadWindow));
}

async function markMessage(id, status) {
    if (document.getElementById(`status-${id}`).innerHTML === status) {
        alert(`You have already marked this message as ${status}`);
    } else {
        const url = `${serverURL}/messages_management/${id}?status=${status}`;
        const response = await fetch(url, {method: 'PUT'});
        if (!await handleError(response)) {
            alert(`Message was marked as "${status}" successfully`);
        }
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
