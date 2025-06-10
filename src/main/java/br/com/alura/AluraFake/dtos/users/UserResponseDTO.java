package br.com.alura.AluraFake.dtos.users;

import java.io.Serializable;

import br.com.alura.AluraFake.models.users.Role;

public record UserResponseDTO(String name, String email, Role role) implements Serializable {
}
