package br.com.alura.AluraFake.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.AluraFake.dtos.NewCourseDTO;
import br.com.alura.AluraFake.exceptions.UserNotFoundException;
import br.com.alura.AluraFake.exceptions.UserNotInstructorException;
import br.com.alura.AluraFake.mappers.CourseMapper;
import br.com.alura.AluraFake.models.Course;
import br.com.alura.AluraFake.models.User;
import br.com.alura.AluraFake.repositories.CourseRepository;
import br.com.alura.AluraFake.repositories.UserRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public void createCourse(NewCourseDTO courseDTO) throws Exception {
        Optional<User> possibleUser = this.userRepository.findByEmail(courseDTO.emailInstructor());

        if (possibleUser.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }

        User user = possibleUser.get();

        if (!user.isInstructor()) {
            throw new UserNotInstructorException("This user is not a instructor.");
        }

        Course course = CourseMapper.toEntity(courseDTO, user);
        this.saveCourse(course);
    }

    public List<Course> getAllCourses() {
        return this.courseRepository.findAll();
    }

    private void saveCourse(Course course) {
        this.courseRepository.save(course);
    }
    
}
