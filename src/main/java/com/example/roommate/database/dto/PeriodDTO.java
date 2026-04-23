package com.example.roommate.database.dto;

import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalTime;

@Table("period")
public record PeriodDTO(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime
) {
}