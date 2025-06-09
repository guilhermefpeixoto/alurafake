package br.com.alura.AluraFake.controllers;

import br.com.alura.AluraFake.dtos.courses.CourseResponseDTO;
import br.com.alura.AluraFake.dtos.courses.NewCourseDTO;
import br.com.alura.AluraFake.mappers.CourseMapper;
import br.com.alura.AluraFake.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Transactional
    @PostMapping
    public ResponseEntity<String> createCourse(@Valid @RequestBody NewCourseDTO newCourse) {
        this.courseService.createCourse(newCourse);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<CourseResponseDTO> courses = this.courseService.getAllCourses().stream().map(CourseMapper::toDTO).toList();

        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @Transactional
    @PostMapping("/{id}/publish")
    public ResponseEntity<String> publishCourse(@PathVariable("id") Long courseId) {
        this.courseService.publishCourse(courseId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
