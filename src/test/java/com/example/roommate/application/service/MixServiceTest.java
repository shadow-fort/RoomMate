package com.example.roommate.application.service;

import com.example.roommate.application.service.repositories.BookingRepository;
import com.example.roommate.application.service.repositories.RoomRepository;
import com.example.roommate.domain.model.bookingAggregat.Booking;
import com.example.roommate.domain.model.roomAggregat.Room;
import com.example.roommate.domain.model.roomAggregat.Workplace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.example.roommate.databaseTest.buider.TestBookingFactory.booking;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MixServiceTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
            "dd.MM.yyyy", Locale.GERMANY);
    RoomRepository roomRepository = mock(RoomRepository.class);

    BookingRepository bookingRepository = mock(BookingRepository.class);

    MixService mixService = new MixService(roomRepository, bookingRepository);


    @Test
    @DisplayName("Die getBookingsForTimeRange Methode gibt Buchungen im angegebenen Zeitbereich zurück")
    void test_2(){
        LocalDate currentDate = createValidDate();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);
        List<Booking> bookings = List.of(
                booking("JohnDoe", "Meeting Room", 1,
                        getGermainDate(currentDate), "08:00", "09:30"),
                booking("JaneDoe", "Conference Room", 2,
                        getGermainDate(currentDate), "09:15", "10:30"),
                booking("Alice", "Meeting Room", 3,
                        getGermainDate(currentDate), "10:00", "11:00"),
                booking("Bob", "Conference Room", 4,
                        getGermainDate(currentDate), "10:30", "12:00")
        );
        // Act
        List<Booking> bookingsInRange = mixService.getBookingsForTimeRange(bookings,
                currentDate, startTime, endTime);
        // Assert
        assertThat(bookingsInRange).hasSize(2);
        assertThat(bookingsInRange).containsExactly(
                booking("JohnDoe", "Meeting Room", 1,
                        getGermainDate(currentDate), "08:00", "09:30"),
                booking("JaneDoe", "Conference Room", 2,
                        getGermainDate(currentDate), "09:15", "10:30")
        );
    }

    @Test
    @DisplayName("Die SearchForRoom Methode gibt den korrekte Raum zurück")
    void test_3(){
        LocalDate currentDate = createValidDate();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);
        List<Booking> bookings = List.of(
                booking("JohnDoe", "Meeting Room", 1,
                        getGermainDate(currentDate), "08:00", "09:30"),
                booking("JaneDoe", "Conference Room", 2,
                        getGermainDate(currentDate), "09:15", "10:30"),
                booking("Alice", "Meeting Room", 3,
                        getGermainDate(currentDate), "10:00", "11:00"),
                booking("Bob", "Conference Room", 4,
                        getGermainDate(currentDate), "10:30", "12:00")
        );
        // Act
        List<Booking> bookingsInRange = mixService.getBookingsForTimeRange(bookings,
                currentDate, startTime, endTime);
        // Assert
        assertThat(bookingsInRange).hasSize(2);
        assertThat(bookingsInRange).containsExactly(
                booking("JohnDoe", "Meeting Room", 1,
                        getGermainDate(currentDate), "08:00", "09:30"),
                booking("JaneDoe", "Conference Room", 2,
                        getGermainDate(currentDate), "09:15", "10:30")
        );
    }


    @Test
    @DisplayName("Die getDescription Methode gibt bei einer nicht leeren Beschreibung das richtige Datum aus")
    void testGetDescription(){
        LocalDate date = LocalDate.of(2024, 2, 12);
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(10, 0);

        String expected = "12.02.2024  09:00->10:00";
        String result = MixService.getDescription(date, start, end);

        assertEquals(expected, result);
    }

    private LocalDate createValidDate(){
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                date.getDayOfWeek().equals(DayOfWeek.MONDAY))
            date = date.plusDays(1);
        return date;
    }

    private static String getGermainDate(LocalDate date) {
        return date.format(FORMATTER);
    }
}
