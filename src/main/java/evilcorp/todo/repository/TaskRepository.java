package evilcorp.todo.repository;

import evilcorp.todo.entity.Task;
import evilcorp.todo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTitle(String title);
    Task getTaskById(Long id);
    List<Task> findByUser(User user);
    void deleteByUser(User user);
}
