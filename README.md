# Java Task Tracker CLI

A tiny command-line application to **track and manage tasks** using only the Java Standard Library and a `tasks.json` file stored next to the executable.  
It is inspired by the [roadmap.sh task tracker project](https://roadmap.sh/projects/task-tracker) and is meant to practice filesystem IO, command-line parsing, and lightweight domain modeling.

---

## Requirements

- Java 17 or newer (tested with Java 21)
- No external build tools and no third-party libraries

---

## Quick start

Compile everything straight from the `src` folder and place the class files under `out`:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

Run the CLI by pointing `java` to the `tasktracker.Main` entry point:

```bash
java -cp out tasktracker.Main <command> [arguments...]
```

To ship a single executable archive you can also create a JAR with nothing but the JDK tooling:

```bash
jar --create --file java-task-tracker-cli.jar -C out .
java -jar java-task-tracker-cli.jar list
```

When the application runs it creates/updates `tasks.json` in the current working directory.

---

## Command reference

| Command | Description | Example |
| --- | --- | --- |
| `add "<description>"` | Create a new task with status `todo`. | `java -cp out tasktracker.Main add "Buy groceries"` |
| `update <id> "<new description>"` | Replace the description of a task. | `... update 1 "Buy groceries and coffee"` |
| `delete <id>` | Remove a task permanently. | `... delete 3` |
| `mark-in-progress <id>` | Move a task into `in-progress`. | `... mark-in-progress 1` |
| `mark-done <id>` | Mark the task as `done`. | `... mark-done 2` |
| `list [todo|in-progress|done]` | Show every task or filter by status. | `... list in-progress` |

All inputs are positional arguments (no flags such as `--name`).

---

## Example workflow

```bash
java -cp out tasktracker.Main add "Study Java 21"
java -cp out tasktracker.Main add "Write README"
java -cp out tasktracker.Main mark-in-progress 2
java -cp out tasktracker.Main list
java -cp out tasktracker.Main mark-done 1
java -cp out tasktracker.Main delete 2
```

Typical CLI output:

```
Created task with id 1
Created task with id 2
Task 2 is now IN_PROGRESS
[1] (TODO) Study Java 21
[2] (IN_PROGRESS) Write README
Task 1 is now DONE
Deleted task 2
```

---

## Data file structure

`tasks.json` is created automatically if it is missing. Every task persists the status and timestamps so that the CLI can recover the history later.

```json
[
  {
    "id": 1,
    "description": "Study Java 21",
    "status": "TODO",
    "createdAt": "2025-12-10T10:15:30",
    "updatedAt": "2025-12-10T10:15:30"
  },
  {
    "id": 2,
    "description": "Write README for the project",
    "status": "IN_PROGRESS",
    "createdAt": "2025-12-10T11:00:00",
    "updatedAt": "2025-12-10T11:05:00"
  }
]
```

Valid statuses: `TODO`, `IN_PROGRESS`, `DONE`.

---

## Error handling

All user-facing errors are printed as `[ERROR] <message>` and the CLI keeps running:

- Unknown command or invalid status filter
- Missing or malformed arguments (e.g., forgetting the task id)
- Non-existing task ids
- Problems reading/writing `tasks.json` (permission denied, corrupt JSON, etc.)

Example:

```bash
[ERROR] Task with id 42 not found.
```

---

## Testing

The repository ships with a tiny self-contained test (`tasktracker.TaskServiceTest`) that exercises the domain logic without any external libraries. Compile it together with the rest of the sources (see *Quick start*) and run:

```bash
java -cp out tasktracker.TaskServiceTest
```

It will exit with a non-zero code if an assertion fails and will print `All TaskService tests passed.` on success.

---

## Possible improvements

- Add more commands (e.g., `update-status <id> <status>` or task priorities)
- Sort tasks by creation date, priority, or status when listing them
- Support human-friendly timestamps in the CLI output
- Extend the test suite to cover the CLI layer and file persistence

---

## License

This project is intended for learning and personal use—fork it and adapt it to your needs.
