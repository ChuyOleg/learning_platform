"use strict";

const registerButton = document.getElementById("registerButton");
const registrationForm = document.getElementById("registration-form");
const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");
const passwordCopyInput = document.getElementById("passwordCopy");
const emailInput = document.getElementById("email");
const birthdayInput = document.getElementById("birthday");
const taxNumberInput = document.getElementById("taxNumber");

const usernameError = document.getElementById("usernameError");
const passwordError = document.getElementById("passwordError");
const passwordCopyError = document.getElementById("passwordCopyError");
const emailError = document.getElementById("emailError");
const birthdayError = document.getElementById("birthdayError");
const taxNumberError = document.getElementById("taxNumberError");

if (registerButton != null) {
    registerButton.addEventListener("click", () => {
        usernameError.style.display = "none";
        passwordError.style.display = "none";
        passwordCopyError.style.display = "none";
        emailError.style.display = "none";
        birthdayError.style.display = "none";
        taxNumberError.style.display = "none";

        if (usernameInput.value.length < 3 || usernameInput.value.length > 32) {
            usernameError.style.display = "block";
        } else if (passwordInput.value.length < 8 || passwordInput.value.length > 32) {
            passwordError.style.display = "block";
        } else if (passwordCopyInput.value === "") {
            passwordCopyError.style.display = "block";
        } else if (!validateEmail(emailInput.value)) {
            emailError.style.display = "block";
        } else if (birthdayInput.value === "") {
            birthdayError.style.display = "block";
        } else if (taxNumberInput.value === "") {
            taxNumberError.style.display = "block";
        } else {
            registrationForm.submit();
        }
    })
}

const validateEmail = (email) => {
    return String(email)
        .toLowerCase()
        .match(
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        );
};