package apap.ti.hospitalization2206082064.service;

import apap.ti.hospitalization2206082064.model.Facility;
import apap.ti.hospitalization2206082064.repository.FacilityDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityDb facilityDb;

    @Override
    public List<Facility> getAllFacilities() {
        return facilityDb.findAll();
    }
    
    @Override
    public List<Facility> getFacilitiesByIds(List<UUID> facilityIds) {
        return facilityDb.findAllById(facilityIds);
    }

    @Override
    public Facility getFacilityById(UUID id) {
        Optional<Facility> facility = facilityDb.findById(id);
        return facility.orElse(null);
    }

    @Override
    public void addFacility(Facility facility) {
        facilityDb.save(facility);
    }

    @Override
    public void updateFacility(Facility facility) {
        facilityDb.save(facility);
    }

    @Override
    public void deleteFacility(UUID id) {
        facilityDb.deleteById(id);
    }
}
