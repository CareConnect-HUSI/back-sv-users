package co.edu.javeriana.sv_users.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.javeriana.sv_users.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

   @Query("SELECT COUNT(u) FROM User u WHERE u.role.name = :roleName")
   int countByRole_Name(String name);
}
