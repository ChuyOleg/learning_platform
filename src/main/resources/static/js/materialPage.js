"use strict";

const addMaterialButton = document.getElementById("addNewMaterialButton");
const divCreateMaterialList = document.querySelectorAll("#material-list-wrapper .create-material-div");
const materialListWrapper = document.getElementById("material-list-wrapper");

const createMaterialBlock = divCreateMaterialList[0];

const materialIndexArray = [];

// create TextArea name based on active index ******** //
const createTextAreaName = (textarea, index) => {
    const firstPart = textarea.name.slice(0, parseInt(textarea.name.indexOf("[")) + 1);
    const secondPart = textarea.name.slice(parseInt(textarea.name.indexOf("]")));
    return firstPart + index + secondPart;
}

// create TextArea id based on active index ******** //
const createTextAreaId = (textarea, index) => {
    const firstPart = textarea.id.slice(0, parseInt(textarea.id.search(/\d/)));
    const secondPart = textarea.id.slice(parseInt(textarea.id.indexOf(".")));
    return firstPart + index + secondPart;
}

const changeConfigForEveryMaterialAfterDeleting = () => {
    const blocks = document.querySelectorAll("#material-list-wrapper .create-material-div");
    for (let index = 0; index < blocks.length; index++) {
        const block = blocks[index];
        block.id = block.id.slice(0, block.id.search(/\d/)) + index;
        block.childNodes.forEach(elem => {
            if (elem.tagName === "TEXTAREA") {
                elem.name = createTextAreaName(elem, index);
                elem.id = createTextAreaId(elem, index);
            }
        })
    }
}

const setEventListenerForDeletingMaterial = materialBlock => {
    materialBlock.childNodes.forEach(elem => {
        if (elem.tagName === "BUTTON") elem.addEventListener("click", () => {
            const index = parseInt(materialBlock.id.slice(parseInt(materialBlock.id.search(/\d/))));
            materialIndexArray.splice(index, 1);
            materialBlock.remove();
            changeConfigForEveryMaterialAfterDeleting();
        })
    })
};

// set value = "", name, id
const configNewMaterialBlock = materialBlock => {
    materialBlock.id = materialBlock.id.slice(0, parseInt(materialBlock.id.search(/\d/))) + materialIndexArray.length;
    materialBlock.childNodes.forEach(elem => {
        if (elem.tagName === "TEXTAREA") {
            elem.name = createTextAreaName(elem, materialIndexArray.length);
            elem.id = createTextAreaId(elem, materialIndexArray.length);
            materialIndexArray.push(elem.id);
            elem.value = "";
        }
    })
}

// create new Material block (by cloning existed <div>) and set EventListener for deleting this block
addMaterialButton.addEventListener("click", () => {
    const divCreateMaterialCopy = createMaterialBlock.cloneNode(true);
    setEventListenerForDeletingMaterial(divCreateMaterialCopy);
    configNewMaterialBlock(divCreateMaterialCopy);
    materialListWrapper.appendChild(divCreateMaterialCopy);
});

const addIdOfAlreadyExistedBlockIntoMaterialIndexArray = block => {
    block.childNodes.forEach(elem => {
        if (elem.tagName === "TEXTAREA") {
            materialIndexArray.push(elem.id);
        }
    })
};

// set EventListener for deleting every already existed block and
divCreateMaterialList.forEach(block => {
    addIdOfAlreadyExistedBlockIntoMaterialIndexArray(block);
    setEventListenerForDeletingMaterial(block);
});
