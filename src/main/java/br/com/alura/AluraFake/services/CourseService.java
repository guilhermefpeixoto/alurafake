package br.com.alura.AluraFake.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.AluraFake.dtos.courses.NewCourseDTO;
import br.com.alura.AluraFake.exceptions.CourseMissingTaskTypeException;
import br.com.alura.AluraFake.exceptions.CourseNotBuildingException;
import br.com.alura.AluraFake.exceptions.CourseNotFoundException;
import br.com.alura.AluraFake.exceptions.UserNotFoundException;
import br.com.alura.AluraFake.exceptions.UserNotInstructorException;
import br.com.alura.AluraFake.mappers.CourseMapper;
import br.com.alura.AluraFake.models.Course;
import br.com.alura.AluraFake.models.Status;
import br.com.alura.AluraFake.models.Task;
import br.com.alura.AluraFake.models.Type;
import br.com.alura.AluraFake.models.User;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.TaskRepository;
import br.com.alura.AluraFake.repositories.UserRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public void createCourse(NewCourseDTO courseDTO) {
        Optional<User> possibleUser = this.userRepository.findByEmail(courseDTO.emailInstructor());

        if (possibleUser.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }

        User user = possibleUser.get();

        if (!user.isInstructor()) {
            throw new UserNotInstructorException("This user is not an instructor.");
        }

        Course course = CourseMapper.toEntity(courseDTO, user);
        this.saveCourse(course);
    }

    public List<Course> getAllCourses() {
        return this.courseRepository.findAll();
    }

    public void publishCourse(Long courseId) {
        Course course = this.getValidatedCourse(courseId);

        List<Task> courseTasks = this.taskRepository.findByCourseIdOrderByOrderAsc(course.getId());

        this.validateTasks(courseTasks);
        course.setStatus(Status.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());

        this.courseRepository.save(course);

    }

    private void saveCourse(Course course) {
        this.courseRepository.save(course);
    }

    private Course getValidatedCourse(Long courseId) {
        Optional<Course> possibleCourse = this.courseRepository.findById(courseId);

        if (possibleCourse.isEmpty()) {
            throw new CourseNotFoundException("Course not found.");
        }

        Course course = possibleCourse.get();

        if (course.getStatus() != Status.BUILDING) {
            throw new CourseNotBuildingException("Only courses with 'BUILDING' status can be published.");
        }

        return course;
    }

    private void validateTasks(List<Task> tasks) {
        boolean hasOpenTextTask = tasks.stream().anyMatch(task -> task.getType().equals(Type.OPEN_TEXT));
        boolean hasSingleChoiceTask = tasks.stream().anyMatch(task -> task.getType().equals(Type.SINGLE_CHOICE));
        boolean hasMultipleChoiceTask = tasks.stream().anyMatch(task -> task.getType().equals(Type.MULTIPLE_CHOICE));

        if (!hasOpenTextTask || !hasSingleChoiceTask || !hasMultipleChoiceTask) {
            throw new CourseMissingTaskTypeException(
                    "A course can only be published if there is at least one task of each type.");
        }
    }

}
