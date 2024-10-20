package apap.ti.hospitalization2206082064.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import apap.ti.hospitalization2206082064.model.Room;
import apap.ti.hospitalization2206082064.repository.RoomDb;
import apap.ti.hospitalization2206082064.restdto.request.AddRoomRequestRestDTO;
import apap.ti.hospitalization2206082064.restdto.response.RoomResponseDTO;

import java.time.LocalDateTime;

@Service
@Transactional
public class RoomRestServiceImpl implements RoomRestService {

    @Autowired
    RoomDb roomDb;


    @Override
    public RoomResponseDTO addRoom(AddRoomRequestRestDTO roomDTO) {
        Room room = new Room();
        
        int roomCount = (int) roomDb.count(); 
        String newRoomId = String.format("RM%04d", roomCount + 1); 
        room.setId(newRoomId); 
        room.setName(roomDTO.getName());
        room.setDescription(roomDTO.getDescription());
        room.setMaxCapacity(roomDTO.getMaxCapacity());
        room.setPricePerDay(roomDTO.getPricePerDay());
        room.setCreatedDate(LocalDateTime.now());
        room.setUpdatedDate(LocalDateTime.now()); 
        room.setDeleted(false); 
        
        Room savedRoom = roomDb.save(room);
        return roomToRoomResponseDTO(savedRoom);
    }


    private RoomResponseDTO roomToRoomResponseDTO(Room room) {
        RoomResponseDTO roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setId(room.getId());
        roomResponseDTO.setName(room.getName());
        roomResponseDTO.setDescription(room.getDescription());
        roomResponseDTO.setMaxCapacity(room.getMaxCapacity());
        roomResponseDTO.setPricePerDay(room.getPricePerDay());
        roomResponseDTO.setCreatedDate(room.getCreatedDate());
        roomResponseDTO.setUpdatedDate(room.getUpdatedDate());
        roomResponseDTO.setDeleted(room.isDeleted());
        return roomResponseDTO;
    }
}
