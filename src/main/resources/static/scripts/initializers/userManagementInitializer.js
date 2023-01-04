import {
    manageModal,
    setDeleteEventListeners,
    setUpdateEventListeners,
    updateUserRole
} from "../management/userManagementService.js";

async function setSaveButtonListener() {
    const saveButton = document.getElementById('save_button');
    const saveButtonListener = () => {
        updateUserRole();
    }

    saveButton.addEventListener("click", saveButtonListener);
}

manageModal();
setUpdateEventListeners();
setDeleteEventListeners();
setSaveButtonListener().then(() => {
});