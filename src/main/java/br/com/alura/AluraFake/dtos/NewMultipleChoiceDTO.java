package br.com.alura.AluraFake.dtos;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record NewMultipleChoiceDTO(
    @NotBlank
    @Length(min = 4, max = 255)
    String statement,
    @NotNull
    @Positive
    Integer order,
    @NotNull
    @Positive
    Long courseId,
    @NotNull
    @Size(min = 3, max = 5)
    List<TaskOptionDTO> options
) {}
