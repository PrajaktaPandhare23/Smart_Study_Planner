package com.SmartStudy;

import com.SmartStudy.controller.ConsoleController;
import com.SmartStudy.service.StudyPlannerService;
import com.SmartStudy.storage.FileStorageService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileStorageService storage = new FileStorageService("data/smartstudy.dat");
        FileStorageService.StoredData savedData = storage.load();

        StudyPlannerService service = new StudyPlannerService(
                savedData.getSubjects(), savedData.getTasks());

        try (Scanner scanner = new Scanner(System.in)) {
            ConsoleController controller =
                    new ConsoleController(scanner, service, storage);
            controller.run();
        } catch (IllegalStateException e) {
            System.out.println("Could not start SmartStudy: " + e.getMessage());
        }
    }
}
