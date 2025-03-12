package co.edu.javeriana.sv_users.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.javeriana.sv_users.Entity.NurseEntity;
import co.edu.javeriana.sv_users.Repository.NurseRepository;

@Service
public class NurseService {

    @Autowired
    private NurseRepository nurseRepository ;

    public Page<NurseEntity> findAll(Pageable pageable) {
        return nurseRepository.findAll(pageable);
    }

    
    
}
