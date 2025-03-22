package co.edu.javeriana.sv_users.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.sv_users.Entity.NurseEntity;

@Repository
public interface UserRepository extends JpaRepository<NurseEntity, Long> {

    Optional<NurseEntity> findByEmail(String email);

}

