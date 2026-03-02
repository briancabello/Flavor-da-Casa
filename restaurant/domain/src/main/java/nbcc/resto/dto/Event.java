package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class Event {

    private Long id;

    private Long menuId;

    @NotBlank(message = "Event name is required")
    private String name;

    private String description;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    @Positive(message = "Duration must be positive")
    private int duration;

    @Positive(message = "Price must be positive")
    private double price;

    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedDate;

    public Event() {}

    public Event(Long id, Long menuId, String name, String description, LocalDateTime startDate, LocalDateTime endDate, int duration, double price, boolean active, LocalDateTime createdAt, LocalDateTime lastUpdatedDate) {
        this.id = id;
        this.menuId = menuId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.price = price;
        this.active = active;
        this.createdAt = createdAt;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getId() {
        return id;
    }

    public Event setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Event setMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Event setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public Event setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Event setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Event setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Event setPrice(double price) {
        this.price = price;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Event setActive(boolean active) {
        this.active = active;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Event setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Event setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }
}
