package evilcorp.todo.controller;

import evilcorp.todo.entity.Task;
import evilcorp.todo.entity.User;
import evilcorp.todo.service.TaskService;
import evilcorp.todo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Object> getTaskById(@PathVariable int id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findUserByUsername(username);
        User user = userOptional.get();
        Task task = taskService.getTaskById(id);
        if (user.getId().equals(task.getUser().getId())) {
            return ResponseEntity.ok(task);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("Error", "Task not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.findUserByUsername(username);
        task.setUser(user.orElse(null));
        return taskService.addTask(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTask(@PathVariable long id, @RequestBody Task updatedTask) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findUserByUsername(username);
        User user = userOptional.get();
        Task task = taskService.getTaskById(id);

        if (task == null || (!user.getId().equals(task.getUser().getId()))) {
            Map<String, Object> response = new HashMap<>();
            response.put("Error", "Task not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
            Task updated = taskService.updateTask(id, task);
            Map<String, Object> response = new HashMap<>();
            response.put("Updated task", task);
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTask(@PathVariable long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findUserByUsername(username);
        User user = userOptional.get();
        Task task = taskService.getTaskById(id);
        if (task == null || (!user.getId().equals(task.getUser().getId()))) {
            Map<String, Object> response = new HashMap<>();
            response.put("Error", "Task not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            taskService.deleteTask(id);
            Map<String, Object> response = new HashMap<>();
            response.put("Deleted task", task);
            return ResponseEntity.ok(response);
        }
    }

}
