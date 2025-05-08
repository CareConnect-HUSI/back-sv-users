package co.edu.javeriana.sv_users.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.sv_users.DTO.UserDTO;
import co.edu.javeriana.sv_users.Entity.Account;
import co.edu.javeriana.sv_users.Entity.EnfermeraEntity;
import co.edu.javeriana.sv_users.Repository.EnfermeraRepository;
import co.edu.javeriana.sv_users.Repository.RolRepository;
import co.edu.javeriana.sv_users.Repository.TipoIdentificacionRepository;
import co.edu.javeriana.sv_users.Repository.TurnoRepository;
import co.edu.javeriana.sv_users.Service.EnfermeraService;

@RestController
@RequestMapping("/enfermeras")
public class EnfermeraController {

    @Autowired
    private EnfermeraService enfermeraService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private EnfermeraRepository enfermeraRepository;

    @Autowired
    private TipoIdentificacionRepository tipoIdentificacionRepository;

    // http://localhost:8080/enfermeras/registrar-enfermera
    @PostMapping("/registrar-enfermera")
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> data) {
        try {
            EnfermeraEntity enfermera = enfermeraService.registrar(data);
            return ResponseEntity.ok(enfermera);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    // http://localhost:8080/enfermeras/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        try {
            Account account = enfermeraService.login(user);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

}
