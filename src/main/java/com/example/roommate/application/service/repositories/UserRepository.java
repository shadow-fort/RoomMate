package com.example.roommate.application.service.repositories;

import com.example.propra.roommate.domain.model.userAggregat.Person;

import java.util.List;

public interface UserRepository {

    /**
     * .
     * @return eine Liste von User
     */
    List<Person> findAll();

    /**
     * .
     * @param username schlüssel von GitHub
     * @return ein Nutzer
     */
    Person findPersonByUsername(String username);

    /**
     * .
     * @param person Objekt
     * @return ein Nutzer
     */
    Person save(Person person);

    /**
     * .
     * @param title der Titel des Nutzers
     * @param username schlüssel von GitHub
     * @param vorname persönliche Vorname
     * @param nachname persönliche Nachname
     */
    void updateUser(
            String title, String username, String vorname, String nachname);

    /**
     * .
     * @param username schlüssel
     * @return anzahle der Nutzer
     */
    int countPersonByUsername(String username);
}