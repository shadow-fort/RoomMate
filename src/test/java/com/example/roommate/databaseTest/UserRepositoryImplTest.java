package com.example.roommate.databaseTest;

import com.example.roommate.application.service.repositories.UserRepository;
import com.example.roommate.database.UserRepositoryImpl;
import com.example.roommate.database.dao.UserRepositoryDAO;
import com.example.roommate.domain.model.userAggregat.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryImplTest {

    @Autowired
    UserRepositoryDAO userRepositoryDAO;

    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        userRepository = new UserRepositoryImpl(userRepositoryDAO);
    }

    @Test
    @DisplayName("Ein User kann gespeichert werden")
    void test_1(){
        Person person = new Person("Dr.", "alzn", "Alex", "Example");
        Person saved = userRepository.save(person);
        assertThat(saved.getTitle()).isEqualTo("Dr.");
        assertThat(saved.getUsername()).isEqualTo("alzn");
        assertThat(saved.getVorname()).isEqualTo("Alex");
        assertThat(saved.getNachname()).isEqualTo("Example");
    }

    @Test
    @DisplayName("Ein User kann anhand sein Username geladen werden")
    void test_2(){
        Person person = new Person("Dr.", "tzntx", "Alex", "Example");
        userRepository.save(person);
        Person loaded = userRepository.findPersonByUsername("tzntx");
        assertThat(loaded.getTitle()).isEqualTo("Dr.");
        assertThat(loaded.getUsername()).isEqualTo("tzntx");
        assertThat(loaded.getVorname()).isEqualTo("Alex");
        assertThat(loaded.getNachname()).isEqualTo("Example");
    }

    @Test
    @DisplayName("Es kann nur ein User mit einem eindeutigen Username in der Tabelle")
    void test_3(){
        Person person = new Person("Dr.", "alznb", "Alex", "Example");
        Person saved = userRepository.save(person);
        int count = userRepository.countPersonByUsername(saved.getUsername());
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Es wird kein User in der Tabelle gefunden, der nicht vorher gespeichert wurde")
    void test_4(){
        int count = userRepository.countPersonByUsername("alznb");
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("Ein Benutzer kann aktualisiert werden")
    void test_5() {
        // Arrange
        Person person = new Person("Dr.", "testUser", "John", "Doe");
        userRepository.save(person);

        // Act
        userRepository.updateUser("Prof.", "testUser", "Jane", "Doe");
        Person updatedPerson = userRepository.findPersonByUsername("testUser");

        // Assert
        assertThat(updatedPerson.getTitle()).isEqualTo("Prof.");
        assertThat(updatedPerson.getVorname()).isEqualTo("Jane");
        assertThat(updatedPerson.getNachname()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Alle Benutzer werden gefunden")
    void test_6() {
        // Arrange
        Person person = new Person("Dr.", "testUser", "John", "Doe");
        Person person_1 = new Person("Prof.", "alkhrn", "Sam", "Example");
        Person person_2 = new Person(null, "njfgz", "Jordan", "Example");
        Person person_3 = new Person(null, "adghf", "Riley", "Example");

        // Act
        userRepository.save(person);
        userRepository.save(person_1);
        userRepository.save(person_2);
        userRepository.save(person_3);

        // Assert
        assertThat(userRepository.findAll().size()).isEqualTo(4);
    }


}
