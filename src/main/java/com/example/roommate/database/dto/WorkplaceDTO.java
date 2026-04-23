package com.example.roommate.database.dto;

import org.springframework.data.relational.core.mapping.Table;

@Table("workplace")
public record WorkplaceDTO(
        int workplaceNummer,
        String equipments,
        String status) {
}