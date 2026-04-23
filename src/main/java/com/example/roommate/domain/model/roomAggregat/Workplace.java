package com.example.roommate.domain.model.roomAggregat;

public record Workplace(
        int workplaceNummer,
        String equipments,
        String status) {

    @Override
    public String toString() {
        return "Workplace{"
                +
                "workPlaceNummer=" + workplaceNummer
                +
                "status=" + status
                +
                ", equipments='" + equipments + '\''
                +
                '}';
    }
}
