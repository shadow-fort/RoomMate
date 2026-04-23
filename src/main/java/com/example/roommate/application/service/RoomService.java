package com.example.roommate.application.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.roommate.application.service.repositories.RoomRepository;
import com.example.roommate.domain.model.roomAggregat.Room;
import com.example.roommate.domain.model.roomAggregat.Workplace;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@Service
public class RoomService {

    /**
     * Attribut für dependency injection.
     */
    private final RoomRepository roomRepository;


    /**
     * .
     * @param roomRepos Instanz
     */
    public RoomService(
            final RoomRepository roomRepos) {
        this.roomRepository = roomRepos;
    }

    /**
     * .
     * @return eine Liste von Raüme
     */
    public List<Room> getAllRooms() {
        return roomRepository.findAll().stream()
                .sorted(Comparator.comparing(Room::getRoomName))
                .toList();
    }

    /**
     * .
     * @param selectedDescription variable
     * @return eine Liste von Raüme
     */

    public List<Room> getAllRooms(final String selectedDescription) {
        if (selectedDescription == null) {
            return getAllRooms();
        }
        return getAllRooms().stream()
                .filter(room -> room.getEquipmentDescription().contains(
                        selectedDescription))
                .map(room -> getRoomWithUpdateEquipmentsDescription(
                        room.getRoomId(), selectedDescription))
                .sorted(Comparator.comparing(Room::getRoomName))
                .collect(Collectors.toList());
    }

    /**
     * .
     * @param contain Neue Equipments des Platzes
     *                or new Name des Raums
     * @return true or false
     */
    public boolean isContainsCorrect(
            final String contain) {
        return !contain.isBlank();
    }


    /**
     * .
     * @param roomId schlüssel
     * @return ein Raum
     */
    public Room findRoomById(
            final Long roomId) {
        Optional<Room> currentRoom = roomRepository.findByRoomId(roomId);
        return currentRoom.orElse(null);
    }

    /**
     * .
     * @param roomId schlüssel
     * @return eine Liste von Equipment
     */
    public List<String> getRoomWithEquipmentsDescription(
            final Long roomId) {
        Room room = findRoomById(roomId);
        return room.getEquipmentDescription().stream().distinct().toList();
    }

    /**
     * .
     * @param roomId Schlüssel
     * @param selectedDescription variable
     * @return ein Raum
     */
    public Room getRoomWithUpdateEquipmentsDescription(
            final Long roomId,
            final String selectedDescription) {
        Room room = findRoomById(roomId);
        if (selectedDescription.isBlank()) {
            return room;
        }
        Set<Workplace> updatedWorkplaces = room.getWorkplaces()
                .stream()
                .filter(workplace -> workplace.equipments()
                        .equals(selectedDescription))
                .collect(Collectors.toSet());

        // Erstelle einen neuen Raum mit den bearbeiteten workplaces
        return new Room(roomId, room.getRoomName(), updatedWorkplaces);
    }


    /**
     * .
     * @return eine Liste von Equipment
     */
    public List<String> getAllEquipmentsDescription() {
        List<String> allEquipments = new ArrayList<>();
        for (Room room : getAllRooms()) {
            Room currentRoom = findRoomById(room.getRoomId());
            allEquipments.addAll(currentRoom.getEquipmentDescription());
        }
        return allEquipments.stream().distinct().toList();
    }

    /**
     * .
     * @param nummer variable
     * @param equipmentDescription variable
     * @param roomId Schlüssel
     */
    public void addWorkplaceForRoom(
            final int nummer,
            final String equipmentDescription,
            final Long roomId) {
        Optional<Room> room = roomRepository.findByRoomId(roomId);
        if (room.isPresent()) {
            Room currentRoom = room.get();
            currentRoom.addWorkplace(nummer, equipmentDescription);
            roomRepository.save(currentRoom);
        }
    }

    /**
     * .
     * @param workplaceNummer variable
     * @param currentStatus variable
     * @param roomId Schlüssel
     * @param equipmentDescription variable
     */
    public void editWorkplace(
            final int workplaceNummer,
            final String currentStatus,
            final Long roomId,
            final String equipmentDescription) {
        Optional<Room> room = roomRepository.findByRoomId(roomId);
        if (room.isPresent()) {
            Room currentRoom = room.get();
            currentRoom.editWorkplace(workplaceNummer,
                    equipmentDescription, currentStatus);
            roomRepository.save(currentRoom);
        }

    }

    /**
     * .
     * @param roomId Schlüssel
     * @param roomName variable
     */
    public void editRoom(
            final Long roomId,
            final String roomName) {
       roomRepository.editRoom(roomId, roomName);
    }

    /**
     * .
     * @param roomId Schlüssel
     */
    public void deleteRoom(
            final Long roomId) {
        Optional<Room> room = roomRepository.findByRoomId(roomId);
        if (room.isPresent()) {
            Room currentRoom = room.get();
            roomRepository.delete(currentRoom);
        }
    }

    /**
     * .
     * @param roomName Name des Raums
     */
    public void addNewRoom(final String roomName) {
       roomRepository.save(new Room(roomName));
    }

    /**
     * .
     * @param roomName Name des Raums
     * @return ture or false
     */
    public boolean isRoomNameIncorrect(final String roomName) {
        if (roomName.isBlank()) {
            return true;
        }
       return getAllRooms().stream()
                .anyMatch(room -> room.getRoomName().equals(roomName));
    }

    /**
     * .
     * @param roomId Schlüssel
     * @param workplaceNumber Nummer des Platzes
     */
    public void deletingWorkplace(
            final Long roomId,
            final int workplaceNumber) {
        Optional<Room> room = roomRepository.findByRoomId(roomId);
        if (room.isPresent()) {
            Room currentRoom = room.get();
            currentRoom.deleteWorkplace(workplaceNumber);
            roomRepository.save(currentRoom);
        }
    }

    /**
     * .
     * @param roomId Schlüssel
     * @param number Nummer des Platzes
     * @param equipments Equipments des Platzes
     * @return true or false
     */
    public boolean isWorkplaceToaddCorrect(
            final Long roomId,
            final int number,
            final  String equipments
    ) {
        if (equipments.isBlank()) {
            return false;
        }
        Optional<Room> room = roomRepository.findByRoomId(roomId);
        if (room.isPresent()) {
            Room currentRoom = room.get();
            return !currentRoom.isNumberPresent(number);
        }
        return true;
    }

}
