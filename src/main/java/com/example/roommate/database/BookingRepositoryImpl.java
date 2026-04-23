package com.example.roommate.database;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.roommate.application.service.repositories.BookingRepository;
import com.example.roommate.database.dao.BookingRepositoryDAO;
import com.example.roommate.database.dto.BookingDTO;
import com.example.roommate.database.dto.PeriodDTO;
import com.example.roommate.domain.model.bookingAggregat.Booking;
import com.example.roommate.domain.model.bookingAggregat.Period;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@Repository
public class BookingRepositoryImpl  implements BookingRepository {

    /**
     * Attribut für dependency injection.
     */
    private final BookingRepositoryDAO bookingRepositoryDAO;

    /**
     * .
     * @param bookingReposDAO Instanz
     */
    public BookingRepositoryImpl(
            final BookingRepositoryDAO bookingReposDAO) {
        this.bookingRepositoryDAO = bookingReposDAO;
    }

    /**
     * .
     * @return eine Liste von Booking
     */

    @Override
    public List<Booking> findAll() {
        List<BookingDTO> bookingDTO = bookingRepositoryDAO.findAll();
        return bookingDTO.stream().map(this::convertBooking).toList();
    }

    /**
     * .
     * @param bookingId Schlüssel
     * @return ein Booking
     */
    @Override
    public Booking findBookingById(final Long bookingId) {
        BookingDTO bookingDTO =
                bookingRepositoryDAO.findBookingDTOById(bookingId);
        Period period = convertPeriod(bookingDTO.periodDTO());
        return new Booking(
                bookingDTO.id(), bookingDTO.username(),
                bookingDTO.roomName(), bookingDTO.workplaceNumber(),
                period
        );
    }

    /**
     * .
     * @param username Schlüssel
     * @return eine optionale Liste von Booking
     */
    @Override
    public Optional<Booking> findBookingByUsername(
            final String username) {
        return bookingRepositoryDAO
                .findBookingDTOByUsername(username).map(this::convertBooking);
    }

    /**
     * .
     * @param roomName Name des Raums
     * @param workplaceNumber Nummer des Platzes
     * @return eine Liste von Booking
     */
    @Override
    public List<Booking> findBookingByRoomNameAndWorkplaceNumber(
            final String roomName,
            final int workplaceNumber) {
        return bookingRepositoryDAO
                .findBookingDTOByRoomNameAndWorkplaceNumber(
                        roomName, workplaceNumber)
                .stream().map(this::convertBooking).toList();
    }

    /**
     * .
     * @param username Schlüssel
     * @return eine Liste von Booking
     */
    @Override
    public List<Booking> findBookingsByUsername(
            final String username) {
      return bookingRepositoryDAO.findBookingsDTOByUsername(username)
              .stream().map(this::convertBooking).toList();
    }

    /**
     * .
     * @param booking Objekt
     */
    @Override
    public void delete(final Booking booking) {
        PeriodDTO periodDTO = convertPeriodDTO(booking.getPeriod());
        BookingDTO bookingDTO = new BookingDTO(
                booking.getId(), booking.getUsername(), booking.getRoomName(),
                booking.getWorkplaceNumber(), periodDTO);
        bookingRepositoryDAO.delete(bookingDTO);
    }

    /**
     * .
     * @param bookingId Schlüssel
     */
    @Override
    public void deleteBookingById(
            final Long bookingId) {
        bookingRepositoryDAO.deleteById(bookingId);
    }

    /**
     * .
     * @param booking Objekt
     * @return ein Booking
     */
    @Override
    public Booking save(final Booking booking) {
        PeriodDTO periodDTO = convertPeriodDTO(booking.getPeriod());
        BookingDTO bookingDTO = new BookingDTO(
                booking.getId(), booking.getUsername(), booking.getRoomName(),
                booking.getWorkplaceNumber(), periodDTO);
        BookingDTO savedBooking = bookingRepositoryDAO.save(bookingDTO);
        return convertBooking(savedBooking);
    }

    /**
     * .
     * @param id Schlüssel
     * @param date Datum
     * @param startTime Beginn
     * @param endTime End
     */
    @Override
    public void updateBooking(
            final Long id,
            final LocalDate date,
            final LocalTime startTime,
            final LocalTime endTime) {
        bookingRepositoryDAO.updateBookingDTOByBookingId(
                id, date, startTime, endTime);
    }

    /**
     * .
     * @param bookingDTO Objekt
     * @return ein Booking
     */
    private Booking convertBooking(
            final BookingDTO bookingDTO) {
        Period period = convertPeriod(bookingDTO.periodDTO());
        return new Booking(
                bookingDTO.id(), bookingDTO.username(), bookingDTO.roomName(),
                bookingDTO.workplaceNumber(), period);
    }

    private Period convertPeriod(
            final PeriodDTO periodDTO) {
        return new Period(
                periodDTO.date(),
                periodDTO.startTime(), periodDTO.endTime());
    }

    private PeriodDTO convertPeriodDTO(
            final Period period) {
        return new PeriodDTO(
                period.date(),
                period.startTime(), period.endTime());
    }
}