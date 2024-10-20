package apap.ti.hospitalization2206082064.service;

import apap.ti.hospitalization2206082064.model.Patient;
import apap.ti.hospitalization2206082064.repository.PatientDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDb patientDb;

    @Override
    public List<Patient> getAllPatients() {
        return patientDb.findAll();
    }

    @Override
    public Patient getPatientById(UUID id) {
        Optional<Patient> patient = patientDb.findById(id);
        return patient.orElse(null);
    }

    @Override
    public Patient getPatientByNIK(String NIK) {
        Optional<Patient> patient = patientDb.findByNIK(NIK);
        return patient.orElse(null);
    }

    @Override
    public void addPatient(Patient patient) {
        patientDb.save(patient);
    }

    @Override
    public void updatePatient(Patient patient) {
        patientDb.save(patient);
    }

    @Override
    public void deletePatient(UUID id) {
        patientDb.deleteById(id);
    }
}
