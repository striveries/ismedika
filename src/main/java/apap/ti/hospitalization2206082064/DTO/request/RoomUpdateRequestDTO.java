package apap.ti.hospitalization2206082064.DTO.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor 
@NoArgsConstructor
@Data
@Setter
@Getter


public class RoomUpdateRequestDTO {

    @NotNull(message = "ID ruangan tidak boleh kosong")
    private String id;

    @NotNull(message = "Nama ruangan tidak boleh kosong")
    @Size(min = 3, max = 50, message = "Nama ruangan harus memiliki panjang antara 3 sampai 50 karakter")
    private String name;

    @Size(max = 255, message = "Deskripsi tidak boleh lebih dari 255 karakter")
    private String description;

    @NotNull(message = "Kapasitas maksimal harus diisi")
    private int maxCapacity;

    @NotNull(message = "Harga per hari harus diisi")
    private double pricePerDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    private boolean isDeleted;
}
