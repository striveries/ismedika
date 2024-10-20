package apap.ti.hospitalization2206082064.repository;

import apap.ti.hospitalization2206082064.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomDb extends JpaRepository<Room, String> {
    List<Room> findByIsDeletedFalseOrderByIdAsc();
    
    @Query("SELECT r FROM Room r WHERE r.isDeleted = False AND r.id NOT IN (" +
    "SELECT res.room.id FROM Reservation res " +
    "WHERE (res.dateIn <= :dateOut AND res.dateOut >= :dateIn) AND (res.isDeleted = False)" +
    ")")
    List<Room> findAvailableRooms(@Param("dateIn") LocalDate dateIn, @Param("dateOut") LocalDate dateOut);
}
