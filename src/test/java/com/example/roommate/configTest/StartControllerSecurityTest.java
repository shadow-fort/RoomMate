package com.example.roommate.configTest;

import com.example.roommate.application.DatabaseSimulator;
import com.example.roommate.config.security.MethodSecurityConfig;
import com.example.roommate.config.security.SecurityConfig;
import com.example.roommate.configTest.helper.WithMockOAuth2User;
import com.example.roommate.controllers.start.StartController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StartController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class StartControllerSecurityTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    DatabaseSimulator databaseSimulator;

    @Test
    @DisplayName("Die userHome-Seite ist für "
            +
            "nicht-authentifizierte User nicht erreichbar")
    void test_0() throws Exception {
        mvc.perform(get("/userHome"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Die adminHome-Seite ist für "
            +
            "nicht-authentifizierte User nicht erreichbar")
    void test_1() throws Exception {
        mvc.perform(get("/adminHome"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockOAuth2User(login = "Tim")
    @DisplayName("Der StartController gibt für normale "
            +
            "authentifizierte User die userHome-Seite zurück")
    void test_2() throws Exception {
        mvc.perform(get("/"))
                .andExpect(redirectedUrl("/userHome"));
    }

    @Test
    @WithMockOAuth2User(login = "Tim", roles = {"ADMIN"})
    @DisplayName("Der StartController gibt für alle authentifizierte "
            +
            "User mit Admin Rolle die adminHome-Seite zurück")
    void test_3() throws Exception {
        mvc.perform(get("/"))
                .andExpect(redirectedUrl("/adminHome"));
    }
}
