package evilcorp.todo.repository;

import evilcorp.todo.entity.Task;
import evilcorp.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTitle(String title);
    List<Task> findByUser(User user);
}
