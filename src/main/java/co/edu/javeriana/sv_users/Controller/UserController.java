package co.edu.javeriana.sv_users.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.sv_users.DTO.UserDTO;
import co.edu.javeriana.sv_users.Entity.Account;
import co.edu.javeriana.sv_users.Entity.NurseEntity;
import co.edu.javeriana.sv_users.Entity.Patient;
import co.edu.javeriana.sv_users.Entity.Role;
import co.edu.javeriana.sv_users.Repository.RoleRepository;
import co.edu.javeriana.sv_users.Service.NurseService;
import co.edu.javeriana.sv_users.Service.UserService;




@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private RoleRepository roleRepository;

    //http://localhost:8080/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        try {
            Account account = userService.login(user);
            return ResponseEntity.ok(account);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Invalid credentials\"}");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"error\": \"Authentication failed\"}");
        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Error occurred during login\"}");
        }
    }

    //http://localhost:8080/register-nurse
    @PostMapping("/register-nurse")
    public ResponseEntity<?> register(@RequestBody NurseEntity nurse) {

        Role nurseRole = roleRepository.findByName("NURSE")
                .orElseThrow(() -> new RuntimeException("El rol NURSE no existe"));
        nurse.setRole(nurseRole);
        userService.registerEnfermera(nurse);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Enfermera registrada exitosamente\"}");
    }

    //http://localhost:8080/register-patient
    @PostMapping("/register-patient")
    public ResponseEntity<?> registerPatient(@RequestBody Patient patient) {
        try {
            userService.registerPaciente(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Paciente registrado exitosamente\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Error en el registro\"}");
        }
    }

    //http://localhost:8080/nurses
    @GetMapping("/nurses")
public ResponseEntity<?> getAllNurses(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int page)  {
    try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<NurseEntity> nursesPage = nurseService.findAll(pageable);

            return ResponseEntity.ok(Map.of(
                "content", nursesPage.getContent(),
                "totalElements", nursesPage.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

       
}
