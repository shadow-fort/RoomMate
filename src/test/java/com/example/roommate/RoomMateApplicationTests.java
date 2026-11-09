package com.example.roommate;

import com.example.roommate.application.DatabaseSimulator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class RoomMateApplicationTests {

    @MockBean
    DatabaseSimulator databaseSimulator;

    @Test
    void contextLoads() {
    }

}
