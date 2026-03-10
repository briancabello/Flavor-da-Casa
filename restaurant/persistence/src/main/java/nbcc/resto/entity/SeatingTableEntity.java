package nbcc.resto.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "seating_table",
        uniqueConstraints = @UniqueConstraint(columnNames = {"seating_id", "dining_table_id"}),
        indexes = {
                @Index(name = "idx_seating_id", columnList = "seating_id"),
                @Index(name = "idx_dining_table_id", columnList = "dining_table_id")
        }
)
public class SeatingTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seating_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_seating")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SeatingEntity seating;

    @ManyToOne
    @JoinColumn(name = "dining_table_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_dining_table")
    )
    private DiningTableEntity diningTable;

    public SeatingTableEntity() {
    }

    public SeatingTableEntity(SeatingEntity seating, DiningTableEntity diningTable) {
        this.seating = seating;
        this.diningTable = diningTable;
    }

    public Long getId() {
        return id;
    }

    public SeatingTableEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public SeatingEntity getSeating() {
        return seating;
    }

    public SeatingTableEntity setSeating(SeatingEntity seating) {
        this.seating = seating;
        return this;
    }

    public DiningTableEntity getDiningTable() {
        return diningTable;
    }

    public SeatingTableEntity setDiningTable(DiningTableEntity diningTable) {
        this.diningTable = diningTable;
        return this;
    }
}
