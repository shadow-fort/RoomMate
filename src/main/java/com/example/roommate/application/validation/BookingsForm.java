package com.example.roommate.application.validation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


public record BookingsForm(
        Long bookingId,
        String bookingForRoom,
        Integer bookingForWorkplace,
        LocalDate date, LocalTime startTime, LocalTime endTime) {


    /**
     * .
     */
    private static final Set<String> ERROR_MESSAGE = new TreeSet<>();

    /**
     * .
     * @return Fehler Meldung
     */
    public static Set<String> getErrorMessage() {
        return new HashSet<>(ERROR_MESSAGE);
    }

    /**
     * .
     * @return true oder false
     */
    public boolean validation() {
        Validation validation = new Validation(date, startTime, endTime);
        if (validation.isValidForm() == null) {
            return false;
        }
        ERROR_MESSAGE.clear();
        ERROR_MESSAGE.addAll(validation.isValidForm());
        return true;
    }
}
