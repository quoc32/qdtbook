package qdt.hcmute.vn.dqtbook_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import qdt.hcmute.vn.dqtbook_backend.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {
  User findByEmail(String email);

  List<User> findTop5ByEmailContainingIgnoreCase(String keyword);

  @Query("SELECT u.id FROM User u")
  List<Integer> findAllUserIds();

  // OAuth2 methods
  User findByOauthProviderAndOauthId(String oauthProvider, String oauthId);

  @Query("SELECT u FROM User u WHERE u.id <> :userId AND u.id NOT IN :friendIds")
  List<User> findSuggestions(Integer userId, List<Integer> friendIds, Pageable pageable);


  @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<User> searchByFullNameOrEmail(String keyword, Pageable pageable);


}
