package co.edu.javeriana.sv_users.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.sv_users.DTO.TipoIdentificacionDTO;
import co.edu.javeriana.sv_users.DTO.UserDTO;
import co.edu.javeriana.sv_users.Entity.Account;
import co.edu.javeriana.sv_users.Entity.BarrioEntity;
import co.edu.javeriana.sv_users.Entity.EnfermeraEntity;
import co.edu.javeriana.sv_users.Entity.TipoIdentificacionEntity;
import co.edu.javeriana.sv_users.Entity.TurnoEntity;
import co.edu.javeriana.sv_users.Repository.EnfermeraRepository;
import co.edu.javeriana.sv_users.Repository.RolRepository;
import co.edu.javeriana.sv_users.Repository.TipoIdentificacionRepository;
import co.edu.javeriana.sv_users.Repository.TurnoRepository;
import co.edu.javeriana.sv_users.Security.JWTGenerator;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTGenerator jwtGenerator;

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

    // https://localhost:8080/enfermeras/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            // Establecer el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar el token
            String token = jwtGenerator.generateToken(authentication);

            // Puedes retornar un objeto con el token
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas: " + e.getMessage());
        }
    }


    // http://localhost:8080/enfermeras
    @GetMapping("")
    public ResponseEntity<?> getAllEnfermeras(@RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<EnfermeraEntity> enfermerasPage = enfermeraService.getAllEnfermeras(pageable);

            return ResponseEntity.ok(Map.of(
                    "content", enfermerasPage.getContent(),
                    "totalElements", enfermerasPage.getTotalElements()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // http://localhost:8080/enfermeras/1
    @PutMapping("/{id}")
    public ResponseEntity<EnfermeraEntity> actualizar(@PathVariable Long id, @RequestBody EnfermeraEntity enfermera) {
        EnfermeraEntity existente = enfermeraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrada"));

        TurnoEntity turno = turnoRepository.findByName(enfermera.getTurnoEntity().getName());
        TipoIdentificacionEntity tipo = tipoIdentificacionRepository
                .findByName(enfermera.getTipoIdentificacion().getName());

        existente.setNombre(enfermera.getNombre());
        existente.setApellido(enfermera.getApellido());
        existente.setNumeroIdentificacion(enfermera.getNumeroIdentificacion());
        existente.setTelefono(enfermera.getTelefono());
        existente.setTurnoEntity(turno);
        existente.setTipoIdentificacion(tipo);
        existente.setDireccion(enfermera.getDireccion());
        existente.setBarrio(enfermera.getBarrio());
        existente.setEmail(enfermera.getEmail());
        existente.setConjunto(enfermera.getConjunto());

        enfermeraRepository.save(existente);
        return ResponseEntity.ok(existente);
    }

    // http://localhost:8088/enfermeras/localidades
    @GetMapping("/localidades")
    public ResponseEntity<?> getLocalidades() {
        try {
            return ResponseEntity.ok(enfermeraService.getLocalidades());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // http://localhost:8088/enfermeras/barrio/1
    @GetMapping("/barrios/{codigoLocalidad}")
    public ResponseEntity<?> getBarrios(@PathVariable String codigoLocalidad) {
        try {
            List<BarrioEntity> barrios = enfermeraService.getBarrios(codigoLocalidad);
            return ResponseEntity.ok(barrios); // ← ya se serializa correctamente
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getBarrioByNombre(@PathVariable String nombre) {
        Optional<BarrioEntity> optionalBarrio = enfermeraService.findBarrioByNombre(nombre);

        if (optionalBarrio.isPresent()) {
            return ResponseEntity.ok(optionalBarrio.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Barrio no encontrado");
        }
    }

    @GetMapping("/tipos-identificacion")
    public ResponseEntity<List<TipoIdentificacionDTO>> obtenerTiposIdentificacion() {
        List<TipoIdentificacionEntity> tipos = tipoIdentificacionRepository.findAll();
        List<TipoIdentificacionDTO> tiposDTO = tipos.stream()
                .map(t -> new TipoIdentificacionDTO(t.getId(), t.getName()))
                .toList();
        return ResponseEntity.ok(tiposDTO);
    }

}
