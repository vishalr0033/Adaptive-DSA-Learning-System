package com.dsa.learning.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "problems")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "example_input", columnDefinition = "TEXT")
    private String exampleInput;

    @Column(name = "example_output", columnDefinition = "TEXT")
    private String exampleOutput;

    @Column(columnDefinition = "TEXT")
    private String constraints;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DsaTopic topic;

    @Column(name = "starter_code", columnDefinition = "TEXT", nullable = false)
    private String starterCode;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "return_type")
    private String returnType;

    @Column(name = "parameter_types")
    private String parameterTypes;

    @Column(name = "driver_code_template", columnDefinition = "TEXT")
    private String driverCodeTemplate;

    @Column(name = "alternative_topics")
    private String alternativeTopics;

    @Column(name = "supported_approaches", columnDefinition = "TEXT")
    private String supportedApproaches;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Problem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getExampleInput() { return exampleInput; }
    public void setExampleInput(String exampleInput) { this.exampleInput = exampleInput; }

    public String getExampleOutput() { return exampleOutput; }
    public void setExampleOutput(String exampleOutput) { this.exampleOutput = exampleOutput; }

    public String getConstraints() { return constraints; }
    public void setConstraints(String constraints) { this.constraints = constraints; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public DsaTopic getTopic() { return topic; }
    public void setTopic(DsaTopic topic) { this.topic = topic; }

    public String getStarterCode() { return starterCode; }
    public void setStarterCode(String starterCode) { this.starterCode = starterCode; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }

    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }

    public String getParameterTypes() { return parameterTypes; }
    public void setParameterTypes(String parameterTypes) { this.parameterTypes = parameterTypes; }

    public String getDriverCodeTemplate() { return driverCodeTemplate; }
    public void setDriverCodeTemplate(String driverCodeTemplate) { this.driverCodeTemplate = driverCodeTemplate; }

    public String getAlternativeTopics() { return alternativeTopics; }
    public void setAlternativeTopics(String alternativeTopics) { this.alternativeTopics = alternativeTopics; }

    public String getSupportedApproaches() { return supportedApproaches; }
    public void setSupportedApproaches(String supportedApproaches) { this.supportedApproaches = supportedApproaches; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
