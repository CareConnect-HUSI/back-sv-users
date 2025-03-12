package co.edu.javeriana.sv_users.Service;


import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.javeriana.sv_users.DTO.UserDTO;
import co.edu.javeriana.sv_users.Entity.Account;
import co.edu.javeriana.sv_users.Entity.NurseEntity;
import co.edu.javeriana.sv_users.Entity.Patient;
import co.edu.javeriana.sv_users.Entity.Role;
import co.edu.javeriana.sv_users.Repository.RoleRepository;
import co.edu.javeriana.sv_users.Repository.UserRepository;
import co.edu.javeriana.sv_users.Security.JWTGenerator;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

  


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
        
            String decryptedPassword = decryptPassword(user.getPassword());

            try {
                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), decryptedPassword));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                Long id = userRepository.findIdByMail(user.getEmail());
                String name = userRepository.findNameByMail(user.getEmail());
                String token = jwtGenerator.generateToken(authentication);
                return new Account(id, name, token);
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid credentials");
            }
            



        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials");
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during login");
        }
    }

    public void registerPaciente(Patient paciente) {
        
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public void registerEnfermera(NurseEntity nurse) {
    
        Role nurseRole = roleRepository.findByName("NURSE")
                .orElseThrow(() -> new RuntimeException("El rol NURSE no existe"));

        nurse.setRole(nurseRole);
        
        userRepository.save(nurse);
}


    


    private String decryptPassword(String encryptedPassword) {
        try {
            System.out.println("Desencriptando contraseña");
            String decodedPassword = new String(Base64.getDecoder().decode(encryptedPassword), "UTF-8");
            StringBuilder decrypted = new StringBuilder();
    
            for (int i = 0; i < decodedPassword.length(); i++) {
                decrypted.append((char) (decodedPassword.charAt(i) ^ secretKey.charAt(i % secretKey.length())));
            }
    
            return decrypted.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password", e);
        }
    }
    
}