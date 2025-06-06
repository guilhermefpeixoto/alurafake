package br.com.alura.AluraFake.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.AluraFake.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
