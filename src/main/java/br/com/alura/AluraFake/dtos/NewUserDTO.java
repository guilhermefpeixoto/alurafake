package br.com.alura.AluraFake.dtos;

import org.hibernate.validator.constraints.Length;

import br.com.alura.AluraFake.models.Role;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NewUserDTO(
    @NotBlank
    @Length(min = 3, max = 50)
    String name,
    @NotBlank
    @Email
    String email,
    @RolesAllowed(value = { "STUDENT", "INSTRUCTOR" })
    Role role,
    @Pattern(regexp = "^$|^.{6}$", message = "Password must be exactly 6 characters long if provided")
    String password
) {}
