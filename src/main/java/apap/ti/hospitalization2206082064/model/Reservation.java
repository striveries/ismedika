package apap.ti.hospitalization2206082064.model;

import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Setter
@Getter

@Entity
public class Reservation {
    @Id
    @Column(unique = true)
    private String id;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateIn;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOut;

    private double totalFee;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "nurse_id", nullable = false)
    private Nurse nurse;

    @ManyToMany
    @JoinTable(
        name = "reservation_facility",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<Facility> facilities;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private boolean isDeleted;
}
