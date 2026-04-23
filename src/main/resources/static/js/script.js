function togglePopupForBooking(workplace) {
    document.getElementById("popup-1").classList.toggle("active");
    var selectedWorkplaceID = document.getElementById("selectedWorkplaceID");
    selectedWorkplaceID.value = workplace;
}
function togglePopupForBookingForSearchCriteria(workplace, roomId, selectedDescription) {
    document.getElementById("popup-1").classList.toggle("active");
    var selectedWorkplaceID = document.getElementById("selectedWorkplaceID");
    selectedWorkplaceID.value = workplace;
    var selectedRoomID = document.getElementById("selectedRoomID");
    selectedRoomID.value = roomId;
    var selectedDescriptionID = document.getElementById("selectedDescriptionID");
    selectedDescriptionID.value = selectedDescription;
}

function togglePopupForStatusCheking(workplaceNumber) {
    document.getElementById("popup-2").classList.toggle("active");
    //var wn = document.getElementsByName("workplaceNumberForCheckingBookings").values();
    //document.createAttribute("workplaceNumberForCheckingBookings").value = workplaceNumber;
    var wn = document.createAttribute("workplaceNumberForCheckingBookings").name;
    //wn.value = workplaceNumber;
}


function togglePopupForDeletingBook(bookingID) {
    document.getElementById("popup-11").classList.toggle("active");
    var bookingToDeleteID = document.getElementById("bookingToDeleteID");
    bookingToDeleteID.value = bookingID;
}

function togglePopupForDeletingRoom(roomID, roomName) {
    document.getElementById("popup-12").classList.toggle("active");
    var roomToDeleteID = document.getElementById("roomToDeleteID");
    roomToDeleteID.value = roomID;
    var roomNameToDelete = document.getElementById("roomNameToDelete");
    roomNameToDelete.value = roomName;
}

function togglePopupForUpdatingBook(bookingID, bookingDate, bookingStart, bookingEnd, bookingRoomName, bookingWorkplaceNumber) {
    document.getElementById("popup-3").classList.toggle("active");
    var bookingToUpdateID = document.getElementById("bookingToUpdateID");
    bookingToUpdateID.value = bookingID;
    var bookingForDate = document.getElementById("bookingForDate");
    bookingForDate.value = bookingDate;
    var bookingForStart = document.getElementById("bookingForStart");
    bookingForStart.value = bookingStart;
    var bookingForEnD = document.getElementById("bookingForEnD");
    bookingForEnD.value = bookingEnd;
    var bookingForRoom = document.getElementById("bookingForRoom");
    bookingForRoom.value = bookingRoomName;
    var bookingForWorkplace = document.getElementById("bookingForWorkplace");
    bookingForWorkplace.value = bookingWorkplaceNumber;
}

function togglePopupForAddingNewRoom() {
    document.getElementById("popup-4").classList.toggle("active");

}

function togglePopupForBookingBlocked() {
    document.getElementById("popup-14").classList.toggle("active");

}

function togglePopupForAddingNewWorkplace() {
    document.getElementById("popup-5").classList.toggle("active");

}

function togglePopupForAvailableWorkplace(roomID, workplaceNumber, workplaceStatus, equipmentDescription) {
    document.getElementById("popup-6").classList.toggle("active");
    var selectedRoomId = document.getElementById("selectedRoomId");
    selectedRoomId.value = roomID;
    var selectedWorkplaceNumber = document.getElementById("selectedWorkplaceNumber");
    selectedWorkplaceNumber.value = workplaceNumber;
    var currentStatus = document.getElementById("currentStatus");
    currentStatus.value = workplaceStatus;
    var selectedEquipment = document.getElementById("selectedEquipment");
    selectedEquipment.value = equipmentDescription;
}

function togglePopupForSearching(roomID) {
    document.getElementById("popup-7").classList.toggle("active");
    var selectedtRoom = document.getElementById("roomIDForSearching");
    selectedtRoom.value = roomID;

}

function togglePopupForAdminSearching() {
    document.getElementById("popup-8").classList.toggle("active");
}

function togglePopupForAdminEditRoom(roomId, roomName) {
    document.getElementById("popup-9").classList.toggle("active");
    var selectedRoomId = document.getElementById("roomId");
    selectedRoomId.value = roomId;
    var selectedRoomName = document.getElementById("RoomNameID");
    selectedRoomName.value = roomName;


}

function togglePopupForDeletingWorkplace(workplaceNumber) {
    document.getElementById("popup-10").classList.toggle("active");
    var workplaceNumberID = document.getElementById("workplaceNumberID");
    workplaceNumberID.value = workplaceNumber;
}

// hides the flash attribute after 10 seconds for success message
setTimeout(function () {
    document.getElementById("success-message").style.display = "none";
}, 3000);

// hides the flash attribute after 10 seconds for success message
setTimeout(function () {
    document.getElementById("error-message").style.display = "none";
}, 3000);



