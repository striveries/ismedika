package apap.ti.hospitalization2206082064.restservice;

import apap.ti.hospitalization2206082064.restdto.request.AddRoomRequestRestDTO;
import apap.ti.hospitalization2206082064.restdto.response.RoomResponseDTO;


public interface RoomRestService {
    public RoomResponseDTO addRoom(AddRoomRequestRestDTO roomDTO);
}
