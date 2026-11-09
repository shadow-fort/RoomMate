package com.example.roommate.databaseTest;


import com.example.roommate.application.service.repositories.BookingRepository;
import com.example.roommate.database.BookingRepositoryImpl;
import com.example.roommate.database.dao.BookingRepositoryDAO;
import com.example.roommate.domain.model.bookingAggregat.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.example.roommate.databaseTest.buider.TestBookingFactory.booking;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookingRepositoryImplTest {

    @Autowired
    BookingRepositoryDAO bookingRepositoryDAO;

    BookingRepository bookingRepository;

    @BeforeEach
    void setUp(){ bookingRepository = new BookingRepositoryImpl(bookingRepositoryDAO);
    }

    @Test
    void testSaveAndFindAll() {
        // Save a booking
        Booking booking = booking("JohnDoe", "Meeting Room", 1, "05.06.2024", "09:00", "10:00");
        Booking savedBooking = bookingRepository.save(booking);
        Optional<Booking> loadedBooking = bookingRepository.findBookingByUsername(savedBooking.getUsername());
        assertThat(loadedBooking).contains(savedBooking);
    }

    @Test
    void testFindBookingDTOByUsername() {
        // Save a booking
        Booking booking = booking("JaneSmith", "Conference Room", 2, "05.06.2024", "10:00", "11:00");
        Booking savedBooking = bookingRepository.save(booking);

        // Retrieve the booking by username and check if it matches the saved booking
        Optional<Booking> retrievedBooking = bookingRepository.findBookingByUsername("JaneSmith");
        assertFalse(retrievedBooking.isEmpty());
        assertEquals(savedBooking, retrievedBooking.get());
    }

    @Test
    void testDelete() {
        // Save a booking
        Booking booking = booking("BobJohnson", "Office Room", 3,  "05.06.2024", "11:00", "12:00");
        Booking savedBooking = bookingRepository.save(booking);

        // Delete the booking and check if it's no longer present in the repository
        bookingRepository.delete(savedBooking);
        List<Booking> allBookings = bookingRepository.findAll();
        assertFalse(allBookings.contains(savedBooking));
    }

    @Test
    void testDeleteById() {
        // Save a booking
        Booking booking = booking("BobJo", "Office Room", 6,  "05.06.2024", "11:00", "12:00");
        Booking savedBooking = bookingRepository.save(booking);

        // Delete the booking and check if it's no longer present in the repository
        bookingRepository.deleteBookingById(savedBooking.getId());
        List<Booking> allBookings = bookingRepository.findAll();
        assertFalse(allBookings.contains(savedBooking));
    }

    @Test
    void testUpdateBooking() {
        // Save a booking
        Booking booking = booking("AliceWilliams", "Study Room", 4, "05.06.2024", "13:00", "14:00");
        Booking savedBooking = bookingRepository.save(booking);

        // Update the booking's time
        LocalTime newStartTime = LocalTime.of(14, 0);
        LocalTime newEndTime = LocalTime.of(15, 0);
        bookingRepository.updateBooking(savedBooking.getId(), savedBooking.getDate(), newStartTime, newEndTime);

        // Retrieve the updated booking and check if the times match
        Optional<Booking> updatedBooking = bookingRepository.findBookingByUsername("AliceWilliams");
        assertFalse(updatedBooking.isEmpty());
        assertEquals(newStartTime, updatedBooking.get().getStartTime());
        assertEquals(newEndTime, updatedBooking.get().getEndTime());
    }

    @Test
    void testFindBookingById() {
        // Save a booking
        Booking booking = booking("BobJo", "Office Room", 6,  "05.06.2024", "11:00", "12:00");
        Booking savedBooking = bookingRepository.save(booking);

        // Find the booking
        Booking findBooking = bookingRepository.findBookingById(savedBooking.getId());

        //Check if the find Booking is correct
        assertThat(findBooking.getUsername()).isEqualTo("BobJo");
        assertThat(findBooking.getRoomName()).isEqualTo("Office Room");
        assertThat(findBooking.getWorkplaceNumber()).isEqualTo(6);
        assertThat(findBooking.getDate()).isEqualTo(LocalDate.of(2024, 6, 5));
        assertThat(findBooking.getStartTime()).isEqualTo(LocalTime.of(11,0));
        assertThat(findBooking.getEndTime()).isEqualTo(LocalTime.of(12, 0));
    }

    @Test
    void testFindBookingByRoomNameAndWorkplaceNumber() {
        // Save a booking
        Booking booking = booking("BobJo", "Office Room", 6,  "05.06.2024", "11:00", "12:00");
        Booking booking_1 = booking("BobJo", "Office Room", 4,  "08.04.2024", "11:00", "12:00");
        Booking booking_2 = booking("BobJo", "Office Room", 6,  "15.06.2024", "11:00", "12:00");
        Booking booking_3 = booking("BobJo", "Office Room", 6,  "25.08.2024", "11:00", "12:00");
        Booking booking_4 = booking("BobJo", "Ofice Room", 6,  "25.08.2024", "11:00", "12:00");
        bookingRepository.save(booking);
        bookingRepository.save(booking_1);
        bookingRepository.save(booking_2);
        bookingRepository.save(booking_3);
        bookingRepository.save(booking_4);

        // Find the booking
        List<Booking> findBookings = bookingRepository.findBookingByRoomNameAndWorkplaceNumber("Office Room", 6);

        //Check if the find Booking is correct
        assertThat(findBookings.size()).isEqualTo(3);

    }

    @Test
    void testFindBookingsByUsername() {
        // Save a booking
        Booking booking = booking("BobJo", "Office Room", 6,  "05.06.2024", "11:00", "12:00");
        Booking booking_1 = booking("BobJo", "Office Room", 4,  "08.04.2024", "11:00", "12:00");
        Booking booking_2 = booking("Milosgd", "Office Room", 6,  "15.06.2024", "11:00", "12:00");
        Booking booking_3 = booking("vhztr", "Office Room", 6,  "25.08.2024", "11:00", "12:00");
        Booking booking_4 = booking("trznn", "Ofice Room", 6,  "25.08.2024", "11:00", "12:00");
        bookingRepository.save(booking);
        bookingRepository.save(booking_1);
        bookingRepository.save(booking_2);
        bookingRepository.save(booking_3);
        bookingRepository.save(booking_4);

        // Find the booking
        List<Booking> findBookings = bookingRepository.findBookingsByUsername("BobJo");

        //Check if the find Booking is correct
        assertThat(findBookings.size()).isEqualTo(2);

    }

}
