package nbcc.resto.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean archived;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    public EventEntity() {}

    public Long getId() {
        return id;
    }

    public EventEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getMenuId() {
        return menuId;
    }

    public EventEntity setMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public String getName() {
        return name;
    }

    public EventEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public EventEntity setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public EventEntity setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public EventEntity setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public EventEntity setPrice(double price) {
        this.price = price;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public EventEntity setActive(boolean active) {
        this.active = active;
        return this;
    }

    public boolean isArchived() {
        return archived;
    }

    public EventEntity setArchived(boolean archived) {
        this.archived = archived;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public EventEntity setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public EventEntity setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }
}
