package br.com.alura.AluraFake.dtos;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskOptionDTO(
    @NotBlank
    @Length(min = 4, max = 80)
    String option,
    @NotNull
    boolean isCorrect
) {}