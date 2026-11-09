package com.example.roommate.controllerTest.admin;

import com.example.roommate.application.service.BookingService;
import com.example.roommate.application.service.MixService;
import com.example.roommate.application.service.RoomService;
import com.example.roommate.application.service.UserService;
import com.example.roommate.application.service.repositories.RoomRepository;
import com.example.roommate.application.validation.BookingsForm;
import com.example.roommate.application.validation.RoomForm;
import com.example.roommate.application.validation.UserForm;
import com.example.roommate.configTest.helper.WithMockOAuth2User;
import com.example.roommate.controllers.admin.AdminController;
import com.example.roommate.domain.model.bookingAggregat.Booking;
import com.example.roommate.domain.model.roomAggregat.Room;
import com.example.roommate.domain.model.userAggregat.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {

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



    private static final String HANDLE = "admin-example";

    @Test
    @DisplayName("Die adminHome Seite wird richtig angezeigt")
    @WithMockOAuth2User(login = HANDLE)
    void test_1() throws Exception {
        when(usc.greetUser(anyString())).thenReturn(HANDLE);
        mvc.perform(get("/adminHome"))
                .andExpect(model().attributeExists("admin"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(view().name("adminHome"));
    }

    @Test
    @DisplayName("Get Request zu adminRoomDetails")
    @WithMockOAuth2User(login = HANDLE)
    void test_2() throws Exception {
        when(rsc.findRoomById(anyLong())).thenReturn(new Room("Balabala Room"));
        mvc.perform(get("/adminHome/adminRoomDetails")
                        .param("roomId", "1"))
                .andExpect(model().attributeExists("room"))
                .andExpect(view().name("adminRoomDetails"));
    }

    @Test
    @DisplayName("Ein User wird hinzugefügt, wenn er noch nicht in der Datenbank ist")
    @WithMockOAuth2User(login = "jordan-example")
    void test_3() throws Exception {
        mvc.perform(get("/adminHome")
                .param("admin", "jordan-example"));
        verify(usc).addUserIfNotPresent("jordan-example");
    }

    @Test
    @DisplayName("Ein User wird mit login Name von GitHub  oder seine echte Name"
            +
            "begrüßt")
    @WithMockOAuth2User(login = "jordan-example")
    void test_4() throws Exception {
        mvc.perform(get("/adminHome")
                .param("admin", "jordan-example"));
        verify(usc).greetUser("jordan-example");
    }

    @Test
    @DisplayName("Alle Räume wird angezeigt")
    @WithMockOAuth2User(login = "jordan-example")
    void test_5() throws Exception {
        mvc.perform(get("/adminHome"));
        verify(rsc).getAllRooms();
    }

    @Test
    @DisplayName("Ein Raum kann mit der eingegeben Name hinzugefügt werden")
    @WithMockOAuth2User(login = "jordan-example")
    void test_6() throws Exception {
        mvc.perform(post("/adminHome/addNewRoom")
                .param("roomName", "Lacas Room"));
        assertThat(rsc.isContainsCorrect("Lacas Room")).isFalse();
    }

    @Test
    @DisplayName("Ein Booking wird erfolgreich gelöscht")
    @WithMockOAuth2User(login = HANDLE)
    void testDeleteUserBooking() throws Exception {
        Long bookingId = 1L;
        mvc.perform(post("/adminHome/allBooking/adminDeleteBooking")
                        .param("bookingToDeleteID", bookingId.toString()))
                .andExpect(redirectedUrl("/adminHome/allBooking"));
        verify(bsc).deleteBooking(bookingId);
    }

    @Test
    @DisplayName("Ein Booking wird erfolgreich geändert werden")
    @WithMockOAuth2User(login = HANDLE)
    void testUpdateUserBooking() throws Exception {
        BookingsForm bookingsForm = mock(BookingsForm.class);
        when(bookingsForm.validation()).thenReturn(false);

        when(bsc.isBookingCollisionFreeForUpdate(
                anyLong(), anyString(), anyInt(), any(), any(), any())).thenReturn(false);

        mvc.perform(post("/adminHome/allBooking/adminUpdateUserBooking")
                        .param("bookingsForm", bookingsForm.toString()))
                .andExpect(redirectedUrl("/adminHome/allBooking"));
    }

    @Test
    @DisplayName("Ein Workplace wird erfolgreich gelöscht")
    @WithMockOAuth2User(login = HANDLE)
    void testDeleteWorkplace() throws Exception {
        Long roomId = 1L;
        int workplaceNumber = 1;
        mvc.perform(post("/adminHome/adminRoomDetails/deleteWorkplace")
                        .param("roomID", roomId.toString())
                        .param("workplaceNumber", String.valueOf(workplaceNumber)))
                .andExpect(status().is3xxRedirection());
        verify(rsc).deletingWorkplace(roomId, workplaceNumber);
    }

    @Test
    @DisplayName("Ein Raum kann gelöscht werden")
    @WithMockOAuth2User(login = HANDLE)
    void testDeleteRaum() throws Exception {
        Long roomId = 1L;

        mvc.perform(post("/adminHome/deleteRoom")
                        .param("roomToDeleteID",roomId.toString()))
                .andExpect(redirectedUrl("/adminHome"));
        verify(rsc).deleteRoom(roomId);
    }

    @Test
    @DisplayName("Der Name von Räume sind eindeutig")
    @WithMockOAuth2User(login = HANDLE)
    void testNoDuplicateRoomName() throws Exception {
        when(rsc.isRoomNameIncorrect(any())).thenReturn(true);
        mvc.perform(post("/adminHome/addNewRoom")
                        .param("roomName", "IT Room"))
                .andExpect(redirectedUrl("/adminHome"));
    }

    @Test
    @DisplayName("Der Name des Raums kann nicht geändert werden")
    @WithMockOAuth2User(login = HANDLE)
    void testEditRoom() throws Exception {
        RoomForm roomForm = mock(RoomForm.class);
        when(rsc.isRoomNameIncorrect(any())).thenReturn(true);
        mvc.perform(post("/adminHome")
                        .param("roomForm", roomForm.toString()))
                .andExpect(redirectedUrl("/adminHome"));
    }

    @Test
    @DisplayName("Der Name des Raum kann geändert werden")
    @WithMockOAuth2User(login = HANDLE)
    void testEditRoom_B() throws Exception {
        RoomForm roomForm = mock(RoomForm.class);
        when(rsc.isRoomNameIncorrect(any())).thenReturn(false);
        mvc.perform(post("/adminHome")
                        .param("roomForm", roomForm.toString()))
                .andExpect(redirectedUrl("/adminHome"));
    }

    @Test
    @DisplayName("Profil Seite wird angezeigt mit Infos von User, Wenn der User seine Infos eingegeben hast")
    @WithMockOAuth2User(login = HANDLE)
    void testAdminProfilMitInfos() throws Exception {
        Person person = new Person( "DR.", HANDLE, "Daisy", "Mickey");
        when(usc.isUserPresent(anyString())).thenReturn(true);
        when(usc.findUserByUsername(anyString())).thenReturn(person);
        mvc.perform(get("/adminHome/adminProfil"))
                .andExpect(model().attributeExists("userForm"))
                .andExpect(view().name("adminProfil"));
    }

    @Test
    @DisplayName("Test für postAdminProfil mit ungültigen Eingaben")
    @WithMockOAuth2User(login = HANDLE)
    void testPostAdminProfil_InvalidInputs() throws Exception {
        // Hier sind ungültigen Parameters
        String title = " ";
        String firstName = " ";
        String surName = " ";
        UserForm userForm = new UserForm(title, firstName, surName);

        mvc.perform(post("/adminHome/adminProfil")
                        .param("admin", HANDLE)
                        .flashAttr("userForm", userForm))
                .andExpect(view().name("adminProfil"));

        verify(usc, never()).modifyUser(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Test für postEditWorkplace mit gültiger Ausrüstung")
    @WithMockOAuth2User(login = HANDLE)
    void testPostEditWorkplace_ValidEquipment() throws Exception {
        RoomForm roomForm = mock(RoomForm.class);

        when(rsc.isContainsCorrect(anyString())).thenReturn(true);

        mvc.perform(post("/adminHome/adminRoomDetails/editWorkplace")
                        .param("roomForm", roomForm.toString()))
                .andExpect(status().is3xxRedirection());

    }

    @Test
    @DisplayName("Test get allBookings")
    @WithMockOAuth2User(login = HANDLE)
    void testAllBooking() throws Exception {
        when(bsc.getAllBookings()).thenReturn(List.of());
        when(usc.getAllUserName()).thenReturn(List.of());
        mvc.perform(get("/adminHome/allBooking"))
                .andExpect(model().attributeExists("bookings"))
                .andExpect(model().attributeExists("userNames"))
                .andExpect(model().attributeExists("filter"))
                .andExpect(view().name("allBooking"));
    }

    @Test
    @DisplayName("Test für postUpdateUserBooking mit ungültiger Buchungsform")
    @WithMockOAuth2User(login = HANDLE)
    void testPostUpdateUserBooking_InvalidBookingsForm() throws Exception {
        BookingsForm bookingsForm = mock(BookingsForm.class);
        when(bookingsForm.validation()).thenReturn(true);

        mvc.perform(post("/adminHome/allBooking/adminUpdateUserBooking")
                        .param("bookingsForm", bookingsForm.toString()))
                .andExpect(redirectedUrl("/adminHome/allBooking"));
    }

    @Test
    @DisplayName("Das Booking, das geändert wird, hat eine collision")
    @WithMockOAuth2User(login = HANDLE)
    void testUpdateUserBookingWithCollision() throws Exception {
        BookingsForm bookingsForm = mock(BookingsForm.class);
        when(bookingsForm.validation()).thenReturn(false);
        when(bsc.isBookingCollisionFreeForUpdate(
                anyLong(), anyString(), anyInt(), any(), any(), any())).thenReturn(true);

        mvc.perform(post("/adminHome/allBooking/adminUpdateUserBooking")
                        .param("bookingsForm", bookingsForm.toString()))
                .andExpect(redirectedUrl("/adminHome/allBooking"));

    }


}

