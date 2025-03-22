package co.edu.javeriana.sv_users.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.sv_users.Entity.NurseEntity;

@Repository
public interface NurseRepository extends JpaRepository<NurseEntity, Long> {

    Optional<NurseEntity> findByEmail(String email);

    boolean existsByIdentificationNumber(String identificationNumber);

    Boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM NurseEntity u WHERE u.role.name = :roleName")
    int countByRole_Name(String name);

    @Query("SELECT u.id FROM NurseEntity u WHERE u.email = :email")
    Long findIdByMail(String mail);

    @Query("SELECT u.name FROM NurseEntity u WHERE u.email = :email")
    String findNameByMail(String mail);
}
