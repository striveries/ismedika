package apap.ti.hospitalization2206082064.controller;

import apap.ti.hospitalization2206082064.DTO.request.SearchPatientDTO;

import apap.ti.hospitalization2206082064.DTO.ReservationDTO;

import apap.ti.hospitalization2206082064.model.Facility;
import apap.ti.hospitalization2206082064.model.Nurse;
import apap.ti.hospitalization2206082064.model.Patient;
import apap.ti.hospitalization2206082064.model.Reservation;
import apap.ti.hospitalization2206082064.model.Room;
import apap.ti.hospitalization2206082064.service.PatientService;
import apap.ti.hospitalization2206082064.service.ReservationService;
import apap.ti.hospitalization2206082064.service.RoomService;
import apap.ti.hospitalization2206082064.service.FacilityService;
import apap.ti.hospitalization2206082064.service.NurseService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Collections;

import java.text.NumberFormat;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final RoomService roomService;
    private final NurseService nurseService;
    private final PatientService patientService;
    private final FacilityService facilityService;

    public ReservationController(
        ReservationService reservationService,
        RoomService roomService,
        NurseService nurseService,
        PatientService patientService,
        FacilityService facilityService
        ) {
        this.reservationService = reservationService;
        this.roomService = roomService;
        this.nurseService = nurseService;
        this.patientService = patientService;
        this.facilityService = facilityService;
    }


    // pakai REST
    @GetMapping("")
    public String listRestReservation(Model model) {
        try {
            var listReservation = reservationService.getAllReservationFromRest();

            model.addAttribute("listReservation", listReservation);
            return "viewall-reservations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }
    }

    // pakai REST
    @GetMapping("/chart")
    public String getReservationStats( 
         Model model) {
        try {
            var reservationStats = reservationService.getReservationStatsFromRest("monthly", 2024);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResult = objectMapper.writeValueAsString(reservationStats);
            model.addAttribute("reservationStats", jsonResult);
            model.addAttribute("period", "Monthly");
            model.addAttribute("year", 2024);     
            List<String> availableYears = reservationService.getAvailableYears();
            model.addAttribute("availableYears", availableYears);         
            return "chart";
        } catch (Exception e) {
            model.addAttribute("responseMessage", e.getMessage());
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }
    }

    @GetMapping("/chart/search")
    public String searchReservationStats( 
            @RequestParam("selectedYear") int selectedYear,
            @RequestParam("selectedPeriod") String selectedPeriod,
            Model model) {
        try {
            var reservationStats = reservationService.getReservationStatsFromRest(selectedPeriod, selectedYear);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResult = objectMapper.writeValueAsString(reservationStats);
            model.addAttribute("reservationStats", jsonResult);
            model.addAttribute("period",selectedPeriod);
            model.addAttribute("year", selectedYear);   
            List<String> availableYears = reservationService.getAvailableYears();
            model.addAttribute("availableYears", availableYears);          
            return "chart";
        } catch (Exception e) {
            model.addAttribute("responseMessage", e.getMessage());
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }
    }


    @GetMapping("/{id}")
    public String viewReservationDetail(@PathVariable("id") String id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            model.addAttribute("errorMessage", "Reservation not found.");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }

        long days = ChronoUnit.DAYS.between(reservation.getDateIn(), reservation.getDateOut());
        if (days == 0) {
            days = 1; // Minimum stay of one day
        }

        double roomPricePerDay = reservation.getRoom().getPricePerDay();

        // Calculate room fee
        double roomFee = roomPricePerDay * days;

        // Get facilities and their fees
        List<Facility> facilities = reservation.getFacilities();

        // Prepare list of facilities with names and formatted fees
        List<Map<String, String>> facilityDetails = new ArrayList<>();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        for (Facility facility : facilities) {
            Map<String, String> facilityDetail = new HashMap<>();
            facilityDetail.put("name", facility.getName());
            facilityDetail.put("fee", currencyFormat.format(facility.getFee()));
            facilityDetails.add(facilityDetail);
        }

        // Calculate total facilities fee
        double facilitiesFee = facilities.stream()
                .mapToDouble(Facility::getFee)
                .sum();

        // Format monetary values
        String formattedRoomPricePerDay = currencyFormat.format(roomPricePerDay);
        String formattedRoomFee = currencyFormat.format(roomFee);
        String formattedFacilitiesFee = currencyFormat.format(facilitiesFee);
        String formattedTotalFee = currencyFormat.format(reservation.getTotalFee());

        model.addAttribute("reservation", reservation);
        model.addAttribute("days", days);
        model.addAttribute("roomPricePerDay", formattedRoomPricePerDay);
        model.addAttribute("roomFee", formattedRoomFee);
        model.addAttribute("facilityDetails", facilityDetails);
        model.addAttribute("facilitiesFee", formattedFacilitiesFee);
        model.addAttribute("formattedTotalFee", formattedTotalFee);

        return "view-reservation-detail";
    }

    @GetMapping("/create")
    public String showReservationForm(Model model) {
        List<Nurse> nurses = nurseService.getAllNurses();
        model.addAttribute("nurses", nurses);
        model.addAttribute("searchPatientDTO", new SearchPatientDTO());
        return "search-patient";
    }


    @PostMapping("/create")
    public String createReservation(@ModelAttribute("reservationDTO") ReservationDTO reservationDTO, Model model) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getId());
        reservation.setDateIn(reservationDTO.getDateIn());
        reservation.setDateOut(reservationDTO.getDateOut());
        reservation.setRoom(roomService.getRoomById(reservationDTO.getRoomId()));
        Nurse assignedNurse = nurseService.getNurseById(reservationDTO.getNurseId());
        reservation.setNurse(assignedNurse); 
        List<Nurse> nurses = nurseService.getAllNurses();
        model.addAttribute("nurses", nurses);        
        reservationService.addReservation(reservation);
        
        model.addAttribute("responseMessage", "Reservation " + reservation.getId() + " created successfully.");
        model.addAttribute("returnUrl", "/reservations");
        model.addAttribute("title", "Reservation");

        return "success-create-feedback";
    }

    @GetMapping("/search-room")
    public String searchRoomPage(@RequestParam("patientId") UUID patientId, Model model) {
        var reservationDTO = new ReservationDTO();
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("patient", patientService.getPatientById(patientId));
        model.addAttribute("patientId",patientId);
        List<Nurse> nurses = nurseService.getAllNurses();
        model.addAttribute("nurses", nurses);

        return "select-room";  
    }


    @PostMapping("/search-room")
    public String searchRoomForReservation( @Valid @ModelAttribute("reservationDTO") ReservationDTO reservationDTO, 
    @RequestParam("patientId") UUID patientId,
    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("responseMessage", "RESERVASI GAGAL DITAMBAHKAN!");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }
        if (reservationDTO.getDateIn().isAfter(reservationDTO.getDateOut())) {
            model.addAttribute("error", "Tanggal Masuk harus kurang dari atau sama dengan Tanggal Keluar.");

            List<Nurse> nurses = nurseService.getAllNurses();
            model.addAttribute("nurses", nurses);
            model.addAttribute("patient", patientService.getPatientById(reservationDTO.getPatientId()));
            return "select-room"; 
        }

        reservationDTO.setPatientId(patientId);
    
        List<Room> availableRooms = roomService.findAvailableRooms(reservationDTO.getDateIn(), reservationDTO.getDateOut());
        model.addAttribute("availableRooms", availableRooms);  // Add available rooms to the model
    
        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("patient", patientService.getPatientById(reservationDTO.getPatientId()));
        List<Nurse> nurses = nurseService.getAllNurses();
        model.addAttribute("nurses", nurses);
        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("reservationDTO", reservationDTO);
    
        return "select-room";  
    }

    @GetMapping("/select-facilities")
    public String selectFacilities( @Valid @ModelAttribute("reservationDTO") ReservationDTO reservationDTO,
    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
     
            model.addAttribute("errorMessages", bindingResult.getAllErrors());  // Tambahkan pesan error ke model
            model.addAttribute("responseMessage", "RESERVASI GAGAL DITAMBAHKAN!");
    
            return "response-reservation";
        }
    
        List<Facility> listFacilities = facilityService.getAllFacilities();

        model.addAttribute("reservationDTO", reservationDTO);

        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("roomId", reservationDTO.getRoomId());
        model.addAttribute("room", roomService.getRoomById(reservationDTO.getRoomId()));
        model.addAttribute("listFacilities", listFacilities);
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());
        return "select-facilities";
    }

    @PostMapping("/finalize-reservation")
    public String finalizeReservation(@Valid @ModelAttribute("reservationDTO") ReservationDTO reservationDTO,
    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
     
            model.addAttribute("responseMessage", "RESERVASI GAGAL DITAMBAHKAN!");
    
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }
        System.out.println(reservationDTO.getPatientId());
        LocalDate dateIn = reservationDTO.getDateIn();
        LocalDate dateOut = reservationDTO.getDateOut();
        Room room = roomService.getRoomById(reservationDTO.getRoomId());   
        Patient patient = patientService.getPatientById(reservationDTO.getPatientId());
        List<Facility> selectedFacilities = reservationDTO.getFacilities() != null ? 
        reservationDTO.getFacilities() : 
        Collections.emptyList();

        double totalFee = room.getPricePerDay() * (dateOut.toEpochDay() - dateIn.toEpochDay());

        String reservationId = generateReservationId(dateIn, dateOut, patient.getNIK());

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setDateIn(dateIn);
        reservation.setDateOut(dateOut);
        reservation.setRoom(room);
        reservation.setPatient(patient);
        reservation.setNurse(nurseService.getNurseById(reservationDTO.getNurseId()));
        reservation.setFacilities(selectedFacilities);
        reservation.setCreatedDate(LocalDateTime.now());
        reservation.setUpdatedDate(LocalDateTime.now()); 

        double facilitiesFee = reservation.getFacilities().stream()
                .mapToDouble(Facility::getFee)
                .sum();

        double fee = totalFee + facilitiesFee;
        System.out.println("total fee: " + fee);
        System.out.println("facilities fee: " + facilitiesFee);

        reservation.setTotalFee(fee);

        // Save the reservation
        reservationService.addReservation(reservation);

        model.addAttribute("reservationDTO", reservationDTO);

        List<Facility> facilities = facilityService.getAllFacilities();


        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("roomId", reservationDTO.getRoomId());
        model.addAttribute("room", roomService.getRoomById(reservationDTO.getRoomId()));
        model.addAttribute("facilities", facilities);
        model.addAttribute("selectedFacilities", selectedFacilities);
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());

        model.addAttribute("responseMessage", "Reservation created successfully with ID: " + reservationId);
        model.addAttribute("returnUrl", "/reservations");
        model.addAttribute("title", "Reservations");
        return "success-create-feedback";
    }

    // Generate unique reservation ID
    private String generateReservationId(LocalDate dateIn, LocalDate dateOut, String patientNIK) {
        String id = "RES";
        long days = dateOut.toEpochDay() - dateIn.toEpochDay();
        String dayDiff = String.format("%02d", Math.min(Math.max(days, 0), 99));
        String dayOfWeek = dateIn.getDayOfWeek().toString().substring(0, 3).toUpperCase();
        String nikEnding = patientNIK.substring(patientNIK.length() - 4);
        int reservationCount = reservationService.getTotalReservations();

        id += dayDiff + dayOfWeek + nikEnding + String.format("%04d", reservationCount);
        return id;
    }
    
    @PostMapping(value = "/finalize-reservation", params = "addRow")
    public String addRowFacilityReservation(@Valid @ModelAttribute("reservationDTO")  ReservationDTO reservationDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
     
            model.addAttribute("responseMessage", "RESERVASI GAGAL DITAMBAHKAN!");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";        }
        if (reservationDTO.getFacilities() == null) {
            reservationDTO.setFacilities(new ArrayList<>());
        }
        reservationDTO.getFacilities().add(new Facility());
        System.out.println("hello!");


        model.addAttribute("listFacilities", facilityService.getAllFacilities());
        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("room", roomService.getRoomById(reservationDTO.getRoomId()));
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());
        model.addAttribute("reservationDTO", reservationDTO);


        return "select-facilities";  
    }


    @PostMapping(value = "/finalize-reservation", params = "deleteRow")
    public String deleteRowFacilityReservation(@Valid @ModelAttribute("reservationDTO")  ReservationDTO reservationDTO, BindingResult bindingResult,
    @RequestParam("deleteRow") int row, Model model) {
        if (bindingResult.hasErrors()) {
                 model.addAttribute("responseMessage", "RESERVASI GAGAL DITAMBAHKAN!");
    
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";        }

        reservationDTO.getFacilities().remove(row);

        model.addAttribute("listFacilities", facilityService.getAllFacilities());
        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("room", roomService.getRoomById(reservationDTO.getRoomId()));
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());
        model.addAttribute("reservationDTO", reservationDTO);

        return "select-facilities";  
    }

    @GetMapping("/{reservationId}/update-facilities")
    public String showUpdateFacilitiesForm(@PathVariable("reservationId") String reservationId, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation == null) {
            model.addAttribute("errorMessage", "Reservation not found.");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";        
        }

        if (LocalDate.now().isAfter(reservation.getDateOut())) {
            model.addAttribute("errorMessage", "Cannot update facilities. Reservation has already ended.");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";        
        }

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());
        reservationDTO.setFacilities(reservation.getFacilities()); 
        reservationDTO.setDateIn(reservation.getDateIn());
        reservationDTO.setDateOut(reservation.getDateOut());
        reservationDTO.setRoomId(reservation.getRoom().getId());
        reservationDTO.setPatientId(reservation.getPatient().getId());
        reservationDTO.setNurseId(reservation.getNurse().getId());

        model.addAttribute("listFacilities", facilityService.getAllFacilities());
        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("room", roomService.getRoomById(reservationDTO.getRoomId()));
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("id", reservationDTO.getId());

        return "update-facilities"; 
    }

    @PostMapping("/update-facilities")
    public String updateFacilities(
                                   @Valid @ModelAttribute("reservationDTO") ReservationDTO reservationDTO,
                                   BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessages", bindingResult.getAllErrors());
            model.addAttribute("listFacilities", facilityService.getAllFacilities());
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";           
        }

        Reservation reservation = reservationService.getReservationById(reservationDTO.getId());
        if (reservation == null) {
            model.addAttribute("errorMessage", "Reservation not found.");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";           
        }

        List<Facility> selectedFacilities = reservationDTO.getFacilities();
        double facilitiesFee = 0.0;
        for (Facility facility : selectedFacilities) {
            Facility fac = facilityService.getFacilityById(facility.getId());
            facilitiesFee += fac.getFee();
        }


        long days = ChronoUnit.DAYS.between(reservation.getDateIn(), reservation.getDateOut());
        if (days == 0) days = 1; // Minimum stay of one day

        double roomFee = reservation.getRoom().getPricePerDay() * days;
        double totalFee = roomFee + facilitiesFee;

        reservation.setFacilities(selectedFacilities);
        reservation.setTotalFee(totalFee);
        reservation.setUpdatedDate(LocalDateTime.now()); 
        reservationService.updateReservation(reservation);
        
        model.addAttribute("responseMessage", "Reservation updated successfully.");
        model.addAttribute("listFacilities", facilityService.getAllFacilities());
        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("room", roomService.getRoomById(reservationDTO.getRoomId()));
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("id", reservationDTO.getId());

        model.addAttribute("returnUrl", "/reservations");
        model.addAttribute("title", "Reservations");
        return "success-update-feedback";
    }

    @PostMapping(value = "/update-facilities", params = "addRow")
    public String addRowUpdateFacilityReservation(
        @Valid @ModelAttribute("reservationDTO") ReservationDTO reservationDTO,BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("responseMessage", "RESERVASI GAGAL DITAMBAHKAN!");
    
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";           
        
        }
        if (reservationDTO.getFacilities() == null || reservationDTO.getFacilities().isEmpty()) {
            reservationDTO.setFacilities(new ArrayList<>());
        }
        reservationDTO.getFacilities().add(new Facility());
        System.out.println("hello!");


        model.addAttribute("listFacilities", facilityService.getAllFacilities());
        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("room", roomService.getRoomById(reservationDTO.getRoomId()));
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("id", reservationDTO.getId());


        return "update-facilities";  
    }

    @PostMapping(value = "/update-facilities", params = "deleteRow")
    public String deleteRowUpdateFacilityReservation(
        @Valid @ModelAttribute("reservationDTO") ReservationDTO reservationDTO, BindingResult bindingResult,
    @RequestParam("deleteRow") int row, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("responseMessage", "RESERVASI GAGAL DITAMBAHKAN!");
    
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";           
        }

        reservationDTO.getFacilities().remove(row);

        model.addAttribute("listFacilities", facilityService.getAllFacilities());
        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("room", roomService.getRoomById(reservationDTO.getRoomId()));
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("id", reservationDTO.getId());

        return "update-facilities";  
    }

    @GetMapping("/{reservationId}/update-room")
    public String showUpdateRoomForm(@PathVariable("reservationId") String reservationId, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation == null) {
            model.addAttribute("responseMessage", "Reservation not found.");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }

        if (LocalDate.now().isAfter(reservation.getDateOut())) {
            model.addAttribute("responseMessage", "Cannot update facilities. Reservation has already ended.");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";        }

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());
        reservationDTO.setFacilities(reservation.getFacilities()); 
        reservationDTO.setDateIn(reservation.getDateIn());
        reservationDTO.setDateOut(reservation.getDateOut());
        reservationDTO.setRoomId(reservation.getRoom().getId());
        reservationDTO.setPatientId(reservation.getPatient().getId());
        reservationDTO.setNurseId(reservation.getNurse().getId());
        List<Nurse> nurses = nurseService.getAllNurses();
        model.addAttribute("nurses", nurses);
        model.addAttribute("listFacilities", facilityService.getAllFacilities());
        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("roomId", reservationDTO.getRoomId());
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("id", reservationDTO.getId());

        return "update-room"; 
    }


    @PostMapping("/update-room")
    public String updateRoom(
            @Valid @ModelAttribute("reservationDTO") ReservationDTO newReservationDTO,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("responseMessage", bindingResult.getAllErrors());
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }

        Reservation reservation = reservationService.getReservationById(newReservationDTO.getId());
        if (reservation == null) {
            model.addAttribute("errorMessage", "Reservation not found.");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }
        if (newReservationDTO.getRoomId() == null) {
            model.addAttribute("errorMessage", 
            String.format("room idnya null :  %s.", newReservationDTO.getRoomId()));

            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }
        Reservation updatedReservation = new Reservation();
        updatedReservation.setId(newReservationDTO.getId());
        updatedReservation.setPatient(reservation.getPatient());
        updatedReservation.setTotalFee(reservation.getTotalFee());
        updatedReservation.setFacilities(reservation.getFacilities());
        updatedReservation.setCreatedDate(reservation.getCreatedDate());
        updatedReservation.setNurse(nurseService.getNurseById(newReservationDTO.getNurseId()));
        updatedReservation.setDateIn(newReservationDTO.getDateIn());
        updatedReservation.setDateOut(newReservationDTO.getDateOut());
        updatedReservation.setRoom(roomService.getRoomById(newReservationDTO.getRoomId()));
        updatedReservation.setUpdatedDate(LocalDateTime.now()); 


        System.out.println(newReservationDTO.getDateIn());

        var reservationResponse = reservationService.updateReservation(updatedReservation);

        if (reservationResponse != null) {
            model.addAttribute("responseMessage",
                    String.format("Reservation dengan ID %s berhasil diupdate.", reservationResponse.getId()));
            model.addAttribute("currentPage", "reservation");
        } else {
            model.addAttribute("responseMessage", "Gagal mengupdate reservation.");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";
        }  


        model.addAttribute("listFacilities", facilityService.getAllFacilities());
        model.addAttribute("patientId", updatedReservation.getPatient().getId());
        model.addAttribute("roomId", newReservationDTO.getRoomId());
        model.addAttribute("nurseId", updatedReservation.getNurse().getId());
        model.addAttribute("dateIn", updatedReservation.getDateIn());
        model.addAttribute("dateOut", updatedReservation.getDateOut());
        model.addAttribute("updatedReservation", reservationResponse);
        model.addAttribute("id", updatedReservation.getId());

        model.addAttribute("returnUrl", "/reservations");
        model.addAttribute("title", "Reservations");
        return "success-update-feedback";
    }

    @PostMapping("/search-room-update")
    public String searchRoomForUpdate ( @Valid @ModelAttribute("reservationDTO") ReservationDTO reservationDTO, 
    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
     
            model.addAttribute("responseMessage", "RESERVASI GAGAL DITAMBAHKAN!");
    
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";           
        }
    
        if ( reservationDTO.getDateIn().isAfter(reservationDTO.getDateOut())) {
            model.addAttribute("error", "Tanggal Masuk harus kurang dari atau sama dengan Tanggal Keluar.");

            List<Nurse> nurses = nurseService.getAllNurses();
            model.addAttribute("nurses", nurses);
            model.addAttribute("patient", patientService.getPatientById(reservationDTO.getPatientId()));
            return "update-room"; 
        }

    
        ReservationDTO newReservationDTO = new ReservationDTO();
    
        List<Room> availableRooms = roomService.findAvailableRooms(reservationDTO.getDateIn(), reservationDTO.getDateOut());
        model.addAttribute("availableRooms", availableRooms); 
    
        model.addAttribute("patientId", reservationDTO.getPatientId());
        model.addAttribute("patient", patientService.getPatientById(reservationDTO.getPatientId()));
        List<Nurse> nurses = nurseService.getAllNurses();
        model.addAttribute("nurses", nurses);
        model.addAttribute("roomId", reservationDTO.getRoomId());
        model.addAttribute("room", reservationDTO.getRoom());

        model.addAttribute("dateIn", reservationDTO.getDateIn());
        model.addAttribute("dateOut", reservationDTO.getDateOut());
        model.addAttribute("nurseId", reservationDTO.getNurseId());
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("newReservationDTO", newReservationDTO);
        model.addAttribute("id", reservationDTO.getId());

        return "update-room";  
    }

    @PostMapping("/{reservationId}/delete")
    public String deletereservation(@PathVariable("reservationId") String reservationId, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation != null) {
            reservationService.deleteReservation(reservationId); 
            model.addAttribute("responseMessage",
                    String.format("Berhasil menghapus reservasi %s", reservation.getId()));
        model.addAttribute("returnUrl", "/reservations");
        model.addAttribute("title", "Reservations");
        return "success-delete-feedback";           
                
        } else {
            model.addAttribute("responseMessage", "Reservation not found");
            model.addAttribute("returnUrl", "/reservations");
            return "error-feedback";           
        
        }
    }


}