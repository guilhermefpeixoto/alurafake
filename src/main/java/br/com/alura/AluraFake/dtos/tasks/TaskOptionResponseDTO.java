package br.com.alura.AluraFake.dtos.tasks;

import java.io.Serializable;

public record TaskOptionResponseDTO(
        String option,
        boolean isCorrect) implements Serializable {
}
