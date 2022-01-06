"use strict";

const englishButton = document.getElementById("englishLanguageButton");
const ukrainianButton = document.getElementById("ukrainianLanguageButton");

englishButton.addEventListener("click", () => {
    const URI = window.location.pathname;
    window.location.href = URI + "?lang=en";
})

ukrainianButton.addEventListener("click", () => {
    const URI = window.location.pathname;
    window.location.href = URI + "?lang=ua";
})