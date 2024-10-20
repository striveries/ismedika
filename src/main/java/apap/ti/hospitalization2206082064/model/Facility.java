package apap.ti.hospitalization2206082064.model;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@Entity
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private double fee;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private boolean isDeleted;

    @ManyToMany(mappedBy = "facilities")
    private List<Reservation> reservations;
}
