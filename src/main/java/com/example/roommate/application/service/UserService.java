package com.example.roommate.application.service;


import com.example.roommate.application.service.repositories.UserRepository;
import com.example.roommate.domain.model.userAggregat.Person;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    /**
     * Attribut für dependency injection.
     */
    private final UserRepository userRepository;

    /**
     * Konstruktor der Klasse.
     * @param userRepos Instanz
     */
    public UserService(
            final UserRepository userRepos) {
        this.userRepository = userRepos;
    }

    /**
     * .
     * @return eine Liste von aller Usern
     */
    public List<Person> findAllUser() {
        return userRepository.findAll();
    }

    /**
     * .
     * @return eine Liste von User Name
     */
    public List<String> getAllUserName() {
        return findAllUser().stream()
                .map(Person::getUsername)
                .toList();
    }

    /**
     * .
     * @param username variable
     */
    public void addUser(
            final String username) {
        userRepository.save(new Person(username));
    }

    /**
     * .
     * @param username variable
     * @return ein Nutzer
     */
    public Person findUserByUsername(
            final String username) {
        return userRepository.findPersonByUsername(username);
    }

    /**
     * .
     * @param user variable
     * @return user voll Name oder nur Vorname
     */
    public String greetUser(
            final String user) {
        String users = user;
        if (userRepository.countPersonByUsername(users) == 1) {
            Person person = userRepository.findPersonByUsername(user);
            if (person.getTitle() != null) {
                if (!person.getTitle().isBlank()) {
                    users = person.getTitle() + " "
                            + person.getVorname() + " " + person.getNachname();
                } else {
                    users = person.getVorname();
                }
            } else if (person.getVorname() != null) {
                users = person.getVorname();
            }
        }
        return users;
    }

    /**
     * .
     * @param username variable
     * @return true or false
     */
    public boolean isUserPresent(
            final String username) {
        return userRepository.countPersonByUsername(username) == 1;
    }

    /**
     * .
     * @param title variable
     * @param username variable
     * @param vorname variable
     * @param nachname variable
     */
    public void modifyUser(
            final String title,
            final String username,
            final String vorname,
            final String nachname) {
        userRepository.updateUser(title, username, vorname, nachname);
    }

    /**
     * .
     * @param user variable
     */
    public void addUserIfNotPresent(
            final String user
    ) {
        if (!isUserPresent(user)) {
            addUser(user);
        }
    }

}
