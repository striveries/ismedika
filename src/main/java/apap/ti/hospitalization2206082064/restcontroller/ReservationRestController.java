package apap.ti.hospitalization2206082064.restcontroller;

import apap.ti.hospitalization2206082064.DTO.ReservationStats;
import apap.ti.hospitalization2206082064.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206082064.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206082064.restservice.ReservationRestService;   
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationRestController {

    @Autowired
    ReservationRestService reservationRestService;

    @GetMapping("")
    public ResponseEntity<?> listReservation() {
        var baseResponseDTO = new BaseResponseDTO<List<ReservationResponseDTO>>();
        List<ReservationResponseDTO> listReservation = reservationRestService.getAllReservation();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listReservation);
        baseResponseDTO.setMessage(String.format("List Reservation berhasil ditemukan"));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
    @GetMapping("/chart")
    public ResponseEntity<?> getReservationStats(
        @RequestParam(value = "period") String period, 
        @RequestParam(value = "year") int year) {
        try {
            var baseResponseDTO = new BaseResponseDTO<List<ReservationStats>>();

            var reservationStats = reservationRestService.getReservationStats(period, year);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(reservationStats);
            baseResponseDTO.setMessage(String.format("Reservation Stats berhasil ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching reservation statistics: " + e.getMessage());
        }
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable("id") String id) {
        try {
            var reservation = reservationRestService.getReservationById(id);

            if (reservation != null) {
                var baseResponseDTO = new BaseResponseDTO<ReservationResponseDTO>();
                baseResponseDTO.setStatus(HttpStatus.OK.value());
                baseResponseDTO.setData(reservation);
                baseResponseDTO.setMessage(String.format("Reservation Stats berhasil ditemukan"));
                baseResponseDTO.setTimestamp(new Date());
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
            } else {
                return ResponseEntity.status(404).body("Reservation with id " + id + " not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching reservation details: " + e.getMessage());
        }
    }
}