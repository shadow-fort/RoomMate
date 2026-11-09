package com.example.roommate.databaseTest.buider;

import com.example.roommate.domain.model.bookingAggregat.Booking;
import com.example.roommate.domain.model.bookingAggregat.Period;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TestBookingFactory {

    private static final DateTimeFormatter DATUM_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter ZEIT_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static Booking booking(String username, String roomName, int workplaceNumber,
                                    String date, String startTime, String endTime){
        return new Booking(username, roomName, workplaceNumber,
               new Period(LocalDate.parse(date, DATUM_FORMAT),
                LocalTime.parse(startTime, ZEIT_FORMAT),
                LocalTime.parse(endTime, ZEIT_FORMAT)));
    }

}
