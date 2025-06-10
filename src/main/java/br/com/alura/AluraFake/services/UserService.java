package br.com.alura.AluraFake.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.alura.AluraFake.exceptions.EmailAlreadyRegisteredException;
import br.com.alura.AluraFake.models.users.User;
import br.com.alura.AluraFake.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyRegisteredException("This email address is already registered.");
        }

        if (!user.getPassword().startsWith("$2a$")) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }

        this.saveUser(user);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    private void saveUser(User user) {
        this.userRepository.save(user);
    }

}
