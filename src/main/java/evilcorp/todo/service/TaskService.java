package evilcorp.todo.service;

import evilcorp.todo.entity.Task;
import evilcorp.todo.entity.User;
import evilcorp.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task addTask(Task task) {
        return taskRepository.save(task);
    }
    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
