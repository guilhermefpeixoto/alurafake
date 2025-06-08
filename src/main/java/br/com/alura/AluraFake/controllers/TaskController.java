package br.com.alura.AluraFake.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import br.com.alura.AluraFake.dtos.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.models.Task;
import br.com.alura.AluraFake.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/task/new")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Transactional
    @PostMapping("/opentext")
    public ResponseEntity<String> createOpenTextExercise(@RequestBody @Valid NewOpenTextTaskDTO newOpenTextTaskDTO) throws Exception {
        this.taskService.createOpenTextExercise(newOpenTextTaskDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    @PostMapping("/singlechoice")
    public ResponseEntity<String> newSingleChoice() {
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping("/multiplechoice")
    public ResponseEntity<String> newMultipleChoice() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/opentext/{courseId}")
    public ResponseEntity<List<Task>> getAllCourseTasks(@PathVariable Long courseId) {
        List<Task> allTasks = this.taskService.getAllTasks(courseId);

        return ResponseEntity.ok().body(allTasks);
    }
    

}