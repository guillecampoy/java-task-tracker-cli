package tasktracker.domain;

import java.time.LocalDateTime;

public record Task(
        int id,
        String description,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDateTime updrtedAt
) {
    public Task witthDescription(String description) {
        return new Task(id, description, status, createdAt, LocalDateTime.now());
    }

    public Task withStatus(TaskStatus status) {
        return new Task(id, description, status, createdAt, LocalDateTime.now());
    }
}
