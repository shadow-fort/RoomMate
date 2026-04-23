package com.example.roommate.database.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("person")
public record PersonDTO(
        @Id Long id, String title,
        String username,
        String vorname,
        String nachname) {

    /**
     * .
     * @param o   the reference object with which to compare.
     * @return true or false
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonDTO personDTO = (PersonDTO) o;
        return Objects.equals(id, personDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}