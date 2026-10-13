package com.dsa.learning.controller;

import com.dsa.learning.dto.AuthRequest;
import com.dsa.learning.dto.AuthResponse;
import com.dsa.learning.dto.RegisterRequest;
import com.dsa.learning.service.AdminService;
import com.dsa.learning.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final StudentService studentService;
    private final AdminService adminService;

    @Autowired
    public AuthController(StudentService studentService, AdminService adminService) {
        this.studentService = studentService;
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req, HttpSession session) {
        AuthResponse resp = studentService.register(req);
        if (resp.isSuccess()) {
            session.setAttribute("userId", resp.getId());
            session.setAttribute("role", "STUDENT");
            return ResponseEntity.ok(resp);
        }
        return ResponseEntity.badRequest().body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req, HttpSession session) {
        AuthResponse resp = studentService.login(req);
        if (resp.isSuccess()) {
            session.setAttribute("userId", resp.getId());
            session.setAttribute("role", "STUDENT");
            return ResponseEntity.ok(resp);
        }
        return ResponseEntity.status(401).body(resp);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> adminLogin(@RequestBody AuthRequest req, HttpSession session) {
        AuthResponse resp = adminService.login(req);
        if (resp.isSuccess()) {
            session.setAttribute("userId", resp.getId());
            session.setAttribute("role", "ADMIN");
            return ResponseEntity.ok(resp);
        }
        return ResponseEntity.status(401).body(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("{\"success\":true,\"message\":\"Logged out successfully\"}");
    }
}
