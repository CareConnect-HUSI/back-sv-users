package co.edu.javeriana.sv_users.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.javeriana.sv_users.Entity.User;
import co.edu.javeriana.sv_users.Repository.UserRepository;


@Service
public class UserService implements CrudService<User, Long> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // I had to change this here so that I could save the entity in a “good way”
    // otherwise it would not let me test things.

    public User save(User userEntity) {
        String password = userEntity.getPassword();
    
        // Codifica solo si la contraseña no está en formato codificado
        if (!isEncoded(password)) {
            String encodedPassword = passwordEncoder.encode(password);
            userEntity.setPassword(encodedPassword);
        }
    
        return userRepository.saveAndFlush(userEntity);
    }
    
    private boolean isEncoded(String password) {
        // Verifica si la contraseña ya está codificada en Base64 (ejemplo)
        return password.matches("^[A-Za-z0-9+/=]+$") && password.length() >= 44;
    }
    

    public User update(User userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public int countByRoleName(String roleName) {
        return userRepository.countByRole_Name(roleName);
    }

}
