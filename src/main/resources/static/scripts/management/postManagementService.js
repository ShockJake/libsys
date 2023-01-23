import {
    handleError,
    resolveElementID,
    serverURL,
    setEventListenerToObjects,
    setEventListenerToSingleObject,
    resolveCSRFToken
} from "../util/utils.js";
import {retrieveUserFromServer} from "./userManagementService.js";

async function retrievePostFromServer(id) {
    const url = `${serverURL}/post_management/${id}`;
    const response = await fetch(url, {method: 'GET'});
    if (!await handleError(response)) {
        const post = JSON.parse(await response.text());
        const user = await retrieveUserFromServer(post.writerID);
        return createPost(post.postID, post.postHeader, post.postText, user.login, post.writerID, post.postPhotoPath);
    }
}

function createPost(id, postHeader, postText, writerLogin, writerID, photoPath) {
    return {
        postID: id,
        postHeader: postHeader,
        postText: postText,
        writerLogin: writerLogin,
        writerID: writerID,
        postPhotoPath: photoPath
    };
}

function setPostDataToForm(post) {
    document.getElementById('post_management_update_id_input').value = post.postID;
    document.getElementById('post_management_update_header_input').value = post.postHeader;
    document.getElementById('post_management_update_text_input').value = post.postText;
    document.getElementById('post_management_update_writer_input').value = post.writerLogin;
    document.getElementById('post_management_update_writerID_input').value = post.writerID;
    document.getElementById('post_management_update_photo_input').value = post.postPhotoPath;
}

function retrieveDataFromForm() {
    const id = document.getElementById('post_management_update_id_input').value;
    const header = document.getElementById('post_management_update_header_input').value;
    const text = document.getElementById('post_management_update_text_input').value;
    const writerLogin = document.getElementById('post_management_update_writer_input').value;
    const writerID = document.getElementById('post_management_update_writerID_input').value;
    const postPhotoPath = document.getElementById('post_management_update_photo_input').value;

    return createPost(id, header, text, writerLogin, writerID, postPhotoPath);
}

export function manageModal() {
    const modal = document.getElementById('post_management_update_form');

    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    };
}

export function initializePostManagement() {
    setEventListenerToObjects('update-button', e => {
        retrievePostFromServer(resolveElementID(e.target.id)).then(r => setPostDataToForm(r));
        document.getElementById('post_management_update_form').style.display = 'block';
    });
    setEventListenerToObjects('delete-button',
        e => deletePost(resolveElementID(e.target.id)).then(() => window.location.reload()));
    setEventListenerToSingleObject('save_button',
        () => updatePost().then(() => window.location.reload()));
}

async function deletePost(id) {
    const url = `${serverURL}/post_management/${id}`;
    if (confirm('Are you sure you want to delete this post?')) {
        const response = await fetch(url, {
            method: 'DELETE', headers: {
                'X-CSRF-TOKEN': resolveCSRFToken().token
            }
        });
        if (!await handleError(response)) {
            const deletedPost = JSON.parse(await response.text());
            alert(`Post with header "${deletedPost.postHeader}" was successfully deleted`);
        }
    }
}

async function updatePost() {
    const postToUpdate = retrieveDataFromForm();
    const url = `${serverURL}/post_management/${postToUpdate.postID}`;
    const response = await fetch(url, {
        method: 'PATCH', body: JSON.stringify(postToUpdate), headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': resolveCSRFToken().token
        }
    });
    if (!await handleError(response)) {
        const updatedPost = JSON.parse(await response.text());
        alert(`Post "${updatedPost.postHeader}" was updated successfully`);
    }
}