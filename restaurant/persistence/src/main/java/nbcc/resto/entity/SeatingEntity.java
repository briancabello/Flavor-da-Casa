package nbcc.resto.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "seating")
@EntityListeners(AuditingEntityListener.class)
public class SeatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    public SeatingEntity() {
    }

    public Long getId() {
        return id;
    }

    public SeatingEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public SeatingEntity setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getName() {
        return name;
    }

    public SeatingEntity setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public SeatingEntity setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public SeatingEntity setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public SeatingEntity setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}
