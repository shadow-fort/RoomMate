package com.example.roommate.application.service.repositories;

import com.example.roommate.domain.model.roomAggregat.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    /**
     * .
     * @return eine Liste von Booking
     */
    List<Room> findAll();

    /**
     * .
     * @param roomId schlüssel
     * @return eine optionale Liste von Room
     */
    Optional<Room> findByRoomId(Long roomId);

    /**
     * .
     * @param room Objekt
     * @return ein Room
     */
    Room save(Room room);

    /**
     * .
     * @param room Objekt
     */
    void delete(Room room);

    /**
     * .
     * @param roomId schlüssel
     * @param roomName Name des Rooms
     */
    void editRoom(Long roomId, String roomName);
}