package br.com.alura.AluraFake.mappers;

import java.util.List;

import br.com.alura.AluraFake.dtos.tasks.NewMultipleChoiceTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.NewSingleChoiceTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.TaskOptionResponseDTO;
import br.com.alura.AluraFake.dtos.tasks.TaskResponseDTO;
import br.com.alura.AluraFake.models.Course;
import br.com.alura.AluraFake.models.Task;
import br.com.alura.AluraFake.models.Type;

public class TaskMapper {
    public static Task toEntity(NewOpenTextTaskDTO openTextTaskDTO, Course course) {
        Task openTextTask = new Task(openTextTaskDTO.statement(), openTextTaskDTO.order(), Type.OPEN_TEXT, course);

        return openTextTask;
    }

    public static Task toEntity(NewSingleChoiceTaskDTO singleChoiceTaskDTO, Course course) {
        Task singleChoinceTask = new Task(singleChoiceTaskDTO.statement(), singleChoiceTaskDTO.order(),
                Type.SINGLE_CHOICE, course);

        return singleChoinceTask;
    }

    public static Task toEntity(NewMultipleChoiceTaskDTO multipleChoiceDTO, Course course) {
        Task multipleChoiceTask = new Task(multipleChoiceDTO.statement(), multipleChoiceDTO.order(),
                Type.MULTIPLE_CHOICE, course);

        return multipleChoiceTask;
    }

    public static TaskResponseDTO toDTO(Task task, List<TaskOptionResponseDTO> options) {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO(task.getStatement(), task.getOrder(), task.getType(),
                options);

        return taskResponseDTO;
    }
}
