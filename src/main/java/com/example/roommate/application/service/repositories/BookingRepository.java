package com.example.roommate.application.service.repositories;

import com.example.roommate.domain.model.bookingAggregat.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    /**
     * .
     * @return eine Liste von Booking
     */
    List<Booking> findAll();

    /**
     * .
     * @param bookingId schlüssel
     * @return ein Booking
     */
    Booking findBookingById(Long bookingId);

    /**
     * .
     * @param username von GitHub
     * @return eine optionale Liste von Booking
     */
    Optional<Booking> findBookingByUsername(String username);

    /**
     * .
     * @param roomName Name des Raums
     * @param workplaceNumber Nummer eines Platzes
     * @return eine Liste von Booking
     */
    List<Booking> findBookingByRoomNameAndWorkplaceNumber(
            String roomName, int workplaceNumber);

    /**
     * .
     * @param username von GitHub
     * @return eine Liste von Booking
     */
    List<Booking> findBookingsByUsername(String username);

    /**
     * .
     * @param booking Objekt
     */
    void delete(Booking booking);

    /**
     * .
     * @param bookingId Schlüssel
     */
    void deleteBookingById(Long bookingId);

    /**
     * .
     * @param booking Objekt
     * @return ein Booking
     */
    Booking save(Booking booking);

    /**
     * .
     * @param id Schlüssel
     * @param date Datum
     * @param startTime Beginn
     * @param endTime Ende
     */
    void updateBooking(
            Long id, LocalDate date, LocalTime startTime, LocalTime endTime);





}
