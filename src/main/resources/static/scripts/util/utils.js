export async function handleError(response) {
    if (response.status !== 200) {
        const text = await response.text();
        alert('Failure: ' + text);
        return true;
    }
    return false;
}

export function resolveElementID(fullID) {
    return fullID.split("-")[1];
}

export const serverURL = 'http://localhost:8080';