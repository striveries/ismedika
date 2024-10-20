package apap.ti.hospitalization2206082064.restservice;


import apap.ti.hospitalization2206082064.DTO.ReservationStats;
import apap.ti.hospitalization2206082064.restdto.response.ReservationResponseDTO;
import java.util.List;


public interface ReservationRestService {
    List<ReservationResponseDTO> getAllReservation();
    List<ReservationStats> getReservationStats(String period, int year);
    List<ReservationStats> getMonthlyReservationStats(int year);
    List<ReservationStats> getQuarterlyReservationStats(int year);
    public ReservationResponseDTO getReservationById(String id);
}
