package evilcorp.todo.controller;

import evilcorp.todo.entity.Task;
import evilcorp.todo.entity.User;
import evilcorp.todo.service.TaskService;
import evilcorp.todo.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findUserByUsername(username);
        return taskService.findByUser(user.orElse(null));
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public Task createTask(Task task) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findUserByUsername(username);
        task.setUser(user.orElse(null));
        return taskService.addTask(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable long id, @RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    public String deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
        return "Task deleted";
    }

}
