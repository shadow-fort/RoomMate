package com.example.roommate.database.dao;

import com.example.roommate.database.dto.RoomDTO;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepositoryDAO extends CrudRepository<RoomDTO, Long> {

    /**
     * .
     * @return eine Liste von RoomDTO
     */
    List<RoomDTO> findAll();

    /**
     * .
     * @param roomId Schlüssel
     * @return ein RoomDTO
     */
    RoomDTO findByRoomId(Long roomId);

    /**
     * .
     * @param roomDTO Objekt
     * @return ein RoomDTO
     */
    RoomDTO save(RoomDTO roomDTO);

    /**
     * .
     * @param roomDTO Objekt
     */
    void delete(RoomDTO roomDTO);

    /**
     * .
     * @param roomName Name der Raum
     * @param roomId Schlüssel
     */
    @Modifying
    @Query("update room set room_name = :roomName where room_id = :roomId")
    void updateRoomDTOByRoomName(
            @Param("roomName") String roomName,
            @Param("roomId") Long roomId);

//    @Modifying
//    @Query("update room set room_name = :roomName where room_id = :roomId")
//    void updateRoomDTOByWorkplaces(
//            @Param("roomName") String roomName,
//            @Param("roomId") Long roomId);
}