package co.edu.javeriana.sv_users.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.sv_users.Entity.PatientEntity;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    boolean existsByIdentificationNumber(String identificationNumber);
    
    Boolean existsByIdentificationNumberAndIdentificationType(String identificationNumber, String identificationType);
}
