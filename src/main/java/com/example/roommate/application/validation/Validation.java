package com.example.roommate.application.validation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Validation {

    /**
     * Attribut.
     */
    private static final Set<String> ERROR_MESSAGE_FORM = new TreeSet<>();

    /**
     * .
     * @return eine Kopie von Set ERROR_MESSAGE
     */
    public Set<String> getErrorMessageForm() {
        return new HashSet<>(ERROR_MESSAGE_FORM);
    }

    /**
     * Attribut Datum.
     */
    private final LocalDate date;

    /**
     * Attribut Start.
     */
    private final LocalTime startTime;

    /**
     * Attribut End.
     */
    private final LocalTime endTime;

    /**
     * Konstanz.
     */
    private static final int START_TIME_FOR_DAY = 8;

    /**
     * Konstanz.
     */
    private static final int END_TIME_FOR_DAY = 17;

    /**
     * .
     * @param daTe Datum
     * @param startTiMe Beginn
     * @param endTiMe End
     */
    public Validation(
            final LocalDate daTe,
            final LocalTime startTiMe,
            final LocalTime endTiMe) {
        this.date = daTe;
        this.startTime = startTiMe;
        this.endTime = endTiMe;
    }

    /**
     * .
     * @return true or false
     */
    public boolean isValidDate() {
        if (date == null) {
            writeErrorMessage("invalid date, should not be empty");
            return false;
        } else if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            writeErrorMessage("no Booking on Weekend");
            return false;
        }
        return true;
    }

    private void writeErrorMessage(final String e) {
        ERROR_MESSAGE_FORM.clear();
        ERROR_MESSAGE_FORM.add(e);
        getInputDateTime();
    }

    private void getInputDateTime() {
        if (date != null && startTime != null && endTime != null) {
            String pattern = "dd.MM.yyyy";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String datum = date.format(formatter);
            ERROR_MESSAGE_FORM.add("date: "
                    + datum + " Start " + startTime + " end: " + endTime);
        } else {
            ERROR_MESSAGE_FORM.add("date: "
                    + date + " Start " + startTime + " end: " + endTime);
        }
    }

    private boolean isValidTime(
            final LocalTime time,
            final String startEndTime) {
        if (time == null) {
            writeErrorMessage(startEndTime + ", should not be empty");
            return false;
        } else if (time.isBefore(LocalTime.of(START_TIME_FOR_DAY, 0, 0, 0))) {
            writeErrorMessage(startEndTime
                    + " time should not be before 08 Hour");
            return false;
        } else if (time.isAfter(LocalTime.of(END_TIME_FOR_DAY, 0, 0, 0))) {
            writeErrorMessage(startEndTime
                    + " time should not be after 17 Hour");
            return false;
        } else if (!startTime.isBefore(endTime)) {
            writeErrorMessage("start most be before end");
            return false;
        }
        return true;
    }

    /**
     * .
     * @return true or false
     */
    public boolean isValidStartTime() {
        return isValidTime(startTime, "start");
    }

    /**
     * .
     * @return true or false
     */
    public boolean isValidEndTime() {
        return isValidTime(endTime, "end");
    }

    /**
     * .
     * @return true or false
     */
    public boolean bookingIsInTheFutur() {
        if (isValidDate() && isValidStartTime()) {
            LocalDateTime dateTime = date.atTime(startTime);
            if (dateTime.isBefore(LocalDateTime.now())) {
                writeErrorMessage("no Booking in the past ");
                return false;
            }
        }
        return true;    }

    /**
     * .
     * @return eine Menge von String
     */
    public Set<String> isValidForm() {
        boolean validForm  = isValidDate()
                && isValidStartTime()
                && isValidEndTime() && bookingIsInTheFutur();
        if (validForm) {
            return null;
        } else {
//            return ERROR_MESSAGE_FORM;
            return getErrorMessageForm();
        }
    }
}