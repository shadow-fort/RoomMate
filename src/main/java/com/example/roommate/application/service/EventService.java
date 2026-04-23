package com.example.roommate.application.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.roommate.domain.model.bookingAggregat.Booking;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@Service
public class EventService {
    /**
     * Attribut.
     */
    private final RoomService roomService;

    /**
     * Attribut.
     */
    private final BookingService bookingService;

    /**
     * .
     */
    private static final int PORT_KONSTANT = 3000;

    /**
     * .
     */
    private static final int SECOND_KONSTANT = 3;

    /**
     * .
     * @param serviceRoom Instant
     * @param serviceBooking Instanz
     */
    public EventService(
            final RoomService serviceRoom,
            final BookingService serviceBooking) {
        this.roomService = serviceRoom;
        this.bookingService = serviceBooking;
    }

    /**
     * .
     * @return eine Liste KeyMaste
     */
    public List<KeyMaster> getRoomKey() {
        List<KeyMaster> keys = new ArrayList<>();
        List<Booking> bookings = bookingService.getAllCurrentBookings();
        String json1 = WebClient.create()
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
        String json2 = WebClient.create()
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(PORT_KONSTANT)
                                .path("/key")
                                .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block(Duration.of(SECOND_KONSTANT, ChronoUnit.SECONDS));

        JSONArray jsonArray1 = new JSONArray(json1);
        JSONArray jsonArray2 = new JSONArray(json2);
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject objects = jsonArray1.getJSONObject(i);
            String roomName = objects.getString("raum");
            String roomKey = objects.getString("id");

            for (int j = 0; j < jsonArray2.length(); j++) {
                JSONObject object = jsonArray2.getJSONObject(j);
                String userName = object.getString("owner");
                String userKey = object.getString("id");

                for (Booking booking : bookings) {
                    if (booking.getRoomName().equals(roomName)
                            && booking.getUsername().equals(userName)) {
                        keys.add(new KeyMaster(UUID.fromString(userKey),
                                UUID.fromString(roomKey), userName, roomName));
                    }
            }
            }
        }
        return keys;
    }
}
