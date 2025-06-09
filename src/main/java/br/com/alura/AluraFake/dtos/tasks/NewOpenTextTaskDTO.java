package br.com.alura.AluraFake.dtos.tasks;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NewOpenTextTaskDTO(
        @NotBlank(message = "Statement cannot be empty.") @Length(min = 4, max = 255, message = "The statement must be between 4 and 255 characters.") String statement,
        @NotNull(message = "Order cannot be null.") @Positive(message = "Order needs to be a positive integer.") Integer order,
        @NotNull(message = "CourseId cannot be empty.") @Positive(message = "CourseId needs to be a positive long.") Long courseId) {
}