package com.example.roommate.application.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.roommate.application.service.repositories.BookingRepository;
import com.example.roommate.domain.model.bookingAggregat.Booking;
import com.example.roommate.domain.model.bookingAggregat.Period;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@Service
public class BookingService {

    /**
     * Attribut für dependency injection.
     */
   private final BookingRepository bookingRepository;

    /**
     * Attribut für selbst injection der Klasse.
     */
   private final BookingService self;
    /**
     * Konstanz für optimistische Locking.
     */
    private static final int FAIL_MAX = 4;


    /**
     * .
     * @param bookingRepos Instanz
     * @param sel Instanz
     */
    public BookingService(
            final BookingRepository bookingRepos,
            final @Lazy BookingService sel) {
        this.bookingRepository = bookingRepos;
        this.self = sel;
    }

    /**
     * .
     * @param username Schlüssel
     * @return eine Liste von Booking
     */
    public List<Booking> getBookingsFromUser(
            final String username) {
       return bookingRepository.findBookingsByUsername(username)
               .stream()
               .sorted(Comparator.comparing(Booking::getDate)
                       .thenComparing(Booking::getStartTime)
                       .thenComparing(Booking::getEndTime))
               .toList();
    }

    /**
     *
     * @return gibt alle Booking nach Datum, StartTime und EndeTime sortiert
     */

    public List<Booking> getAllBookings() {

        return bookingRepository.findAll().stream()
                .sorted(Comparator.comparing(Booking::getDate)
                                .thenComparing(Booking::getStartTime)
                                .thenComparing(Booking::getEndTime)
                ).toList();
    }

    /**
     * .
     * @return eine Liste von Booking, die noch nicht abgelaufen sind
     */
    public List<Booking> getAllCurrentBookings() {
        return getAllBookings().stream()
                .filter(booking -> booking.getDateAndEndTime()
                        .isAfter(LocalDateTime.now()))
                .toList();
    }

    /**
     * .
     * @param username variable
     * @param date variable
     * @param startTime variable
     * @param endTime variable
     * @param workplaceNumber variable
     * @param roomName variable
     */
    public void doAddBooking(
            final String username,
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime,
            final int workplaceNumber,
            final String roomName) {
        boolean successfull = false;
        int failcount = 0;
        while (!successfull && failcount < FAIL_MAX) {
            try {
                self.addNewBooking(
                        username, date, startTime, endTime,
                workplaceNumber, roomName);
                successfull = true;
            } catch (OptimisticLockingFailureException ex) {
                failcount++;
            }
        }
        if (!successfull) {
            throw new RuntimeException("Hinzufügung fehlgeschlagen");
        }
    }

    /**
     * .
     * @param username Schlüssel
     * @param date variable
     * @param startTime variable
     * @param endTime variable
     * @param workplaceNumber variable
     * @param roomName variable
     */
    @Transactional
    public void addNewBooking(
            final String username,
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime,
            final int workplaceNumber,
            final String roomName) {
        Booking booking = new Booking(
                username, roomName, workplaceNumber,
                new Period(date, startTime, endTime));
        bookingRepository.save(booking);
    }

    /**
     * .
     * @param roomName Schlüssel
     * @param workplaceNumber Nummer des Platzes
     * @return ein Booking
     */
    public List<Booking> findBookingByRoomNameAndWorkplaceNumber(
            final String roomName,
            final int workplaceNumber) {
            return bookingRepository.findBookingByRoomNameAndWorkplaceNumber(
                roomName, workplaceNumber).stream()
                .filter(b -> !b.getDate().isBefore(LocalDate.now()))
                        .sorted(Comparator.comparing(Booking::getDate)
                                .thenComparing(Booking::getStartTime)
                                .thenComparing(Booking::getEndTime))
                .toList();
    }

    /**
     * .
     * @param bookingId Schlüssel
     * @param dateBooking variable
     * @param startBooking variable
     * @param endBooking variable
     */
    public void updateBooking(
            final Long bookingId,
            final LocalDate dateBooking,
            final LocalTime startBooking,
            final LocalTime endBooking) {
        bookingRepository.updateBooking(
                bookingId, dateBooking, startBooking, endBooking);
    }

    /**
     * .
     * @param bookingId Schlüssel
     */
    public void deleteBooking(
            final Long bookingId) {
        bookingRepository.deleteBookingById(bookingId);
    }

    /**
     * .
     * @param roomName variable
     * @param workplaceNumber variable
     * @param date variable
     * @param startTime variable
     * @param endTime variable
     * @return true oder false
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
     * @param bookingId Schlüssel
     * @param roomName variable
     * @param workplaceNumber variable
     * @param date variable
     * @param startTime variable
     * @param endTime variable
     * @return true oder false
     */
    public boolean isBookingCollisionFreeForUpdate(
            final Long bookingId,
            final String roomName,
            final int workplaceNumber,
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime) {
        List<Booking> bookings =
                bookingRepository
                        .findBookingByRoomNameAndWorkplaceNumber(
                        roomName, workplaceNumber);
        System.out.println(bookings);
        var bookingToCheck = bookings.stream()
                .filter(b -> !(b.getId().equals(bookingId))
                        && b.getDate().equals(date)).toList();
        System.out.println(bookingToCheck);
        return !validatorForCollision(bookingToCheck, startTime, endTime);
    }

    /**
     * .
     * @param bookings variable
     * @param startTime variable
     * @param endTime variable
     * @return true oder false
     */
    public boolean validatorForCollision(
            final List<Booking> bookings,
            final LocalTime startTime,
            final LocalTime endTime) {
        return fixDuplicate(bookings, startTime, endTime);
    }

    static boolean fixDuplicate(
            final List<Booking> bookings,
            final LocalTime startTime,
            final LocalTime endTime) {
        for (Booking booking : bookings) {
            if (booking.getStartTime().isBefore(startTime)
                    && booking.getEndTime().isAfter(startTime)) {
                return false;
            } else if (booking.getStartTime().isBefore(endTime)
                    && booking.getEndTime().isAfter(endTime)) {
                return false;
            } else if (startTime.isBefore(booking.getStartTime())
                    && endTime.isAfter(booking.getStartTime())) {
                return false;
            } else if (startTime.isBefore(booking.getEndTime())
                    && endTime.isAfter(booking.getEndTime())) {
                return false;
            } else if (startTime.equals(booking.getStartTime())
                    && endTime.equals(booking.getEndTime())) {
                return false;
            }
        }
        return true;
    }


    /**
     * .
     * @param bookingId Schlüssel
     * @return ein Booking
     */
    public Booking findBookingByBookingId(
            final Long bookingId) {
        return bookingRepository.findBookingById(bookingId);
    }
}
