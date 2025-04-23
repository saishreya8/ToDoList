import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ToDoApp {
    private static final String FILE_NAME = "tasks.txt";
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadTasksFromFile();

        boolean running = true;

        while (running) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addTask();
                case 2 -> viewTasks();
                case 3 -> markTaskAsDone();
                case 4 -> deleteTask();
                case 5 -> {
                    saveTasksToFile();
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n==== TO-DO LIST MENU ====");
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Mark Task as Done");
        System.out.println("4. Delete Task");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    private static void addTask() {
        System.out.print("Enter task description: ");
        String desc = scanner.nextLine();
        tasks.add(new Task(desc));
        System.out.println("Task added.");
    }

    private static void viewTasks() {
        System.out.println("\n---- Your Tasks ----");
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet!");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    private static void markTaskAsDone() {
        viewTasks();
        System.out.print("Enter task number to mark as done: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsDone();
            System.out.println("Marked as done.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    private static void deleteTask() {
        viewTasks();
        System.out.print("Enter task number to delete: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            System.out.println("Task deleted.");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    private static void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.write(task.isDone() + "|" + task.getDescription());
                writer.newLine();
            }
            System.out.println("Tasks saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private static void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    boolean done = Boolean.parseBoolean(parts[0]);
                    String desc = parts[1];
                    Task task = new Task(desc);
                    if (done) task.markAsDone();
                    tasks.add(task);
                }
            }
            System.out.println("Tasks loaded from file.");
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }
}
