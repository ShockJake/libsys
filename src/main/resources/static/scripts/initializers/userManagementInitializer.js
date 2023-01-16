import {
    manageModal,
    initializeUserManagementService,
    updateUserData
} from "../management/userManagementService.js";

async function setSaveButtonListener() {
    const saveButton = document.getElementById('save_button');
    saveButton.addEventListener("click", updateUserData);
}

manageModal();
initializeUserManagementService();
setSaveButtonListener().then(() => {
});