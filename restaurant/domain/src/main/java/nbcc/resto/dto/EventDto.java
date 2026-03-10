package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventDto {

    private Long id;

    private Long menuId;

    @NotBlank(message = "Event name is required")
    private String name;

    private String description;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Positive(message = "Duration must be positive")
    private int duration;

    @Positive(message = "Price must be positive")
    private double price;

    private boolean active;

    private boolean archived;

    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;

    public EventDto() {}

    public EventDto(Long id, Long menuId, String name, String description, LocalDate startDate, LocalDate endDate, int duration, double price, boolean active, LocalDateTime createdDate, LocalDateTime lastUpdatedDate) {
        this.id = id;
        this.menuId = menuId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.price = price;
        this.active = active;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Long getId() {
        return id;
    }

    public EventDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getMenuId() {
        return menuId;
    }

    public EventDto setMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public String getName() {
        return name;
    }

    public EventDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public EventDto setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public EventDto setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public EventDto setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public EventDto setPrice(double price) {
        this.price = price;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public EventDto setActive(boolean active) {
        this.active = active;
        return this;
    }

    public boolean isArchived() {
        return archived;
    }

    public EventDto setArchived(boolean archived) {
        this.archived = archived;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public EventDto setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public EventDto setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        return this;
    }
}
