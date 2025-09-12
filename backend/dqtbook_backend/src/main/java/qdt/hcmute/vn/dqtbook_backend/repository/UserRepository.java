package qdt.hcmute.vn.dqtbook_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qdt.hcmute.vn.dqtbook_backend.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
  User findByEmail(String email);
}
