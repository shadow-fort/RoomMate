package com.example.roommate.database;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.roommate.application.service.repositories.RoomRepository;
import com.example.roommate.database.dao.RoomRepositoryDAO;
import com.example.roommate.database.dto.RoomDTO;
import com.example.roommate.database.dto.WorkplaceDTO;
import com.example.roommate.domain.model.roomAggregat.Room;
import com.example.roommate.domain.model.roomAggregat.Workplace;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@Repository
public class RoomRepositoryImpl implements RoomRepository {

    /**
     * Attribut für dependency injection.
     */
    private final RoomRepositoryDAO roomRepositoryDAO;

    /**
     * .
     * @param roomReposDAO Instanz
     */
    public RoomRepositoryImpl(
            final RoomRepositoryDAO roomReposDAO) {
        this.roomRepositoryDAO = roomReposDAO;
    }

    /**
     * .
     * @return eine Liste von Raüme
     */
    @Override
    public List<Room> findAll() {
        List<RoomDTO> roomDTO = roomRepositoryDAO.findAll();
        return roomDTO.stream().map(this::convertedRoom).toList();

    }

    /**
     * .
     * @param roomId schlüssel
     * @return eine optionale Liste von Raüme
     */
    @Override
    public Optional<Room> findByRoomId(
            final Long roomId) {
        return roomRepositoryDAO.findById(roomId).map(this::convertedRoom);
    }

    /**
     * .
     * @param room Objekt
     * @return ein Raum
     */
    @Override
    public Room save(
            final Room room) {
        Set<WorkplaceDTO> wpl = room.getWorkplaces().stream()
                .map(this::extractWorkplaceDTO).collect(Collectors.toSet());
        RoomDTO roomDTO = new RoomDTO(room.getRoomId(),
                room.getRoomName(), wpl);
        RoomDTO savedRoom = roomRepositoryDAO.save(roomDTO);
        return convertedRoom(savedRoom);
    }

    /**
     * .
     * @param room Objekt
     */
    @Override
    public void delete(
            final Room room) {
        Set<WorkplaceDTO> wpl = room.getWorkplaces().stream()
                .map(this::extractWorkplaceDTO).collect(Collectors.toSet());
        RoomDTO roomDTO = new RoomDTO(room.getRoomId(),
                room.getRoomName(), wpl);
        roomRepositoryDAO.delete(roomDTO);
    }

    /**
     * .
     * @param roomId schlüssel
     * @param roomName Name des Rooms
     */
    @Override
    public void editRoom(
            final Long roomId,
            final String roomName) {
        roomRepositoryDAO.updateRoomDTOByRoomName(roomName, roomId);
    }

    /**
     * .
     * @param roomDTO Objekt
     * @return ein Raum
     */
    private Room convertedRoom(
            final RoomDTO roomDTO) {
        Room result = new Room(roomDTO.roomId(), roomDTO.roomName());
        roomDTO.workplaces().forEach(workplace ->
                result.addWorkplace(convertedWorkplace(workplace)));
        return result;
    }

    /**
     * .
     * @param workplaceDTO Objekt
     * @return ein Platz
     */
    private Workplace convertedWorkplace(
            final WorkplaceDTO workplaceDTO) {
        return new Workplace(workplaceDTO.workplaceNummer(),
                workplaceDTO.equipments(), workplaceDTO.status());
    }

    private WorkplaceDTO extractWorkplaceDTO(
            final Workplace workplace) {
        return new WorkplaceDTO(workplace.workplaceNummer(),
                workplace.equipments(), workplace.status());
    }
}
