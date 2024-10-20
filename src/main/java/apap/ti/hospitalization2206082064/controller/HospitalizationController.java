package apap.ti.hospitalization2206082064.controller;

import apap.ti.hospitalization2206082064.service.PatientService;
import apap.ti.hospitalization2206082064.service.ReservationService;
import apap.ti.hospitalization2206082064.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HospitalizationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/")
    public String homePage(Model model) {
        long totalReservations = reservationService.getAllReservations().size();
        long totalRooms = roomService.getAllRooms().size();
        long totalPatients = patientService.getAllPatients().size();
        model.addAttribute("totalReservations", totalReservations);
        model.addAttribute("totalRooms", totalRooms);
        model.addAttribute("totalPatients", totalPatients);

        return "view-home.html";
    }
}
