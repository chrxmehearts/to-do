package evilcorp.todo.controller;

import evilcorp.todo.DTO.UserAdminDto;
import evilcorp.todo.DTO.UserTaskAdminDto;
import evilcorp.todo.DTO.UserTasksAdminDto;
import evilcorp.todo.entity.Task;
import evilcorp.todo.entity.User;
import evilcorp.todo.service.TaskService;
import evilcorp.todo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;

    public AdminController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/users")
    public List<UserAdminDto> showAllUsers() {
        return userService.findAllUsers().stream().map
                (user -> new UserAdminDto(user.getId(), user.getUsername())).toList();
    }

    @GetMapping("/users/{userid}/tasks")
    public ResponseEntity<Object> showAllUserTasks(@PathVariable Long userid) {
        User user = userService.findUserById(userid);
        List<Task> tasks = taskService.findByUser(user);
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User with id " + userid + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(new UserTasksAdminDto(user.getUsername(), tasks));
    }

    @GetMapping("/users/{userid}/tasks/{taskId}")
    public ResponseEntity<Object> showUserTask(@PathVariable Long userid, @PathVariable Long taskId) {
        User user = userService.findUserById(userid);
        Task task = taskService.getTaskById(taskId);
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User with id " + userid + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (task == null || !userid.equals(task.getUser().getId())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Incorrect task id " +taskId + " for user with id " + userid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(new UserTaskAdminDto(user.getUsername(), task));

    }

    @DeleteMapping("/users/{userid}/tasks/{taskId}")
    public ResponseEntity<Map<String, Object>> deleteUserTask(@PathVariable Long userid, @PathVariable Long taskId) {
        User user = userService.findUserById(userid);
        Task task = taskService.getTaskById(taskId);
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User with id " + userid + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if(task == null  || !userid.equals(task.getUser().getId())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Incorrect task id " +taskId + " for user with id " + userid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        taskService.deleteTask(taskId);
        Map<String, Object> response = new HashMap<>();
        response.put("Deleted task", task);
        return ResponseEntity.ok(response);
    }

}
