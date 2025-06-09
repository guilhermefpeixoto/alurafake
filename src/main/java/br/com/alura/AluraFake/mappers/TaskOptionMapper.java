package br.com.alura.AluraFake.mappers;

import java.util.List;

import br.com.alura.AluraFake.dtos.tasks.TaskOptionDTO;
import br.com.alura.AluraFake.dtos.tasks.TaskOptionResponseDTO;
import br.com.alura.AluraFake.models.Task;
import br.com.alura.AluraFake.models.TaskOption;

public class TaskOptionMapper {

    public static List<TaskOption> toEntities(List<TaskOptionDTO> taskOptionsDTO, Task task) {
        List<TaskOption> taskOptions = taskOptionsDTO.stream()
                .map(taskOption -> new TaskOption(taskOption.option(), taskOption.isCorrect(), task)).toList();

        return taskOptions;
    }

    public static List<TaskOptionResponseDTO> toDTOs(List<TaskOption> taskOptions) {
        List<TaskOptionResponseDTO> taskOptionResponseDTOs = taskOptions.stream()
                .map(taskOption -> new TaskOptionResponseDTO(taskOption.getOption(), taskOption.getIsCorrect()))
                .toList();

        return taskOptionResponseDTOs;
    }
}
