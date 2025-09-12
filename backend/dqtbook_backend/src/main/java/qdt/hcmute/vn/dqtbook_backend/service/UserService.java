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

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUserFullName(Integer id, String newFullName) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setFullName(newFullName);
            return userRepository.save(user);
        }
        return null;
    }

    public User updateUserAllFields(Integer id, User updated) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        // Overwrite editable fields
        existing.setFullName(updated.getFullName());
        existing.setEmail(updated.getEmail());
        existing.setPasswordHash(updated.getPasswordHash());
        existing.setRole(updated.getRole());
        existing.setAvatarUrl(updated.getAvatarUrl());
        existing.setCoverPhotoUrl(updated.getCoverPhotoUrl());
        existing.setBio(updated.getBio());
        existing.setSchoolId(updated.getSchoolId());
        existing.setDepartment(updated.getDepartment());
        existing.setAcademicYear(updated.getAcademicYear());
        existing.setGender(updated.getGender());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setPhone(updated.getPhone());
        existing.setWebsite(updated.getWebsite());
        existing.setCountry(updated.getCountry());
        existing.setCity(updated.getCity());
        existing.setEducation(updated.getEducation());
        existing.setWorkplace(updated.getWorkplace());
        existing.setFacebookUrl(updated.getFacebookUrl());
        existing.setInstagramUrl(updated.getInstagramUrl());
        existing.setLinkedinUrl(updated.getLinkedinUrl());
        existing.setTwitterUrl(updated.getTwitterUrl());
        existing.setLastSeenAt(updated.getLastSeenAt());
        // Do not modify: id, createdAt
        return userRepository.save(existing);
    }
}
