package co.edu.javeriana.sv_users.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.edu.javeriana.sv_users.DTO.UserDTO;
import co.edu.javeriana.sv_users.Entity.Account;
import co.edu.javeriana.sv_users.Entity.BarrioEntity;
import co.edu.javeriana.sv_users.Entity.EnfermeraEntity;
import co.edu.javeriana.sv_users.Entity.LocalidadEntity;
import co.edu.javeriana.sv_users.Entity.RolEntity;
import co.edu.javeriana.sv_users.Entity.TipoIdentificacionEntity;
import co.edu.javeriana.sv_users.Entity.TurnoEntity;
import co.edu.javeriana.sv_users.Repository.BarrioRepository;
import co.edu.javeriana.sv_users.Repository.EnfermeraRepository;
import co.edu.javeriana.sv_users.Repository.LocalidadRepository;
import co.edu.javeriana.sv_users.Repository.RolRepository;
import co.edu.javeriana.sv_users.Repository.TipoIdentificacionRepository;
import co.edu.javeriana.sv_users.Repository.TurnoRepository;
import co.edu.javeriana.sv_users.Security.JWTGenerator;

@Service
public class EnfermeraService {

    @Autowired
    private EnfermeraRepository enfermeraRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private TipoIdentificacionRepository tipoIdentificacionRepository;
    
    @Autowired
    private BarrioRepository barrioRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registrarConNombres(Map<String, Object> data) {
        String email = (String) data.get("email");
        if (enfermeraRepository.existsByEmail(email)) {
            throw new RuntimeException("El correo ya está registrado");
        }

        String nombreTurno = (String) data.get("turno");
        TurnoEntity turnoEntity = turnoRepository.findByName(nombreTurno);
        if (turnoEntity == null) {
            throw new RuntimeException("El turno especificado no existe");
        }

        RolEntity rol = rolRepository.findByName("Enfermera");
        if (rol == null) {
            throw new RuntimeException("El rol ENFERMERA no existe");
        }

        String nombreTipoId = (String) data.get("tipoIdentificacion");
        TipoIdentificacionEntity tipoIdentificacionEntity = tipoIdentificacionRepository.findByName(nombreTipoId);
        if (tipoIdentificacionEntity == null) {
            throw new RuntimeException("El tipo de identificación especificado no existe");
        }

        EnfermeraEntity enfermera = new EnfermeraEntity();
        enfermera.setNombre((String) data.get("nombre"));
        enfermera.setApellido((String) data.get("apellido"));
        enfermera.setNumeroIdentificacion((String) data.get("numeroIdentificacion"));
        enfermera.setDireccion((String) data.get("direccion"));
        enfermera.setTelefono((String) data.get("telefono"));
        enfermera.setEmail(email);
        String password = (String) data.get("password");
        enfermera.setPassword(passwordEncoder.encode(password));
        enfermera.setBarrio((String) data.get("barrio"));
        enfermera.setConjunto((String) data.get("conjunto"));
        enfermera.setTipoIdentificacion(tipoIdentificacionEntity);

        enfermera.setTurnoEntity(turnoEntity);
        enfermera.setRolEntity(rol);

        enfermeraRepository.save(enfermera);
    }

    public Page<EnfermeraEntity> getAllEnfermeras(Pageable pageable) {
        return enfermeraRepository.findAll(pageable);
    }

    public boolean emailExists(String email) {
        return enfermeraRepository.existsByEmail(email);
    }

