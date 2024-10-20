package apap.ti.hospitalization2206082064.DTO.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class createRoomRequestDTO {
    @NotNull(message = "Nama ruangan tidak boleh kosong")
    @Size(min = 3, max = 50, message = "Nama ruangan harus memiliki panjang antara 3 sampai 50 karakter")
    private String name;

    @Size(max = 255, message = "Deskripsi tidak boleh lebih dari 255 karakter")
    private String description;

    @NotNull(message = "Kapasitas maksimal ruangan harus diisi")
    @Min(value = 1, message = "Kapasitas ruangan minimal 1")
    private int maxCapacity;

    @NotNull(message = "Harga per hari harus diisi")
    @Min(value = 1, message = "Harga per hari minimal 1")
    private double pricePerDay;

    @AssertTrue(message = "Harga harus lebih besar dari 0")
    public boolean isPriceValid() {
        return pricePerDay > 0;
    }
}
