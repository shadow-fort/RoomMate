package com.example.roommate.controllers.rest;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.roommate.application.service.EventService;
import com.example.roommate.application.service.KeyMaster;
import com.example.roommate.application.service.RoomService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@RestController
public class EventController {



    /**
     * Attribut für dependency injection.
     */
    private final EventService eventService;

    /**
     * Attribut für dependency injection.
     */
    private final RoomService roomService;

    /**
     * .
     */
    private static final int PORT_KONSTANT = 3000;

    /**
     * .
     */
    private static final int DELAY_KONSTANT = 60000;

    /**
     * .
     */
    private static final int SECOND_KONSTANT = 3;

    /**
     * .
     * @param serviceEvent Instanz
     * @param serviceRoom Instanz
     */
    public EventController(
            final EventService serviceEvent,
            final RoomService serviceRoom) {
        this.eventService = serviceEvent;
        this.roomService = serviceRoom;
    }

    /**
     * .
     * @return eine Liste
     */
    @GetMapping(value = "/api/access")
    public List<KeyMaster> rooms() {
        return eventService.getRoomKey();
    }

    /**
     * .
     */
    @Scheduled(fixedDelay = DELAY_KONSTANT)
    public void fetchEvents() {
        String json = WebClient.create()
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(PORT_KONSTANT)
                                .path("/room")
                                .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block(Duration.of(SECOND_KONSTANT, ChronoUnit.SECONDS));

        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject objects = jsonArray.getJSONObject(i);
            String roomName = objects.getString("raum");
            if (!roomService.isRoomNameIncorrect(roomName)) {
                roomService.addNewRoom(roomName);
        }

        }
    }

}