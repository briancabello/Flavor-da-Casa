package nbcc.resto.entity;

import jakarta.persistence.*;
import nbcc.resto.dto.ReservationStatus;

import java.util.UUID;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "uuid", unique = true, updatable = false)
    private UUID uuid;

    @Column(name = "event_id", nullable = false)
    private long eventId;

    @Column(name = "seating_id", nullable = false)
    private long seatingId;

    @Column(name = "guest_first_name", nullable = false)
    private String guestFirstName;

    @Column(name = "guest_last_name", nullable = false)
    private String guestLastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "group_size", nullable = false)
    private int groupSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "assigned_table_id")
    private Long assignedTableId;


    public ReservationEntity() {
    }


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }

    public long getEventId() { return eventId; }
    public void setEventId(long eventId) { this.eventId = eventId; }

    public long getSeatingId() { return seatingId; }
    public void setSeatingId(long seatingId) { this.seatingId = seatingId; }

    public String getGuestFirstName() { return guestFirstName; }
    public void setGuestFirstName(String guestFirstName) { this.guestFirstName = guestFirstName; }

    public String getGuestLastName() { return guestLastName; }
    public void setGuestLastName(String guestLastName) { this.guestLastName = guestLastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getGroupSize() { return groupSize; }
    public void setGroupSize(int groupSize) { this.groupSize = groupSize; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public Long getAssignedTableId() { return assignedTableId; }
    public void setAssignedTableId(Long assignedTableId) { this.assignedTableId = assignedTableId; }
}