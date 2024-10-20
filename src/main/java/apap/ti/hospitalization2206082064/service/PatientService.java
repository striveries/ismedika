package apap.ti.hospitalization2206082064.service;

import apap.ti.hospitalization2206082064.model.Patient;
import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<Patient> getAllPatients();
    Patient getPatientById(UUID patientId);
    Patient getPatientByNIK(String NIK);
    void addPatient(Patient patient);
    void updatePatient(Patient patient);
    void deletePatient(UUID id);
}
