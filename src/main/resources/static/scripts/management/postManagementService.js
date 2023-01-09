import {handleError, resolveElementID, serverURL} from "../util/utils.js";
import {retrieveUserFromServer} from "./userManagementService.js";

async function retrievePostFromServer(id) {
    const url = `${serverURL}/post_management/${id}`;
    const response = await fetch(url, {method: 'GET'});
    if (!await handleError(response)) {
        const post = JSON.parse(await response.text());
        const user = await retrieveUserFromServer(post.writerID);
        return createPost(post.postID, post.postHeader, post.postText, user.login, user.id);
    }
}

function createPost(id, postHeader, postText, writerLogin, writerID) {
    return {
        postID: id,
        postHeader: postHeader,
        postText: postText,
        writerLogin: writerLogin,
        writerID: writerID
    };
}

function setPostDataToForm(post) {
    document.getElementById('post_management_update_id_input').value = post.postID;
    document.getElementById('post_management_update_header_input').value = post.postHeader;
    document.getElementById('post_management_update_text_input').value = post.postText;
    document.getElementById('post_management_update_writer_input').value = post.writerLogin;
    document.getElementById('post_management_update_writerID_input').value = post.writerID;
}

function retrieveDataFromForm() {
    const id = document.getElementById('post_management_update_id_input').value;
    const header = document.getElementById('post_management_update_header_input').value;
    const text = document.getElementById('post_management_update_text_input').value;
    const writerLogin = document.getElementById('post_management_update_writer_input').value;
    const writerID = document.getElementById('post_management_update_writerID_input').value;

    return createPost(id, header, text, writerLogin, writerID);
}

export function manageModal() {
    const modal = document.getElementById('post_management_update_form');

    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    };
}

export function setUpdateEventListeners() {
    const buttons = document.getElementsByClassName('update-button');
    const updateButtonListener = e => {
        retrievePostFromServer(resolveElementID(e.target.id)).then(r => setPostDataToForm(r));
        document.getElementById('post_management_update_form').style.display = 'block';
    }

    for (let button of buttons) {
        button.addEventListener('click', updateButtonListener);
    }
}

export function setDeleteEventListener() {
    const buttons = document.getElementsByClassName('delete-button');
    const deleteButtonListener = e => {
        deletePost(resolveElementID(e.target.id)).then();
        window.location.reload();
    }

    for (let button of buttons) {
        button.addEventListener('click', deleteButtonListener);
    }
}

export function setSaveEventListener() {
    const button = document.getElementById('save_button');
    button.addEventListener('click', updatePost);
}

async function deletePost(id) {
    const url = `${serverURL}/post_management/${id}`;
    const response = await fetch(url, {method: 'DELETE'});
    if (!await handleError(response)) {
        const deletedPost = JSON.parse(await response.text());
        alert(`Post with header "${deletedPost.postHeader}" was successfully deleted`);
    }
}

async function updatePost() {
    const postToUpdate = retrieveDataFromForm();
    const url = `${serverURL}/post_management/${postToUpdate.postID}`;
    const response = await fetch(url, {method: 'PATCH', body: JSON.stringify(postToUpdate)});
    if (!await handleError(response)) {
        const updatedPost = JSON.parse(await response.text());
        alert(`Post "${updatedPost.postHeader}" was updated successfully`);
    }
}