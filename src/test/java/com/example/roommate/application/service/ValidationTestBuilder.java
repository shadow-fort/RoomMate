package com.example.roommate.application.service;

import com.example.roommate.application.validation.Validation;

import java.time.LocalDate;
import java.time.LocalTime;

public class ValidationTestBuilder {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public ValidationTestBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public ValidationTestBuilder withStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public ValidationTestBuilder withEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public Validation build() {
        return new Validation(date, startTime, endTime);
    }
}

