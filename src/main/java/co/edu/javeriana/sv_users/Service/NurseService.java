package co.edu.javeriana.sv_users.Service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.javeriana.sv_users.Entity.NurseEntity;
import co.edu.javeriana.sv_users.Entity.Role;
import co.edu.javeriana.sv_users.Repository.NurseRepository;
import co.edu.javeriana.sv_users.Repository.RoleRepository;
import co.edu.javeriana.sv_users.Security.JWTGenerator;

@Service
public class NurseService {

    @Autowired
    private NurseRepository nurseRepository ;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Value("${SECRET_KEY}")
    private String secretKey;



    public Page<NurseEntity> findAll(Pageable pageable) {
        return nurseRepository.findAll(pageable);
    }

    public void registerEnfermera(NurseEntity nurse) {
        if (nurseRepository.existsByEmail(nurse.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }
    
        Role nurseRole = roleRepository.findByName("NURSE")
                .orElseThrow(() -> new RuntimeException("El rol NURSE no existe"));
    
        nurse.setRole(nurseRole);
    
        if (nurse.getPassword() == null || nurse.getPassword().isEmpty()) {
            nurse.setPassword(generateRandomPassword());
        }

        System.out.println("🔐 Generando contraseña aleatoria: " + passwordEncoder.encode(nurse.getPassword()));
        nurse.setPassword(passwordEncoder.encode(nurse.getPassword()));
    
        nurseRepository.save(nurse);
    }

    public boolean emailExists(String email) {
        return nurseRepository.existsByEmail(email);
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    
}
