package apap.ti.hospitalization2206082064.DTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    
    private UUID id; // Used for updating an existing patient

    @NotNull(message = "NIK is required.")
    @Size(min = 16, max = 16, message = "NIK must be exactly 16 characters.")
    private String NIK;

    @NotNull(message = "Name is required.")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters.")
    private String name;

    @Email(message = "Email should be valid.")
    @NotNull(message = "Email is required.")
    private String email;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy") // Define the format for inputting the date
    private LocalDate birthDate; 

    @NotNull(message = "Gender is required.") // true = male, false = female
    private Boolean gender;

    private boolean isDeleted; // Used for soft deletion
}
