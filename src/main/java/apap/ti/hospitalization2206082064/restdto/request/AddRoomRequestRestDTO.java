package apap.ti.hospitalization2206082064.restdto.request;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class AddRoomRequestRestDTO {
    private String name;
    private String description;
    private int maxCapacity;
    private double pricePerDay;
}
