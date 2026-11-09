package com.example.roommate.application.service;

import com.example.roommate.application.service.repositories.RoomRepository;
import com.example.roommate.domain.model.roomAggregat.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RoomServiceTest {


    RoomRepository roomRepository = mock(RoomRepository.class);

    RoomService roomService = new RoomService(roomRepository);

    @Test
    @DisplayName("Ein Raum kann hinzugefügt werden")
    void test_1() {
        Room room = new Room("Bella Room");
        when(roomRepository.save(any())).thenReturn(room);
        roomService.addNewRoom("Bella Room");

        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(captor.capture());
        Room savedRoom = captor.getValue();
        assertThat(savedRoom.getRoomName()).isEqualTo("Bella Room");
    }

    @Test
    @DisplayName("Ein Raum kann anhand sein id gefunden werden")
    void test_2() {
        Room room = new Room(2L, "Bella Room");
        when(roomRepository.findByRoomId(anyLong())).thenReturn(Optional.of(room));

        Room findRoom = roomService.findRoomById(2L);

        assertThat(findRoom.getRoomName()).isEqualTo("Bella Room");
    }

    @Test
    @DisplayName("Alle Räume können zurückgegeben werden")
    void test_3() {
        when(roomRepository.findAll()).thenReturn(
                List.of(new Room("Bengol Room"), new Room("Premium Room")));

        List<Room> rooms = roomService.getAllRooms();
        assertThat(rooms.size()).isEqualTo(2);
    }
}
