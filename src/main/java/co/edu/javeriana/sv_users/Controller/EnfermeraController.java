package co.edu.javeriana.sv_users.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.sv_users.Entity.EnfermeraEntity;
import co.edu.javeriana.sv_users.Entity.RolEntity;
import co.edu.javeriana.sv_users.Entity.TipoIdentificacionEntity;
import co.edu.javeriana.sv_users.Entity.TurnoEntity;
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

    @PostMapping("/registrar-enfermera")
    public ResponseEntity<EnfermeraEntity> registrar(@RequestBody EnfermeraEntity enfermera) {

        if (enfermera.getTurnoEntity() == null || enfermera.getTurnoEntity().getName() == null) {
            throw new IllegalArgumentException("El campo 'turnoEntity.name' es requerido");
        }

        if (enfermera.getTipoIdentificacion() == null || enfermera.getTipoIdentificacion().getName() == null) {
            throw new IllegalArgumentException("El campo 'tipoIdentificacion.name' es requerido");
        }

        TurnoEntity turno = turnoRepository.findByName(enfermera.getTurnoEntity().getName());
        TipoIdentificacionEntity tipo = tipoIdentificacionRepository.findByName(enfermera.getTipoIdentificacion().getName());
        
        if (turno == null) throw new IllegalArgumentException("Turno no válido");
        if (tipo == null) throw new IllegalArgumentException("Tipo de identificación no válido");


        RolEntity rol = rolRepository.findByName("Enfermera");
        if (rol == null) {
            throw new IllegalArgumentException("No se encontró el rol 'Enfermera'");
        }
        enfermera.setRolEntity(rol);
        enfermera.setTurnoEntity(turno);
        enfermera.setTipoIdentificacion(tipo);

        EnfermeraEntity enfermeraGuardada = enfermeraRepository.save(enfermera);
        return ResponseEntity.ok(enfermeraGuardada);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEnfermeras(@RequestParam(defaultValue = "10") int limit,
                                              @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<EnfermeraEntity> enfermerasPage = enfermeraService.getAllEnfermeras(pageable);

            return ResponseEntity.ok(Map.of(
                    "content", enfermerasPage.getContent(),
                    "totalElements", enfermerasPage.getTotalElements()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnfermeraEntity> actualizar(@PathVariable Long id, @RequestBody EnfermeraEntity enfermera) {
        EnfermeraEntity existente = enfermeraRepository.findById(id).orElseThrow(() -> new RuntimeException("No encontrada"));

        TurnoEntity turno = turnoRepository.findByName(enfermera.getTurnoEntity().getName());
        TipoIdentificacionEntity tipo = tipoIdentificacionRepository.findByName(enfermera.getTipoIdentificacion().getName());

        existente.setNombre(enfermera.getNombre());
        existente.setApellido(enfermera.getApellido());
        existente.setNumeroIdentificacion(enfermera.getNumeroIdentificacion());
        existente.setTelefono(enfermera.getTelefono());
        existente.setTurnoEntity(turno);
        existente.setTipoIdentificacion(tipo);

        enfermeraRepository.save(existente);
        return ResponseEntity.ok(existente);
    }

}