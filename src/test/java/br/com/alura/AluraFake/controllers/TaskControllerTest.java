package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.dtos.tasks.NewMultipleChoiceTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.NewSingleChoiceTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.TaskOptionDTO;
import br.com.alura.AluraFake.exceptions.ContinuousSequenceException;
import br.com.alura.AluraFake.exceptions.CourseNotBuildingException;
import br.com.alura.AluraFake.exceptions.CourseNotFoundException;
import br.com.alura.AluraFake.exceptions.DuplicateOptionException;
import br.com.alura.AluraFake.exceptions.DuplicateStatementException;
import br.com.alura.AluraFake.exceptions.WrongNumberOfCorrectOptionsException;
import br.com.alura.AluraFake.exceptions.WrongNumberOfWrongOptionsException;
import br.com.alura.AluraFake.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOpenTextExercise_should_return_NotFound_when_course_not_found() throws Exception {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta válida", 1, 1L);

        doThrow(new CourseNotFoundException("Course not found"))
                .when(taskService).createOpenTextExercise(dto);

        mockMvc.perform(post("/task/new/opentext")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value("404"))
                .andExpect(jsonPath("$.messages[0]").value("Course not found"));
    }

    @Test
    void createOpenTextExercise_should_return_BadRequest_when_course_not_building() throws Exception {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta válida", 1, 1L);

        doThrow(new CourseNotBuildingException("Course not in BUILDING status"))
                .when(taskService).createOpenTextExercise(dto);

        mockMvc.perform(post("/task/new/opentext")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.messages[0]").value("Course not in BUILDING status"));
    }

    @Test
    void createOpenTextExercise_should_return_Conflict_when_duplicate_statement() throws Exception {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta duplicada", 1, 1L);

        doThrow(new DuplicateStatementException("Duplicate statement"))
                .when(taskService).createOpenTextExercise(dto);

        mockMvc.perform(post("/task/new/opentext")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value("409"))
                .andExpect(jsonPath("$.messages[0]").value("Duplicate statement"));
    }

    @Test
    void createOpenTextExercise_should_return_BadRequest_when_invalid_order() throws Exception {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta válida", 10, 1L);

        doThrow(new ContinuousSequenceException("Invalid task order"))
                .when(taskService).createOpenTextExercise(dto);

        mockMvc.perform(post("/task/new/opentext")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.messages[0]").value("Invalid task order"));
    }

    @Test
    void createOpenTextExercise_should_return_Created_when_valid_request() throws Exception {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Java é uma linguagem de programação?", 1, 1L);

        mockMvc.perform(post("/task/new/opentext")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createSingleChoice_should_return_NotFound_when_course_not_found() throws Exception {
        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO(
                "Pergunta válida",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Python", false)));

        doThrow(new CourseNotFoundException("Course not found."))
                .when(taskService).createSingleChoiceTask(dto);

        mockMvc.perform(post("/task/new/singlechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value("404"))
                .andExpect(jsonPath("$.messages[0]").value("Course not found."));
    }

    @Test
    void createSingleChoice_should_return_BadRequest_when_course_not_building() throws Exception {
        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO(
                "Pergunta válida",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Python", false)));

        doThrow(new CourseNotBuildingException("Only courses with 'BUILDING' status can receive new tasks."))
                .when(taskService).createSingleChoiceTask(dto);

        mockMvc.perform(post("/task/new/singlechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(
                        jsonPath("$.messages[0]").value("Only courses with 'BUILDING' status can receive new tasks."));
    }

    @Test
    void createSingleChoice_should_return_Conflict_when_duplicate_statement() throws Exception {
        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO(
                "Pergunta duplicada",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Python", false)));

        doThrow(new DuplicateStatementException("There is already a task with this statement."))
                .when(taskService).createSingleChoiceTask(dto);

        mockMvc.perform(post("/task/new/singlechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value("409"))
                .andExpect(jsonPath("$.messages[0]").value("There is already a task with this statement."));
    }

    @Test
    void createSingleChoice_should_return_Conflict_when_Duplicate_Options() throws Exception {
        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO(
                "Pergunta válida",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Java", false)));

        doThrow(new DuplicateOptionException("There can be no duplicate options."))
                .when(taskService).createSingleChoiceTask(dto);

        mockMvc.perform(post("/task/new/singlechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value("409"))
                .andExpect(jsonPath("$.messages[0]").value("There can be no duplicate options."));
    }

    @Test
    void createSingleChoice_should_return_BadRequest_when_invalid_order() throws Exception {
        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO(
                "Pergunta válida",
                99,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Python", false)));

        doThrow(new ContinuousSequenceException("The order of tasks must be continuous."))
                .when(taskService).createSingleChoiceTask(dto);

        mockMvc.perform(post("/task/new/singlechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.messages[0]").value("The order of tasks must be continuous."));
    }

    @Test
    void createSingleChoice_should_return_Conflict_when_wrong_correct_options() throws Exception {
        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO(
                "Pergunta válida",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Opção 1", true),
                        new TaskOptionDTO("Opção 2", true)));

        doThrow(new WrongNumberOfCorrectOptionsException("There must be exactly 1 correct option."))
                .when(taskService).createSingleChoiceTask(dto);

        mockMvc.perform(post("/task/new/singlechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value("409"))
                .andExpect(jsonPath("$.messages[0]").value("There must be exactly 1 correct option."));
    }

    @Test
    void createSingleChoice_should_return_created_when_valid_request() throws Exception {
        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO(
                "Java é uma linguagem de programação?",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Não", false),
                        new TaskOptionDTO("Sim", true)));

        mockMvc.perform(post("/task/new/singlechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createMultipleChoice_should_return_NotFound_when_course_not_found() throws Exception {
        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO(
                "Pergunta válida",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Python", false),
                        new TaskOptionDTO("Ruby", false)));

        doThrow(new CourseNotFoundException("Course not found."))
                .when(taskService).createMultipleChoiceTask(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value("404"))
                .andExpect(jsonPath("$.messages[0]").value("Course not found."));
    }

    @Test
    void createMultipleChoice_should_return_BadRequest_when_course_not_building() throws Exception {
        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO(
                "Pergunta válida",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Python", false),
                        new TaskOptionDTO("Ruby", false)));

        doThrow(new CourseNotBuildingException("Only courses with 'BUILDING' status can receive new tasks."))
                .when(taskService).createMultipleChoiceTask(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(
                        jsonPath("$.messages[0]").value("Only courses with 'BUILDING' status can receive new tasks."));
    }

    @Test
    void createMultipleChoice_should_return_Conflict_when_duplicate_statement() throws Exception {
        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO(
                "Pergunta duplicada",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Python", false),
                        new TaskOptionDTO("Ruby", false)));

        doThrow(new DuplicateStatementException("There is already a task with this statement."))
                .when(taskService).createMultipleChoiceTask(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value("409"))
                .andExpect(jsonPath("$.messages[0]").value("There is already a task with this statement."));
    }

    @Test
    void createMultipleChoice_should_return_BadRequest_when_invalid_order() throws Exception {
        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO(
                "Pergunta válida",
                99,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Python", false),
                        new TaskOptionDTO("Ruby", true)));

        doThrow(new ContinuousSequenceException("The order of tasks must be continuous."))
                .when(taskService).createMultipleChoiceTask(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.messages[0]").value("The order of tasks must be continuous."));
    }

    @Test
    void createMultipleChoice_should_ReturnConflict_when_insufficient_correct_options() throws Exception {
        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO(
                "Pergunta válida",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Opção 1", true),
                        new TaskOptionDTO("Opção 2", false),
                        new TaskOptionDTO("Opção 3", false)));

        doThrow(new WrongNumberOfCorrectOptionsException("Must have at least 2 correct options."))
                .when(taskService).createMultipleChoiceTask(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value("409"))
                .andExpect(jsonPath("$.messages[0]").value("Must have at least 2 correct options."));
    }

    @Test
    void createMultipleChoice_should_return_Conflict_when_no_wrong_options() throws Exception {
        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO(
                "Pergunta inválida",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Opção 1", true),
                        new TaskOptionDTO("Opção 2", true),
                        new TaskOptionDTO("Opção 3", true)));

        doThrow(new WrongNumberOfWrongOptionsException("There must be at least 1 wrong option."))
                .when(taskService).createMultipleChoiceTask(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value("409"))
                .andExpect(jsonPath("$.messages[0]").value("There must be at least 1 wrong option."));
    }

    @Test
    void createMultipleChoice_should_return_Conflict_when_duplicate_options() throws Exception {
        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO(
                "Pergunta inválida",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Python", false)));

        doThrow(new DuplicateOptionException("There can be no duplicate options."))
                .when(taskService).createMultipleChoiceTask(dto);

        mockMvc.perform(post("/task/new/multiplechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value("409"))
                .andExpect(jsonPath("$.messages[0]").value("There can be no duplicate options."));
    }

    @Test
    void createMultipleChoice_should_return_Created_when_valid_request() throws Exception {
        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO(
                "Quais são linguagens JVM?",
                1,
                1L,
                List.of(
                        new TaskOptionDTO("Java", true),
                        new TaskOptionDTO("Kotlin", true),
                        new TaskOptionDTO("C#", false)));

        mockMvc.perform(post("/task/new/multiplechoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

}