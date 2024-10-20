package apap.ti.hospitalization2206082064.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Data
@AllArgsConstructor
public class ReservationStats {
    private int period;  // Month number or quarter number
    private long reservationCount;  // Total reservations

    // Default constructor (optional if using Lombok @Data)
    public ReservationStats() {}

}
