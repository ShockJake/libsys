import {handleError, resolveElementID, serverURL, setEventListenerToObjects} from "../util/utils.js";

export function manageModal() {
    const modal = document.getElementById('user_management_update_form');

    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    };
}

function createUser(id, name, login, userRole, postsNumber) {
    return {
        userID: id,
        login: login,
        name: name,
        userRole: userRole,
        postsNumber: postsNumber
    };
}

function retrieveUserDetailsFromForm() {
    const id = document.getElementById("user_management_update_id_input").value;
    const name = document.getElementById("user_management_update_name_input").value;
    const login = document.getElementById("user_management_update_login_input").value;
    const userRole = document.getElementById("user_management_update_role_input").value;
    const postsNumber = document.getElementById("user_management_update_posts_input").value;
    return createUser(id, name, login, userRole, postsNumber);
}

function setUserDetailsToForm(id, name, login, userRole, postsNumber) {
    document.getElementById("user_management_update_id_input").value = id;
    document.getElementById("user_management_update_name_input").value = name;
    document.getElementById("user_management_update_login_input").value = login;
    document.getElementById("user_management_update_role_input").value = userRole;
    document.getElementById("user_management_update_posts_input").value = postsNumber;
}

function getUserDetailsFromRow(userId) {
    const userName = document.getElementById(`name-${userId}`).innerHTML;
    const userLogin = document.getElementById(`login-${userId}`).innerHTML;
    const userRole = document.getElementById(`userRole-${userId}`).innerHTML;
    const postsNumber = document.getElementById(`postsNumber-${userId}`).innerHTML;
    return createUser(userId, userName, userLogin, userRole, postsNumber);
}

export function initializeUserManagementService() {
    setEventListenerToObjects('update-button', e => {
        const user = getUserDetailsFromRow(resolveElementID(e.target.id));
        setUserDetailsToForm(user.userID, user.name, user.login, user.userRole, user.postsNumber);
        document.getElementById('user_management_update_form').style.display = 'block'
    });
    setEventListenerToObjects('delete-button', e => {
        deleteUser(getUserDetailsFromRow(resolveElementID(e.target.id))).then(() => window.location.reload());
    });
    setEventListenerToObjects('delete-buttons', e => {
        deleteUser(getUserDetailsFromRow(resolveElementID(e.target.id))).then(() => window.location.reload());
    });
}

export async function updateUserData() {
    const user = retrieveUserDetailsFromForm();
    const url = `${serverURL}/user_management/${user.userID}`;

    const response = await fetch(url, {
        method: 'PATCH', body: JSON.stringify(user), headers: {
            'Content-Type': 'application/json'
        }
    });
    if (!await handleError(response)) {
        const updatedUser = JSON.parse(await response.text());
        document.getElementById('user_management_update_form').style.display = 'none'
        alert(`User ${updatedUser.login} was updated successfully`);
        window.location.reload();
    }
}

async function deleteUser(user) {
    const url = `${serverURL}/user_management/${user.userID}`;
    if (confirm(`Are you sure you want to delete user '${user.login}'`)) {
        const response = await fetch(url, {method: 'DELETE'});
        if (!await handleError(response)) {
            const deletedUser = JSON.parse(await response.text())
            alert(`User ${deletedUser.login} was deleted successfully`);
        }
    }
}

export async function retrieveUserFromServer(id) {
    const url = `${serverURL}/user_management/${id}`;
    const response = await fetch(url, {method: 'GET'});
    if (!await handleError(response)) {
        return JSON.parse(await response.text());
    }
}
