function togglePopupForBooking() {
    document.getElementById("popup-1").classList.toggle("active");

    // Get date and time elements
    const dateInput = document.getElementById("dateID");
    const startTimeInput = document.getElementById("startTimeID");
    const endTimeInput = document.getElementById("endTimeID");
    const submitButton = document.getElementById("submitBtn");

    submitButton.addEventListener("click", function (event) {
        event.preventDefault(); // Avoid form submission

        // Get current Date and Time
        const currentDate = new Date();
        const currentTime = new Date();

        // Get User selected Date
        const selectedDate = new Date(dateInput.value.trim()); // Trim leading/trailing spaces

        // Get User selected Start Time
        const selectedStartTime = new Date(`2000-01-01T${startTimeInput.value}:00`);

        // Get User selected End Time
        const selectedEndTime = new Date(`2000-01-01T${endTimeInput.value}:00`);

        // Check if selected Date, Start Time, or End Time is empty or in the past
        if (selectedDate < currentDate) {
            alert("No date selected or the date is in the past.");
        } else if (selectedDate.getTime() < currentDate.getTime()) {
            alert("The selected time is in the past for the current date.");
        } else if (selectedStartTime >= selectedEndTime) {
            alert("The end time must be later than the start time.");
        } else {
            // Submit the Form
            document.forms[0].submit();
        }
    });
}

function togglePopupForStatusCheking(){
    document.getElementById("popup-2").classList.toggle("active");

}


function togglePopupForUpdatingBook(){
    document.getElementById("popup-3").classList.toggle("active");

}

function togglePopupForAddingNewRoom(){
    document.getElementById("popup-4").classList.toggle("active");

}
function togglePopupForAddingNewWorkplace(){
    document.getElementById("popup-5").classList.toggle("active");

}

function togglePopupForEditingWorkplace(){
    document.getElementById("popup-6").classList.toggle("active");

}