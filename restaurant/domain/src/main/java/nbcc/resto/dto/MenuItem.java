package nbcc.resto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class MenuItem {

    private Long id;

    @NotNull(message = "A valid menu is required")
    private Menu menu;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private LocalDateTime createdDate;

    public MenuItem() {
    }

    public MenuItem(Long id, Menu menu, String name, String description, LocalDateTime createdDate) {
        this.id = id;
        this.menu = menu;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public MenuItem setId(Long id) {
        this.id = id;
        return this;
    }

    public Menu getMenu() {
        return menu;
    }

    public MenuItem setMenu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public MenuItem setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}