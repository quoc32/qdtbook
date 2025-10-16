package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qdt.hcmute.vn.dqtbook_backend.dto.*;
import qdt.hcmute.vn.dqtbook_backend.model.*;
import qdt.hcmute.vn.dqtbook_backend.repository.*;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMemberRepository chatMemberRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Check if a direct chat exists between two users
     * 
     * @param request The check chat request containing userId1 and userId2
     * @return CheckChatResponseDTO with existence status and chat ID if found
     */
    public CheckChatResponseDTO checkDirectChatExists(CheckChatRequestDTO request) {
        try {
            // Validate that both users exist
            Optional<User> user1 = userRepository.findById(request.getUserId1());
            Optional<User> user2 = userRepository.findById(request.getUserId2());

            if (user1.isEmpty()) {
                return new CheckChatResponseDTO(false, null, "User with ID " + request.getUserId1() + " not found");
            }

            if (user2.isEmpty()) {
                return new CheckChatResponseDTO(false, null, "User with ID " + request.getUserId2() + " not found");
            }

            // Check if direct chat exists
            Optional<Integer> chatId = chatRepository.findChatIdBetweenUsers(
                    request.getUserId1(),
                    request.getUserId2());

            if (chatId.isPresent()) {
                return new CheckChatResponseDTO(true, chatId.get(), "Direct chat found");
            } else {
                return new CheckChatResponseDTO(false, null, "No direct chat exists between these users");
            }

        } catch (Exception e) {
            return new CheckChatResponseDTO(false, null, "Error checking chat: " + e.getMessage());
        }
    }

    /**
     * Find or create a direct chat between two users
     * This ensures uniqueness - only one chat exists between any two users
     * 
     * @param request The direct chat request containing userId1 and userId2
     * @return Optional containing DirectChatResponseDTO if successful
     */
    public Optional<DirectChatResponseDTO> findOrCreateDirectChat(DirectChatRequestDTO request) {
        try {
            // Validate that both users exist
            Optional<User> user1Opt = userRepository.findById(request.getUserId1());
            Optional<User> user2Opt = userRepository.findById(request.getUserId2());

            if (user1Opt.isEmpty() || user2Opt.isEmpty()) {
                return Optional.empty();
            }

            User user1 = user1Opt.get();
            User user2 = user2Opt.get();

            // Check if chat already exists
            Optional<Chat> existingChat = chatRepository.findDirectChatBetweenUsers(
                    request.getUserId1(),
                    request.getUserId2());

            Chat chat;
            if (existingChat.isPresent()) {
                // Chat already exists
                chat = existingChat.get();
            } else {
                // Create new chat
                chat = createNewDirectChat(user1, user2);
            }

            // Create response DTO
            // For a direct chat, the "other user" depends on who is requesting
            // We'll use user2 as the "other user" by default
            DirectChatResponseDTO responseDTO = new DirectChatResponseDTO();
            responseDTO.setChatId(chat.getId());
            responseDTO.setOtherUserId(user2.getId());
            responseDTO.setOtherUserName(user2.getFullName());
            responseDTO.setOtherUserAvatar(user2.getAvatarUrl());
            responseDTO.setCreatedAt(chat.getCreatedAt());
            // Note: isOnline and lastMessage would need additional logic/services

            return Optional.of(responseDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Get chat ID between two users
     * 
     * @param userId1 First user ID
     * @param userId2 Second user ID
     * @return Optional containing the chat ID if found
     */
    public Optional<Integer> getChatIdBetweenUsers(Integer userId1, Integer userId2) {
        try {
            // Validate that both users exist
            if (!userRepository.existsById(userId1) || !userRepository.existsById(userId2)) {
                return Optional.empty();
            }

            return chatRepository.findChatIdBetweenUsers(userId1, userId2);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Create a new direct chat between two users
     * 
     * @param user1 First user
     * @param user2 Second user
     * @return The created chat
     */
    private Chat createNewDirectChat(User user1, User user2) {
        // Create new chat
        Chat chat = new Chat();
        chat.setIsGroup(false);
        chat.setChatName(null); // Direct chats don't need names
        chat.setChatAvatarUrl(null); // Direct chats use user avatars
        chat.setCreatedAt(Instant.now());

        // Save chat
        chat = chatRepository.save(chat);

        // Create chat members
        createChatMember(chat, user1, "member");
        createChatMember(chat, user2, "member");

        return chat;
    }

    /**
     * Create a chat member
     * 
     * @param chat The chat
     * @param user The user
     * @param role The role in chat
     */
    private void createChatMember(Chat chat, User user, String role) {
        ChatMember chatMember = new ChatMember();

        // Create composite key
        ChatMemberId chatMemberId = new ChatMemberId();
        chatMemberId.setChatId(chat.getId());
        chatMemberId.setUserId(user.getId());

        chatMember.setId(chatMemberId);
        chatMember.setChat(chat);
        chatMember.setUser(user);
        chatMember.setRoleInChat(role);

        chatMemberRepository.save(chatMember);
    }
}