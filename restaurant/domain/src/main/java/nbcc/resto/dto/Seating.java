package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Seating {

    private Long id;

    @NonNull
    private Long eventId;

    @NotBlank
    private String name;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @Positive(message = "Duration must be greater than 0")
    private Integer duration;

    private LocalDateTime createdDate;

    private Collection<DiningTable> tables;
    private List<Long> selectedTableIds;

    public Seating() {
        this.tables = new ArrayList<>();
        this.selectedTableIds = new ArrayList<>();
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
            this.selectedTableIds = new ArrayList<>(seating.getSelectedTableIds());
        }
    }

    public Long getId() {
        return id;
    }

    public Seating setId(Long id) {
        this.id = id;
        return this;
    }

    public @NonNull Long getEventId() {
        return eventId;
    }

    public Seating setEventId(@NonNull Long eventId) {
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

    public List<Long> getSelectedTableIds() {
        return selectedTableIds;
    }

    public Seating setSelectedTableIds(List<Long> selectedTableIds) {
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
