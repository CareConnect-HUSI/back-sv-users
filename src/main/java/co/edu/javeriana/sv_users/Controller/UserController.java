package co.edu.javeriana.sv_users.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.sv_users.Entity.NurseEntity;
import co.edu.javeriana.sv_users.Entity.Role;
import co.edu.javeriana.sv_users.Repository.RoleRepository;
import co.edu.javeriana.sv_users.Service.NurseService;




@RestController
@RequestMapping("")
public class UserController {
    @Autowired
    private NurseService nurseService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //http://localhost:8085/login
   

    //http://localhost:8085/register-nurse
    @PostMapping("/register-nurse")
    public ResponseEntity<?> register(@RequestBody NurseEntity nurse) {

        if (nurseService.emailExists(nurse.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"El correo ya está registrado\"}");
        }

        Role nurseRole = roleRepository.findByName("NURSE")
                .orElseThrow(() -> new RuntimeException("El rol NURSE no existe"));
        nurse.setRole(nurseRole);

        nurse.setPassword(passwordEncoder.encode(nurse.getPassword()));

        nurseService.registerEnfermera(nurse);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Enfermera registrada exitosamente\"}");
    }

    //http://localhost:8085/nurses
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
