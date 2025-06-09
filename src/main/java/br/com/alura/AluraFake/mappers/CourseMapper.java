package br.com.alura.AluraFake.mappers;

import br.com.alura.AluraFake.dtos.courses.CourseResponseDTO;
import br.com.alura.AluraFake.dtos.courses.NewCourseDTO;
import br.com.alura.AluraFake.models.Course;
import br.com.alura.AluraFake.models.User;

public class CourseMapper {
    public static Course toEntity(NewCourseDTO courseDTO, User instructor) {
        Course course = new Course(courseDTO.title(), courseDTO.description(), instructor);

        return course;
    }

    public static CourseResponseDTO toDTO(Course course) {
        CourseResponseDTO courseDTO = new CourseResponseDTO(course.getId(), course.getTitle(), course.getDescription(),
                course.getStatus());

        return courseDTO;
    }
}