    public Account login(UserDTO user) {
        if (!enfermeraRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email does not exist");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long id = enfermeraRepository.findByEmail(user.getEmail()).getId();
        String email = user.getEmail();
        String token = jwtGenerator.generateToken(authentication);

        return new Account(id, email, token);
    }

    public EnfermeraEntity registrar(Map<String, Object> data) {
        String email = (String) data.get("email");
        if (enfermeraRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        String nombreTurno = null;
        if (data.containsKey("turno")) {
            nombreTurno = (String) data.get("turno");
        } else if (data.get("turnoEntity") instanceof Map turnoMap) {
            nombreTurno = (String) turnoMap.get("name");
        }

        if (nombreTurno == null) {
            throw new IllegalArgumentException("El campo 'turno' es requerido");
        }

        TurnoEntity turnoEntity = turnoRepository.findByName(nombreTurno);
        if (turnoEntity == null) {
            throw new IllegalArgumentException("El turno especificado no existe");
        }

        String nombreTipoId = null;
        if (data.get("tipoIdentificacion") instanceof String tipoPlano) {
            nombreTipoId = tipoPlano;
        } else if (data.get("tipoIdentificacion") instanceof Map tipoMap) {
            nombreTipoId = (String) tipoMap.get("name");
        }

        if (nombreTipoId == null) {
            throw new IllegalArgumentException("El campo 'tipoIdentificacion' es requerido");
        }

        TipoIdentificacionEntity tipoIdentificacionEntity = tipoIdentificacionRepository.findByName(nombreTipoId);
        if (tipoIdentificacionEntity == null) {
            throw new IllegalArgumentException("El tipo de identificación especificado no existe");
        }

        String numeroIdentificacion = (String) data.get("numeroIdentificacion");
        if (numeroIdentificacion == null) {
            throw new IllegalArgumentException("El número de identificación es requerido");
        }

        if (enfermeraRepository.existsByTipoIdentificacionAndNumeroIdentificacion(
                tipoIdentificacionEntity, numeroIdentificacion)) {
            throw new IllegalArgumentException("Ya existe una enfermera con ese número y tipo de identificación");
        }

        RolEntity rol = rolRepository.findByName("Enfermera");
        if (rol == null) {
            throw new IllegalArgumentException("No se encontró el rol de enfermera");
        }

        EnfermeraEntity enfermera = new EnfermeraEntity();
        enfermera.setNombre((String) data.get("nombre"));
        enfermera.setApellido((String) data.get("apellido"));
        enfermera.setNumeroIdentificacion(numeroIdentificacion);
        enfermera.setDireccion((String) data.get("direccion"));
        enfermera.setTelefono((String) data.get("telefono"));
        enfermera.setEmail(email);
        String password = (String) data.get("password");
        enfermera.setPassword(passwordEncoder.encode(password));
        enfermera.setBarrio((String) data.get("barrio"));
        enfermera.setConjunto((String) data.get("conjunto"));
        enfermera.setTipoIdentificacion(tipoIdentificacionEntity);
        enfermera.setTurnoEntity(turnoEntity);
        enfermera.setRolEntity(rol);
        enfermera.setEstado("Activo");

        // Geocodificación
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> geocodeRequest = Map.of(
                    "direccion", enfermera.getDireccion(),
                    "conjunto", enfermera.getConjunto(),
                    "barrio", enfermera.getBarrio(),
                    "ciudad", "Bogotá");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(geocodeRequest, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    "http://0.0.0.0:8001/geocode", HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                enfermera.setLatitud((Double) responseBody.get("latitud"));
                enfermera.setLongitud((Double) responseBody.get("longitud"));
            } else {
                throw new IllegalArgumentException("No se pudo obtener latitud y longitud de la geocodificación.");
            }
        } catch (Exception e) {
            System.err.println("Error en geocodificación: " + e.getMessage());
            throw new IllegalArgumentException("Error al obtener latitud y longitud.");
        }

        return enfermeraRepository.save(enfermera);
    }

    public List<LocalidadEntity> getLocalidades() {
        return localidadRepository.findAll();
    }

    public List<BarrioEntity> getBarrios(String codigoLocalidad) {
        List<BarrioEntity> barrios = barrioRepository.findByLocalidad_Codigo(codigoLocalidad);
        if (barrios.isEmpty()) {
            throw new RuntimeException("No se encontraron barrios para la localidad especificada");
        }
        return barrios;
    }
}
