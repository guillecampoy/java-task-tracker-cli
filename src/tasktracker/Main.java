package tasktracker;

import tasktracker.cli.TaskCli;
import tasktracker.domain.TaskService;
import tasktracker.infra.JsonFileTaskRepository;

public class Main {
    public static void main(String[] args) {
        var repository = new JsonFileTaskRepository("tasks.json");
        var service = new TaskService(repository);
        var cli = new TaskCli(service);

        cli.run(args);
    }
}
