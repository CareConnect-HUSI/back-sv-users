package co.edu.javeriana.sv_users.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.javeriana.sv_users.Entity.EnfermeraEntity;
import co.edu.javeriana.sv_users.Entity.RolEntity;
import co.edu.javeriana.sv_users.Entity.TipoIdentificacionEntity;
import co.edu.javeriana.sv_users.Entity.TurnoEntity;
import co.edu.javeriana.sv_users.Repository.EnfermeraRepository;
import co.edu.javeriana.sv_users.Repository.RolRepository;
import co.edu.javeriana.sv_users.Repository.TipoIdentificacionRepository;
import co.edu.javeriana.sv_users.Repository.TurnoRepository;

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
        enfermera.setPassword((String) data.get("password"));
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
}
