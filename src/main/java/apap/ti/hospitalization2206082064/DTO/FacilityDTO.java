package apap.ti.hospitalization2206082064.DTO;

import java.util.UUID;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class FacilityDTO {
    private UUID id;
    private String name;
    private double fee;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean isDeleted;
  }
