package com.SmartStudy.model;

import java.io.Serializable;
import java.time.LocalDate;

public class StudyTask implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int taskId;
    private final int subjectId;
    private String title;
    private int estimatedMinutes;
    private Difficulty difficulty;
    private int importance; // 1 (low) to 5 (high)
    private LocalDate dueDate;
    private TaskStatus status;

    public StudyTask(int taskId, int subjectId, String title,
                     int estimatedMinutes, Difficulty difficulty,
                     int importance, LocalDate dueDate) {
        this.taskId = taskId;
        this.subjectId = subjectId;
        this.title = title;
        this.estimatedMinutes = estimatedMinutes;
        this.difficulty = difficulty;
        this.importance = importance;
        this.dueDate = dueDate;
        this.status = TaskStatus.PENDING;
    }

    public int getTaskId() { return taskId; }
    public int getSubjectId() { return subjectId; }
    public String getTitle() { return title; }
    public int getEstimatedMinutes() { return estimatedMinutes; }
    public Difficulty getDifficulty() { return difficulty; }
    public int getImportance() { return importance; }
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }

    public void setTitle(String title) { this.title = title; }
    public void setEstimatedMinutes(int estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public void setImportance(int importance) { this.importance = importance; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setStatus(TaskStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Task ID: " + taskId
                + " | Subject ID: " + subjectId
                + " | " + title
                + " | " + estimatedMinutes + " min"
                + " | " + difficulty
                + " | Importance: " + importance + "/5"
                + " | Due: " + dueDate
                + " | " + status;
    }
}
