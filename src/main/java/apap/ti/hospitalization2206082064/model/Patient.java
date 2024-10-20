package apap.ti.hospitalization2206082064.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;


@Setter
@Getter

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String NIK;

    private String name;

    @Column(unique = true)
    private String email;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate; 

    private boolean gender;  // Assuming true = male, false = female

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private boolean isDeleted;

    @OneToMany(mappedBy = "patient")
    private List<Reservation> reservations;

}
