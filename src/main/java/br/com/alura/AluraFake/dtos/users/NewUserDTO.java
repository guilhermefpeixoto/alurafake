package br.com.alura.AluraFake.dtos.users;

import org.hibernate.validator.constraints.Length;

import br.com.alura.AluraFake.models.users.Role;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NewUserDTO(
        @NotBlank(message = "Name cannot be empty.") @Length(min = 3, max = 50, message = "The name must be between 4 and 255 characters.") String name,
        @NotBlank(message = "The email cannot be empty.") @Email(message = "Email format required.") String email,
        @RolesAllowed(value = {
                "STUDENT", "INSTRUCTOR" }) Role role,
        @Pattern(regexp = "^$|^.{6}$", message = "Password must be exactly 6 characters long if provided") String password){
}
