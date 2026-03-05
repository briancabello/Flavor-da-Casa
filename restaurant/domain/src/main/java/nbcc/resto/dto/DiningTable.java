package nbcc.resto.dto;

import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class DiningTable {

    private Long id;
    private String name;

    @Positive
    private int capacity;

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

    public int getCapacity() {
        return capacity;
    }

    public DiningTable setCapacity(int capacity) {
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
