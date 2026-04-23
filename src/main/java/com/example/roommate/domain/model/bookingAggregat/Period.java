package com.example.roommate.domain.model.bookingAggregat;


import java.time.LocalDate;
import java.time.LocalTime;

public record Period(LocalDate date, LocalTime startTime, LocalTime endTime) {
    @Override
    public String toString() {
        return "Period{"
                +
                "date=" + date
                +
                ", startTime=" + startTime
                +
                ", endTime=" + endTime
                +
                '}';
    }
}
