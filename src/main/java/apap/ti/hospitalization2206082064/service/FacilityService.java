package apap.ti.hospitalization2206082064.service;

import apap.ti.hospitalization2206082064.model.Facility;
import java.util.List;
import java.util.UUID;

public interface FacilityService {
    List<Facility> getAllFacilities();
    Facility getFacilityById(UUID id);
    List<Facility> getFacilitiesByIds(List<UUID> facilityIds);
    void addFacility(Facility facility);
    void updateFacility(Facility facility);
    void deleteFacility(UUID id);
}
