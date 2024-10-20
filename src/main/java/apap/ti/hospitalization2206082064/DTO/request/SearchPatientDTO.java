package apap.ti.hospitalization2206082064.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPatientDTO {

    @NotBlank(message = "NIK is required.")
    @Pattern(regexp = "\\d{16}", message = "NIK must be 16 digits.")
    private String nik;  // Field for NIK (Indonesian National ID)
}
