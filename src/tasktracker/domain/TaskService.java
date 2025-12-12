package tasktracker.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task addTask(String description) {
        int id = repository.getNextId();
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task(id, description, TaskStatus.TODO, now, now);
        return repository.save(task);
    }

    public Task updateDescription(int id, String newDescription){
        Task existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " does not exist"));
        Task updated = existing.witthDescription(newDescription);
        return repository.save(updated);
    }

    public Task markInProgress(int id){
        return updateStatus(id,TaskStatus.IN_PROGRESS );
    }

    public Task markDone(int id){
        return updateStatus(id,TaskStatus.DONE );
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public List<Task> listByStatus(TaskStatus status) {
        return repository.findAll().stream()
                .filter(t -> t.status() == status)
                .collect(Collectors.toList());
    }

    private Task updateStatus(int id, TaskStatus newStatus) {
        Task existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " not found"));
        Task updated = existing.withStatus(newStatus);
        return repository.save(updated);
    }

}
