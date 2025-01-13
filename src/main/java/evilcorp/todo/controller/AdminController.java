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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                (user -> new UserAdminDto(user.getId(), user.getUsername(), user.getRole())).toList();
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
        if ("admin".equals(user.getRole().getName())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cannot watch tasks of user with role 'admin'");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
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
            response.put("message", "Incorrect task id " + taskId + " for user with id " + userid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if ("admin".equals(user.getRole().getName())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cannot watch task of user with role 'admin'");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
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
        if (task == null || !userid.equals(task.getUser().getId())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Incorrect task id " + taskId + " for user with id " + userid);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if ("admin".equals(user.getRole().getName())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cannot delete task of user with role 'admin'");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        taskService.deleteTask(taskId);
        Map<String, Object> response = new HashMap<>();
        response.put("Deleted task", task);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/users/{userid}/ban")
    public ResponseEntity<Map<String, Object>> banUser(@PathVariable Long userid) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentOptionalUser = userService.findUserByUsername(currentUsername);
        User currentUser = currentOptionalUser.get();
        User user = userService.findUserById(userid);
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User with ID " + userid + " does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if ("admin".equals(user.getRole().getName())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cannot ban user with role 'admin'");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        if(user.getId().equals(currentUser.getId())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cannot ban yourself'");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User with ID " + userid + "and username " + user.getUsername() + " has been banned");
        userService.deleteUser(user);
        return ResponseEntity.ok(response);
    }

}
