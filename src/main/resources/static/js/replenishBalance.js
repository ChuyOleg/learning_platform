"use strict";

const replenishBalanceButton = document.getElementById("replenishBalanceButton");
const replenishBalanceForm = document.getElementById("replenishBalanceForm");
const cancelButton = document.getElementById("cancel-button");
const replenishmentInput = document.getElementById("replenishment");
const okButton = document.getElementById("ok-button");
const replenishmentError = document.getElementById("replenishmentError");


if (replenishBalanceButton != null) {
    replenishBalanceButton.addEventListener("click", () => {
        replenishmentError.style.display = "none";
        replenishBalanceForm.style.display = "block";
    })
}

if (cancelButton != null) {
    cancelButton.addEventListener("click", () => {
        replenishmentError.style.display = "none";
        replenishBalanceForm.style.display = "none";
    })
}

if (okButton != null) {
    okButton.addEventListener("click", () => {
        const replenishment = replenishmentInput.value;
        if (replenishment === "" || parseInt(replenishment) <= 0) {
            replenishmentError.style.display = "block";
        } else {
            replenishBalanceForm.submit();
        }
    })
}
