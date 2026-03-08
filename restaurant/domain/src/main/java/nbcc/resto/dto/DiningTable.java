package nbcc.resto.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class DiningTable {

    private Long id;

    @Size(max = 100, message = "Table name cannot exceed 100 characters")
    private String name;

    @Positive
    private Integer capacity;

    private LocalDateTime createdDate;

    public DiningTable() {
    }

    public DiningTable(DiningTable table) {
        this(table.getId(), table.getName(), table.getCapacity(), table.getCreatedDate());
    }

    public DiningTable(Long id, String name, int capacity, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public DiningTable setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DiningTable setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public DiningTable setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public DiningTable setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}
