package co.edu.javeriana.sv_users.Service;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.javeriana.sv_users.DTO.UserDTO;
import co.edu.javeriana.sv_users.Entity.Account;
import co.edu.javeriana.sv_users.Entity.NurseEntity;
import co.edu.javeriana.sv_users.Entity.PatientEntity;
import co.edu.javeriana.sv_users.Entity.Role;
import co.edu.javeriana.sv_users.Repository.PatientRepository;
import co.edu.javeriana.sv_users.Repository.RoleRepository;
import co.edu.javeriana.sv_users.Repository.UserRepository;
import co.edu.javeriana.sv_users.Security.JWTGenerator;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

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

    public Account login(UserDTO user) {
        try {
            System.out.println("Iniciando login para: " + user.getEmail());
    
            NurseEntity nurse = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> {
                    System.out.println("⚠ Usuario no encontrado: " + user.getEmail());
                    return new UsernameNotFoundException("Usuario no encontrado");
                });
    
            System.out.println("Contraseña ingresada: " + user.getPassword());
            System.out.println("Contraseña en BD: " + nurse.getPassword());
    
            // ⚠ Verificar si la contraseña en la BD está cifrada
            if (!nurse.getPassword().startsWith("$2a$")) {
                System.out.println("⚠ Contraseña no cifrada detectada. Cifrando y guardando...");
                nurse.setPassword(passwordEncoder.encode(nurse.getPassword()));
                userRepository.save(nurse);
            }
    
            // 🔒 Comparar la contraseña ingresada con la cifrada
            if (!passwordEncoder.matches(user.getPassword(), nurse.getPassword())) {
                System.out.println("Error: Contraseña incorrecta.");
                throw new BadCredentialsException("Credenciales incorrectas");
            }
    
            System.out.println("Credenciales correctas. Autenticando...");
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String token = jwtGenerator.generateToken(authentication);
            System.out.println("Token generado: " + token);
            
            return new Account(nurse.getId(), nurse.getName(), token);
            
        } catch (BadCredentialsException e) {
            System.out.println("Error: Credenciales incorrectas.");
            throw new BadCredentialsException("Credenciales incorrectas");
        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
            throw new RuntimeException("Error durante el login");
        }
    }
    
    

    public void registerPaciente(PatientEntity patient) {
        System.out.println("Identidicación en el service: " + patient.getIdentificationNumber());
    
        patientRepository.save(patient);
    }
    

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public boolean patientExistsByIdentification(String identificationNumber, String identificationType) {
        return patientRepository.existsByIdentificationNumberAndIdentificationType(identificationNumber, identificationType);
    }
    
        
    public void registerEnfermera(NurseEntity nurse) {
        if (userRepository.existsByEmail(nurse.getEmail())) {
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
    
        userRepository.save(nurse);
    }
    
    


    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    
}