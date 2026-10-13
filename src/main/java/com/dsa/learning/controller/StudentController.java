package com.dsa.learning.controller;

import com.dsa.learning.dto.StudentDashboardDto;
import com.dsa.learning.model.Student;
import com.dsa.learning.repository.StudentRepository;
import com.dsa.learning.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<StudentDashboardDto> getDashboard(@PathVariable Long id) {
        StudentDashboardDto dto = studentService.getDashboard(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getProfile(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
