"use strict";

const addTaskButton = document.getElementById("addNewQuestionButton");
const divCreateTaskList = document.querySelectorAll("#question-list-wrapper .create-question-div");
const questionListWrapper = document.getElementById("question-list-wrapper");

const createQuestionBlock = divCreateTaskList[0];

const questionIndexArray = [];


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

const changeConfigForEveryQuestionAfterDeleting = () => {
    const blocks = document.querySelectorAll("#question-list-wrapper .create-question-div");
    for (let index = 0; index < blocks.length; index++) {
        const block = blocks[index];
        block.id = block.id.slice(0, block.id.search(/\d/)) + index;
        block.childNodes.forEach(elem => {
            if (elem.tagName === "TEXTAREA") {
                elem.name = createTextAreaName(elem, index);
                elem.id = createTextAreaId(elem, index);
            } else if (elem.tagName === "DIV" && elem.className.includes("form-check-inline")) {
                elem.childNodes.forEach(child => {
                    if (child.tagName === "INPUT") {
                        child.name = createTextAreaName(child, index);
                        child.id = createTextAreaId(child, index);
                    }
                });
            }
        })
    }
}

const setEventListenerForDeletingQuestion = questionBlock => {
    questionBlock.childNodes.forEach(elem => {
        if (elem.tagName === "BUTTON") elem.addEventListener("click", () => {
            const index = parseInt(questionBlock.id.slice(parseInt(questionBlock.id.search(/\d/))));
            questionIndexArray.splice(index, 1);
            questionBlock.remove();
            changeConfigForEveryQuestionAfterDeleting();
        })
    })
};

// set value = "", name, id
const configNewMaterialBlock = questionBlock => {
    questionBlock.id = questionBlock.id.slice(0, parseInt(questionBlock.id.search(/\d/))) + questionIndexArray.length;

    questionBlock.childNodes.forEach(elem => {
        if (elem.tagName === "TEXTAREA") {
            elem.name = createTextAreaName(elem, questionIndexArray.length);
            elem.id = createTextAreaId(elem, questionIndexArray.length);
            elem.value = "";
        } else if (elem.tagName === "DIV" && elem.className.includes("form-check-inline")) {
            elem.childNodes.forEach(child => {
                if (child.tagName === "INPUT") {
                    child.name = createTextAreaName(child, questionIndexArray.length);
                    child.id = createTextAreaId(child, questionIndexArray.length);
                    child.checked = false;
                }
            });

        }
    })

    questionIndexArray.push(questionBlock.id);
}

// create new Material block (by cloning existed <div>) and set EventListener for deleting this block
addTaskButton.addEventListener("click", () => {
    const divCreateQuestionCopy = createQuestionBlock.cloneNode(true);
    setEventListenerForDeletingQuestion(divCreateQuestionCopy);
    configNewMaterialBlock(divCreateQuestionCopy);
    questionListWrapper.appendChild(divCreateQuestionCopy);
});

const addIdOfAlreadyExistedBlockIntoQuestionIndexArray = block => {
    block.childNodes.forEach(elem => {
        if (elem.tagName === "TEXTAREA" && elem.id.includes("answer3")) {
            questionIndexArray.push(block.id);
        }
    })
};

// set EventListener for deleting and add id of into array every already existed block
divCreateTaskList.forEach(block => {
    addIdOfAlreadyExistedBlockIntoQuestionIndexArray(block);
    setEventListenerForDeletingQuestion(block);
});
