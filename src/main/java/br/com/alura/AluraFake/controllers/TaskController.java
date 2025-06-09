package br.com.alura.AluraFake.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import br.com.alura.AluraFake.dtos.tasks.NewMultipleChoiceTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.NewSingleChoiceTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.TaskResponseDTO;
import br.com.alura.AluraFake.services.TaskService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/task/new")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Transactional
    @PostMapping("/opentext")
    public ResponseEntity<String> createOpenTextExercise(@RequestBody @Valid NewOpenTextTaskDTO newOpenTextTaskDTO) {
        this.taskService.createOpenTextExercise(newOpenTextTaskDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    @PostMapping("/singlechoice")
    public ResponseEntity<String> newSingleChoice(@RequestBody @Valid NewSingleChoiceTaskDTO newSingleChoiceTaskDTO) {
        this.taskService.createSingleChoiceTask(newSingleChoiceTaskDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    @PostMapping("/multiplechoice")
    public ResponseEntity<String> newMultipleChoice(@RequestBody @Valid NewMultipleChoiceTaskDTO newMultipleChoiceDTO) {
        this.taskService.createMultipleChoiceTask(newMultipleChoiceDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/opentext/{courseId}") // Endpoint criado para visualizar como as Tasks estavam sendo criadas. Utilizado apenas para testes no Insomnia.
    public ResponseEntity<List<TaskResponseDTO>> getAllCourseTasks(@PathVariable Long courseId) {
        List<TaskResponseDTO> allTasks = this.taskService.getAllTasks(courseId);

        return ResponseEntity.ok().body(allTasks);
    }

}