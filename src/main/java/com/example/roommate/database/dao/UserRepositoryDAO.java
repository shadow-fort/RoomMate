package com.example.roommate.database.dao;

import com.example.roommate.database.dto.PersonDTO;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepositoryDAO extends CrudRepository<PersonDTO, Long> {

    /**
     * .
     * @return eine Liste von aller User
     */
    List<PersonDTO> findAll();

    /**
     * .
     * @param username Schlüssel
     * @return ein PersonDTO
     */
    PersonDTO findPersonDTOByUsername(String username);

    /**
     * .
     * @param username Schlüssel
     * @return Anzahle von Nutzer
     */
    int countPersonDTOByUsername(String username);


    /**
     * .
     * @param title Variable
     * @param vorname Variable
     * @param nachname Variable
     * @param username Schlüssel
     */
    @Modifying
    @Query("update person set title = :title,"
            +
            "vorname = :vorname, nachname = :nachname"
            +
            " where username = :username")
    void updatePersonDTOByTitleAndVornameAndNachname(
            @Param("title") String title,
            @Param("vorname") String vorname,
            @Param("nachname") String nachname,
            @Param("username") String username);
}
