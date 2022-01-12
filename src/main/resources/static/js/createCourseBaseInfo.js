"use strict";

const createCourseForm = document.getElementById("create-course-form");
const sendFormButton = document.getElementById("sendCourseBaseInfoButton");
const courseNameInput = document.getElementById("courseName");
const categorySelect = document.getElementById("category");
const enRadio = document.getElementById("enRadio");
const ukrRadio = document.getElementById("ukrRadio");

const courseNameIsEmptyErrorMessage = document.getElementById("courseNameIsEmptyErrorMessage");
const courseCategoryIsEmptyErrorMessage = document.getElementById("courseCategoryIsEmptyErrorMessage");
const courseLanguageIsEmptyMessage = document.getElementById("courseLanguageIsEmptyMessage");

if (sendFormButton != null) {
    sendFormButton.addEventListener("click", () => {
        clearErrorMessages();
        if (courseNameInput.value === "") {
            courseNameIsEmptyErrorMessage.style.display = "block";
        } else if (categorySelect.value === "") {
            courseCategoryIsEmptyErrorMessage.style.display = "block";
        } else if (!enRadio.checked && !ukrRadio.checked) {
            courseLanguageIsEmptyMessage.style.display = "block";
        } else {
            createCourseForm.submit();
        }
    })
}

const clearErrorMessages = () => {
    courseNameIsEmptyErrorMessage.style.display = "none";
    courseCategoryIsEmptyErrorMessage.style.display = "none";
    courseLanguageIsEmptyMessage.style.display = "none";
}