package br.com.alura.AluraFake.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.alura.AluraFake.models.users.Role;
import br.com.alura.AluraFake.models.users.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail__should_return_existis_user() {
        User caio = new User("Etevaldo", "etevaldo@gmail.com", Role.STUDENT);
        userRepository.save(caio);

        Optional<User> result = userRepository.findByEmail("etevaldo@gmail.com");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Etevaldo");

        result = userRepository.findByEmail("jemerson@alura.com.br");
        assertThat(result).isEmpty();
    }

    @Test
    void existsByEmail__should_return_true_when_user_existis() {
        User caio = new User("Jurema", "jurema@gmail.com", Role.STUDENT);
        userRepository.save(caio);

        assertThat(userRepository.existsByEmail("jurema@gmail.com")).isTrue();
        assertThat(userRepository.existsByEmail("marivaldo@gmail.com")).isFalse();
    }

}