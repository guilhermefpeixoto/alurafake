package br.com.alura.AluraFake.dtos;

import br.com.alura.AluraFake.models.Status;

public record CourseResponseDTO(Long id, String title, String description, Status status) {}
