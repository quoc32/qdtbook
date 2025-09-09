package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;
import qdt.hcmute.vn.dqtbook_backend.model.User;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUserFullName(Long id, String newFullName) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setFullName(newFullName);
            return userRepository.save(user);
        }
        return null;
    }
}
