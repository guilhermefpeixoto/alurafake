package br.com.alura.AluraFake.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.AluraFake.dtos.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.exceptions.ContinuousSequenceException;
import br.com.alura.AluraFake.exceptions.CourseNotBuildingException;
import br.com.alura.AluraFake.exceptions.CourseNotFoundException;
import br.com.alura.AluraFake.exceptions.DuplicateStatementException;
import br.com.alura.AluraFake.mappers.TaskMapper;
import br.com.alura.AluraFake.models.Course;
import br.com.alura.AluraFake.models.Status;
import br.com.alura.AluraFake.models.Task;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseRepository courseRepository;

    public void createOpenTextExercise(NewOpenTextTaskDTO newOpenTextTaskDTO) throws Exception {
        Optional<Course> possibleCourse = this.courseRepository.findById(newOpenTextTaskDTO.courseId());

        if (possibleCourse.isEmpty()) {
            throw new CourseNotFoundException("Course not found");
        }

        Course course = possibleCourse.get();
        this.validateCourse(course, newOpenTextTaskDTO);

        Task task = TaskMapper.toEntity(newOpenTextTaskDTO, course);
        this.taskRepository.save(task);
    }

    public List<Task> getAllTasks(Long courseId) {
        return this.taskRepository.findByCourseIdOrderByOrderAsc(courseId);
    }

    private void validateCourse(Course course, NewOpenTextTaskDTO newOpenTextTaskDTO) throws Exception {
        if (!course.getStatus().equals(Status.BUILDING)) {
            throw new CourseNotBuildingException("Only courses with 'BUILDING' status can receive new tasks.");
        }

        List<Task> tasks = this.taskRepository.findByCourseId(course.getId());

        if (!tasks.isEmpty()) {
            if (tasks.stream().filter(task -> task.getStatement().equals(newOpenTextTaskDTO.statement())).count() != 0) {
                throw new DuplicateStatementException("There is already a task with this statement.");
            }      
        }

        if (tasks.size() + 1 < newOpenTextTaskDTO.order()) {
            throw new ContinuousSequenceException("The order of tasks must be continuous.");
        }

        if (tasks.size() >= newOpenTextTaskDTO.order()) {
            List<Task> updatedTasks = tasks.stream().filter(task -> task.getOrder() >= newOpenTextTaskDTO.order()).peek(task -> task.setOrder(task.getOrder() + 1)).toList();
            this.taskRepository.saveAll(updatedTasks);
        }
    }
}
