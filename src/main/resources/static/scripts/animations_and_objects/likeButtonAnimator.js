import {serverURL, resolveElementID, setEventListenerToObjects} from "../util/utils.js";
import {likePost, unlikePost} from "../util/postUtil.js";

function animateLikeButton() {
    setEventListenerToObjects("like-button", e => {
            const button = document.getElementById(e.target.id);
            if (button.src === `${serverURL}/svg/Filled_Heart.svg`) {
                unlikePost(resolveElementID(e.target.id));
                button.src = "/svg/heart.svg";
            } else {
                likePost(resolveElementID(e.target.id));
                button.src = "/svg/Filled_Heart.svg";
            }
        }
    );
}

animateLikeButton();