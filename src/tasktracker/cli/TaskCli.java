package tasktracker.cli;

import tasktracker.domain.Task;
import tasktracker.domain.TaskService;
import tasktracker.domain.TaskStatus;

import java.util.List;

public class TaskCli {
    private final TaskService service;

    public TaskCli(TaskService service) {
        this.service = service;
    }

    public void run(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];

        try {
            switch (command) {
                case "add" -> handleAdd(args);
                case "update" -> handleUpdate(args);
                case "delete" -> handleDelete(args);
                case "mark-in-progress" -> handleMarkInProgress(args);
                case "mark-done" -> handleMarkDone(args);
                case "list" -> handleList(args);
                default -> {
                    System.out.println("[ERROR] Unknown command: " + command);
                    printUsage();
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private void handleAdd(String[] args) {
        if (args.length < 2) {
            System.out.println("[ERROR] Missing description for add command.");
            return;
        }
        String description = args[1];
        Task task = service.addTask(description);
        System.out.println("Created task with id " + task.id());
    }

    private void handleUpdate(String[] args) {
        if (args.length < 3) {
            System.out.println("[ERROR] Usage: update <id> \"new description\"");
            return;
        }
        int id = Integer.parseInt(args[1]);
        String newDescription = args[2];
        Task updated = service.updateDescription(id, newDescription);
        System.out.println("Updated task " + updated.id());
    }

    private void handleDelete(String[] args) {
        if (args.length < 2) {
            System.out.println("[ERROR] Usage: delete <id>");
            return;
        }
        int id = Integer.parseInt(args[1]);
        service.delete(id);
        System.out.println("Deleted task " + id);
    }

    private void handleMarkInProgress(String[] args) {
        if (args.length < 2) {
            System.out.println("[ERROR] Usage: mark-in-progress <id>");
            return;
        }
        int id = Integer.parseInt(args[1]);
        Task t = service.markInProgress(id);
        System.out.println("Task " + t.id() + " is now IN_PROGRESS");
    }

    private void handleMarkDone(String[] args) {
        if (args.length < 2) {
            System.out.println("[ERROR] Usage: mark-done <id>");
            return;
        }
        int id = Integer.parseInt(args[1]);
        Task t = service.markDone(id);
        System.out.println("Task " + t.id() + " is now DONE");
    }

    private void handleList(String[] args) {
        List<Task> tasks;
        if (args.length == 1) {
            tasks = service.getAllTasks();
        } else {
            String statusArg = args[1];
            TaskStatus status = switch (statusArg) {
                case "todo" -> TaskStatus.TODO;
                case "in-progress" -> TaskStatus.IN_PROGRESS;
                case "done" -> TaskStatus.DONE;
                default -> throw new IllegalArgumentException("Invalid status: " + statusArg);
            };
            tasks = service.listByStatus(status);
        }

        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
            return;
        }

        for (Task t : tasks) {
            System.out.printf("[%d] (%s) %s%n", t.id(), t.status(), t.description());
        }
    }

    private void printUsage() {
        System.out.println("Usage:");
        System.out.println("  add \"description\"");
        System.out.println("  update <id> \"new description\"");
        System.out.println("  delete <id>");
        System.out.println("  mark-in-progress <id>");
        System.out.println("  mark-done <id>");
        System.out.println("  list [todo|in-progress|done]");
    }
}
