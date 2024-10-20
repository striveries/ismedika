package apap.ti.hospitalization2206082064.repository;

import apap.ti.hospitalization2206082064.model.Nurse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NurseDb extends JpaRepository<Nurse, UUID> {

}
