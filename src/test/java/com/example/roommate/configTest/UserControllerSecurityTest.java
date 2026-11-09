package com.example.roommate.configTest;

import com.example.roommate.application.service.BookingService;
import com.example.roommate.application.service.MixService;
import com.example.roommate.application.service.RoomService;
import com.example.roommate.application.service.UserService;
import com.example.roommate.config.security.MethodSecurityConfig;
import com.example.roommate.config.security.SecurityConfig;
import com.example.roommate.configTest.helper.WithMockOAuth2User;
import com.example.roommate.controllers.user.UserController;
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

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class UserControllerSecurityTest {


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
    @DisplayName("Die userHome-Seite ist für nicht-authentifizierte"
            +
            " User nicht erreichbar")
    void test_0() throws Exception {
        mvc.perform(get("/userHome"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockOAuth2User(login = "Tim")
    @DisplayName("Die userHome-Seite ist für authentifizierte User "
            +
            "erreichbar")
    void test_1() throws Exception {
        mvc.perform(get("/userHome"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockOAuth2User(login = "Tim")
    @DisplayName("Die userHome-Seite ist für authentifizierte User "
            +
            "erreichbar und nur mit login Name als Attribute")
    void test_2() throws Exception {
        when(usc.greetUser(anyString())).thenReturn("Tim");
        mvc.perform(get("/userHome"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockOAuth2User(login = "Tim")
    @DisplayName("Die userHome-Seite gibt richtige View zurück")
    void test_3() throws Exception {
        when(usc.greetUser(anyString())).thenReturn("Tim");
        mvc.perform(get("/userHome"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(view().name("userHome"));
    }
}
