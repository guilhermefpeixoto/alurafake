package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.dtos.courses.NewCourseDTO;
import br.com.alura.AluraFake.exceptions.UserNotFoundException;
import br.com.alura.AluraFake.exceptions.UserNotInstructorException;
import br.com.alura.AluraFake.models.courses.Course;
import br.com.alura.AluraFake.models.users.Role;
import br.com.alura.AluraFake.models.users.User;
import br.com.alura.AluraFake.services.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Import(br.com.alura.AluraFake.infra.security.SecurityConfig.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "INSTRUCTOR")
    void newCourseDTO__should_return_bad_request_when_email_is_invalid() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO("Java", "Curso de Java", "paulo@alura.com.br");

        doThrow(new UserNotFoundException("User not found.")).when(courseService).createCourse(newCourseDTO);

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value("404"))
                .andExpect(jsonPath("$.messages[0]").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR")
    void newCourseDTO__should_return_bad_request_when_email_is_no_instructor() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO("Java", "Curso de Java", "paulo@alura.com.br");

        doThrow(new UserNotInstructorException("This user is not an instructor.")).when(courseService)
                .createCourse(newCourseDTO);

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.statusCode").value("403"))
                .andExpect(jsonPath("$.messages[0]").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR")
    void newCourseDTO__should_return_created_when_new_course_request_is_valid() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO("Java", "Curso de Java", "paulo@alura.com.br");

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void listAllCourses__should_list_all_courses() throws Exception {
        User paulo = new User("Paulo", "paulo@alua.com.br", Role.INSTRUCTOR, "encodedPassword");
        Course java = new Course("Java", "Curso de java", paulo);
        Course hibernate = new Course("Hibernate", "Curso de hibernate", paulo);
        Course spring = new Course("Spring", "Curso de spring", paulo);

        when(courseService.getAllCourses()).thenReturn(Arrays.asList(java, hibernate, spring));

        mockMvc.perform(get("/course")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java"))
                .andExpect(jsonPath("$[0].description").value("Curso de java"))
                .andExpect(jsonPath("$[1].title").value("Hibernate"))
                .andExpect(jsonPath("$[1].description").value("Curso de hibernate"))
                .andExpect(jsonPath("$[2].title").value("Spring"))
                .andExpect(jsonPath("$[2].description").value("Curso de spring"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void createCourse__should_return_forbidden_with_student_role() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO("Java", "Curso de Java", "paulo@alura.com.br");

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createCourse__should_return_unauthorized_without_authentication() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO("Java", "Curso de Java", "paulo@alura.com.br");

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isUnauthorized());
    }
}