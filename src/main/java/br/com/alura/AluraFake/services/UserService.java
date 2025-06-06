package br.com.alura.AluraFake.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.alura.AluraFake.exceptions.EmailAlreadyRegisteredException;
import br.com.alura.AluraFake.models.User;
import br.com.alura.AluraFake.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) throws Exception {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyRegisteredException("This email address is already registered.");
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
