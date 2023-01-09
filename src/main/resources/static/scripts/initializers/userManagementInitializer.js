import {
    manageModal,
    setDeleteEventListeners,
    setUpdateEventListeners,
    updateUserData
} from "../management/userManagementService.js";

async function setSaveButtonListener() {
    const saveButton = document.getElementById('save_button');
    saveButton.addEventListener("click", updateUserData);
}

manageModal();
setUpdateEventListeners();
setDeleteEventListeners();
setSaveButtonListener().then(() => {
});