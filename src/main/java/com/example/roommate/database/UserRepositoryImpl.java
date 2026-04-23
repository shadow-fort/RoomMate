package com.example.roommate.database;

import com.example.roommate.application.service.repositories.UserRepository;
import com.example.roommate.database.dao.UserRepositoryDAO;
import com.example.roommate.database.dto.PersonDTO;
import com.example.roommate.domain.model.userAggregat.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    /**
     * Attribut für dependency injection.
     */
    private final UserRepositoryDAO userRepositoryDAO;

    /**
     * .
     * @param userReposDAO Instanz
     */
    public UserRepositoryImpl(
            final UserRepositoryDAO userReposDAO) {
        this.userRepositoryDAO = userReposDAO;
    }

    /**
     * .
     * @return eine Liste von User
     */
    public List<Person> findAll() {
        return userRepositoryDAO.findAll()
                .stream().map(this::convertPerson).toList();
    }

    /**
     * .
     * @param username schlüssel von GitHub
     * @return einer Person
     */
    @Override
    public Person findPersonByUsername(
            final String username) {
        PersonDTO personDTO =
                userRepositoryDAO.findPersonDTOByUsername(username);
        return new Person(
                personDTO.id(), personDTO.title(),
                personDTO.username(), personDTO.vorname(), personDTO.nachname()
        );
    }

    /**
     * .
     * @param person Objekt
     * @return einer Person
     */
    @Override
    public Person save(
            final Person person) {
        PersonDTO personDTO = new PersonDTO(
                person.getId(), person.getTitle(),
                person.getUsername(), person.getVorname(), person.getNachname()
        );
        PersonDTO personDTO1 = userRepositoryDAO.save(personDTO);
        return new Person(
                personDTO1.id(), personDTO1.title(),
                personDTO1.username(),
                personDTO1.vorname(),
                personDTO1.nachname());
    }

    /**
     * .
     * @param title der Titel des Nutzers
     * @param username schlüssel von GitHub
     * @param vorname persönliche Vorname
     * @param nachname persönliche Nachname
     */
    @Override
    public void updateUser(
            final String title,
            final String username,
            final String vorname,
            final String nachname) {
        userRepositoryDAO.updatePersonDTOByTitleAndVornameAndNachname(
                title, vorname, nachname, username);
    }

    /**
     * .
     * @param username schlüssel
     * @return Anzahle der Nutzer
     */
    @Override
    public int countPersonByUsername(
            final String username) {
        return userRepositoryDAO.countPersonDTOByUsername(username);
    }

    private Person convertPerson(
            final PersonDTO personDTO) {
        return new Person(personDTO.id(), personDTO.title(),
                personDTO.username(), personDTO.vorname(),
                personDTO.nachname());
    }
}
