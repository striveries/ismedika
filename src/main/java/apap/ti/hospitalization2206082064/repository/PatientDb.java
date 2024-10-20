package apap.ti.hospitalization2206082064.repository;

import apap.ti.hospitalization2206082064.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientDb extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByNIK(String NIK);
}
