package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class Menu {

    private Long id;

    @NotBlank(message = "Menu name is required")
    private String name;

    private String description;

    private LocalDateTime createdDate;

    public Menu() {
    }

    public Menu(Menu menu) {
        this(menu.getId(), menu.getName(), menu.getDescription(), menu.getCreatedDate());
    }

    public Menu(Long id, String name, String description, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public Menu setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Menu setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Menu setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Menu setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}
