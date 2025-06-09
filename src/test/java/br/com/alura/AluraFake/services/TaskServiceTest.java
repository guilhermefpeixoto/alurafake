package br.com.alura.AluraFake.services;

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
import br.com.alura.AluraFake.models.Course;
import br.com.alura.AluraFake.models.Status;
import br.com.alura.AluraFake.models.Task;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createOpenTextExercise_should_throw_course_NotFoundException_when_course_not_found() {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta", 1, 1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        CourseNotFoundException exception = assertThrows(
                CourseNotFoundException.class,
                () -> taskService.createOpenTextExercise(dto));

        assertEquals("Course not found.", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createOpenTextExercise_should_throw_CourseNotBuildingException_when_status_invalid() {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta", 1, 1L);
        Course course = new Course();
        course.setStatus(Status.PUBLISHED);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseNotBuildingException exception = assertThrows(
                CourseNotBuildingException.class,
                () -> taskService.createOpenTextExercise(dto));

        assertEquals("Only courses with 'BUILDING' status can receive new tasks.", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createOpenTextExercise_should_throw_DuplicateStatementException_when_statement_exists() {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta duplicada", 1, 1L);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        Task existingTask = new Task();
        existingTask.setStatement("Pergunta duplicada");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.existsByStatementAndCourseId(dto.statement(), course.getId())).thenReturn(true);

        DuplicateStatementException exception = assertThrows(
                DuplicateStatementException.class,
                () -> taskService.createOpenTextExercise(dto));

        assertEquals("There is already a task with this statement.", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createOpenTextExercise_should_throw_ContinuousSequenceException_when_order_invalid() {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta", 5, 1L);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(course.getId())).thenReturn(List.of(
                createTask(1), createTask(2)));

        ContinuousSequenceException exception = assertThrows(
                ContinuousSequenceException.class,
                () -> taskService.createOpenTextExercise(dto));

        assertEquals("The order of tasks must be continuous.", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createOpenTextExercise_should_reoder_tasks_when_inserting_in_middle() throws Exception {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta", 2, 1L);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        Task task1 = createTask(1);
        Task task2 = createTask(2);
        Task task3 = createTask(3);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(course.getId())).thenReturn(List.of(task1, task2, task3));
        when(taskRepository.save(any(Task.class))).thenReturn(new Task());

        taskService.createOpenTextExercise(dto);

        assertEquals(3, task2.getOrder());
        assertEquals(4, task3.getOrder());
        verify(taskRepository, times(1)).saveAll(List.of(task2, task3));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createOpenTextExercise_should_not_reorder_when_inserting_at_end() throws Exception {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta", 4, 1L);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(course.getId())).thenReturn(Arrays.asList(
                createTask(1), createTask(2), createTask(3)));
        when(taskRepository.save(any(Task.class))).thenReturn(new Task());

        taskService.createOpenTextExercise(dto);

        verify(taskRepository, never()).saveAll(any());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createOpenTextExercise_should_create_task_when_first_task() throws Exception {
        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Pergunta", 1, 1L);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(course.getId())).thenReturn(Collections.emptyList());

        taskService.createOpenTextExercise(dto);

        verify(taskRepository, never()).saveAll(any());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createSingleChoiceTask_should_validate_correct_option_count() {
        List<TaskOptionDTO> noCorrectOptions = List.of(
                new TaskOptionDTO("Opção 1", false),
                new TaskOptionDTO("Opção 2", false));

        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO("Pergunta", 1, 1L, noCorrectOptions);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        WrongNumberOfCorrectOptionsException exception = assertThrows(
                WrongNumberOfCorrectOptionsException.class,
                () -> taskService.createSingleChoiceTask(dto));

        assertEquals("There must be exactly 1 correct option.", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createSingleChoiceTask_should_validate_correct_option_count_when_more_than_one_correct() {
        List<TaskOptionDTO> noCorrectOptions = List.of(
                new TaskOptionDTO("Opção 1", true),
                new TaskOptionDTO("Opção 2", true));

        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO("Pergunta", 1, 1L, noCorrectOptions);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        WrongNumberOfCorrectOptionsException exception = assertThrows(
                WrongNumberOfCorrectOptionsException.class,
                () -> taskService.createSingleChoiceTask(dto));

        assertEquals("There must be exactly 1 correct option.", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createSingleChoiceTask_should_validate_option_uniqueness() {
        List<TaskOptionDTO> duplicateOptions = List.of(
                new TaskOptionDTO("Opção Duplicada", true),
                new TaskOptionDTO("opção duplicada", false));

        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO("Pergunta", 1, 1L, duplicateOptions);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        DuplicateOptionException exception = assertThrows(
                DuplicateOptionException.class,
                () -> taskService.createSingleChoiceTask(dto));

        assertEquals("There can be no duplicate options.", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createSingleChoiceTask_should_validate_option_statement_equality() {
        String statement = "Pergunta";
        List<TaskOptionDTO> options = List.of(
                new TaskOptionDTO(statement, true),
                new TaskOptionDTO("Opção", false));

        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO(statement, 1, 1L, options);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        DuplicateStatementException exception = assertThrows(
                DuplicateStatementException.class,
                () -> taskService.createSingleChoiceTask(dto));

        assertEquals("There can be no options identical to the statement.", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createSingleChoiceTask_should_save_valid_task() throws Exception {
        List<TaskOptionDTO> validOptions = List.of(
                new TaskOptionDTO("Correta", true),
                new TaskOptionDTO("Incorreta", false),
                new TaskOptionDTO("Errada", false));

        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO("Pergunta válida", 1, 1L, validOptions);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.save(any())).thenReturn(new Task());

        taskService.createSingleChoiceTask(dto);

        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void createSingleChoiceTask_should_reorder_existing_tasks() throws Exception {
        List<TaskOptionDTO> options = List.of(new TaskOptionDTO("Correta", true),
                new TaskOptionDTO("Incorreta", false));
        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO("Pergunta", 2, 1L, options);

        Course course = new Course();
        course.setStatus(Status.BUILDING);

        Task task1 = createTask(1);
        Task task2 = createTask(2);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(course.getId())).thenReturn(List.of(task1, task2));
        when(taskRepository.save(any())).thenReturn(new Task());

        taskService.createSingleChoiceTask(dto);

        assertEquals(3, task2.getOrder());
        verify(taskRepository, times(1)).saveAll(List.of(task2));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createMultipleChoiceTask_should_validate_minimum_correct_options() {
        List<TaskOptionDTO> insufficientCorrectOptions = List.of(
                new TaskOptionDTO("Correta", true),
                new TaskOptionDTO("Incorreta", false),
                new TaskOptionDTO("Errada", false));

        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO("Pergunta", 1, 1L, insufficientCorrectOptions);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        WrongNumberOfCorrectOptionsException exception = assertThrows(
                WrongNumberOfCorrectOptionsException.class,
                () -> taskService.createMultipleChoiceTask(dto));

        assertEquals("Must have at least 2 correct options.", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createMultipleChoiceTask_should_validate_wrong_options_presence() {
        List<TaskOptionDTO> noWrongOptions = List.of(
                new TaskOptionDTO("Correta 1", true),
                new TaskOptionDTO("Correta 2", true),
                new TaskOptionDTO("Correta 3", true));

        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO("Pergunta", 1, 1L, noWrongOptions);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        WrongNumberOfWrongOptionsException exception = assertThrows(
                WrongNumberOfWrongOptionsException.class,
                () -> taskService.createMultipleChoiceTask(dto));

        assertEquals("There must be at least 1 wrong option.", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createMultipleChoiceTask_should_save_valid_task() throws Exception {
        List<TaskOptionDTO> validOptions = List.of(
                new TaskOptionDTO("Correta 1", true),
                new TaskOptionDTO("Correta 2", true),
                new TaskOptionDTO("Incorreta", false));

        NewMultipleChoiceTaskDTO dto = new NewMultipleChoiceTaskDTO("Pergunta válida", 1, 1L, validOptions);
        Course course = new Course();
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.save(any())).thenReturn(new Task());

        taskService.createMultipleChoiceTask(dto);

        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void createMultipleChoiceTask_should_reorder_existing_tasks() throws Exception {
        List<TaskOptionDTO> options = List.of(new TaskOptionDTO("Correta", true),
                new TaskOptionDTO("Incorreta 1", false),
                new TaskOptionDTO("Incorreta 2", false));
        NewSingleChoiceTaskDTO dto = new NewSingleChoiceTaskDTO("Pergunta", 2, 1L, options);

        Course course = new Course();
        course.setStatus(Status.BUILDING);

        Task task1 = createTask(1);
        Task task2 = createTask(2);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(course.getId())).thenReturn(List.of(task1, task2));
        when(taskRepository.save(any())).thenReturn(new Task());

        taskService.createSingleChoiceTask(dto);

        assertEquals(3, task2.getOrder());
        verify(taskRepository, times(1)).saveAll(List.of(task2));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    private Task createTask(int order) {
        Task task = new Task();
        task.setOrder(order);
        return task;
    }
}