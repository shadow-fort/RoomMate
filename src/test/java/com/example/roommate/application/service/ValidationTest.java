package com.example.roommate.application.service;

import com.example.roommate.application.validation.Validation;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ValidationTest {

    @Test
    public void testNullDate() {
        LocalDate invalidDate = null;

        Validation validation = new ValidationTestBuilder()
                .withDate(invalidDate)
                .build();
        assertThat(validation.isValidDate()).isFalse();
    }

    @Test
    public void testValidDate() {
        LocalDate validDate = LocalDate.of(2023, 12, 11);

        Validation validation = new ValidationTestBuilder()
                .withDate(validDate)
                .build();
        assertThat(validation.isValidDate()).isTrue();
    }



    @Test
    public void testWeekendDate() {
        LocalDate validDate = LocalDate.of(2023, 12, 10);

        Validation validation = new ValidationTestBuilder()
                .withDate(validDate)
                .build();
        assertThat(validation.isValidDate()).isFalse();
    }


    @Test
    public void testValidStartTime() {
        LocalDate validDate = LocalDate.of(2023, 12, 11);

        Validation validation = new ValidationTestBuilder()
                .withDate(validDate)
                .withStartTime(LocalTime.of(9, 0))
                .withEndTime(LocalTime.of(13,0))
                .build();

        assertThat(validation.isValidStartTime()).isTrue();

    }

    @Test
    public void testInvalidStartTimeBefore8AM() {

        Validation validation = new ValidationTestBuilder()
                .withStartTime(LocalTime.of(7, 0))
                .build();
        assertThat(validation.isValidStartTime()).isFalse();
    }

    @Test
    public void testInvalidStartTimeAfter5PM() {
        Validation validation = new ValidationTestBuilder()
                .withStartTime(LocalTime.of(18, 0))
                .build();

        assertThat(validation.isValidStartTime()).isFalse();
    }

    @Test
    public void testValidEndTime() {
        LocalDate validDate = LocalDate.of(2023, 12, 11);

        Validation validation = new ValidationTestBuilder()
                .withDate(validDate)
                .withStartTime(LocalTime.of(8,0))
                .withEndTime(LocalTime.of(12, 0))
                .build();

        assertThat(validation.isValidEndTime()).isTrue();
    }

    @Test
    public void testInvalidEndTimeBefore8AM() {
        Validation validation = new ValidationTestBuilder()
                .withEndTime(LocalTime.of(7, 0))
                .build();

        assertThat(validation.isValidEndTime()).isFalse();
    }

    @Test
    public void testInvalidEndTimeAfter5PM() {
        Validation validation = new ValidationTestBuilder()
                .withEndTime(LocalTime.of(18, 0))
                .build();

        assertThat(validation.isValidEndTime()).isFalse();
                  }

    @Test
    public void testBookingInThePast() {
        LocalDate pastDate = LocalDate.of(2023,12,7);
        LocalTime pastStartTime = LocalTime.of(9,0);
        Validation validation = new ValidationTestBuilder()
                .withDate(pastDate)
                .withStartTime(pastStartTime)
                .withEndTime(pastStartTime.plusHours(2))
                .build();

        assertThat(validation.bookingIsInTheFutur()).isFalse();
    }


}
