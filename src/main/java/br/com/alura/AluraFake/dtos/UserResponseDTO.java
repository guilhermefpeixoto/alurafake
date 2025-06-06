package br.com.alura.AluraFake.dtos;

import java.io.Serializable;

import br.com.alura.AluraFake.models.Role;

public record UserResponseDTO(String name, String email, Role role) implements Serializable {}
