package com.example.roommate.controllerTest.startTest;

import com.example.roommate.application.DatabaseSimulator;
import com.example.roommate.controllers.start.StartController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(StartController.class)
@AutoConfigureMockMvc(addFilters = false)
class StartControllerTest {

   @Autowired
    MockMvc mvc;

   @MockBean
    DatabaseSimulator databaseSimulator;

   @Test
   @DisplayName("Die start-Methode der StartController wird aufgeruft mit dem Statuscode 200")
   void start() throws Exception{
       mvc.perform(get("/"))
               .andExpect(status().isOk());
   }

    @Test
    @DisplayName("Die start-Methode der StartController gibt die LoginSeite zurück")
    void startReturnLoginView() throws Exception{
        mvc.perform(get("/"))
                .andExpect(view().name("login"));
    }

}