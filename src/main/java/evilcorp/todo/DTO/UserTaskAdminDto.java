package evilcorp.todo.DTO;

import evilcorp.todo.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTaskAdminDto {
    private String username;
    private Task task;
}
