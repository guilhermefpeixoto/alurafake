package br.com.alura.AluraFake.dtos;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NewCourseDTO(
    @NotBlank
    String title,
    @NotBlank
    @Length(min = 4, max = 255)
    String description,
    @NotBlank
    @Email
    String emailInstructor
) {}
