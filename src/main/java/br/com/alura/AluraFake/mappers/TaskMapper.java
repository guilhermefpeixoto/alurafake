package br.com.alura.AluraFake.mappers;

import java.util.List;

import br.com.alura.AluraFake.dtos.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.dtos.NewSingleChoiceTaskDTO;
import br.com.alura.AluraFake.dtos.TaskOptionResponseDTO;
import br.com.alura.AluraFake.dtos.TaskResponseDTO;
import br.com.alura.AluraFake.models.Course;
import br.com.alura.AluraFake.models.Task;
import br.com.alura.AluraFake.models.Type;

public class TaskMapper {
    public static Task toEntity(NewOpenTextTaskDTO openTextTaskDTO, Course course) {
        Task openTextTask = new Task(openTextTaskDTO.statement(), openTextTaskDTO.order(), Type.OPEN_TEXT, course);

        return openTextTask;
    }

    public static Task toEntity(NewSingleChoiceTaskDTO singleChoiceTaskDTO, Course course) {
        Task singleChoinceTask = new Task(singleChoiceTaskDTO.statement(), singleChoiceTaskDTO.order(), Type.SINGLE_CHOICE, course);

        return singleChoinceTask;
    }

    public static TaskResponseDTO toDTO(Task task, List<TaskOptionResponseDTO> options) {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO(task.getStatement(), task.getOrder(), task.getType(), options);

        return taskResponseDTO;
    }
}
