package nbcc.resto.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "menu_item")
@EntityListeners(AuditingEntityListener.class)
public class MenuItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_item_menu"), nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MenuEntity menu;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    public MenuItemEntity() {
    }

    public Long getId() {
        return id;
    }

    public MenuItemEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public MenuItemEntity setMenu(MenuEntity menu) {
        this.menu = menu;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuItemEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuItemEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public MenuItemEntity setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}