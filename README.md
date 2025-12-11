# Java Task Tracker CLI

A small command-line application to **track and manage tasks** using a JSON file as storage.

This project is inspired by the [roadmap.sh](https://roadmap.sh) project idea [task-tracker](https://roadmap.sh/projects/task-tracker) and is meant to practice:

- Working with the filesystem
- Handling command-line arguments
- Designing a simple domain model
- Building a small but robust CLI tool in modern Java

---

## Features

The CLI allows you to:

- Add new tasks ✨
- Update and delete existing tasks
- Mark tasks as:
    - `todo`
    - `in-progress`
    - `done`
- List:
    - All tasks
    - Only done tasks
    - Only not done tasks (`todo`)
    - Only in-progress tasks

Tasks are stored in a **`tasks.json`** file created in the current working directory.  
No external libraries or frameworks are used.

---

## Tech stack

- **Language:** Java (recommended: Java 21, minimum: Java 17)
- **Storage:** Local JSON file (`tasks.json`)
- **Dependencies:** None (no external libraries)

---

## Usage

After building the project into a JAR file, you can run:

```bash
java -jar java-task-tracker-cli.jar <command> [arguments...]
```
---

## Examples

### Add a new task
```bash
java -jar java-task-tracker-cli.jar add "Buy groceries"
```

### List all tasks
```bash
java -jar java-task-tracker-cli.jar list
```

### Update a task description
```bash
java -jar java-task-tracker-cli.jar update 1 "Buy groceries and coffee"
```

### Mark a task as in progress
```bash
java -jar java-task-tracker-cli.jar mark-in-progress 1
```

### Mark a task as done
```bash
java -jar java-task-tracker-cli.jar mark-done 1
```
### Delete a task
```bash
java -jar java-task-tracker-cli.jar delete 1
```
## Supported commands

>All inputs are positional arguments (no flags like --name).

### 1. add

Add a new task with default status todo.
```bash
java -jar java-task-tracker-cli.jar add "<description>"
```
### 2. update

Update the description of an existing task.
```bash
java -jar java-task-tracker-cli.jar update <id> "<new description>"
```
### 3. delete

Delete a task by its ID.
```bash
java -jar java-task-tracker-cli.jar delete <id>
```
### 4. mark-in-progress

Mark a task as in-progress.
```bash
java -jar java-task-tracker-cli.jar mark-in-progress <id>
```
### 5. mark-done

Mark a task as done.
```bash
java -jar java-task-tracker-cli.jar mark-done <id>
```
### 6. list

List tasks. When no filter is provided, all tasks are listed.

- All tasks
```bash
java -jar java-task-tracker-cli.jar list
```
- Only todo tasks
```bash
java -jar java-task-tracker-cli.jar list todo
```
- Only in-progress tasks
```bash
java -jar java-task-tracker-cli.jar list in-progress
```
- Only done tasks
```bash
java -jar java-task-tracker-cli.jar list done
```

## Formato del archivo tasks.json

The tasks.json file is automatically created in the current directory if it does not exist.

Example:
```json
[
{
"id": 1,
"description": "Study Java 21",
"status": "todo",
"createdAt": "2025-12-10T10:15:30",
"updatedAt": "2025-12-10T10:15:30"
},
{
"id": 2,
"description": "Write README for the project",
"status": "in-progress",
"createdAt": "2025-12-10T11:00:00",
"updatedAt": "2025-12-10T11:05:00"
}
]
```

### Valid statuses:
- todo
- in-progress
- done

## Error handling

The CLI should handle errors gracefully, for example:

- Unknown commands
- Missing required arguments
- Non-existing task IDs
- Invalid or corrupted tasks.json file
- Read/write permission issues

### Example of an error message:
```bash
[ERROR] Task with id 42 not found.
```

## Possible improvements

Some ideas for future extensions:
- Change task status via a dedicated command: update-status <id> <status>
- Add priorities (e.g. low, medium, high)
- Sort tasks by creation date, status, or priority
- Add unit tests for the domain layer (task management logic)

## License

>This project is intended for learning and personal use.
Feel free to fork it and adapt it to your own needs.