package qdt.hcmute.vn.dqtbook_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qdt.hcmute.vn.dqtbook_backend.model.Chat;

import java.util.List;
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

    /**
     * Find a group chat with exactly the same set of members
     * Logic đơn giản: 
     * 1. Tìm các chat có đúng memberCount members
     * 2. Kiểm tra tất cả members trong chat có nằm trong memberIds không
     * 3. Kiểm tra tất cả memberIds có nằm trong chat không
     * 
     * @param memberIds List of user IDs in the group (đã sort)
     * @param memberCount Number of members
     * @return Optional containing the chat if found
     */
    @Query("SELECT c FROM Chat c " +
            "WHERE c.isGroup = true " +
            "AND c.id IN (" +
            "  SELECT cm.chat.id FROM ChatMember cm " +
            "  WHERE cm.user.id IN :memberIds " +
            "  GROUP BY cm.chat.id " +
            "  HAVING COUNT(DISTINCT cm.user.id) = :memberCount" +
            ") " +
            "AND c.id NOT IN (" +
            "  SELECT cm2.chat.id FROM ChatMember cm2 " +
            "  WHERE cm2.user.id NOT IN :memberIds" +
            ")")
    Optional<Chat> findGroupChatWithExactMembers(@Param("memberIds") List<Integer> memberIds,
                                                   @Param("memberCount") Integer memberCount);

    /**
     * Find all group chats that a user is a member of
     * 
     * @param userId User ID
     * @return List of group chats the user belongs to
     */
    @Query("SELECT c FROM Chat c " +
            "WHERE c.isGroup = true " +
            "AND c.id IN (" +
            "  SELECT cm.chat.id FROM ChatMember cm " +
            "  WHERE cm.user.id = :userId" +
            ") " +
            "ORDER BY c.createdAt DESC")
    List<Chat> findGroupChatsByUserId(@Param("userId") Integer userId);

    /**
     * Find all direct chats (non-group chats) for a user
     * 
     * @param userId User ID
     * @return List of direct chats the user belongs to
     */
    @Query("SELECT c FROM Chat c " +
            "WHERE c.isGroup = false " +
            "AND c.id IN (" +
            "  SELECT cm.chat.id FROM ChatMember cm " +
            "  WHERE cm.user.id = :userId" +
            ") " +
            "ORDER BY c.createdAt DESC")
    List<Chat> findDirectChatsByUserId(@Param("userId") Integer userId);
}