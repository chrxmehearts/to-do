package evilcorp.todo.DTO;

import evilcorp.todo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminDto {
    private Long id;
    private String username;
    private Role role;
}
