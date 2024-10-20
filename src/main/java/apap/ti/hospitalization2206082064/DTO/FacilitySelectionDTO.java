package apap.ti.hospitalization2206082064.DTO;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class FacilitySelectionDTO {
    private UUID patientId;
    private String roomId;
    private UUID nurseId;
    private List<UUID> selectedFacilityIds; 
    private List<FacilityDTO> facilities; 
}
