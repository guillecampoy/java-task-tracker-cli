package tasktracker.domain;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    Optional<Task> findById(int id);

    // same for insert and update
    Task save(Task task);

    void deleteById(int id);

    int getNextId();
}
