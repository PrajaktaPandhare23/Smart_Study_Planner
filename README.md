# SmartStudy — Intelligent Study Planner 📚

**SmartStudy** is a console-based study planning application developed using Core Java. It helps students organize their subjects, manage study tasks, prioritize important topics, plan daily study sessions, and track their learning progress.

The project demonstrates Object-Oriented Programming (OOP), the Java Collections Framework, exception handling, file handling, and software design principles.

## 🎯 Objectives

- Organize subjects and their associated study tasks.
- Create a daily study plan based on available study time.
- Prioritize tasks according to urgency, difficulty, importance, and remaining workload.
- Track pending and completed tasks.
- Identify overdue study tasks.
- Save study data locally so it can be restored when the application restarts.

## ✨ Features

### 1. Subject Management
- Add, view, search, update, and delete subjects.
- Maintain unique subject IDs and subject names.
- Prevent deleting subjects that are associated with study tasks.

### 2. Study Task Management
- Create study tasks linked to subjects.
- Set task titles, estimated study duration, difficulty, importance, and due dates.
- Update task details.
- Mark tasks as pending or completed.

### 3. Intelligent Daily Study Planner
- Generate a daily study plan based on the available study time.
- Prioritize pending tasks using urgency, difficulty, importance, and remaining workload.
- Identify tasks that do not fit within the available time.

### 4. Progress Tracking
- View total, completed, and pending tasks.
- Monitor study progress.
- Identify overdue tasks.

### 5. Data Persistence
- Store subjects and tasks locally using Java file handling and object serialization.
- Load previously saved data when the application starts.

## 🛠️ Technologies Used

- **Language:** Java
- **Java Concepts:** OOP, Collections Framework, Exception Handling, Enums, File Handling, Serialization
- **Build Tool:** Apache Maven
- **IDE:** IntelliJ IDEA
- **Version Control:** Git and GitHub

## 🏗️ Project Structure

```text
SmartStudy/
├── pom.xml
├── .gitignore
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── SmartStudy/
                    ├── Main.java
                    ├── controller/
                    │   └── ConsoleController.java
                    ├── exception/
                    │   └── SmartStudyException.java
                    ├── model/
                    │   ├── Subject.java
                    │   ├── StudyTask.java
                    │   ├── Difficulty.java
                    │   └── TaskStatus.java
                    ├── service/
                    │   └── StudyPlannerService.java
                    └── storage/
                        └── FileStorageService.java
```

## ⚙️ Prerequisites

Make sure you have installed:

- Java JDK 17 or later
- Apache Maven
- Git (optional, for cloning the repository)

Verify Java and Maven installations:

```bash
java -version
mvn -version
```

## 🚀 Getting Started

### 1. Clone the repository

Replace `YOUR-USERNAME` with your GitHub username.

```bash
git clone https://github.com/YOUR-USERNAME/SmartStudy-Intelligent-Study-Planner.git
```

### 2. Navigate to the project directory

```bash
cd SmartStudy-Intelligent-Study-Planner
```

### 3. Compile the project

```bash
mvn clean compile
```

### 4. Run the application

```bash
mvn exec:java -Dexec.mainClass="com.SmartStudy.Main"
```

If Maven's exec plugin is not configured in `pom.xml`, run the `Main.java` class directly from IntelliJ IDEA.

## 🖥️ Application Menu

The application provides a console-based menu similar to the following:

```text
========== SMARTSTUDY ==========
1. Manage subjects
2. Manage study tasks
3. Generate daily study plan
4. View progress
5. View overdue tasks
0. Exit

Choose an option:
```

Select an option and follow the instructions to manage your study schedule.

## 🧠 Core Java Concepts Demonstrated

| Concept | Application |
|---|---|
| Encapsulation | Private fields and controlled access through methods |
| Abstraction | Separating business operations from console interaction |
| Collections Framework | Using lists to manage subjects and study tasks |
| Exception Handling | Handling invalid inputs and business rule violations |
| Enums | Representing task difficulty and completion status |
| File Handling | Saving and loading application data |
| Object Serialization | Persisting subjects and tasks between runs |

## 🏛️ Design Principles

The project can demonstrate SOLID principles and suitable design patterns:

- **Single Responsibility Principle (SRP):** Separate models, business logic, console interaction, and data storage.
- **Open/Closed Principle (OCP):** Keep the design extensible for additional scheduling strategies.
- **Dependency Inversion Principle (DIP):** Business logic can depend on abstractions for storage rather than concrete implementations.
- **Strategy Pattern:** A potential extension for supporting multiple task-prioritization algorithms.
- **Repository Pattern:** A potential extension for abstracting data access.

These principles and patterns should be implemented and verified in the code before claiming them as fully implemented features.

## 🔮 Future Enhancements

- Weekly and monthly study planning.
- Study reminders and break scheduling.
- Multiple scheduling strategies.
- Study streaks and productivity reports.
- Unit testing with JUnit.
- Database integration as an optional future version.
- A graphical user interface or web application.

## 👨‍💻 Author

Developed as a Core Java learning project to practice object-oriented programming, collections, exception handling, and structured software design.

## 📄 License

This project is intended for educational and learning purposes. Add a license file if you plan to distribute it under a specific open-source license.
