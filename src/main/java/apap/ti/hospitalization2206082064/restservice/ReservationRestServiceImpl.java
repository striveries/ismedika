package apap.ti.hospitalization2206082064.restservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import apap.ti.hospitalization2206082064.DTO.ReservationStats;
import apap.ti.hospitalization2206082064.model.Facility;
import apap.ti.hospitalization2206082064.model.Nurse;
import apap.ti.hospitalization2206082064.model.Patient;
import apap.ti.hospitalization2206082064.model.Reservation;
import apap.ti.hospitalization2206082064.model.Room;
import apap.ti.hospitalization2206082064.repository.ReservationDb;
import apap.ti.hospitalization2206082064.restdto.response.FacilityResponseDTO;
import apap.ti.hospitalization2206082064.restdto.response.NurseResponseDTO;
import apap.ti.hospitalization2206082064.restdto.response.PatientResponseDTO;
import apap.ti.hospitalization2206082064.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206082064.restdto.response.RoomResponseDTO;

import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationRestServiceImpl implements ReservationRestService {
    @Autowired
    ReservationDb reservationDb;
   
    @Override
    public List<ReservationResponseDTO> getAllReservation() {

        var listReservation = reservationDb.findByIsDeletedFalseOrderByIdAsc().stream()
        .collect(Collectors.toList());
        
        var listReservationResponseDTO = new ArrayList<ReservationResponseDTO>();
        listReservation.forEach(Reservation -> {
            var ReservationResponseDTO =ReservationToReservationResponseDTO(Reservation);
            listReservationResponseDTO.add(ReservationResponseDTO);
        });

        return listReservationResponseDTO;
    }

    public List<ReservationStats> getReservationStats(@RequestParam(value = "period") String period, 
        @RequestParam(value = "year") int year) {
        if (period.equalsIgnoreCase("monthly")) {
            return getMonthlyReservationStats(year);
        } else if (period.equalsIgnoreCase("quarterly")) {
            return getQuarterlyReservationStats(year);
        }
        throw new IllegalArgumentException("Invalid period: " + period);
    }

    public List<ReservationStats> getMonthlyReservationStats(int year) {
        return reservationDb.findMonthlyStats(year);
    }

    public List<ReservationStats> getQuarterlyReservationStats(int year) {
        return reservationDb.findQuarterlyStats(year);
    }

    public ReservationResponseDTO getReservationById(String id) {
        return ReservationToReservationResponseDTO(reservationDb.findById(id).orElse(null));
    }


    public ReservationResponseDTO ReservationToReservationResponseDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationResponseDTO dto = new ReservationResponseDTO();

        dto.setId(reservation.getId());
        dto.setDateIn(reservation.getDateIn());
        dto.setDateOut(reservation.getDateOut());
        dto.setTotalFee(reservation.getTotalFee());
        dto.setDeleted(reservation.isDeleted());
        dto.setCreatedDate(reservation.getCreatedDate());
        dto.setUpdatedDate(reservation.getUpdatedDate());

        // Map associated entities to their DTOs
        dto.setPatient(PatientToPatientResponseDTO(reservation.getPatient()));
        dto.setRoom(RoomToRoomResponseDTO(reservation.getRoom()));
        dto.setNurse(NurseToNurseResponseDTO(reservation.getNurse()));

        // Map facilities to FacilityResponseDTO list
        if (reservation.getFacilities() != null) {
            List<FacilityResponseDTO> facilityDTOs = reservation.getFacilities().stream()
                    .map(this::FacilityToFacilityResponseDTO)
                    .collect(Collectors.toList());
            dto.setFacilities(facilityDTOs);
        } else {
            dto.setFacilities(new ArrayList<>());
        }

        return dto;
    }
    
    public PatientResponseDTO PatientToPatientResponseDTO(Patient patient) {
        if (patient == null) {
            return null;
        }

        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setBirthDate(patient.getBirthDate());
        dto.setGender(patient.isGender() ? "Male" : "Female");
        dto.setCreatedDate(patient.getCreatedDate());
        dto.setUpdatedDate(patient.getUpdatedDate());
        dto.setDeleted(patient.isDeleted());
        return dto;
    }
    public RoomResponseDTO RoomToRoomResponseDTO(Room room) {
        if (room == null) {
            return null;
        }

        RoomResponseDTO dto = new RoomResponseDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setDescription(room.getDescription());
        dto.setMaxCapacity(room.getMaxCapacity());
        dto.setPricePerDay(room.getPricePerDay());
        dto.setCreatedDate(room.getCreatedDate());
        dto.setUpdatedDate(room.getUpdatedDate());
        dto.setDeleted(room.isDeleted());
        return dto;
    }

    public NurseResponseDTO NurseToNurseResponseDTO(Nurse nurse) {
        if (nurse == null) {
            return null;
        }

        NurseResponseDTO dto = new NurseResponseDTO();
        dto.setId(nurse.getId());
        dto.setName(nurse.getName());
        dto.setEmail(nurse.getEmail());
        dto.setGender(nurse.isGender() ? "Male" : "Female");
        dto.setCreatedDate(nurse.getCreatedDate());
        dto.setUpdatedDate(nurse.getUpdatedDate());
        dto.setDeleted(nurse.isDeleted());
        return dto;
    }

    public FacilityResponseDTO FacilityToFacilityResponseDTO(Facility facility) {
        if (facility == null) {
            return null;
        }

        FacilityResponseDTO dto = new FacilityResponseDTO();
        dto.setId(facility.getId());
        dto.setName(facility.getName());
        dto.setFee(facility.getFee());
        dto.setCreatedDate(facility.getCreatedDate());
        dto.setUpdatedDate(facility.getUpdatedDate());
        dto.setDeleted(facility.isDeleted());
        return dto;
    }





}

