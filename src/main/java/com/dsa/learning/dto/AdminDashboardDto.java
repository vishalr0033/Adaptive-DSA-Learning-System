package com.dsa.learning.dto;

import com.dsa.learning.model.Student;
import com.dsa.learning.model.Submission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDashboardDto {
    private long totalStudents;
    private long totalProblems;
    private long totalSubmissions;
    private String mostDifficultTopic;
    private String mostCommonMistake;

    private List<Student> students = new ArrayList<>();
    private List<StudentPerformanceSummaryDto> studentPerformances = new ArrayList<>();
    private List<Submission> recentSubmissions = new ArrayList<>();
    private Map<String, Long> topicProblemDistribution = new HashMap<>();
    private List<MistakeStat> commonMistakes = new ArrayList<>();

    public static class MistakeStat {
        private String specificMistake;
        private String topic;
        private String category;
        private long count;

        public MistakeStat() {}
        public MistakeStat(String specificMistake, String topic, String category, long count) {
            this.specificMistake = specificMistake;
            this.topic = topic;
            this.category = category;
            this.count = count;
        }

        public String getSpecificMistake() { return specificMistake; }
        public void setSpecificMistake(String specificMistake) { this.specificMistake = specificMistake; }

        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }

    public AdminDashboardDto() {}

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getTotalProblems() { return totalProblems; }
    public void setTotalProblems(long totalProblems) { this.totalProblems = totalProblems; }

    public long getTotalSubmissions() { return totalSubmissions; }
    public void setTotalSubmissions(long totalSubmissions) { this.totalSubmissions = totalSubmissions; }

    public String getMostDifficultTopic() { return mostDifficultTopic; }
    public void setMostDifficultTopic(String mostDifficultTopic) { this.mostDifficultTopic = mostDifficultTopic; }

    public String getMostCommonMistake() { return mostCommonMistake; }
    public void setMostCommonMistake(String mostCommonMistake) { this.mostCommonMistake = mostCommonMistake; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    public List<StudentPerformanceSummaryDto> getStudentPerformances() { return studentPerformances; }
    public void setStudentPerformances(List<StudentPerformanceSummaryDto> studentPerformances) { this.studentPerformances = studentPerformances; }

    public List<Submission> getRecentSubmissions() { return recentSubmissions; }
    public void setRecentSubmissions(List<Submission> recentSubmissions) { this.recentSubmissions = recentSubmissions; }

    public Map<String, Long> getTopicProblemDistribution() { return topicProblemDistribution; }
    public void setTopicProblemDistribution(Map<String, Long> topicProblemDistribution) { this.topicProblemDistribution = topicProblemDistribution; }

    public List<MistakeStat> getCommonMistakes() { return commonMistakes; }
    public void setCommonMistakes(List<MistakeStat> commonMistakes) { this.commonMistakes = commonMistakes; }
}
