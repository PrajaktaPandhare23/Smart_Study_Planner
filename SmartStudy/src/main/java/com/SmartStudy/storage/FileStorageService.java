package com.SmartStudy.storage;

import com.SmartStudy.model.StudyTask;
import com.SmartStudy.model.Subject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService {
    private final Path dataFile;

    public FileStorageService(String filePath) {
        this.dataFile = Path.of(filePath);
    }

    public StoredData load() {
        if (!Files.exists(dataFile)) {
            return new StoredData(new ArrayList<>(), new ArrayList<>());
        }

        try (ObjectInputStream input = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(dataFile)))) {
            @SuppressWarnings("unchecked")
            List<Subject> subjects = (List<Subject>) input.readObject();
            @SuppressWarnings("unchecked")
            List<StudyTask> tasks = (List<StudyTask>) input.readObject();
            return new StoredData(subjects, tasks);
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new IllegalStateException(
                    "Could not load data file. If this file is corrupted, back it up and remove it: "
                            + dataFile, e);
        }
    }

    public void save(List<Subject> subjects, List<StudyTask> tasks) {
        try {
            Path parent = dataFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (ObjectOutputStream output = new ObjectOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(dataFile)))) {
                output.writeObject(new ArrayList<>(subjects));
                output.writeObject(new ArrayList<>(tasks));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not save SmartStudy data.", e);
        }
    }

    public static class StoredData {
        private final List<Subject> subjects;
        private final List<StudyTask> tasks;

        public StoredData(List<Subject> subjects, List<StudyTask> tasks) {
            this.subjects = subjects;
            this.tasks = tasks;
        }

        public List<Subject> getSubjects() { return subjects; }
        public List<StudyTask> getTasks() { return tasks; }
    }
}
