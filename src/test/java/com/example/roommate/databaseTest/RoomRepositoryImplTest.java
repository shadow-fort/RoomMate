package com.example.roommate.databaseTest;

import com.example.roommate.application.service.repositories.RoomRepository;
import com.example.roommate.database.RoomRepositoryImpl;
import com.example.roommate.database.dao.RoomRepositoryDAO;
import com.example.roommate.domain.model.roomAggregat.Room;
import com.example.roommate.domain.model.roomAggregat.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoomRepositoryImplTest {

    @Autowired
    RoomRepositoryDAO roomRepositoryDAO;

    RoomRepository roomRepository;

    @BeforeEach
    void setUp(){
        roomRepository = new RoomRepositoryImpl(roomRepositoryDAO);
    }

    @Test
    @DisplayName("Ein Room kann gespeichert und geladen werden")
    void test_1(){
        Room room = new Room("BingaloRoom");
        room.addWorkplace(new Workplace(1, "Maus und Monitor", "AVAILABLE"));
        room.addWorkplace(new Workplace(2, "Maus und Tastatur","AVAILABLE"));
        Room savedRoom = roomRepository.save(room);
        Optional<Room> loadedRoom = roomRepository.findByRoomId(savedRoom.getRoomId());
        assertThat(loadedRoom.map(Room::getRoomName).orElseThrow()).isEqualTo("BingaloRoom");
        assertThat(loadedRoom.map(Room::getWorkplaces).orElseThrow()).hasSize(2);
    }



    @Test
    @DisplayName("Ein Raum kann bearbeitet werden")
    void testEditRoom() {
        // Vorbereitung
        Room room = new Room("BungalowRoom");
        room = roomRepository.save(room);

        // Ausführung
        roomRepository.editRoom(room.getRoomId(), "EditedRoomName");
        Optional<Room> editedRoom = roomRepository.findByRoomId(room.getRoomId());

        // Überprüfung
        assertTrue(editedRoom.isPresent());
        assertThat(editedRoom.get().getRoomName()).isEqualTo("EditedRoomName");
    }

    @Test
    @DisplayName("Ein Raum kann gelöscht werden")
    void testDeleteRoom() {
        // Vorbereitung
        Room room = new Room("RoomToDelete");
        room = roomRepository.save(room);

        // Ausführung
        roomRepository.delete(room);
        Optional<Room> deletedRoom = roomRepository.findByRoomId(room.getRoomId());

        // Überprüfung
        assertFalse(deletedRoom.isPresent());
    }

    @Test
    @DisplayName("Ein Arbeitsplatz kann einem Raum hinzugefügt werden")
    void testAddWorkplaceToRoom() {
        // Vorbereitung
        Room room = new Room("RoomWithWorkplace");

        // Ausführung
        room.addWorkplace(new Workplace(1, "Maus und Monitor", "AVAILABLE"));
        room = roomRepository.save(room);
        Optional<Room> roomWithWorkplace = roomRepository.findByRoomId(room.getRoomId());

        // Überprüfung
        assertTrue(roomWithWorkplace.isPresent());
        assertThat(roomWithWorkplace.get().getWorkplaces()).hasSize(1);
    }

    @Test
    @DisplayName("Ein Arbeitsplatz kann aus einem Raum entfernt werden")
    void testRemoveWorkplaceFromRoom() {
        // Vorbereitung
        Room room = new Room("RoomToRemoveWorkplace");
        room.addWorkplace(new Workplace(1, "Maus und Monitor", "AVAILABLE"));
        room.addWorkplace(new Workplace(2, "Maus und Tastatur", "AVAILABLE"));
        room = roomRepository.save(room);

        // Ausführung
        room.deleteWorkplace(1);
        room = roomRepository.save(room);
        Optional<Room> roomWithoutWorkplace = roomRepository.findByRoomId(room.getRoomId());

        // Überprüfung
        assertTrue(roomWithoutWorkplace.isPresent());
        assertThat(roomWithoutWorkplace.get().getWorkplaces()).hasSize(1);
    }

    @Test
    @DisplayName("Der Status eines Arbeitsplatzes kann aktualisiert werden")
    void testUpdateWorkplaceStatus() {
        // Vorbereitung
        Room room = new Room("RoomToUpdateWorkplaceStatus");
        room.addWorkplace(new Workplace(1, "Maus und Monitor", "AVAILABLE"));
        room = roomRepository.save(room);

        // Ausführung
        room.editWorkplaceStatus(1, "Maus und Monitor", "AVAILABLE");
        room = roomRepository.save(room);
        Optional<Room> roomWithUpdatedStatus = roomRepository.findByRoomId(room.getRoomId());

        Workplace workplaceWithUpdatedStatus = roomWithUpdatedStatus.get().getWorkplaces().stream().findFirst().get();

        // Überprüfung
        assertThat(workplaceWithUpdatedStatus.status()).isEqualTo("BLOCKED");
    }

    @Test
    @DisplayName("Test für findAll Methode")
    @Sql("findAll.sql")
    void testFindAll() {
        List<Room> allRooms = roomRepository.findAll();

        assertThat(allRooms).hasSize(3);
    }
}
