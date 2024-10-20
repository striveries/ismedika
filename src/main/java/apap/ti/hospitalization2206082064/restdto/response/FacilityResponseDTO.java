package apap.ti.hospitalization2206082064.restdto.response;

import java.time.LocalDateTime;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class FacilityResponseDTO {
    private UUID id;

    private String name;

    private double fee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Jakarta")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Jakarta")
    private LocalDateTime updatedDate;

    private boolean isDeleted;
}
