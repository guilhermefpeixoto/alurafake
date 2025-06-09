package br.com.alura.AluraFake.dtos.tasks;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record NewMultipleChoiceTaskDTO(
        @NotBlank(message = "Statement cannot be empty.") @Length(min = 4, max = 255, message = "The statement must be between 4 and 255 characters.") String statement,
        @NotNull(message = "Order cannot be null.") @Positive(message = "Order needs to be a positive integer.") Integer order,
        @NotNull(message = "CourseId cannot be empty.") @Positive(message = "CourseId needs to be a positive long.") Long courseId,
        @NotNull(message = "Task options cannot be null.") @Size(min = 3, max = 5, message = "A multiple choice task must have at least 3 and no more than 5 options.") List<TaskOptionDTO> options) {
}
