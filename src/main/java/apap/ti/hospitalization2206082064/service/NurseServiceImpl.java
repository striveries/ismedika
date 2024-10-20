package apap.ti.hospitalization2206082064.service;

import apap.ti.hospitalization2206082064.model.Nurse;
import apap.ti.hospitalization2206082064.repository.NurseDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NurseServiceImpl implements NurseService {

    @Autowired
    private NurseDb nurseDb;

    @Override
    public List<Nurse> getAllNurses() {
        return nurseDb.findAll();
    }

    @Override
    public Nurse getNurseById(UUID id) {
        Optional<Nurse> nurse = nurseDb.findById(id);
        return nurse.orElse(null);
    }

    @Override
    public void addNurse(Nurse nurse) {
        nurseDb.save(nurse);
    }

    @Override
    public void updateNurse(Nurse nurse) {
        nurseDb.save(nurse);
    }

    @Override
    public void deleteNurse(UUID id) {
        nurseDb.deleteById(id);
    }
}
