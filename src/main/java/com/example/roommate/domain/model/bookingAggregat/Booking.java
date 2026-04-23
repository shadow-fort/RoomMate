package com.example.roommate.domain.model.bookingAggregat;

import com.example.roommate.config.annotation.AggregateRoot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

@AggregateRoot
public class Booking {

    /**
     * Attribut Schlüssel.
     */
    private final Long id;

    /**
     * Attribut username.
     */
    private final String username;

    /**
     * Attribut Name des Raums.
     */
    private final String roomName;

    /**
     * Attribut Nummer des Platzes.
     */
    private final int workplaceNumber;

    /**
     * Attribut Objekt Zeitraum.
     */
    private final Period period;

    /**
     * .
     * Attribut Formatter
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("dd.MM.yyyy", Locale.GERMANY);


    /**
     * .
     * @param bookingId Schlüssel
     * @param userName Instanz
     * @param roomsName Instanz
     * @param workplacesNumber Instanz
     * @param periode Instanz
     */
    public Booking(
            final Long bookingId,
            final String userName,
            final String roomsName,
            final int workplacesNumber,
            final Period periode) {
        this.id = bookingId;
        this.username = userName;
        this.roomName = roomsName;
        this.workplaceNumber = workplacesNumber;
        this.period = periode;
    }

    /**
     * .
     * @param userName Instanz
     * @param roomsName Instanz
     * @param workplacesNumber Instanz
     * @param periode Instanz
     */
    public Booking(
            final String userName,
            final String roomsName,
            final int workplacesNumber,
            final Period periode) {
        this(null, userName, roomsName,
                workplacesNumber, periode);
    }

    /**
     * .
     * @return Schlüssel Id
     */
    public Long getId() {
        return id;
    }

    /**
     * .
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * .
     * @return Name des Raums
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * .
     * @return Nummer des Platzes
     */
    public int getWorkplaceNumber() {
        return workplaceNumber;
    }

    /**
     * .
     * @return Datum
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * .
     * @return Datum mit deutschem Format
     */
    public String getGermainDate() {
        return period.date().format(FORMATTER);
    }


    /**
     * .
     * @return Datum
     */
    public LocalDate getDate() {
        return period.date();
    }

    /**
     * .
     * @return Beginn
     */
    public LocalTime getStartTime() {
        return period.startTime();
    }

    /**
     * .
     * @return End
     */
    public LocalTime getEndTime() {
        return period.endTime();
    }

    /**
     * .
     * @return ein LocalDateTime vom Date and endTime
     */
    public LocalDateTime getDateAndEndTime() {
        return period.date().atTime(period.endTime());
    }
    /**
     * .
     * @param o Objekt referenz
     * @return true or false
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Booking booking = (Booking) o;
        return workplaceNumber == booking.workplaceNumber
                && Objects.equals(id, booking.id)
                && Objects.equals(username, booking.username)
                && Objects.equals(roomName, booking.roomName)
                && Objects.equals(period, booking.period);
    }

    /**
     * .
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                id, username, roomName,
                workplaceNumber, period);
    }

    /**
     * .
     * @return String
     */
    @Override
    public String toString() {
        return "Booking{"
                +
                "id=" + id
                +
                ", username='" + username + '\''
                +
                ", roomName='" + roomName + '\''
                +
                ", workplaceNumber=" + workplaceNumber
                +
                ", period=" + period
                +
                '}';
    }

    /**
     * .
     * @return ein toString Format for Booking
     */
    public String getBooking() {
        return period.date() + "->"
                + period.startTime() + "-" + period.endTime();
    }
}