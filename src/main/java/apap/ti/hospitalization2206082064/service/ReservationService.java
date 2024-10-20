package apap.ti.hospitalization2206082064.service;

import apap.ti.hospitalization2206082064.model.Reservation;
import apap.ti.hospitalization2206082064.restdto.response.ReservationResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

public interface ReservationService {
    List<Reservation> getAllReservations();
    List<ReservationResponseDTO> getAllReservationFromRest() throws Exception;
    List<Map<String, Object>> getReservationStatsFromRest(
        @RequestParam String period, 
        @RequestParam int year) throws Exception;
    Reservation getReservationById(String id);
    void addReservation(Reservation reservation);
    Reservation updateReservation(Reservation reservation);
    void deleteReservation(String id);
    List<Reservation> getReservationsByRoomAndDateRange(String roomId, LocalDate dateIn, LocalDate dateOut);
    List<Reservation> filterReservationsByDate(String roomId, LocalDate dateIn, LocalDate dateOut);
    int getTotalReservations();
    List<String> getAvailableYears();
}

