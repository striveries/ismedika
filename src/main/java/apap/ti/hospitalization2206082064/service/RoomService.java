package apap.ti.hospitalization2206082064.service;

import apap.ti.hospitalization2206082064.model.Room;
import java.util.List;
import java.time.LocalDate;

public interface RoomService {
    List<Room> getAllRooms();
    Room getRoomById(String roomId);
    void addRoom(Room room);
    void updateRoom(String roomId, Room room);
    void deleteRoom(String id);
    int getTotalRooms();
    void saveRoom(Room roomDTO);
    List<Room> findAvailableRooms(LocalDate dateIn, LocalDate dateOut);
}