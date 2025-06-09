package br.com.alura.AluraFake.dtos.tasks;

import java.io.Serializable;
import java.util.List;

import br.com.alura.AluraFake.models.Type;

public record TaskResponseDTO(
        String statement,
        Integer order,
        Type type,
        List<TaskOptionResponseDTO> options) implements Serializable {
}
