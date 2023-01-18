import {serverURL} from "../util/utils.js";

function animateLikeButton() {
    const likeButtons = document.getElementsByClassName("like-button");

    const listener = e => {
        const button = document.getElementById(e.target.id);
        console.log("Previous src: " + button.src);
        if (button.src === `${serverURL}/svg/Filled_Heart.svg`) {
            console.log("Changing button to unliked: " + e.target.id);
            button.src = "/svg/heart.svg";
        } else {
            console.log("Changing button to liked: " + e.target.id);
            button.src = "/svg/Filled_Heart.svg";
        }
    };

    console.log("Setting event Listeners: " + likeButtons.length);
    for (let button of likeButtons) {
        button.addEventListener('click', listener);
    }
}

animateLikeButton();