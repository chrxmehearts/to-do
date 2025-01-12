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

    public Task getTaskById(long id) {
        return taskRepository.getTaskById(id);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = taskRepository.getTaskById(id);
        if (existingTask == null) {
            throw new RuntimeException("Task with id " + id + " not found");
        }
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());
        return taskRepository.save(existingTask);
    }

    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findByUser(User user) {
        return taskRepository.findByUser(user);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
