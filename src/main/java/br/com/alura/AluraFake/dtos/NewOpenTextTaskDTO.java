package br.com.alura.AluraFake.dtos;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NewOpenTextTaskDTO(
    @NotBlank
    @Length(min = 4, max = 255)
    String statement,
    @NotNull
    @Positive
    Integer order,
    @NotNull
    @Positive
    Long courseId
) {}