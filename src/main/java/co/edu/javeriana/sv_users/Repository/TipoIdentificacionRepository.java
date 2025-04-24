package co.edu.javeriana.sv_users.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.sv_users.Entity.TipoIdentificacionEntity;

@Repository
public interface  TipoIdentificacionRepository extends JpaRepository<TipoIdentificacionEntity, Long>{
    TipoIdentificacionEntity findByName(String name);
}
