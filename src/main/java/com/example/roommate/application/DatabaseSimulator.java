package com.example.roommate.application;

import com.example.roommate.application.service.RoomService;
import com.example.roommate.domain.model.roomAggregat.Room;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DatabaseSimulator {

    /**
     * Attribut.
     */
    private final RoomService roomService;

    /**
     * Attribut.
     */
    private final Random random = new Random();

    /**
     * Konstant für die Limitierung der Rndom.
     */
    private static final int LIMIT = 6;

    /**
     * Attribut.
     */
    private static final String[] ROOM_NAMEN =
            {"IT Room", "Marketing Room", "Dev Room"};

    /**
     * Attribut.
     */
    private static final String[] EQUIPMENT = {"Monitor, Tastatur, Maus",
            "3 Monitore, Tastatur JimTech, Maus_Bluetooth, Port USB_c, Webcam",
            "2 Monitore, Tastaur, Maus, Port USB_C",
            "Monitor, Tastatur, Maus, Tastatur, Maus",
            "2 Apple Monitore, Apple Tastatur, Apple Maus",
            "3 Monitore, Tastatur JimTech, Maus_Bluetooth, Port USB_c, Webcam",
            "Monitor_Marketing, Tastatur Bluethoot, Maus Bleuthoot",
            "Monitor_Marketing", "3 Monitore, Tastatur JimTech, Maus_Bluetooth",
            "1 Monitore, Tastatur JimTech, Maus, Port USB_c, Webcam"};

    /**
     * .
     * @param serviceRoom Instanz
     */
    DatabaseSimulator(
            final RoomService serviceRoom) {
        this.roomService = serviceRoom;

        if (roomService.getAllRooms().isEmpty()) {
            erzeugeRaeume();
            erzeugeWorkplace();
        }
    }

    private void erzeugeRaeume() {
        for (String roomNaman : ROOM_NAMEN) {
            roomService.addNewRoom(roomNaman);
        }
    }

    private void erzeugeWorkplace() {
        var roomListe = roomService.getAllRooms();
        for (Room room : roomListe) {
            for (int i = 0; i < selectRandom(); i++) {
                roomService.addWorkplaceForRoom(
                        i + 1, EQUIPMENT[i], room.getRoomId());
            }
        }
    }

    private int selectRandom() {
        return random.nextInt(LIMIT) + 1;
    }
}