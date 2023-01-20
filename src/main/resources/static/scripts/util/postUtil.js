import {catchRedirect, serverURL} from "./utils.js";

export function likePost(postID) {
    const url = `${serverURL}/posts/like/${postID}`;
    fetch(url, {method: 'POST'}).then(catchRedirect);
}

export function unlikePost(postID) {
    const url = `${serverURL}/posts/unlike/${postID}`;
    fetch(url, {method: 'POST'}).then(catchRedirect);
}
