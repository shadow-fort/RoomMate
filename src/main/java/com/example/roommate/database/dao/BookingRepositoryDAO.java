package com.example.roommate.database.dao;

import com.example.roommate.database.dto.BookingDTO;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;



public interface BookingRepositoryDAO extends CrudRepository<BookingDTO, Long> {

    /**
     * Gibt uns eine Liste von objekten BookingDTO von der Database.
     * @return List<BookingDTO>
     */
    List<BookingDTO> findAll();

    /**
     * Gibt uns ein objekte BookingDTO von der Datenbank.
     * @param id for Booking
     * @return BookingDTO
     */
    BookingDTO findBookingDTOById(Long id);

    /**
     * Gibt uns eine optionale Liste von objekten BookingDTO von der Database.
     * @param username von GitHub
     * @return Optional<BookingDTO>
     */
    Optional<BookingDTO> findBookingDTOByUsername(String username);

    /**
     * Gibt uns eine Liste von objekte BookingDTO von der Database anhand von
     * username.
     * @param username von GitHub
     * @return List<BookingDTO>
     */
    List<BookingDTO> findBookingsDTOByUsername(String username);

    /**
     * Löscht ein Booking der Datenbank.
     * @param bookingDTO ein objekt
     */
    void delete(BookingDTO bookingDTO);


    /**Löscht ein Booking der Datenbank anhand sein Id.
     * @param bookingId id der Objekt
     */
    void deleteById(Long bookingId);

    /**
     * Speichert ein Booking in der Datenbank.
     * @param bookingDTO ein Objekt
     * @return BookingDTO
     */
    BookingDTO save(BookingDTO bookingDTO);

     /**
     * Gibt uns ein Objekte BookingDTO von der Datenbank.
     * @param roomName der Name des Raums
     * @param workplaceNumber der Platz in Raum
     * @return Lis<BookingDTO>
     */
    List<BookingDTO> findBookingDTOByRoomNameAndWorkplaceNumber(
            String roomName, int workplaceNumber);

    /**
     * Führen wir eine Änderung an das Booking in der Datenbank.
     * @param id id das Booking
     * @param date Datum des Booking
     * @param startTime Beginn des Booking
     * @param endTime Ende des Bookings
     */
    @Modifying
    @Query("update period set date = :date, "
            +
            "start_time = :startTime, end_time = :endTime where booking = :id")
    void updateBookingDTOByBookingId(
            @Param("id") Long id,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
