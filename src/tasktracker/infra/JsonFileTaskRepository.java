package tasktracker.infra;

import tasktracker.domain.Task;
import tasktracker.domain.TaskRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonFileTaskRepository implements TaskRepository {

    private final Path filePath;

    public JsonFileTaskRepository(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    @Override
    public List<Task> findAll() {
        return loadAll();
    }

    @Override
    public Optional<Task> findById(int id) {
        return loadAll().stream()
                .filter(t -> t.id() == id)
                .findFirst();
    }

    @Override
    public Task save(Task task) {
        List<Task> all = new ArrayList<>(loadAll());
        all.removeIf(t -> t.id() == task.id());
        all.add(task);
        saveAll(all);
        return task;
    }

    @Override
    public void deleteById(int id) {
        List<Task> all = new ArrayList<>(loadAll());
        all.removeIf(t -> t.id() == id);
        saveAll(all);
    }

    @Override
    public int getNextId() {
        return loadAll().stream()
                .mapToInt(Task::id)
                .max()
                .orElse(0) + 1;
    }

    private List<Task> loadAll() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try {
            String json = Files.readString(filePath);
            // TODO: parse JSON manually and build List<Task>
            // For now, return empty list to keep the skeleton compilable.
            return new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read tasks file", e);
        }
    }

    private void saveAll(List<Task> tasks) {
        try {
            // TODO: serialize tasks to JSON manually and write to filePath
            String json = "[]"; // placeholder
            Files.writeString(filePath, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write tasks file", e);
        }
    }

}
