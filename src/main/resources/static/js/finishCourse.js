"use strict";

const finishCourseButton = document.getElementById("finishCourseButton");
const answersForm = document.getElementById("answersForm");
const radioInputList = document.getElementsByClassName("form-check-input");
const errorMessage = document.getElementById("errorMessage");

if (finishCourseButton != null) {
    finishCourseButton.addEventListener("click", () => {
        let checkedCounter = 0;
        for (let index = 0; index < radioInputList.length; index++) {
            if (radioInputList[index].checked) checkedCounter++;
        }
        if (checkedCounter !== radioInputList.length / 3) {
            errorMessage.style.display = "block";
        } else {
            answersForm.submit();
        }
    })
}

