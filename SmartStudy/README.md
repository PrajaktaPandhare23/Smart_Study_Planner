# SmartStudy — Intelligent Study Planner

A Core Java console application built with OOP, Collections, custom exceptions, Java Time API, file persistence, and a priority-based study schedule.

## Requirements
- JDK 17 or newer
- IntelliJ IDEA
- Maven (bundled Maven in IntelliJ is fine)

## IntelliJ setup
1. Extract this project ZIP.
2. In IntelliJ, choose **File → Open** and select the extracted `SmartStudy` folder (the folder containing `pom.xml`).
3. If prompted, choose **Load Maven Project**.
4. Set **File → Project Structure → Project SDK** to JDK 17 or newer.
5. Open `src/main/java/com/SmartStudy/Main.java` and click the green Run icon beside `main`.
6. Application data is automatically saved to the `data` folder after changes. The `data` folder is created when needed.

## Features
- Add, list, update, and delete subjects
- Unique subject IDs and case-insensitive unique subject names
- Add, list, and update study tasks
- Task difficulty, importance, estimated minutes, and due dates
- Mark tasks as completed or pending
- Generate a daily plan within available study minutes
- Priority score considers urgency, difficulty, importance, and remaining work
- Progress summary and overdue-task list
- Save and load data using Java object serialization

## Important notes
- This is an educational local console application, not a multi-user or security-hardened system.
- The schedule is a heuristic recommendation, not an AI prediction. Tasks that do not fit are shown separately.
- Do not open serialized data files from untrusted sources.
- Data is local to the project folder and is ignored by Git through `.gitignore`.

## Suggested Git commit
`Implement complete SmartStudy planner`
