package apap.ti.hospitalization2206082064.model;

import java.util.List;
import java.util.UUID;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Setter;
import lombok.Getter;


@Setter
@Getter
@Entity
public class Nurse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private boolean gender;  

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private boolean isDeleted;

    @OneToMany(mappedBy = "nurse")
    private List<Reservation> reservations;
}
