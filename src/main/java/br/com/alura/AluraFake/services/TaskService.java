package br.com.alura.AluraFake.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.AluraFake.dtos.tasks.NewMultipleChoiceTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.NewSingleChoiceTaskDTO;
import br.com.alura.AluraFake.dtos.tasks.TaskOptionDTO;
import br.com.alura.AluraFake.dtos.tasks.TaskResponseDTO;
import br.com.alura.AluraFake.exceptions.ContinuousSequenceException;
import br.com.alura.AluraFake.exceptions.CourseNotBuildingException;
import br.com.alura.AluraFake.exceptions.CourseNotFoundException;
import br.com.alura.AluraFake.exceptions.DuplicateOptionException;
import br.com.alura.AluraFake.exceptions.DuplicateStatementException;
import br.com.alura.AluraFake.exceptions.WrongNumberOfCorrectOptionsException;
import br.com.alura.AluraFake.exceptions.WrongNumberOfWrongOptionsException;
import br.com.alura.AluraFake.mappers.TaskMapper;
import br.com.alura.AluraFake.mappers.TaskOptionMapper;
import br.com.alura.AluraFake.models.courses.Course;
import br.com.alura.AluraFake.models.courses.Status;
import br.com.alura.AluraFake.models.tasks.Task;
import br.com.alura.AluraFake.models.tasks.TaskOption;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseRepository courseRepository;
    
    public void createOpenTextExercise(NewOpenTextTaskDTO newOpenTextTaskDTO) {
        Course course = this.getValidatedCourse(newOpenTextTaskDTO.courseId());
        this.validateStatement(newOpenTextTaskDTO.statement(), course.getId());
        this.validateTaskOrder(newOpenTextTaskDTO.order(), course);

        Task task = TaskMapper.toEntity(newOpenTextTaskDTO, course);
        this.taskRepository.save(task);
    }

    public void createSingleChoiceTask(NewSingleChoiceTaskDTO newSingleChoiceTaskDTO) {
        Course course = this.getValidatedCourse(newSingleChoiceTaskDTO.courseId());
        this.validateStatement(newSingleChoiceTaskDTO.statement(), course.getId());
        this.validateSingleChoiceOptions(newSingleChoiceTaskDTO.statement(), newSingleChoiceTaskDTO.options());
        this.validateTaskOrder(newSingleChoiceTaskDTO.order(), course);

        Task task = TaskMapper.toEntity(newSingleChoiceTaskDTO, course);
        List<TaskOption> options = TaskOptionMapper.toEntities(newSingleChoiceTaskDTO.options(), task);
        task.setOptions(options);
        this.taskRepository.save(task);

    }

    public void createMultipleChoiceTask(NewMultipleChoiceTaskDTO newMultipleChoiceDTO) {
        Course course = this.getValidatedCourse(newMultipleChoiceDTO.courseId());
        this.validateStatement(newMultipleChoiceDTO.statement(), course.getId());
        this.validateMultipleChoiceOptions(newMultipleChoiceDTO.statement(), newMultipleChoiceDTO.options());
        this.validateTaskOrder(newMultipleChoiceDTO.order(), course);

        Task task = TaskMapper.toEntity(newMultipleChoiceDTO, course);
        List<TaskOption> options = TaskOptionMapper.toEntities(newMultipleChoiceDTO.options(), task);
        task.setOptions(options);
        this.taskRepository.save(task);

    }

    public List<TaskResponseDTO> getAllTasks(Long courseId) {
        List<TaskResponseDTO> taskResponseDTOs = new ArrayList<TaskResponseDTO>();
        List<Task> tasks = this.taskRepository.findByCourseIdOrderByOrderAsc(courseId);
        for (Task task : tasks) {
            TaskResponseDTO taskResponseDTO = TaskMapper.toDTO(task, TaskOptionMapper.toDTOs(task.getOptions()));
            taskResponseDTOs.add(taskResponseDTO);
        }

        return taskResponseDTOs;
    }

    private Course getValidatedCourse(Long courseId) {
        Optional<Course> possibleCourse = this.courseRepository.findById(courseId);

        if (possibleCourse.isEmpty()) {
            throw new CourseNotFoundException("Course not found.");
        }

        Course course = possibleCourse.get();

        if (course.getStatus() != Status.BUILDING) {
            throw new CourseNotBuildingException("Only courses with 'BUILDING' status can receive new tasks.");
        }

        return course;
    }

    private void validateStatement(String statement, Long courseId) {
        if (taskRepository.existsByStatementAndCourseId(statement, courseId)) {
            throw new DuplicateStatementException("There is already a task with this statement.");
        }
    }

    private void validateTaskOrder(Integer order, Course course) {
        List<Task> tasks = taskRepository.findByCourseIdOrderByOrderAsc(course.getId());

        if (order > tasks.size() + 1) {
            throw new ContinuousSequenceException("The order of tasks must be continuous.");
        }

        if (order <= tasks.size()) {
            shiftTaskOrders(order, tasks);
        }
    }

    private void shiftTaskOrders(Integer newOrder, List<Task> tasks) {
        List<Task> tasksToUpdate = tasks.stream()
                .filter(task -> task.getOrder() >= newOrder)
                .peek(task -> task.setOrder(task.getOrder() + 1))
                .toList();

        taskRepository.saveAll(tasksToUpdate);
    }

    private void validateSingleChoiceOptions(String statement, List<TaskOptionDTO> options) {
        validateSingleCorrectOption(options);
        validateOptionUniqueness(options);
        validateOptionsStatement(statement, options);
    }

    private void validateSingleCorrectOption(List<TaskOptionDTO> options) {
        long correctOptions = options.stream().filter(TaskOptionDTO::isCorrect).count();

        if (correctOptions != 1) {
            throw new WrongNumberOfCorrectOptionsException("There must be exactly 1 correct option.");
        }
    }

    private void validateOptionUniqueness(List<TaskOptionDTO> options) {
        boolean hasDuplicateOption = options.stream()
                .map(taskOption -> taskOption.option().toLowerCase())
                .distinct().count() < options.size();

        if (hasDuplicateOption) {
            throw new DuplicateOptionException("There can be no duplicate options.");
        }
    }

    private void validateOptionsStatement(String statement, List<TaskOptionDTO> options) {
        boolean hasOptionEqualStatement = options.stream()
                .anyMatch(opt -> opt.option().toLowerCase().equals(statement.toLowerCase()));

        if (hasOptionEqualStatement) {
            throw new DuplicateStatementException("There can be no options identical to the statement.");
        }
    }

    private void validateMultipleChoiceOptions(String statement, List<TaskOptionDTO> options) {
        validateMultipleCorrectOptions(options);
        validateAtLeastOneWrongOption(options);
        validateOptionUniqueness(options);
        validateOptionsStatement(statement, options);
    }

    private void validateMultipleCorrectOptions(List<TaskOptionDTO> options) {
        long correctOptions = options.stream().filter(TaskOptionDTO::isCorrect).count();

        if (correctOptions < 2) {
            throw new WrongNumberOfCorrectOptionsException("Must have at least 2 correct options.");
        }
    }

    private void validateAtLeastOneWrongOption(List<TaskOptionDTO> options) {
        long wrongOptions = options.stream().filter(taskOption -> !taskOption.isCorrect()).count();

        if (wrongOptions == 0) {
            throw new WrongNumberOfWrongOptionsException("There must be at least 1 wrong option.");
        }
    }

}
