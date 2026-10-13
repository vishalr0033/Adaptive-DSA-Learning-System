package com.dsa.learning.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private Long id;
    private String name;
    private String email;
    private String role; // "STUDENT" or "ADMIN"
    private String college;
    private String branch;
    private Integer graduationYear;

    public AuthResponse() {}

    public static AuthResponse success(Long id, String name, String email, String role) {
        AuthResponse resp = new AuthResponse();
        resp.setSuccess(true);
        resp.setId(id);
        resp.setName(name);
        resp.setEmail(email);
        resp.setRole(role);
        resp.setMessage("Authentication successful");
        return resp;
    }

    public static AuthResponse failure(String message) {
        AuthResponse resp = new AuthResponse();
        resp.setSuccess(false);
        resp.setMessage(message);
        return resp;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public Integer getGraduationYear() { return graduationYear; }
    public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }
}
