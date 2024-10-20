package apap.ti.hospitalization2206082064.model;

import java.util.List;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter


@Entity
public class Room {
    @Id
    @Column(unique = true)
    private String id;

    private String name;

    private String description;

    private int maxCapacity;

    private double pricePerDay;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private boolean isDeleted;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;
}
