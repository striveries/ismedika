package apap.ti.hospitalization2206082064.controller;

import apap.ti.hospitalization2206082064.DTO.request.createRoomRequestDTO;
import apap.ti.hospitalization2206082064.DTO.request.RoomUpdateRequestDTO;
import apap.ti.hospitalization2206082064.model.Reservation;
import apap.ti.hospitalization2206082064.model.Room;
import apap.ti.hospitalization2206082064.service.RoomService;
import apap.ti.hospitalization2206082064.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/rooms/create")
    public String createRoomForm(Model model) {
        var roomDTO = new createRoomRequestDTO();

        model.addAttribute("roomDTO", roomDTO);
        return "form-create-room";
    }


    @PostMapping("/rooms/create")
    public String createRoom(@Valid @ModelAttribute("roomDTO") createRoomRequestDTO roomDTO,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "form-create-room"; 
        }

        var room = new Room();
        room.setName(roomDTO.getName());
        room.setDescription(roomDTO.getDescription());
        room.setMaxCapacity(roomDTO.getMaxCapacity());
        room.setPricePerDay(roomDTO.getPricePerDay());
        room.setCreatedDate(LocalDateTime.now());
        room.setUpdatedDate(LocalDateTime.now()); 

        roomService.addRoom(room);

        model.addAttribute("returnUrl", "/rooms");
        model.addAttribute("title", "Room");
        model.addAttribute("responseMessage",
                String.format("Room %s successfully added with ID: %s", room.getName(), room.getId()));
        return "success-create-feedback";
    }

    
    @GetMapping("/rooms/{roomId}")
    public String handleRoomDetail(@PathVariable("roomId") String roomId,
    @RequestParam(name="dateIn", required = false) String dateInStr,
    @RequestParam(name="dateOut", required = false) String dateOutStr,
    Model model) {

    LocalDate today = LocalDate.now();
    LocalDate dateIn;
    LocalDate dateOut;

    if (dateInStr == null || dateInStr.isEmpty()) {
        dateIn = today;  
    } else {
        dateIn = LocalDate.parse(dateInStr);
    }

    if (dateOutStr == null || dateOutStr.isEmpty()) {
        dateOut = today;  
    } else {
        dateOut = LocalDate.parse(dateOutStr);
    }
        var room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);

        List<Reservation> reservations = reservationService.getReservationsByRoomAndDateRange(roomId, dateIn, dateOut);

        int remainingQuota = room.getMaxCapacity() - reservations.size();
        model.addAttribute("reservations", reservations);
        model.addAttribute("remainingQuota", remainingQuota);
        model.addAttribute("dateIn", dateIn);
        model.addAttribute("dateOut", dateOut);
        
        return "view-room-detail"; 
    }


    
    @GetMapping("/rooms/{id}/update")
    public String updateRoomForm(@PathVariable("id") String id, Model model) {
        var room = roomService.getRoomById(id);

        var roomDTO = new RoomUpdateRequestDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setDescription(room.getDescription());
        roomDTO.setMaxCapacity(room.getMaxCapacity());
        roomDTO.setPricePerDay(room.getPricePerDay());
        roomDTO.setUpdatedDate(LocalDateTime.now()); 

        model.addAttribute("roomDTO", roomDTO);
        return "form-update-room";
    }

    @PostMapping("/rooms/{id}/update")
    public String updateRoom(@Valid @ModelAttribute("roomDTO") RoomUpdateRequestDTO roomDTO,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "form-update-room";  
        }

        var roomFromDTO = new Room();
        roomFromDTO.setId(roomDTO.getId());
        roomFromDTO.setName(roomDTO.getName());
        roomFromDTO.setDescription(roomDTO.getDescription());
        roomFromDTO.setMaxCapacity(roomDTO.getMaxCapacity());
        roomFromDTO.setPricePerDay(roomDTO.getPricePerDay());

        roomService.updateRoom(roomDTO.getId(),roomFromDTO);

        model.addAttribute("returnUrl", "/rooms");
        model.addAttribute("title", "Room");
        model.addAttribute("responseMessage",
                String.format("Room %s successfully updated with ID: %s", roomFromDTO.getName(), roomFromDTO.getId()));
        return "success-update-feedback";
    }

    @PostMapping("/rooms/{roomId}/delete")
    public String deleteRoom(@PathVariable("roomId") String roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        if (room != null) {
            roomService.deleteRoom(roomId); 
            model.addAttribute("responseMessage",
                    String.format("Room %s successfully deleted with ID: %s", room.getName(), room.getId()));
        } else {
            model.addAttribute("responseMessage", "Room not found");
        }
        model.addAttribute("returnUrl", "/rooms");
        model.addAttribute("title", "Room");
        return "success-delete-feedback"; 
    }

    @GetMapping("/rooms")
    public String listRooms(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "view-rooms"; 
    }
}
