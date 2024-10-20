package apap.ti.hospitalization2206082064.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apap.ti.hospitalization2206082064.DTO.ReservationStats;
import apap.ti.hospitalization2206082064.model.Reservation;

@Repository
public interface ReservationDb extends JpaRepository<Reservation, String> {
       List<Reservation> findByIsDeletedFalseOrderByIdAsc();

       List<Reservation> findByRoomIdAndDateInBetween(String roomId, LocalDate dateIn, LocalDate dateOut);

       @Query("SELECT DISTINCT YEAR(r.dateIn) FROM Reservation r")
       List<Integer> findAvailableYears();

       @Query("SELECT new apap.ti.hospitalization2206082064.DTO.ReservationStats(m.month, COALESCE(COUNT(r), 0)) " +
       "FROM (SELECT 1 AS month UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL " +
       "SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL " +
       "SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12) m " +
       "LEFT JOIN Reservation r ON MONTH(r.dateIn) = m.month AND YEAR(r.dateIn) = :year " +
       "GROUP BY m.month " +
       "ORDER BY m.month")
       List<ReservationStats> findMonthlyStats(@Param("year") int year);

       
       @Query("SELECT new apap.ti.hospitalization2206082064.DTO.ReservationStats(EXTRACT(QUARTER FROM r.dateIn), COALESCE(COUNT(r), 0)) " +
       "FROM Reservation r " +
       "WHERE YEAR(r.dateIn) = :year " +
       "GROUP BY EXTRACT(QUARTER FROM r.dateIn) " +
       "ORDER BY EXTRACT(QUARTER FROM r.dateIn)")
       List<ReservationStats> findQuarterlyStats(@Param("year") int year);
}