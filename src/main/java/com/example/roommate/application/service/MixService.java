package com.example.roommate.application.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.roommate.application.service.repositories.BookingRepository;
import com.example.roommate.application.service.repositories.RoomRepository;
import com.example.roommate.domain.model.bookingAggregat.Booking;
import com.example.roommate.domain.model.roomAggregat.Room;
import com.example.roommate.domain.model.roomAggregat.Workplace;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.roommate.application.service.BookingService.fixDuplicate;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@Service
public class MixService {

    /**
     * Attribut.
     */
    private final RoomRepository roomRepository;

    /**
     * Attribut.
     */
    private final BookingRepository bookingRepository;

    /**
     * .
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("dd.MM.yyyy", Locale.GERMANY);

    /**
     * .
     * @param roomRepos Instanz
     * @param bookingRepos Instanz
     */
    public MixService(
            final RoomRepository roomRepos,
            final BookingRepository bookingRepos) {
        this.roomRepository = roomRepos;
        this.bookingRepository = bookingRepos;
    }

    /**
     * .
     * @param date variable
     * @return deutsches Datum Format
     */
    private static String getGermainDate(final LocalDate date) {
        return date.format(FORMATTER);
    }

    /**
     * .
     * @param date Variable
     * @param startTime Variable
     * @param endTime Variable
     * @param equipments Variable
     * @return eine Liste von Raume
     */
    public List<Room> getAllFreeRoom(
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime,
            final String equipments
            ) {
        List<Room> freeRooms = new ArrayList<>();
        for (Room room : roomRepository.findAll()) {
            if (!equipments.isBlank()) {
                Room roomWithUpdatedEquipment =
                        getRoomWithUpdateEquipmentsDescription(
                                room.getRoomId(), equipments);
                Room roomWithFreePlaces = findFreeWorkplacesForTimeRangeForRoom(
                                    roomWithUpdatedEquipment, date,
                                    startTime, endTime);
                freeRooms.add(roomWithFreePlaces);

            } else {
                Room roomWithFreePlaces =
                        findFreeWorkplacesForTimeRangeForRoom(room, date,
                                startTime, endTime);
                freeRooms.add(roomWithFreePlaces);
            }
        }
        return getOnlyRoomsWithWorkplaces(freeRooms);
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
        Room room = roomRepository.findByRoomId(roomId).orElse(null);
        if (selectedDescription.isBlank()) {
            return room;
        }
        assert room != null;
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
     * @param room variable
     * @param date variable
     * @param startTime variable
     * @param endTime variable
     * @return gibt alle freie Arbeit Plätze in einem Raum
     */
    public Room findFreeWorkplacesForTimeRangeForRoom(
            final Room room,
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime) {
        List<Booking> allBookings = bookingRepository.findAll()
                .stream().filter(b -> b.getRoomName()
                        .equals(room.getRoomName()))
                .sorted(Comparator.comparing(Booking::getDate)
                        .thenComparing(Booking::getStartTime)
                        .thenComparing(Booking::getEndTime))
                .toList();
        Set<Integer> bookedWorkplaceNumbers =
                getBookingsForTimeRange(allBookings, date, startTime, endTime)
                        .stream().map(Booking::getWorkplaceNumber)
                        .collect(Collectors.toSet());
        Set<Workplace> freeWorkplaces = room.getWorkplaces().stream()
                .filter(workplace -> !bookedWorkplaceNumbers.contains(workplace
                        .workplaceNummer())).collect(Collectors.toSet());

        return new Room(room.getRoomId(), room.getRoomName(), freeWorkplaces);
    }

    /**
     * .
     * @param bookings  variable
     * @param date variable
     * @param startTime variable
     * @param endTime variable
     * @return list von Bookings, für eine bestimmte Zeit
     */
    public List<Booking> getBookingsForTimeRange(
            final List<Booking> bookings,
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime) {
        List<Booking> booked = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getDate().equals(date)) {
                if (booking.getStartTime().equals(startTime)
                        && booking.getEndTime().equals(endTime)) {
                    booked.add(booking);
                }
                if (booking.getStartTime().isBefore(startTime)
                        && booking.getEndTime().isAfter(endTime)) {
                    booked.add(booking);
                }
                if ((booking.getStartTime().isAfter(startTime)
                        && booking.getStartTime().isBefore(endTime))
                        || (booking.getEndTime().isAfter(startTime)
                        && booking.getEndTime().isBefore(endTime))) {
                    booked.add(booking);
                }
                if (startTime.isAfter(booking.getStartTime())
                        && endTime.isBefore(booking.getEndTime())) {
                    booked.add(booking);
                }
            }
        }
        return booked;
    }

    /**
     * .
     * @param date Variable
     * @param startTime Variable
     * @param endTime Variable
     * @return description
     */
    public static String getDescription(
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime) {
            return getGermainDate(date) + "  " + startTime
                    + "->" + endTime;

    }

    /**
     * .
     * @param freeRooms available Raume
     * @return eine liste von Raume
     */
    public List<Room> getOnlyRoomsWithWorkplaces(final List<Room> freeRooms) {
        return freeRooms.stream()
                .filter(room -> !(room.getWorkplaces().isEmpty()))
                .toList();
    }

    /**
     * .
     * @param workplaces Variable
     * @param workplaceNumber Variable
     * @return true or false
     */
    public boolean isWorkplaceAvailable(
            final Set<Workplace> workplaces,
            final int workplaceNumber) {
        for (Workplace workplace : workplaces) {
            if (workplace.workplaceNummer() == workplaceNumber
                    && workplace.status().equals("AVAILABLE")) {
                return true;
            }
        }
        return false;
    }

    /**
     * .
     * @param roomName Variable
     * @param workplaceNumber Variable
     * @param date Variable
     * @param startTime Variable
     * @param endTime Variable
     * @return true or false
     */
    public boolean isBookingCollisionFree(
            final String roomName,
            final int workplaceNumber,
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime) {
        List<Booking> bookings = bookingRepository
                .findBookingByRoomNameAndWorkplaceNumber(
                        roomName, workplaceNumber).stream()
                .filter(b -> b.getDate().equals(date))
                .toList();
        return validatorForCollision(bookings, startTime, endTime);
    }

    /**
     * .
     * @param bookings Variable
     * @param startTime Variable
     * @param endTime Variable
     * @return true or false
     */
    public boolean validatorForCollision(
            final List<Booking> bookings,
            final LocalTime startTime,
            final LocalTime endTime) {
        return fixDuplicate(bookings, startTime, endTime);
    }

    /**
     * .
     * @param workplaces Variable
     * @param workplaceNumber Variable
     * @param roomName Variable
     * @param date Variable
     * @param startTime Variable
     * @param endTime Variable
     * @return message
     */
    public String notValidBooking(
            final Set<Workplace> workplaces,
            final int workplaceNumber,
            final String roomName,
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime) {
        if (!isWorkplaceAvailable(workplaces, workplaceNumber)) {
            return "this Workplace is actually Unavailable!!!";
        } else if (!isBookingCollisionFree(roomName, workplaceNumber,
                date, startTime, endTime)) {
            return "Unable to update Booking, "
                    +
                    "because collision with another Booking!!! ";
        }
        return null;
    }

    /**
     * .
     * @param roomId Variable
     * @param equipments Variable
     * @param date Variable
     * @param startTime Variable
     * @param endTime Variable
     * @return Raum
     */
    public Room searchForRoom(
            final Long roomId,
            final String equipments,
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime
    ) {
        Room selectedRoom = roomRepository.findByRoomId(roomId).orElse(null);
        if (equipments != null) {
            assert selectedRoom != null;
            Room updatedRoom = getRoomWithUpdateEquipmentsDescription(
                            selectedRoom.getRoomId(), equipments);
            return findFreeWorkplacesForTimeRangeForRoom(
                            updatedRoom, date, startTime, endTime);
        } else if (date != null) {
            assert selectedRoom != null;
            return findFreeWorkplacesForTimeRangeForRoom(
                            selectedRoom, date, startTime, endTime);
        }
        return selectedRoom;
    }

}
