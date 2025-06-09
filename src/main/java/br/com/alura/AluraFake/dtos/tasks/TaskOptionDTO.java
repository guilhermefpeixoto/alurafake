package br.com.alura.AluraFake.dtos.tasks;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskOptionDTO(
        @NotBlank(message = "Option cannot be empty.") @Length(min = 4, max = 80, message = "The option must be between 4 and 80 characters.") String option,
        @NotNull(message = "IsCorrect cannot be empty.") boolean isCorrect) {
}