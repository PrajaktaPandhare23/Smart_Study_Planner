package com.SmartStudy.service;

import com.SmartStudy.exception.SmartStudyException;
import com.SmartStudy.model.Difficulty;
import com.SmartStudy.model.StudyTask;
import com.SmartStudy.model.Subject;
import com.SmartStudy.model.TaskStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class StudyPlannerService {
    private final List<Subject> subjects;
    private final List<StudyTask> tasks;

    public StudyPlannerService(List<Subject> subjects, List<StudyTask> tasks) {
        this.subjects = new ArrayList<>(subjects);
        this.tasks = new ArrayList<>(tasks);
        validateLoadedData();
    }

    private void validateLoadedData() {
        Set<Integer> subjectIds = new HashSet<>();
        Set<String> subjectNames = new HashSet<>();
        for (Subject subject : subjects) {
            if (!subjectIds.add(subject.getSubjectId())
                    || !subjectNames.add(normalize(subject.getSubjectName()))) {
                throw new SmartStudyException("Saved data contains duplicate subjects.");
            }
        }
        Set<Integer> taskIds = new HashSet<>();
        for (StudyTask task : tasks) {
            if (!taskIds.add(task.getTaskId())) {
                throw new SmartStudyException("Saved data contains duplicate task IDs.");
            }
        }
    }

    public void addSubject(int id, String name) {
        if (id <= 0) throw new SmartStudyException("Subject ID must be positive.");
        String cleanName = requireText(name, "Subject name");
        for (Subject subject : subjects) {
            if (subject.getSubjectId() == id) {
                throw new SmartStudyException("Subject ID already exists.");
            }
            if (normalize(subject.getSubjectName()).equals(normalize(cleanName))) {
                throw new SmartStudyException("Subject name already exists.");
            }
        }
        subjects.add(new Subject(id, cleanName));
    }

    public List<Subject> getSubjects() {
        return subjects.stream()
                .sorted(Comparator.comparingInt(Subject::getSubjectId))
                .collect(Collectors.toUnmodifiableList());
    }

    public Subject findSubject(int id) {
        return subjects.stream().filter(s -> s.getSubjectId() == id).findFirst()
                .orElseThrow(() -> new SmartStudyException("Subject not found: " + id));
    }

    public void updateSubject(int id, String newName) {
        Subject target = findSubject(id);
        String cleanName = requireText(newName, "Subject name");
        for (Subject subject : subjects) {
            if (subject.getSubjectId() != id
                    && normalize(subject.getSubjectName()).equals(normalize(cleanName))) {
                throw new SmartStudyException("Another subject already uses that name.");
            }
        }
        target.setSubjectName(cleanName);
    }

    public void deleteSubject(int id) {
        boolean hasTasks = tasks.stream().anyMatch(t -> t.getSubjectId() == id);
        if (hasTasks) {
            throw new SmartStudyException("Cannot delete this subject while it has study tasks.");
        }
        Subject subject = findSubject(id);
        subjects.remove(subject);
    }

    public void addTask(int taskId, int subjectId, String title, int minutes,
                        Difficulty difficulty, int importance, LocalDate dueDate) {
        if (taskId <= 0) throw new SmartStudyException("Task ID must be positive.");
        if (tasks.stream().anyMatch(t -> t.getTaskId() == taskId)) {
            throw new SmartStudyException("Task ID already exists.");
        }
        findSubject(subjectId);
        String cleanTitle = requireText(title, "Task title");
        if (minutes <= 0) throw new SmartStudyException("Estimated minutes must be positive.");
        if (difficulty == null) throw new SmartStudyException("Choose a difficulty.");
        if (importance < 1 || importance > 5) {
            throw new SmartStudyException("Importance must be between 1 and 5.");
        }
        if (dueDate == null) throw new SmartStudyException("Due date is required.");
        tasks.add(new StudyTask(taskId, subjectId, cleanTitle, minutes,
                difficulty, importance, dueDate));
    }

    public List<StudyTask> getTasks() {
        return tasks.stream()
                .sorted(Comparator.comparing(StudyTask::getDueDate)
                        .thenComparingInt(StudyTask::getTaskId))
                .collect(Collectors.toUnmodifiableList());
    }

    public StudyTask findTask(int id) {
        return tasks.stream().filter(t -> t.getTaskId() == id).findFirst()
                .orElseThrow(() -> new SmartStudyException("Task not found: " + id));
    }

    public void setTaskStatus(int id, TaskStatus status) {
        if (status == null) throw new SmartStudyException("Task status is required.");
        findTask(id).setStatus(status);
    }

    public void updateTask(int id, String title, int minutes, Difficulty difficulty,
                           int importance, LocalDate dueDate) {
        StudyTask task = findTask(id);
        String cleanTitle = requireText(title, "Task title");
        if (minutes <= 0) throw new SmartStudyException("Estimated minutes must be positive.");
        if (difficulty == null) throw new SmartStudyException("Choose a difficulty.");
        if (importance < 1 || importance > 5) {
            throw new SmartStudyException("Importance must be between 1 and 5.");
        }
        if (dueDate == null) throw new SmartStudyException("Due date is required.");
        task.setTitle(cleanTitle);
        task.setEstimatedMinutes(minutes);
        task.setDifficulty(difficulty);
        task.setImportance(importance);
        task.setDueDate(dueDate);
    }

    public List<PlannedTask> createDailyPlan(int availableMinutes) {
        if (availableMinutes <= 0) {
            throw new SmartStudyException("Available minutes must be positive.");
        }

        List<StudyTask> pending = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.PENDING)
                .sorted(Comparator.comparingDouble(this::priorityScore).reversed()
                        .thenComparing(StudyTask::getDueDate))
                .toList();

        List<PlannedTask> scheduled = new ArrayList<>();
        List<StudyTask> notScheduled = new ArrayList<>();
        int remaining = availableMinutes;

        for (StudyTask task : pending) {
            if (task.getEstimatedMinutes() <= remaining) {
                scheduled.add(new PlannedTask(task, priorityScore(task)));
                remaining -= task.getEstimatedMinutes();
            } else {
                notScheduled.add(task);
            }
        }
        return scheduled;
    }

    public List<StudyTask> tasksNotFittingPlan(int availableMinutes) {
        if (availableMinutes <= 0) {
            throw new SmartStudyException("Available minutes must be positive.");
        }
        List<StudyTask> pending = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.PENDING)
                .sorted(Comparator.comparingDouble(this::priorityScore).reversed())
                .toList();

        List<StudyTask> notScheduled = new ArrayList<>();
        int remaining = availableMinutes;
        for (StudyTask task : pending) {
            if (task.getEstimatedMinutes() <= remaining) remaining -= task.getEstimatedMinutes();
            else notScheduled.add(task);
        }
        return notScheduled;
    }

    // A transparent heuristic: urgency 35%, difficulty 25%, importance 25%, remaining work 15%.
    public double priorityScore(StudyTask task) {
        long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), task.getDueDate());
        double urgency = daysUntilDue < 0 ? 100
                : daysUntilDue == 0 ? 95
                : daysUntilDue == 1 ? 85
                : daysUntilDue <= 3 ? 70
                : daysUntilDue <= 7 ? 50
                : Math.max(10, 40 - daysUntilDue);
        double difficulty = switch (task.getDifficulty()) {
            case EASY -> 35;
            case MEDIUM -> 65;
            case HARD -> 100;
        };
        double importance = task.getImportance() * 20.0;
        double remainingWork = Math.min(100, task.getEstimatedMinutes() / 3.0);
        return 0.35 * urgency + 0.25 * difficulty
                + 0.25 * importance + 0.15 * remainingWork;
    }

    public Progress getProgress() {
        long total = tasks.size();
        long completed = tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        long pending = total - completed;
        long overdue = tasks.stream().filter(t -> t.getStatus() == TaskStatus.PENDING
                && t.getDueDate().isBefore(LocalDate.now())).count();
        int completedMinutes = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .mapToInt(StudyTask::getEstimatedMinutes).sum();
        double percentage = total == 0 ? 0.0 : completed * 100.0 / total;
        return new Progress(total, completed, pending, overdue, completedMinutes, percentage);
    }

    public List<StudyTask> getOverdueTasks() {
        return tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.PENDING
                        && t.getDueDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(StudyTask::getDueDate))
                .toList();
    }

    public List<Subject> snapshotSubjects() { return new ArrayList<>(subjects); }
    public List<StudyTask> snapshotTasks() { return new ArrayList<>(tasks); }

    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new SmartStudyException(label + " cannot be empty.");
        }
        return value.trim();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    public record PlannedTask(StudyTask task, double priorityScore) {}
    public record Progress(long total, long completed, long pending,
                           long overdue, int completedMinutes, double percentage) {}
}
