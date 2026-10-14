package com.SmartStudy.controller;

import com.SmartStudy.exception.SmartStudyException;
import com.SmartStudy.model.Difficulty;
import com.SmartStudy.model.StudyTask;
import com.SmartStudy.model.Subject;
import com.SmartStudy.model.TaskStatus;
import com.SmartStudy.service.StudyPlannerService;
import com.SmartStudy.storage.FileStorageService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleController {
    private final Scanner scanner;
    private final StudyPlannerService service;
    private final FileStorageService storage;

    public ConsoleController(Scanner scanner, StudyPlannerService service,
                             FileStorageService storage) {
        this.scanner = scanner;
        this.service = service;
        this.storage = storage;
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n========== SMARTSTUDY ==========");
            System.out.println("1. Manage subjects");
            System.out.println("2. Manage study tasks");
            System.out.println("3. Generate daily study plan");
            System.out.println("4. View progress");
            System.out.println("5. View overdue tasks");
            System.out.println("0. Exit");
            int choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1 -> subjectMenu();
                    case 2 -> taskMenu();
                    case 3 -> generatePlan();
                    case 4 -> showProgress();
                    case 5 -> showOverdue();
                    case 0 -> running = false;
                    default -> System.out.println("Please choose a listed option.");
                }
            } catch (SmartStudyException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                saveData();
            }
        }
        saveData();
        System.out.println("Your data has been saved. Goodbye!");
    }

    private void subjectMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- SUBJECTS ---");
            System.out.println("1. Add subject");
            System.out.println("2. List subjects");
            System.out.println("3. Update subject name");
            System.out.println("4. Delete subject");
            System.out.println("0. Back");
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> {
                        int id = readInt("Subject ID (positive integer): ");
                        String name = readText("Subject name: ");
                        service.addSubject(id, name);
                        System.out.println("Subject added.");
                    }
                    case 2 -> listSubjects();
                    case 3 -> {
                        int id = readInt("Subject ID to update: ");
                        String name = readText("New subject name: ");
                        service.updateSubject(id, name);
                        System.out.println("Subject updated.");
                    }
                    case 4 -> {
                        int id = readInt("Subject ID to delete: ");
                        service.deleteSubject(id);
                        System.out.println("Subject deleted.");
                    }
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SmartStudyException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                saveData();
            }
        }
    }

    private void taskMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- STUDY TASKS ---");
            System.out.println("1. Add task");
            System.out.println("2. List tasks");
            System.out.println("3. Mark task completed");
            System.out.println("4. Mark task pending");
            System.out.println("5. Update task");
            System.out.println("0. Back");
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> addTask();
                    case 2 -> listTasks();
                    case 3 -> changeTaskStatus(TaskStatus.COMPLETED);
                    case 4 -> changeTaskStatus(TaskStatus.PENDING);
                    case 5 -> updateTask();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SmartStudyException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                saveData();
            }
        }
    }

    private void addTask() {
        if (service.getSubjects().isEmpty()) {
            System.out.println("Add a subject before creating a task.");
            return;
        }
        listSubjects();
        int taskId = readInt("Task ID (positive integer): ");
        int subjectId = readInt("Subject ID for this task: ");
        String title = readText("Topic/task title: ");
        int minutes = readInt("Estimated study time in minutes: ");
        Difficulty difficulty = readDifficulty();
        int importance = readInt("Importance (1 low - 5 high): ");
        LocalDate dueDate = readDate("Due date (YYYY-MM-DD): ");
        service.addTask(taskId, subjectId, title, minutes, difficulty,
                importance, dueDate);
        System.out.println("Study task added.");
    }

    private void updateTask() {
        int id = readInt("Task ID to update: ");
        StudyTask current = service.findTask(id);
        System.out.println("Current: " + current);
        String title = readText("New title: ");
        int minutes = readInt("New estimated minutes: ");
        Difficulty difficulty = readDifficulty();
        int importance = readInt("New importance (1-5): ");
        LocalDate dueDate = readDate("New due date (YYYY-MM-DD): ");
        service.updateTask(id, title, minutes, difficulty, importance, dueDate);
        System.out.println("Task updated.");
    }

    private void changeTaskStatus(TaskStatus status) {
        int id = readInt("Task ID: ");
        service.setTaskStatus(id, status);
        System.out.println("Task status set to " + status + ".");
    }

    private void generatePlan() {
        int minutes = readInt("How many minutes can you study today? ");
        List<StudyPlannerService.PlannedTask> plan = service.createDailyPlan(minutes);
        List<StudyTask> notFitting = service.tasksNotFittingPlan(minutes);

        System.out.println("\n--- TODAY'S SUGGESTED PLAN ---");
        if (plan.isEmpty()) {
            System.out.println("No pending tasks fit the available time.");
        } else {
            int total = 0;
            for (int i = 0; i < plan.size(); i++) {
                var item = plan.get(i);
                StudyTask task = item.task();
                total += task.getEstimatedMinutes();
                System.out.printf("%d. %s | %d min | priority %.1f | due %s%n",
                        i + 1, task.getTitle(), task.getEstimatedMinutes(),
                        item.priorityScore(), task.getDueDate());
            }
            System.out.println("Planned total: " + total + " minutes.");
        }

        if (!notFitting.isEmpty()) {
            System.out.println("\nTasks not fitting in today's remaining time:");
            for (StudyTask task : notFitting) {
                System.out.println("- " + task.getTitle() + " (" +
                        task.getEstimatedMinutes() + " min)");
            }
        }
    }

    private void showProgress() {
        var p = service.getProgress();
        System.out.println("\n--- PROGRESS ---");
        System.out.println("Total tasks: " + p.total());
        System.out.println("Completed: " + p.completed());
        System.out.println("Pending: " + p.pending());
        System.out.println("Overdue pending tasks: " + p.overdue());
        System.out.println("Estimated completed study time: " + p.completedMinutes() + " minutes");
        System.out.printf("Completion: %.1f%%%n", p.percentage());
    }

    private void showOverdue() {
        List<StudyTask> overdue = service.getOverdueTasks();
        if (overdue.isEmpty()) {
            System.out.println("No overdue pending tasks. Nice work!");
            return;
        }
        System.out.println("\n--- OVERDUE TASKS ---");
        overdue.forEach(System.out::println);
    }

    private void listSubjects() {
        List<Subject> subjects = service.getSubjects();
        if (subjects.isEmpty()) {
            System.out.println("No subjects found.");
        } else {
            subjects.forEach(System.out::println);
        }
    }

    private void listTasks() {
        List<StudyTask> tasks = service.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("No study tasks found.");
        } else {
            tasks.forEach(System.out::println);
        }
    }

    private Difficulty readDifficulty() {
        while (true) {
            System.out.println("Difficulty: 1. EASY  2. MEDIUM  3. HARD");
            int value = readInt("Choose difficulty: ");
            switch (value) {
                case 1: return Difficulty.EASY;
                case 2: return Difficulty.MEDIUM;
                case 3: return Difficulty.HARD;
                default: System.out.println("Enter 1, 2, or 3.");
            }
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            String value = readText(prompt);
            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                System.out.println("Use date format YYYY-MM-DD, for example 2026-10-20.");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void saveData() {
        try {
            storage.save(service.snapshotSubjects(), service.snapshotTasks());
        } catch (IllegalStateException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }
}
