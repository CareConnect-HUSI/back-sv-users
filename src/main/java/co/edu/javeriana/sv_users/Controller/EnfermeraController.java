package co.edu.javeriana.sv_users.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;

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

    // http://localhost:8080/enfermeras/registrar-enfermera
    @PostMapping("/registrar-enfermera")
    public ResponseEntity<?> registrar(@RequestBody EnfermeraEntity enfermera) {

        if (enfermera.getTurnoEntity() == null || enfermera.getTurnoEntity().getName() == null) {
            throw new IllegalArgumentException("El campo 'turnoEntity.name' es requerido");
        }

        if (enfermera.getTipoIdentificacion() == null || enfermera.getTipoIdentificacion().getName() == null) {
            throw new IllegalArgumentException("El campo 'tipoIdentificacion.name' es requerido");
        }

        TurnoEntity turno = turnoRepository.findByName(enfermera.getTurnoEntity().getName());
        TipoIdentificacionEntity tipo = tipoIdentificacionRepository
                .findByName(enfermera.getTipoIdentificacion().getName());

        if (turno == null){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Turno inválido");
        }
        if (tipo == null){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Tipo identificación inválido");
        }
        RolEntity rol = rolRepository.findByName("Enfermera");
        if (rol == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se encontró el rol de enfermera");
        }
        enfermera.setRolEntity(rol);
        enfermera.setTurnoEntity(turno);
        enfermera.setTipoIdentificacion(tipo);

        boolean yaExiste = enfermeraRepository.existsByTipoIdentificacionAndNumeroIdentificacion(
                tipo, enfermera.getNumeroIdentificacion());
        if (yaExiste) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe una enfermera con ese número y tipo de identificación");
        }

        // Enviar la solicitud HTTP al servicio de geocodificación
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Crear el JSON de la solicitud
            Map<String, String> geocodeRequest = Map.of(
                    "direccion", enfermera.getDireccion(),
                    "conjunto", enfermera.getConjunto(),
                    "barrio", enfermera.getBarrio(),
                    "ciudad", "Bogotá");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(geocodeRequest, headers);

            // Hacer la solicitud POST a la URL geocode
            ResponseEntity<Map> response = restTemplate.exchange(
                    "http://0.0.0.0:8001/geocode", HttpMethod.POST, entity, Map.class);

            // Verificar si la respuesta tiene latitud y longitud
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Double latitud = (Double) responseBody.get("latitud");
                Double longitud = (Double) responseBody.get("longitud");

                // Actualizar la entidad enfermera con los valores de latitud y longitud
                enfermera.setLatitud(latitud);
                enfermera.setLongitud(longitud);
            } else {
                throw new IllegalArgumentException("No se pudo obtener latitud y longitud de la geocodificación.");
            }
        } catch (Exception e) {
            // Manejo de excepciones si la solicitud falla
            System.err.println("Error al hacer la solicitud de geocodificación: " + e.getMessage());
            throw new IllegalArgumentException("Error al obtener latitud y longitud.");
        }

        // Registrar la enfermera en la base de datos
        EnfermeraEntity enfermeraGuardada = enfermeraRepository.save(enfermera);

        return ResponseEntity.ok(enfermeraGuardada);
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

}