package com.example.roommate.database.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("booking")
public final class BookingDTO {

    /**
     * Attribut Schlüssel.
     */
    @Id
    private final Long id;

    /**
     * .
     * @return Version Nummer
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * counter der optimistische Locking.
     */
    @Version
    private Integer version;

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
     * Attribut End.
     */
    private final PeriodDTO periodDTO;

    /**
     * .
     * @param id Schlüssel
     * @param version Instanz
     * @param username Instanz
     * @param roomName Instanz
     * @param workplaceNumber Instanz
     * @param periodDTO Instanz
     */
    @SuppressWarnings("checkstyle:HiddenField")
    @PersistenceCreator
    public BookingDTO(
            final Long id,
            final Integer version,
            final String username,
            final String roomName,
            final int workplaceNumber,
            final PeriodDTO periodDTO) {
        this.id = id;
        this.version = version;
        this.username = username;
        this.roomName = roomName;
        this.workplaceNumber = workplaceNumber;
        this.periodDTO = periodDTO;
    }

    /**
     * .
     * @param id Instanz
     * @param username Instanz
     * @param roomname Instanz
     * @param workplaceNumber Instanz
     * @param periodDTO Instanz
     */
    public BookingDTO(
            final Long id,
            final String username,
            final String roomname,
            final int workplaceNumber,
            final PeriodDTO periodDTO) {
        this.id = id;
        this.username = username;
        this.roomName = roomname;
        this.workplaceNumber = workplaceNumber;
        this.periodDTO = periodDTO;
    }

    /**
     * .
     *
     * @param o the reference object with which to compare.
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
        BookingDTO that = (BookingDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * .
     * @return Schlüssel
     */
    @Id
    public Long id() {
        return id;
    }

    /**
     * .
     * @return username
     */
    public String username() {
        return username;
    }

    /**
     * .
     * @return Name desRaums
     */
    public String roomName() {
        return roomName;
    }

    /**
     * .
     * @return Nummer des Platzes
     */
    public int workplaceNumber() {
        return workplaceNumber;
    }

    /**
     * .
     * @return Objekt Zeitraum
     */
    public PeriodDTO periodDTO() {
        return periodDTO;
    }

    @Override
    public String toString() {
        return "BookingDTO["
                +
                "id=" + id + ", "
                +
                "username=" + username + ", "
                +
                "roomName=" + roomName + ", "
                +
                "workplaceNumber=" + workplaceNumber + ", "
                +
                "date=" + periodDTO + ']';
    }

}