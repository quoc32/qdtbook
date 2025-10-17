package qdt.hcmute.vn.dqtbook_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qdt.hcmute.vn.dqtbook_backend.model.Chat;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    /**
     * Find a direct chat between two users.
     * A direct chat is defined as a non-group chat that has exactly these two users
     * as members.
     * 
     * @param userId1 First user ID
     * @param userId2 Second user ID
     * @return Optional containing the chat if found
     */
    @Query("SELECT c FROM Chat c " +
            "WHERE c.isGroup = false " +
            "AND c.id IN (" +
            "  SELECT cm1.chat.id FROM ChatMember cm1 " +
            "  WHERE cm1.user.id = :userId1 " +
            "  AND cm1.chat.id IN (" +
            "    SELECT cm2.chat.id FROM ChatMember cm2 " +
            "    WHERE cm2.user.id = :userId2" +
            "  )" +
            ")")
    Optional<Chat> findDirectChatBetweenUsers(@Param("userId1") Integer userId1,
            @Param("userId2") Integer userId2);

    /**
     * Check if a direct chat exists between two users
     * 
     * @param userId1 First user ID
     * @param userId2 Second user ID
     * @return true if chat exists, false otherwise
     */
    @Query("SELECT COUNT(c) > 0 FROM Chat c " +
            "WHERE c.isGroup = false " +
            "AND c.id IN (" +
            "  SELECT cm1.chat.id FROM ChatMember cm1 " +
            "  WHERE cm1.user.id = :userId1 " +
            "  AND cm1.chat.id IN (" +
            "    SELECT cm2.chat.id FROM ChatMember cm2 " +
            "    WHERE cm2.user.id = :userId2" +
            "  )" +
            ")")
    boolean existsDirectChatBetweenUsers(@Param("userId1") Integer userId1,
            @Param("userId2") Integer userId2);

    /**
     * Get chat ID for a direct chat between two users
     * 
     * @param userId1 First user ID
     * @param userId2 Second user ID
     * @return Optional containing the chat ID if found
     */
    @Query("SELECT c.id FROM Chat c " +
            "WHERE c.isGroup = false " +
            "AND c.id IN (" +
            "  SELECT cm1.chat.id FROM ChatMember cm1 " +
            "  WHERE cm1.user.id = :userId1 " +
            "  AND cm1.chat.id IN (" +
            "    SELECT cm2.chat.id FROM ChatMember cm2 " +
            "    WHERE cm2.user.id = :userId2" +
            "  )" +
            ")")
    Optional<Integer> findChatIdBetweenUsers(@Param("userId1") Integer userId1,
            @Param("userId2") Integer userId2);
}