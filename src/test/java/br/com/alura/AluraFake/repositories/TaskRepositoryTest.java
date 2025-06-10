package br.com.alura.AluraFake.repositories;

import br.com.alura.AluraFake.models.courses.Course;
import br.com.alura.AluraFake.models.tasks.Task;
import br.com.alura.AluraFake.models.tasks.Type;
import br.com.alura.AluraFake.models.users.Role;
import br.com.alura.AluraFake.models.users.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        User instructor = new User("Paulo Silveira", "paulo@alura.com.br", Role.INSTRUCTOR, "123456");
        userRepository.save(instructor);

        course1 = new Course("Java OO", "Curso de Java Orientado a Objetos", instructor);
        course2 = new Course("Spring Boot", "Curso de Spring Boot", instructor);
        courseRepository.save(course1);
        courseRepository.save(course2);

        Task task1 = new Task("O que é herança?", 2, Type.OPEN_TEXT, course1);
        Task task2 = new Task("O que é polimorfismo?", 1, Type.OPEN_TEXT, course1);
        Task task3 = new Task("O que é um Bean?", 1, Type.OPEN_TEXT, course2);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);
    }

    @Test
    void findByCourseIdOrderByOrderAsc_should_return_tasks_ordered_by_order_asc() {
        List<Task> tasks = taskRepository.findByCourseIdOrderByOrderAsc(course1.getId());

        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getStatement()).isEqualTo("O que é polimorfismo?");
        assertThat(tasks.get(0).getOrder()).isEqualTo(1);
        assertThat(tasks.get(1).getStatement()).isEqualTo("O que é herança?");
        assertThat(tasks.get(1).getOrder()).isEqualTo(2);
    }

    @Test
    void findByCourseIdOrderByOrderAsc_should_return_empty_list_for_course_without_tasks() {
        Course newCourse = new Course("Novo Curso", "Descrição", course1.getInstructor());
        courseRepository.save(newCourse);

        List<Task> tasks = taskRepository.findByCourseIdOrderByOrderAsc(newCourse.getId());

        assertThat(tasks).isEmpty();
    }

    @Test
    void existsByStatementAndCourseId_should_return_true_for_existing_statement_in_course() {
        assertThat(taskRepository.existsByStatementAndCourseId("O que é herança?", course1.getId())).isTrue();
        assertThat(taskRepository.existsByStatementAndCourseId("O que é um Bean?", course2.getId())).isTrue();
    }

    @Test
    void existsByStatementAndCourseId_should_return_false_for_non_existing_statement_or_course() {
        assertThat(taskRepository.existsByStatementAndCourseId("O que é encapsulamento?", course1.getId())).isFalse();

        assertThat(taskRepository.existsByStatementAndCourseId("O que é um Bean?", course1.getId())).isFalse();

        assertThat(taskRepository.existsByStatementAndCourseId("O que é herança?", 999L)).isFalse();
    }
}