package co.edu.javeriana.sv_users.Service;


import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.javeriana.sv_users.DTO.UserDTO;
import co.edu.javeriana.sv_users.Entity.Account;
import co.edu.javeriana.sv_users.Entity.User;
import co.edu.javeriana.sv_users.Repository.UserRepository;
import co.edu.javeriana.sv_users.Security.JWTGenerator;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${SECRET_KEY}")
    private String secretKey;

    public Account login(UserDTO user) {
        try {
            String decryptedPassword = decryptPassword(user.getPassword());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), decryptedPassword));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Long id = userRepository.findIdByMail(user.getEmail());
            String name = userRepository.findNameByMail(user.getEmail());
            String token = jwtGenerator.generateToken(authentication);

            return new Account(id, name, token);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials");
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during login");
        }
    }

    public void register(User user) {
        try {
            String decryptedPassword = decryptPassword(user.getPassword());
            user.setPassword(passwordEncoder.encode(decryptedPassword));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during registration", e);
        }
    }

    private String decryptPassword(String encryptedPassword) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password", e);
        }
    }
}