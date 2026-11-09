package com.example.roommate.controllerTest.user;

import com.example.roommate.application.service.BookingService;
import com.example.roommate.application.service.RoomService;
import com.example.roommate.application.service.MixService;
import com.example.roommate.application.service.UserService;
import com.example.roommate.application.service.repositories.RoomRepository;
import com.example.roommate.application.validation.BookingsForm;
import com.example.roommate.configTest.helper.WithMockOAuth2User;
import com.example.roommate.controllers.user.UserController;
import com.example.roommate.domain.model.roomAggregat.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService usc;

    @MockBean
    RoomService rsc;

    @MockBean
    BookingService bsc;

    @MockBean
    MixService msc;

    private static final String HANDLE = "alex-example";

    @Test
    @DisplayName("Die userHome Seite wird richtig angezeigt")
    @WithMockOAuth2User(login = HANDLE)
    void test_1() throws Exception {
        when(usc.greetUser(anyString())).thenReturn(HANDLE);
        mvc.perform(get("/userHome"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(view().name("userHome"));
    }

    @Test
    @DisplayName("post Request auf userRoomDetails")
    @WithMockOAuth2User(login = HANDLE)
    void test_3() throws Exception {
        mvc.perform(post("/userHome/userRoomDetails")
                        .param("roomID", "1")
                        .param("selectedWorkplaceID", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GetMapping zu MyBookings")
    @WithMockOAuth2User(login = HANDLE)
    void test_4() throws Exception {
        mvc.perform(get("/userHome/userBooking"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("myBookings"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PostMapping auf updateUserBooking, um ein Booking zu update, gibt uns redirect")
    @WithMockOAuth2User(login = HANDLE)
    void test_5() throws Exception {
        mvc.perform(post("/userHome/userBooking/updateUserBooking")
                        .param("bookingToUpdateID", "1")
                .param("bookingForWorkplace", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Ein User wird hinzugefügt, wenn er noch nicht in der Datenbank ist")
    @WithMockOAuth2User(login = "jordan-example")
    void test_6() throws Exception {
        mvc.perform(get("/userHome")
                .param("user", "jordan-example"));
        verify(usc).addUserIfNotPresent("jordan-example");
    }

    @Test
    @DisplayName("Ein User wird mit login Name von GitHub  oder seine echte Name"
            +
            "begrüßt")
    @WithMockOAuth2User(login = "jordan-example")
    void test_7() throws Exception {
        mvc.perform(get("/userHome")
                .param("user", "jordan-example"));
        verify(usc).greetUser("jordan-example");
    }


    @Test
    @DisplayName("Die getAllRooms-Methode mit selectedDescription"
            +
            "wird aufgerufen")
    @WithMockOAuth2User(login = "jordan-example")
    void test_9() throws Exception {
        mvc.perform(get("/userHome")
                .param("model", "model")
                .param("user", "jordan-example")
                .param("selectedDescription", "selectedDescription"));
        verify(rsc).getAllRooms("selectedDescription");
    }

    @Test
    @DisplayName("Die getAllEquipmentsDescription-Methode"
            +
            "wird auf")
    @WithMockOAuth2User(login = "jordan-example")
    void test_10() throws Exception {
        mvc.perform(get("/userHome")
                .param("model", "model")
                .param("user", "jordan-example")
                .param("selectedDescription", "null"));
        verify(rsc).getAllEquipmentsDescription();
    }

}
