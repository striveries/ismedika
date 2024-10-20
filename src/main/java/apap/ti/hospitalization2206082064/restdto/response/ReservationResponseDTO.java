package apap.ti.hospitalization2206082064.restdto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservationResponseDTO {
    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateIn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOut;

    private double totalFee;

    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PatientResponseDTO patient;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RoomResponseDTO room;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private NurseResponseDTO nurse;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FacilityResponseDTO> facilities;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Jakarta")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Jakarta")
    private LocalDateTime updatedDate;

    private boolean isDeleted;
}
