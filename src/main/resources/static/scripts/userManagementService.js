function manageModal() {
    const modal = document.getElementById('user_management_update_form');

    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    };
}

function retrieveUserDetailsFromForm() {
    const id = document.getElementById("user_management_update_id_input").value;
    const name = document.getElementById("user_management_update_name_input").value;
    const login = document.getElementById("user_management_update_login_input").value;
    const userRole = document.getElementById("user_management_update_role_input").value;
    return createUser(id, name, login, userRole);
}

function setUserDetailsToForm(id, name, login, userRole) {
    document.getElementById("user_management_update_id_input").value = id;
    document.getElementById("user_management_update_name_input").value = name;
    document.getElementById("user_management_update_login_input").value = login;
    document.getElementById("user_management_update_role_input").value = userRole;
}

function createUser(id, name, login, userRole) {
    return {
        userid: id,
        login: login,
        name: name,
        userRole: userRole
    };
}

function getUserDetailsFromRow(userId) {
    const userName = document.getElementById(`name-${userId}`).innerHTML;
    const userLogin = document.getElementById(`login-${userId}`).innerHTML;
    const userRole = document.getElementById(`userRole-${userId}`).innerHTML;
    return createUser(userId, userName, userLogin, userRole);
}

function setUpdateEventListeners() {
    const buttons = document.getElementsByClassName("update-button");
    const buttonPressedListener = e => {
        const buttonId = e.target.id;
        const userId = buttonId.split("-")[1];
        const user = getUserDetailsFromRow(userId);
        setUserDetailsToForm(user.userid, user.name, user.login, user.userRole);
        document.getElementById('user_management_update_form').style.display = 'block'
    }

    for (let button of buttons) {
        button.addEventListener("click", buttonPressedListener);
    }
}

function setDeleteEventListeners() {
    const buttons = document.getElementsByClassName("delete-button");

    const buttonPressedListener = e => {
        const buttonId = e.target.id;
        const userId = buttonId.split("-")[1];
        const user = getUserDetailsFromRow(userId);
        deleteUser(user).then(r => window.location.reload());
    }

    for (let button of buttons) {
        button.addEventListener("click", buttonPressedListener);
    }
}

async function updateUserRole() {
    const user = retrieveUserDetailsFromForm();
    const url = `http://localhost:8080/user_management/${user.userid}?name=${user.name}&login=${user.login}&userRole=${user.userRole}`;

    const response = await fetch(url, {
        method: 'PATCH', body: JSON.stringify(user), headers: {
            'ContentType': 'application/json'
        }
    });
    const updatedUser = JSON.parse(await response.text());
    document.getElementById('user_management_update_form').style.display = 'none'
    alert(`User ${updatedUser.login} was updated successfully`);
    window.location.reload();
}

async function deleteUser(user) {
    const url = `http://localhost:8080/user_management/${user.userid}`;
    const response = await fetch(url, {method: 'DELETE'});
    const deletedUser = JSON.parse(await response.text())
    alert(`User ${deletedUser.login} was deleted successfully`);
}