package apap.ti.hospitalization2206082064.service;
import apap.ti.hospitalization2206082064.DTO.ReservationStats;
import apap.ti.hospitalization2206082064.model.Reservation;
import apap.ti.hospitalization2206082064.repository.ReservationDb;
import apap.ti.hospitalization2206082064.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206082064.restdto.response.ReservationResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationDb reservationDb;
    @Override
    public List<Reservation> getAllReservations() {
        return reservationDb.findByIsDeletedFalseOrderByIdAsc().stream()
            .collect(Collectors.toList());
    }

    private final WebClient webClient;

    public ReservationServiceImpl(WebClient.Builder webClientBuilder, ReservationDb reservationDb) {
        this.webClient = webClientBuilder
            .baseUrl("http://localhost:8080/api")
            .build();
        this.reservationDb = reservationDb;
    }

    public List<Reservation> filterReservationsByDate(String roomId, LocalDate dateIn, LocalDate dateOut) {
        return reservationDb.findByRoomIdAndDateInBetween(roomId, dateIn, dateOut);
    }


    @Override
    public List<ReservationResponseDTO> getAllReservationFromRest() throws Exception {
        var response = webClient
            .get()
            .uri("/reservations")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<ReservationResponseDTO>>>() {})
            .block();

        if (response == null) {
            throw new Exception("Failed to consume API getAllReservation");
        }

        if (response.getStatus() != 200) {
            System.out.println(response);
            throw new Exception(response.getMessage());
        }

        return response.getData();
    }

    @Override
    public List<Map<String, Object>> getReservationStatsFromRest(
        @RequestParam String period, 
        @RequestParam int year) throws Exception {
        var response = webClient
            .get()
            .uri(String.format("/reservations/chart?period=%s&year=%d", period, year))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<ReservationStats>>>() {})
            .block();

        if (response == null) {
            throw new Exception("Failed to consume API get4ReservationStats");
        }

        if (response.getStatus() != 200) {
            System.out.println(response);
            throw new Exception(response.getMessage());
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (ReservationStats reservation : response.getData()) {
            Map<String, Object> item = new HashMap<>();
            item.put("period", reservation.getPeriod()); // Pastikan ada method getPeriod() di ReservationResponseDTO
            item.put("reservationCount", reservation.getReservationCount()); // Pastikan ada method getReservationCount() di ReservationResponseDTO
            result.add(item);
        }

        return result;
    }

    @Override
    public int getTotalReservations() {
        return reservationDb.findAll().size();
    }

    public List<String> getAvailableYears() {
        List<Integer> years = reservationDb.findAvailableYears();
        return years.stream().map(String::valueOf).collect(Collectors.toList());
    }

    @Override
    public Reservation getReservationById(String id) {
        Optional<Reservation> reservation = reservationDb.findById(id);
        return reservation.orElse(null);
    }

    @Override
    public void addReservation(Reservation reservation) {
        reservationDb.save(reservation);
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        Reservation getReservation = getReservationById(reservation.getId());
        if (getReservation != null) {
            getReservation.setId(reservation.getId());
            getReservation.setPatient(reservation.getPatient());
            getReservation.setTotalFee(reservation.getTotalFee());
            getReservation.setFacilities(reservation.getFacilities());
            getReservation.setNurse(reservation.getNurse());
            getReservation.setDateIn(reservation.getDateIn());
            getReservation.setDateOut(reservation.getDateOut());
            getReservation.setCreatedDate(reservation.getCreatedDate());
            getReservation.setRoom(reservation.getRoom());
            
            reservationDb.save(getReservation);

            return getReservation;
        }
        return null;
    }



    @Override
    public void deleteReservation(String id) {
        Reservation reservation = reservationDb.findById(id).orElse(null);
        if (reservation != null) {
            reservation.setDeleted(true);
            reservationDb.save(reservation);
        }
    }

    @Override
    public List<Reservation> getReservationsByRoomAndDateRange(String roomId, LocalDate dateIn, LocalDate dateOut) {
        return reservationDb.findByRoomIdAndDateInBetween(roomId, dateIn, dateOut);
    }
}
