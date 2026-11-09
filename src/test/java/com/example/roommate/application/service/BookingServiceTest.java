package com.example.roommate.application.service;

import com.example.roommate.application.service.repositories.BookingRepository;
import com.example.roommate.domain.model.bookingAggregat.Booking;
import com.example.roommate.domain.model.bookingAggregat.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.example.roommate.databaseTest.buider.TestBookingFactory.booking;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    BookingRepository bookingRepository = mock(BookingRepository.class);

    BookingService self;

    BookingService bookingService = new BookingService(bookingRepository, self);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
            "dd.MM.yyyy", Locale.GERMANY);


    @Test
    @DisplayName("Ein Booking kann hinzugefügt werden")
    void test_1(){
        LocalDate currentDate = createValidDate();
        Booking booking = booking("JohnDoe", "Meeting Room",
                1, getGermainDate(currentDate), "09:00", "10:00");
        when(bookingRepository.save(any())).thenReturn(booking);

        bookingService.addNewBooking("alntzpst", createValidDate(),
                LocalTime.of(11, 12), LocalTime.of(13, 15), 3, "Malibou Room");

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(captor.capture());
        Booking savedBooking = captor.getValue();
        assertThat(savedBooking.getUsername()).isEqualTo("alntzpst");
        assertThat(savedBooking.getDate()).isEqualTo(currentDate);
        assertThat(savedBooking.getStartTime()).isEqualTo(LocalTime.of(11, 12));
        assertThat(savedBooking.getEndTime()).isEqualTo(LocalTime.of(13, 15));
    }

    @Test
    @DisplayName("Die isBookingCollisionFree Methode gibt false zurück, wenn die StartTime eines Bookings liegt, in einer schon vorhandenen Booking")
    void test_2(){
        LocalDate currentDate = createValidDate();
        Booking booking = booking("JohnDoe", "Meeting Room", 1,
                getGermainDate(currentDate), "09:00", "10:00");
        when(bookingRepository.findBookingByRoomNameAndWorkplaceNumber(any(),
                anyInt())).thenReturn(List.of(booking));

        boolean isFree = bookingService.isBookingCollisionFree("Meeting Room", 1,
                currentDate, LocalTime.of(9, 12), LocalTime.of(13, 15));

        assertThat(isFree).isFalse();
    }

    @Test
    @DisplayName("Die isBookingCollisionFree Methode gibt false zurück, wenn die EndTime eines Bookings liegt, in einer schon vorhandenen Booking")
    void test_3(){
        LocalDate currentDate = createValidDate();
        Booking booking = booking("JohnDoe", "Meeting Room", 1,
                getGermainDate(currentDate), "09:00", "10:00");
        when(bookingRepository.findBookingByRoomNameAndWorkplaceNumber(any(),
                anyInt())).thenReturn(List.of(booking));

        boolean isFree = bookingService.isBookingCollisionFree("Meeting Room",
                1, currentDate,
                LocalTime.of(8, 12), LocalTime.of(9, 30));
        assertThat(isFree).isFalse();
    }

    @Test
    @DisplayName("Die isBookingCollisionFree Methode gibt false zurück, wenn ein vorhandene Booking liegt, in der bereich des Bookings")
    void test_4(){
        LocalDate currentDate = createValidDate();
        Booking booking = booking("JohnDoe", "Meeting Room",
                1, getGermainDate(currentDate), "09:00", "10:00");
        when(bookingRepository.findBookingByRoomNameAndWorkplaceNumber(any(),
                anyInt())).thenReturn(List.of(booking));

        boolean isFree = bookingService.isBookingCollisionFree("Meeting Room",
                1, currentDate,
                LocalTime.of(8, 30), LocalTime.of(10, 15));

        assertThat(isFree).isFalse();
    }

    @Test
    @DisplayName("Die isBookingCollisionFree Methode gibt true zurück, wenn kein vorhandene Booking liegt, in der bereich des Bookings")
    void test_5(){
        LocalDate currentDate = createValidDate();
        Booking booking = booking("JohnDoe", "Meeting Room",
                1, getGermainDate(currentDate), "09:00", "10:00");
        when(bookingRepository.findBookingByRoomNameAndWorkplaceNumber(any(),
                anyInt())).thenReturn(List.of(booking));

        boolean isFree = bookingService.isBookingCollisionFree("Meeting Room",
                1, currentDate,
                LocalTime.of(8, 30), LocalTime.of(9, 0));

        assertThat(isFree).isTrue();
    }


    @Test
    @DisplayName("Die getBookingsFromUser Methode gibt die richtigen Buchungen für einen Benutzer zurück")
    void test_6(){
        LocalDate currentDate = createValidDate();
        String username = "JohnDoe";
        when(bookingRepository.findBookingsByUsername(username)).thenReturn(
                List.of(
                        booking(username, "Meeting Room", 1
                                , getGermainDate(currentDate), "09:00", "10:00"),
                        booking(username, "Conference Room", 2,
                                getGermainDate(currentDate.plusDays(1)), "11:00", "12:00")
                )
        );

        List<Booking> bookings = bookingService.getBookingsFromUser(username);

        assertThat(bookings).hasSize(2);
        assertThat(bookings.get(0).getRoomName()).isEqualTo("Meeting Room");
        assertThat(bookings.get(1).getRoomName()).isEqualTo("Conference Room");
    }

    @Test
    @DisplayName("Die getAllCurrentBookings Methode gibt nur aktuelle Buchungen zurück")
    void test_7(){
        LocalDate currentDate = createValidDate();
        Booking currentBooking = booking("JohnDoe", "Meeting Room",
                1, getGermainDate(currentDate), "09:00", "10:00");
        Booking pastBooking = booking("JaneDoe", "Conference Room",
                2, "01.02.2024", "11:00", "12:00");

        when(bookingRepository.findAll()).thenReturn(
                List.of(currentBooking, pastBooking)
        );

        List<Booking> currentBookings = bookingService.getAllCurrentBookings();

        assertThat(currentBookings).containsExactly(currentBooking);
    }

    @Test
    @DisplayName("Die findBookingByBookingId Methode gibt die richtige Buchung zurück")
    void test_8(){
        Long bookingId = 1L;
        LocalDate currentDate = createValidDate();
        Booking expectedBooking = booking("JohnDoe", "Meeting Room",
                1, getGermainDate(currentDate), "09:00", "10:00");
        when(bookingRepository.findBookingById(bookingId)).thenReturn(expectedBooking);

        Booking foundBooking = bookingService.findBookingByBookingId(bookingId);

        assertThat(foundBooking).isEqualTo(expectedBooking);
    }

    @Test
    @DisplayName("Die updateBooking Methode ruft die updateBooking Methode des Repositories mit den richtigen Parametern auf")
    void test_9(){
        LocalDate currentDate = createValidDate();
        Booking bookingToUpdate = booking("JohnDoe", "Meeting Room", 1,
                getGermainDate(currentDate), "09:00", "10:00");
        LocalDate newDate = createValidDate().plusDays(2);
        LocalTime newStartTime = LocalTime.of(10, 30);
        LocalTime newEndTime = LocalTime.of(12, 30);

        bookingService.updateBooking(bookingToUpdate.getId(), newDate, newStartTime, newEndTime);

        verify(bookingRepository).updateBooking(bookingToUpdate.getId(), newDate, newStartTime, newEndTime);
    }
    @Test
    @DisplayName("Die deleteBooking Methode ruft die getAllBookings Methode des Repositories mit den richtigen Parametern auf")
    void test_10(){
        LocalDate currentDate = createValidDate();
        Booking bookingToDelete = booking("JohnDoe", "Meeting Room", 1,
                getGermainDate(currentDate), "09:00", "10:00");
        bookingService.deleteBooking(bookingToDelete.getId());

        assertThat(bookingService.getAllBookings()).isEmpty();
    }

    @Test
    @DisplayName("Die isBookingCollisionFree Methode gibt false zurück, wenn ein vorhandene Booking liegt, in der bereich des Bookings")
    void test_11(){
        LocalDate currentDate = createValidDate();
        Booking booking = booking("JohnDoe", "Meeting Room",
                1, getGermainDate(currentDate), "09:00", "10:00");
        when(bookingRepository.findBookingByRoomNameAndWorkplaceNumber(any(),
                anyInt())).thenReturn(List.of(booking));

        boolean isFree = bookingService.isBookingCollisionFree("Meeting Room",
                1, currentDate,
                LocalTime.of(9, 30 ), LocalTime.of(9, 45));

        assertThat(isFree).isFalse();
    }

    @Test
    @DisplayName("Die isBookingCollisionFree Methode gibt false zurück, wenn ein vorhandene Booking hat die gleiche " +
            "StartTime und EndTime als die neue Booking")
    void test_12(){
        LocalDate currentDate = createValidDate();
        Booking booking = booking("JohnDoe", "Meeting Room",
                1, getGermainDate(currentDate), "09:00", "10:00");
        when(bookingRepository.findBookingByRoomNameAndWorkplaceNumber(any(),
                anyInt())).thenReturn(List.of(booking));

        boolean isFree = bookingService.isBookingCollisionFree("Meeting Room",
                1, currentDate,
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertThat(isFree).isFalse();
    }

    @Test
    @DisplayName("Eine Liste von Booking kann ausgegeben anhand der Raum Name und Arbeitsplatz")
    void test_13(){
        LocalDate currentDate = createValidDate();
        Booking booking = booking("JohnDoe", "Meeting Room",
                1, getGermainDate(currentDate), "09:00", "10:00");
        when(bookingRepository.findBookingByRoomNameAndWorkplaceNumber(any(),
                anyInt())).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.findBookingByRoomNameAndWorkplaceNumber("Meeting Room",
                1);

        assertThat(bookings.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Die doAddBooking Methode gibt eine RuntimeException zurück, wenn die Hinzufügung fehlschlägt")
    void test_14(){
        String username = "JohnDoe";
        LocalDate date = createValidDate();
        LocalTime startTime = LocalTime.of(11, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        int workplaceNumber = 1;
        String roomName = "Meeting Room";

        doThrow(OptimisticLockingFailureException.class).when(mock(BookingService.class))
                .addNewBooking(username, date, startTime, endTime, workplaceNumber, roomName);

        assertThrows(RuntimeException.class, () ->
                bookingService.doAddBooking(username, date, startTime, endTime, workplaceNumber, roomName));
    }

    @Test
    @DisplayName("Die getDescription Methode gibt bei einer nicht leeren Beschreibung das richtige Datum aus")
    void testIsBookingCollisionFreeForUpdate(){
        Period period = new Period(LocalDate.of(2024, 2, 12),
                LocalTime.of(11, 0), LocalTime.of(12, 0));
        Booking booking1 = new Booking(1L, "mspring", "IT Room", 1, period);
        Booking booking2 = new Booking(2L, "mspring", "IT Room", 1, period);
        Booking booking3 = new Booking(3L, "mspring", "IT Room", 1, period);

        List<Booking> expected = Arrays.asList(booking1, booking2, booking3);
        when(bookingRepository.findBookingByRoomNameAndWorkplaceNumber("IT Room", 1)).thenReturn(expected);

        boolean notcolfree = bookingService.isBookingCollisionFreeForUpdate(4L, "IT Room",
                1, LocalDate.of(2024,2, 12),
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertThat(notcolfree).isFalse();
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
