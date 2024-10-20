package apap.ti.hospitalization2206082064.DTO;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import apap.ti.hospitalization2206082064.model.Facility;
import apap.ti.hospitalization2206082064.model.Room;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    
    private String id; 
    
    @NotNull(message = "Date In is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateIn;
    
    @NotNull(message = "Date Out is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOut;
    
    @NotNull(message = "Nurse ID is required.")
    private UUID nurseId;
    
    private double totalFee;
    
    @NotNull(message = "Patient ID is required.")
    private UUID patientId;
    
    private String roomId;

    private Room room;
    
    private List<Facility> facilities; 

    private boolean isDeleted; 
}
