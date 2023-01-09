export async function handleError(response) {
    if (response.status !== 200) {
        const text = await response.json();
        alert('Failure: ' + text.message);
        return true;
    }
    return false;
}

export function resolveElementID(fullID) {
    return fullID.split("-")[1];
}

export const serverURL = 'http://localhost:8080';