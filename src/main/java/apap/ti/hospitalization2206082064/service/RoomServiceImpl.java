package apap.ti.hospitalization2206082064.service;

import apap.ti.hospitalization2206082064.model.Room;
import apap.ti.hospitalization2206082064.repository.RoomDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomDb roomDb;

    @Override
    public List<Room> getAllRooms() {
        return roomDb.findByIsDeletedFalseOrderByIdAsc().stream()
            .collect(Collectors.toList());
    }

    public List<Room> findAvailableRooms(LocalDate dateIn, LocalDate dateOut) {
        return roomDb.findAvailableRooms(dateIn, dateOut);
    }

    @Override
    public Room getRoomById(String id) {
        for (Room room : getAllRooms()) {
            if (room.getId().equals(id)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public void addRoom(Room room) {
        int roomCount = (int) roomDb.count(); // Count existing rooms
        String newRoomId = String.format("RM%04d", roomCount + 1); // e.g., RM0001, RM0002
    
        // Set the manually generated ID before saving
        room.setId(newRoomId);
        roomDb.save(room);
    }

    @Override
    public void updateRoom(String roomId, Room room) {
        Room getRoom = roomDb.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
        if (getRoom != null) {
            room.setName(room.getName());
            room.setMaxCapacity(room.getMaxCapacity());
            room.setPricePerDay(room.getPricePerDay());
            room.setDescription(room.getDescription());
            roomDb.save(room);
        }
    }

    @Override
    public void deleteRoom(String id) {
        Room room = roomDb.findById(id).orElse(null);
        if (room != null) {
            room.setDeleted(true);
            roomDb.save(room);
        }
    }

    @Override
    public void saveRoom(Room room) {
        roomDb.save(room);
    }

    @Override
    public int getTotalRooms() {
        return (int) roomDb.count();
    }


}
