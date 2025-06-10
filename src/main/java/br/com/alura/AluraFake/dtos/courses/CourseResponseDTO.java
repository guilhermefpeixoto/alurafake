package br.com.alura.AluraFake.dtos.courses;

import java.io.Serializable;

import br.com.alura.AluraFake.models.courses.Status;

public record CourseResponseDTO(Long id, String title, String description, Status status) implements Serializable {
}
