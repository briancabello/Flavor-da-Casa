package nbcc.resto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class ReservationDto {
    private long id;
    private UUID uuid;

    @NotNull(message = "Event is required")
    private Long eventId;

    @NotNull(message = "Seating is required")
    private Long seatingId;

    @NotBlank(message = "First name is required")
    private String guestFirstName;

    @NotBlank(message = "Last name is required")
    private String guestLastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotNull(message = "Group size is required")
    @Positive(message = "Group size must be at least 1")
    private Integer groupSize;

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

    public Long getEventId() {
        return eventId;
    }

    public ReservationDto setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public Long getSeatingId() {
        return seatingId;
    }

    public ReservationDto setSeatingId(Long seatingId) {
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

    public Integer getGroupSize() {
        return groupSize;
    }

    public ReservationDto setGroupSize(Integer groupSize) {
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
