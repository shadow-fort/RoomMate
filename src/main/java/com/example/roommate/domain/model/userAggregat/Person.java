package com.example.roommate.domain.model.userAggregat;

import com.example.roommate.config.annotation.AggregateRoot;

import java.util.Objects;

@AggregateRoot
public class Person {

    /**
     * Attribut Schlüssel.
     */
    private final Long id;

    /**
     * Attribut Titel.
     */
    private final String title;

    /**
     * Attribut username.
     */
    private final String username;

    /**
     * Attribut Vorname.
     */
    private final String vorname;

    /**
     * Attribut Nachname.
     */
    private final String nachname;

    /**
     * .
     * @param userId Schlüssel
     * @param userTitle Instanz
     * @param userName Instanz
     * @param vorName Instanz
     * @param nachName Instanz
     */
    public Person(
            final Long userId,
            final String userTitle,
            final String userName,
            final String vorName,
            final String nachName) {
        this.id = userId;
        this.title = userTitle;
        this.username = userName;
        this.vorname = vorName;
        this.nachname = nachName;
    }

    /**
     * .
     * @param userTitle Instanz
     * @param userName Instanz
     * @param vorName Instanz
     * @param nachName Instanz
     */
    public Person(
            final String userTitle,
            final String userName,
            final String vorName,
            final String nachName) {
        this(null, userTitle, userName, vorName, nachName);
    }

    /**
     * .
     * @param userName Instanz
     */
    public Person(final String userName) {
        this(null, null,
                userName, null, null);
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
     * @return Titel
     */
    public String getTitle() {
        return title;
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
     * @return Vorname
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * .
     * @return Nachname
     */
    public String getNachname() {
        return nachname;
    }

    /**
     * .
     * @return String
     */
    @Override
    public String toString() {
        return "Person{"
                +
                "id=" + id
                +
                ", title='" + title + '\''
                +
                ", username='" + username + '\''
                +
                ", vorname='" + vorname + '\''
                +
                ", nachname='" + nachname + '\''
                +
                '}';
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
        Person person = (Person) o;
        return Objects.equals(id, person.id)
                && Objects.equals(title, person.title)
                && Objects.equals(username, person.username)
                && Objects.equals(vorname, person.vorname)
                && Objects.equals(nachname, person.nachname);
    }

    /**
     * .
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, username, vorname, nachname);
    }
}
