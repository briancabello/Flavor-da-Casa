package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.*;

public class Seating {

    private Long id;

    @NotNull(message = "An event must be selected.")
    private Long eventId;

    @NotBlank(message = "Seating name is required.")
    private String name;

    @NotNull(message = "Start date and time are required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be greater than 0")
    private Integer duration;

    private LocalDateTime createdDate;

    private Collection<DiningTable> tables;
    private Set<Long> selectedTableIds;

    public Seating() {
        this.tables = new ArrayList<>();
        this.selectedTableIds = new HashSet<>();
    }

    public Seating(Seating seating) {
        this();
        this.id = seating.getId();
        this.eventId = seating.getEventId();
        this.name = seating.getName();
        this.startDateTime = seating.getStartDateTime();
        this.duration = seating.getDuration();
        this.createdDate = seating.getCreatedDate();

        if (seating.getTables() != null) {
            this.tables = new ArrayList<>(seating.getTables());
        }

        if (seating.getSelectedTableIds() != null) {
            this.selectedTableIds = new HashSet<>(seating.getSelectedTableIds());
        }
    }

    public Long getId() {
        return id;
    }

    public Seating setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public Seating setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Seating setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public Seating setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    public Seating setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Seating setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Collection<DiningTable> getTables() {
        return tables;
    }

    public Seating setTables(Collection<DiningTable> tables) {
        this.tables = tables;
        return this;
    }

    public Set<Long> getSelectedTableIds() {
        return selectedTableIds;
    }

    public Seating setSelectedTableIds(Set<Long> selectedTableIds) {
        this.selectedTableIds = selectedTableIds;
        return this;
    }

    public LocalDateTime getEndDateTime() {
        if (startDateTime != null && duration != null) {
            return startDateTime.plusMinutes(duration);
        }
        return null;
    }
}
