package com.example.roommate.database.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
import java.util.Set;

@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@Table("room")
public record RoomDTO(
        @Id Long roomId, String roomName, Set<WorkplaceDTO> workplaces) {
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomDTO roomDTO = (RoomDTO) o;
        return Objects.equals(roomId, roomDTO.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }
}