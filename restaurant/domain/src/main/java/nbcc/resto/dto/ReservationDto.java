package nbcc.resto.dto;

import java.util.UUID;

public class ReservationDto {
    private long id;
    private UUID uuid;
    private long seatingId;
    private String guestFirstName;
    private String guestLastName;
    private String email;
    private int groupSize;
    private String status;
    private Long assignedTableId;

    public ReservationDto() {}

    public long getId() {
        return id;
    }

    public ReservationDto setId(long id) {
        this.id = id;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ReservationDto setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public long getSeatingId() {
        return seatingId;
    }

    public ReservationDto setSeatingId(long seatingId) {
        this.seatingId = seatingId;
        return this;
    }

    public String getGuestFirstName() {
        return guestFirstName;
    }

    public ReservationDto setGuestFirstName(String guestFirstName) {
        this.guestFirstName = guestFirstName;
        return this;
    }

    public String getGuestLastName() {
        return guestLastName;
    }

    public ReservationDto setGuestLastName(String guestLastName) {
        this.guestLastName = guestLastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ReservationDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public ReservationDto setGroupSize(int groupSize) {
        this.groupSize = groupSize;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ReservationDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public Long getAssignedTableId() {
        return assignedTableId;
    }

    public ReservationDto setAssignedTableId(Long assignedTableId) {
        this.assignedTableId = assignedTableId;
        return this;
    }
}
