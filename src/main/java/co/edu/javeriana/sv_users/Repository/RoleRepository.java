package co.edu.javeriana.sv_users.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.javeriana.sv_users.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

