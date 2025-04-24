package co.edu.javeriana.sv_users.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.sv_users.Entity.RolEntity;

@Repository
public interface  RolRepository extends  JpaRepository<RolEntity, Long>{
    RolEntity findByName(String name);
}
