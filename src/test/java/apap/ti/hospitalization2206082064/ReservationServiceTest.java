package apap.ti.hospitalization2206082064;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import apap.ti.hospitalization2206082064.model.Reservation;
import apap.ti.hospitalization2206082064.model.Room;
import apap.ti.hospitalization2206082064.repository.ReservationDb;
import apap.ti.hospitalization2206082064.service.ReservationServiceImpl;
import apap.ti.hospitalization2206082064.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {


    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationDb reservationDb;

    @Mock
    private RoomService roomService;

    @Mock
    private WebClient.Builder webClientBuilder;

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        // Create a mock instance of WebClient
        webClient = mock(WebClient.class);
        
        // Set up the webClientBuilder to return itself
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        
        reservationService = new ReservationServiceImpl(webClientBuilder, reservationDb);
    }

    @Test
    public void testFilterReservationsByDate() {
        // Define the test data
        String roomId = "RM000";
        LocalDate dateIn = LocalDate.of(2023, 1, 1);
        LocalDate dateOut = LocalDate.of(2023, 10, 31);

        Room room = new Room();
        room.setId(roomId);

        Reservation reservation1 = new Reservation();
        reservation1.setId("RES05FRI34560345");
        reservation1.setRoom(room);
        reservation1.setDateIn(LocalDate.of(2023, 10, 5));
        reservation1.setDateOut(LocalDate.of(2023, 10, 10));

        Reservation reservation2 = new Reservation();
        reservation2.setId("RES05FRI34560346");
        reservation2.setRoom(room);
        reservation2.setDateIn(LocalDate.of(2023, 10, 15));
        reservation2.setDateOut(LocalDate.of(2023, 10, 20));


        List<Reservation> mockReservations = Arrays.asList(reservation1, reservation2);

        when(reservationDb.findByRoomIdAndDateInBetween(roomId, dateIn, dateOut)).thenReturn(mockReservations);
        List<Reservation> filteredReservations = reservationService.getReservationsByRoomAndDateRange(roomId, dateIn, dateOut);

        assertEquals(roomId, reservation1.getRoom().getId());
        assertEquals(2, filteredReservations.size());
        assertEquals("RES05FRI34560345", filteredReservations.get(0).getId());
        assertEquals("RES05FRI34560346", filteredReservations.get(1).getId());

        verify(reservationDb).findByRoomIdAndDateInBetween(roomId, dateIn, dateOut);
    }
}
