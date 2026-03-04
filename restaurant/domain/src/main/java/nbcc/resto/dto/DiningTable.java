package nbcc.resto.dto;

import java.time.LocalDateTime;

public class DiningTable {

    private Long id;
    private String name;
    private int capacity;
    private LocalDateTime createdDate;

    public DiningTable() {
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
