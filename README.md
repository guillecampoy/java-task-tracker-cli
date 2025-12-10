# Java Task Tracker CLI

A small command-line application to **track and manage tasks** using a JSON file as storage.

This project is inspired by the [roadmap.sh](https://roadmap.sh) project idea and is meant to practice:

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