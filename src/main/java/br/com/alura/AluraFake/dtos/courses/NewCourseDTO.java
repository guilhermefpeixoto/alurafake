package br.com.alura.AluraFake.dtos.courses;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewCourseDTO(
        @NotBlank(message = "The title cannot be empty.") String title,
        @NotBlank(message = "The description cannot be empty.") @Length(min = 4, max = 255, message = "The description must be between 4 and 255 characters.") String description,
        @NotBlank(message = "The email instructor cannot be empty.") @Email(message = "Email format required.") String emailInstructor) {
}