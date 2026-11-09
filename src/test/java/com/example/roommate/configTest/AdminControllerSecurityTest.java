package com.example.roommate.configTest;

import com.example.roommate.application.service.BookingService;
import com.example.roommate.application.service.MixService;
import com.example.roommate.application.service.RoomService;
import com.example.roommate.application.service.UserService;
import com.example.roommate.config.security.MethodSecurityConfig;
import com.example.roommate.config.security.SecurityConfig;
import com.example.roommate.configTest.helper.WithMockOAuth2User;
import com.example.roommate.controllers.admin.AdminController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class AdminControllerSecurityTest {

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


    @Test
    @DisplayName("Die adminHome-Seite ist für "
            +
            "nicht-authentifizierte User nicht erreichbar")
    void test_0() throws Exception {
        mvc.perform(get("/adminHome"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockOAuth2User(login = "Tim")
    @DisplayName("Die adminHome-Seite ist für normal "
            +
            "authentifizierte User nicht erreichbar")
    void test_1() throws Exception {
        mvc.perform(get("/adminHome"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockOAuth2User(login = "Tim", roles = {"ADMIN"})
    @DisplayName("Die adminHome-Seite ist für normale Admins erreichbar")
    void test_2() throws Exception {
        mvc.perform(get("/adminHome"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockOAuth2User(login = "Tim", roles = {"ADMIN"})
    @DisplayName("Die adminHome-Seite gibt richtige View zurück")
    void test_3() throws Exception {
        mvc.perform(get("/adminHome"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminHome"));
    }

    @Test
    @WithMockOAuth2User(login = "Raoul", roles = {"ADMIN"})
    @DisplayName("Die adminHome Seite ist für authentifizierte User,"
            +
            "die Admin sind, erreichbar und mit loginName als ModelAttribute")
    void test_4() throws Exception{
        when(usc.greetUser(anyString())).thenReturn("Raoul");
        mvc.perform(get("/adminHome"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("admin", "Raoul"));
    }

    @Test
    @WithMockOAuth2User(login = "Raoul", roles = {"ADMIN"})
    @DisplayName("Die adminHome Seite ist für authentifizierte User, "
            +
            "die Admin sind, erreichbar, mit loginName als ModelAttribute und View")
    void test_5() throws Exception{
        when(usc.greetUser(anyString())).thenReturn("Raoul");
        mvc.perform(get("/adminHome"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("admin", "Raoul"))
                .andExpect(view().name("adminHome"));
    }

}
