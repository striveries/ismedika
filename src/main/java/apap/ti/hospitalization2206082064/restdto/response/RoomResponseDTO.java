package apap.ti.hospitalization2206082064.restdto.response;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class RoomResponseDTO {
    private String id;

    private String name;

    private String description;

    private int maxCapacity;

    private double pricePerDay;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Jakarta")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Jakarta")
    private LocalDateTime updatedDate;

    private boolean isDeleted;
}
