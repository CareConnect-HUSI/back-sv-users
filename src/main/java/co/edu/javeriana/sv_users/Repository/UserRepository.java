package co.edu.javeriana.sv_users.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.sv_users.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

   @Query("SELECT COUNT(u) FROM User u WHERE u.role.name = :roleName")
   int countByRole_Name(String name);

   @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Long findIdByMail(String mail);
   @Query("SELECT u.username FROM User u WHERE u.email = :email")
    String findNameByMail(String mail);
}

