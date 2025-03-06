package co.edu.javeriana.sv_users.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import co.edu.javeriana.sv_users.Repository.UserRepository;

@Controller
@Profile("default")
public class Databaseinit implements ApplicationRunner{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Contraseña encriptada
        String password = "12345678";
        String passwordEncoded = passwordEncoder.encode(password);

        userRepository.save(new User(null, "Lina Salamanca", "salamanca.lm@javeriana.edu.co", passwordEncoded));
    }
    
}