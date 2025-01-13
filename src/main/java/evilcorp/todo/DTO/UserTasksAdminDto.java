package evilcorp.todo.DTO;

import evilcorp.todo.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTasksAdminDto {
    private String username;
    private List<Task> tasks;
}
