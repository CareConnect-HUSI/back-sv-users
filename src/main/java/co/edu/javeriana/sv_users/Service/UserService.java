package co.edu.javeriana.sv_users.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.javeriana.sv_users.Entity.PatientEntity;
import co.edu.javeriana.sv_users.Repository.PatientRepository;
import co.edu.javeriana.sv_users.Repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    public void registerPaciente(PatientEntity patient) {
        System.out.println("Identidicación en el service: " + patient.getIdentificationNumber());
    
        patientRepository.save(patient);
    }
    
    public boolean patientExistsByIdentification(String identificationNumber, String identificationType) {
        return patientRepository.existsByIdentificationNumberAndIdentificationType(identificationNumber, identificationType);
    }
    
    public Page<PatientEntity> findAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

}