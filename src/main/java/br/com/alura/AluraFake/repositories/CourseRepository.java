package br.com.alura.AluraFake.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.AluraFake.models.courses.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
