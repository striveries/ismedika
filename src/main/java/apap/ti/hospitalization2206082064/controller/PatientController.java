package apap.ti.hospitalization2206082064.controller;

import apap.ti.hospitalization2206082064.DTO.request.SearchPatientDTO;
import apap.ti.hospitalization2206082064.DTO.PatientDTO;
import apap.ti.hospitalization2206082064.DTO.ReservationDTO;
import apap.ti.hospitalization2206082064.DTO.request.RoomUpdateRequestDTO;
import apap.ti.hospitalization2206082064.model.Nurse;
import apap.ti.hospitalization2206082064.model.Patient;
import apap.ti.hospitalization2206082064.service.PatientService;
import apap.ti.hospitalization2206082064.service. NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private NurseService nurseService;

    @GetMapping("/create")
    public String showPatientForm(Model model) {
        model.addAttribute("patientDTO", new PatientDTO());

        return "form-create-patient";
    }


    @PostMapping("/search")
    public String searchPatient(@ModelAttribute("searchPatientDTO") SearchPatientDTO searchPatientDTO, Model model) {
        Patient patient = patientService.getPatientByNIK(searchPatientDTO.getNik());
        if (patient == null) {
            model.addAttribute("patientNotFound", true);
            return "new-patient";
        } else {
            model.addAttribute("patient", patient);
            List<Nurse> nurses = nurseService.getAllNurses();
            model.addAttribute("nurses", nurses);
            return "patient-found";
        }
    }

    @PostMapping("/create")
    public String createNewPatient(@ModelAttribute("patientDTO") PatientDTO newPatientDTO, Model model) {
        Patient patient = new Patient();
        patient.setId(newPatientDTO.getId());
        patient.setNIK(newPatientDTO.getNIK());
        patient.setName(newPatientDTO.getName());
        patient.setGender(newPatientDTO.getGender());
        patient.setBirthDate(newPatientDTO.getBirthDate());
        patient.setEmail(newPatientDTO.getEmail());
        patient.setCreatedDate(LocalDateTime.now());
        patient.setUpdatedDate(LocalDateTime.now()); 
        
        patientService.addPatient(patient);
        model.addAttribute("patient", patient);

        List<Nurse> nurses = nurseService.getAllNurses();
        model.addAttribute("nurses", nurses);
        var reservationDTO = new ReservationDTO();
        model.addAttribute("reservationDTO", reservationDTO);
        return "select-room";  

    }
   
}
