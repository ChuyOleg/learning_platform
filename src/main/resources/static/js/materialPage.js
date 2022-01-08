"use strict";

const addMaterialButton = document.getElementById("addNewMaterialButton");
const divCreateMaterialList = document.querySelectorAll("#material-list-wrapper .create-material-div");
const materialListWrapper = document.getElementById("material-list-wrapper");

const createMaterialBlock = divCreateMaterialList[0];


const setEventListenerForDeletingMaterial = materialBlock => {
    materialBlock.childNodes.forEach(elem => {
        if (elem.tagName === "BUTTON") elem.addEventListener("click", () => materialBlock.remove());
    })
};

const setEmptyTextForTextAreaInNewMaterialBlock = materialBlock => {
    materialBlock.childNodes.forEach(elem => {
        if (elem.tagName === "TEXTAREA") elem.value = "";
    })
}

// create new Material block (by cloning existed <div>) and set EventListener for deleting this block
addMaterialButton.addEventListener("click", () => {
    const divCreateMaterialCopy = createMaterialBlock.cloneNode(true);
    setEventListenerForDeletingMaterial(divCreateMaterialCopy);
    setEmptyTextForTextAreaInNewMaterialBlock(divCreateMaterialCopy);
    materialListWrapper.appendChild(divCreateMaterialCopy);
});

// set EventListener for deleting every already existed block
divCreateMaterialList.forEach(block => {
    setEventListenerForDeletingMaterial(block);
});



