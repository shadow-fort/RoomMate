package com.example.roommate.application.service;

import com.example.roommate.application.service.repositories.UserRepository;
import com.example.roommate.domain.model.userAggregat.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    Person person = new Person(52L, "Dr.", "The Alpinist", "Alex", "Example");

    Person person1 = new Person(54L, null, "alex-example", "Alex", "Example");

    Person person2 = new Person(54L, "", "sam-example", "Sam", "Example");

    UserRepository userRepository = mock(UserRepository.class);

    UserService userService = new UserService(userRepository);


    @Test
    @DisplayName("Die User kann hinzugefügt werden")
    void test_1() {
        userService.addUser("alnpjst");

        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(userRepository).save(captor.capture());
        Person savedPerson = captor.getValue();
        assertThat(savedPerson.getUsername()).isEqualTo("alnpjst");
    }

    @Test
    @DisplayName("Die Person wird mit seinem Titel, Vornamen und Nachnamen begrüßt")
    void test_2() {
        String username = "The Alpinist";
        when(userRepository.countPersonByUsername(username)).thenReturn(1);
        when(userRepository.findPersonByUsername(username)).thenReturn(person);
        String user = userService.greetUser(username);
        assertThat(user).isEqualTo("Dr. Alex Example");
    }

    @Test
    @DisplayName("Die Person wird nur mit seinem Vornamen begrüßt")
    void test_3() {
        String username = "The Alpinist";
        when(userRepository.countPersonByUsername(username)).thenReturn(1);
        when(userRepository.findPersonByUsername(username)).thenReturn(person1);
        String user = userService.greetUser(username);
        assertThat(user).isEqualTo("Alex");
    }

    @Test
    @DisplayName("Die isUserPresent-Methode soll uns false zurückgeben, wenn ein username nicht gespeichert ist")
    void test_4() {
        when(userRepository.countPersonByUsername(any())).thenReturn(0);
        assertThat(userService.isUserPresent("The Alpinist")).isFalse();
    }

    @Test
    @DisplayName("Die isUserPresent-Methode soll uns true zurückgeben, wenn ein username gespeichert ist")
    void test_5() {
        when(userRepository.countPersonByUsername(any())).thenReturn(1);
        assertThat(userService.isUserPresent(person.getUsername())).isTrue();
    }

    @Test
    @DisplayName("Die User kann geändert werden")
    void test_6() {
        when(userService.findUserByUsername(any())).thenReturn(person);
        userService.modifyUser("Prof.", person.getUsername(), person.getVorname(), person.getNachname());
        verify(userRepository).updateUser("Prof.", person.getUsername(), person.getVorname(), person.getNachname());
    }

    @Test
    @DisplayName("Ein User kann hinzugefügt werden, wenn er noch nicht vorhanden ist")
    void test_7() {
        when(userRepository.countPersonByUsername(any())).thenReturn(0);
        userService.addUserIfNotPresent("lamntz");
        verify(userRepository).save(new Person("lamntz"));
    }

    @Test
    @DisplayName("Alle Username können ausgegeben werden")
    void test_8() {
        when(userRepository.findAll()).thenReturn(List.of(new Person("lamntz"), new Person("uhzt")));
        List<String> list = userService.getAllUserName();
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Die Person wird nur mit seinem Vornamen begrüßt")
    void test_9() {
        String username = "The Alpinist";
        when(userRepository.countPersonByUsername(username)).thenReturn(1);
        when(userRepository.findPersonByUsername(username)).thenReturn(person2);
        String user = userService.greetUser(username);
        assertThat(user).isEqualTo("Sam");
    }
}
