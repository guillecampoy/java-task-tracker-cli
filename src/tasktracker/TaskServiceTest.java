package tasktracker;

import tasktracker.domain.Task;
import tasktracker.domain.TaskRepository;
import tasktracker.domain.TaskService;
import tasktracker.domain.TaskStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Minimal, dependency-free test harness for {@link TaskService}.
 * Run with: {@code java -cp out tasktracker.TaskServiceTest}
 */
public class TaskServiceTest {

    public static void main(String[] args) {
        TaskServiceTest testSuite = new TaskServiceTest();
        testSuite.testAddTaskAssignsIncrementalIdsAndTodoStatus();
        testSuite.testMarkDoneUpdatesStatus();
        testSuite.testListByStatusFiltersCorrectly();
        System.out.println("All TaskService tests passed.");
    }

    private void testAddTaskAssignsIncrementalIdsAndTodoStatus() {
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        TaskService service = new TaskService(repo);

        Task first = service.addTask("first");
        Task second = service.addTask("second");

        check(first.id() == 1, "First task should have id=1");
        check(second.id() == 2, "Second task should have id=2");
        check(first.status() == TaskStatus.TODO, "New tasks default to TODO");
        check(second.status() == TaskStatus.TODO, "New tasks default to TODO");
    }

    private void testMarkDoneUpdatesStatus() {
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        TaskService service = new TaskService(repo);

        Task created = service.addTask("mark me");
        Task done = service.markDone(created.id());

        check(done.status() == TaskStatus.DONE, "markDone should set status to DONE");
        check(repo.findById(created.id()).orElseThrow().status() == TaskStatus.DONE,
                "Repository should persist the DONE status");
    }

    private void testListByStatusFiltersCorrectly() {
        InMemoryTaskRepository repo = new InMemoryTaskRepository();
        TaskService service = new TaskService(repo);

        Task todo = service.addTask("todo");
        Task progress = service.addTask("progress");
        Task done = service.addTask("done");

        service.markInProgress(progress.id());
        service.markDone(done.id());

        List<Task> todos = service.listByStatus(TaskStatus.TODO);
        List<Task> inProgress = service.listByStatus(TaskStatus.IN_PROGRESS);
        List<Task> doneTasks = service.listByStatus(TaskStatus.DONE);

        check(todos.size() == 1 && todos.get(0).id() == todo.id(), "list TODO should return only todo tasks");
        check(inProgress.size() == 1 && inProgress.get(0).id() == progress.id(), "list IN_PROGRESS should return filtered task");
        check(doneTasks.size() == 1 && doneTasks.get(0).id() == done.id(), "list DONE should return filtered task");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException("Test failed: " + message);
        }
    }

    private static final class InMemoryTaskRepository implements TaskRepository {
        private final List<Task> tasks = new ArrayList<>();
        private int nextId = 1;

        @Override
        public List<Task> findAll() {
            return new ArrayList<>(tasks);
        }

        @Override
        public Optional<Task> findById(int id) {
            return tasks.stream().filter(t -> t.id() == id).findFirst();
        }

        @Override
        public Task save(Task task) {
            tasks.removeIf(t -> t.id() == task.id());
            tasks.add(task);
            tasks.sort(Comparator.comparingInt(Task::id));
            if (task.id() >= nextId) {
                nextId = task.id() + 1;
            }
            return task;
        }

        @Override
        public void deleteById(int id) {
            tasks.removeIf(t -> t.id() == id);
        }

        @Override
        public int getNextId() {
            return nextId;
        }
    }
}
