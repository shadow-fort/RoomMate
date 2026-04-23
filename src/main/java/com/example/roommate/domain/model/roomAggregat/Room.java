package com.example.roommate.domain.model.roomAggregat;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.example.roommate.config.annotation.AggregateRoot;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.LinkedHashSet;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@AggregateRoot
public class Room {

    /**
     * Attribut der Klasse.
     */
    private final Long roomId;

    /**
     * Attribut der Klasse.
     */
    private final String roomName;

    /**
     * Attribut der Klasse.
     */
    private final Set<Workplace> workplaces;

    /**
     * .
     * @return der Id des Raums
     */
    public Long getRoomId() {
        return roomId;
    }

    /**
     * .
     * @return der Name des Raums
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * .
     * @return eine kopie von sortierten Plätzen
     */
    public Set<Workplace> getWorkplaces() {
        return new HashSet<>(workplaces).stream()
                .sorted(Comparator.comparing(Workplace::workplaceNummer))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * .
     * @return Anzahle den Plätzen im Raum
     */
    public int getNumbersWorkplaces() {
        return workplaces.size();
    }

    /**
     * .
     * @return eine Liste von Equipment
     */
    public List<String> getEquipmentDescription() {
        return workplaces.stream().map(Workplace::equipments).toList();
    }

    /**
     * .
     * @param id Instanz
     * @param nameRoom Instanz
     * @param workplaceMenge Instanz
     */
    public Room(
            final Long id,
            final String nameRoom,
            final Set<Workplace> workplaceMenge) {
        this.roomId = id;
        this.roomName = nameRoom;
        this.workplaces = workplaceMenge;
    }

    /**
     * .
     * @param id Instanz
     * @param nameRoom Instanz
     */
    public Room(
            final Long id,
            final String  nameRoom) {
        this.roomId = id;
        this.roomName = nameRoom;
        this.workplaces = new HashSet<>();
    }

    /**
     * .
     * @param nameRoom Instanz
     */
    public Room(
            final String  nameRoom) {
        this(null, nameRoom, new HashSet<>());
    }

    /**
     * .
     * @param nummer variable
     * @param equipmentDescription variable
     */
    public void addWorkplace(
            final int nummer,
            final String equipmentDescription) {
        workplaces.add(new Workplace(nummer,
                equipmentDescription, statusAorB("BLOCKED")));
    }

    /**
     * .
     * @param workplace Objekt
     */
    public void addWorkplace(
            final Workplace workplace) {
        workplaces.add(workplace);
    }

    /**
     * Der Platz hat Status verfügbar order gesperrt.
     * @param status Status des Platzes
     * @return Status des Platzes
     */
    private String statusAorB(final String status) {
        if (status.equals("BLOCKED")) {
            return "AVAILABLE";
        }
        return "BLOCKED";
    }

    /**
     * .
     * @param nummer variable
     * @param currentStatus variable
     * @param equipmentDescription variable
     */
    public void editWorkplace(
            final int nummer,
            final String equipmentDescription,
            final String currentStatus) {
        for (Workplace workplace : workplaces) {
            if (workplace.workplaceNummer() == nummer) {
                workplaces.remove(workplace);
                break;
            }
        }
        workplaces.add(new Workplace(nummer,
                equipmentDescription, currentStatus));
    }

    /**
     * .
     * @param nummer variable
     * @param currentStatus variable
     * @param equipmentDescription variable
     * @return Status des Platzes
     */
    public String editWorkplaceStatus(
            final int nummer,
            final String equipmentDescription,
            final String currentStatus) {
        for (Workplace workplace : workplaces) {
            if (workplace.workplaceNummer() == nummer) {
                workplaces.remove(workplace);
                break;
            }
        }
        workplaces.add(new Workplace(nummer,
                equipmentDescription, statusAorB(currentStatus)));
        return statusAorB(currentStatus);
    }

    /**
     * .
     * @param number Nummer des Platzes
     */
    public void deleteWorkplace(
            final int number) {
        for (Workplace workplace : workplaces) {
            if (workplace.workplaceNummer() == number) {
                workplaces.remove(workplace);
                break;
            }
        }
    }

    /**
     * .
     * @param number Nummer des Platzes
     * @return true or false
     */
    public boolean isNumberPresent(final int number) {
        for (Workplace workplace : workplaces) {
            if (workplace.workplaceNummer() == number) {
                return true;
            }
        }
        return false;
    }

    /**
     * .
     * @return ein String
     */
    @Override
    public String toString() {
        return "Room{"
                +
                "roomId="
                + roomId
                +
                ", roomName='"
                + roomName + '\''
                +
                ", workplaces=" + workplaces
                +
                '}';
    }
}
