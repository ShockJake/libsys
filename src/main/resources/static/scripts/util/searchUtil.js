import {serverURL} from "./utils.js";

const input = document.getElementById("search_bar");

function search(e, text) {
    if (e.key === "Enter") {
        const url = `${serverURL}/search?prompt=${text}`;
        window.location.replace(url);
    }
}

input.addEventListener("keydown", (event) => search(event, input.value));