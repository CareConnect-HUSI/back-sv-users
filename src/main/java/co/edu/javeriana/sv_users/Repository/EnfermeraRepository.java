package co.edu.javeriana.sv_users.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.sv_users.Entity.EnfermeraEntity;
import co.edu.javeriana.sv_users.Entity.TipoIdentificacionEntity;

@Repository
public interface  EnfermeraRepository extends JpaRepository<EnfermeraEntity, Long>{
    EnfermeraEntity findByEmail(String email);
    Optional<EnfermeraEntity> findById(Long id);
    EnfermeraEntity findByEmailAndId(String email, Long id);
    boolean existsByEmail(String email);
    boolean existsByTipoIdentificacionAndNumeroIdentificacion(
    TipoIdentificacionEntity tipoIdentificacion,
    String numeroIdentificacion
);
}
