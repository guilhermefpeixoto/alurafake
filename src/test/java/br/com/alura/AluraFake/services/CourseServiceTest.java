package br.com.alura.AluraFake.services;

import br.com.alura.AluraFake.dtos.courses.NewCourseDTO;
import br.com.alura.AluraFake.exceptions.UserNotFoundException;
import br.com.alura.AluraFake.exceptions.UserNotInstructorException;
import br.com.alura.AluraFake.models.Course;
import br.com.alura.AluraFake.models.Role;
import br.com.alura.AluraFake.models.User;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void createCourse_should_throw_UserNotFoundException_when_user_not_found() {
        NewCourseDTO courseDTO = new NewCourseDTO("Java", "Description", "instructor@alura.com.br");

        when(userRepository.findByEmail(courseDTO.emailInstructor())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> courseService.createCourse(courseDTO));

        assertEquals("User not found.", exception.getMessage());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void createCourse_should_throw_UserNotInstructorException_when_user_is_not_instructor() {
        NewCourseDTO courseDTO = new NewCourseDTO("Java", "Description", "student@alura.com.br");
        User student = new User("Aluno", "aluno@alura.com.br", Role.STUDENT);

        when(userRepository.findByEmail(courseDTO.emailInstructor())).thenReturn(Optional.of(student));

        UserNotInstructorException exception = assertThrows(
                UserNotInstructorException.class,
                () -> courseService.createCourse(courseDTO));

        assertEquals("This user is not an instructor.", exception.getMessage());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void createCourse_should_save_course_when_user_is_instructor() throws Exception {
        NewCourseDTO courseDTO = new NewCourseDTO("Java", "Description", "instructor@alura.com.br");
        User instructor = new User("Instructor", "instructor@alura.com.br", Role.INSTRUCTOR);

        when(userRepository.findByEmail(courseDTO.emailInstructor())).thenReturn(Optional.of(instructor));

        courseService.createCourse(courseDTO);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void getAllCourses_should_return_empty_list_when_no_courses_exist() {
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        List<Course> result = courseService.getAllCourses();

        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getAllCourses_should_return_course_list_when_courses_exist() {
        User instructor = new User("Instructor", "instructor@alura.com.br", Role.INSTRUCTOR);
        Course course1 = new Course("Java", "Description", instructor);
        Course course2 = new Course("Spring Boot", "Description", instructor);

        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));

        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getTitle());
        assertEquals("Spring Boot", result.get(1).getTitle());
        verify(courseRepository, times(1)).findAll();
    }
}