package com.example.roommate.application.validation;

public record RoomForm(
        Long roomId,
        String roomName,
        Integer workplaceNumber,
        String workplaceStatus,
        String equipmentDescription
) {
}
