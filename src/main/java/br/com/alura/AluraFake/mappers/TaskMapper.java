package br.com.alura.AluraFake.mappers;

import br.com.alura.AluraFake.dtos.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.models.Course;
import br.com.alura.AluraFake.models.Task;
import br.com.alura.AluraFake.models.Type;

public class TaskMapper {
    public static Task toEntity(NewOpenTextTaskDTO openTextTaskDTO, Course course) {
        Task openTextTask = new Task(openTextTaskDTO.statement(), openTextTaskDTO.order(), Type.OPEN_TEXT, null, course);

        return openTextTask;
    }
}
