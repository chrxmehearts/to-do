package evilcorp.todo.service;

import evilcorp.todo.entity.User;
import evilcorp.todo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }
}
