package apap.ti.hospitalization2206082064.service;

import apap.ti.hospitalization2206082064.model.Nurse;
import java.util.List;
import java.util.UUID;

public interface NurseService {
    List<Nurse> getAllNurses();
    Nurse getNurseById(UUID id);
    void addNurse(Nurse nurse);
    void updateNurse(Nurse nurse);
    void deleteNurse(UUID id);
}
