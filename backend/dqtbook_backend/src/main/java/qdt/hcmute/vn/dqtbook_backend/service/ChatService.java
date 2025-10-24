package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qdt.hcmute.vn.dqtbook_backend.dto.*;
import qdt.hcmute.vn.dqtbook_backend.model.*;
import qdt.hcmute.vn.dqtbook_backend.repository.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
                // Create new chat with optional custom name
                chat = createNewDirectChat(user1, user2, request.getChatName());
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
     * @param chatName Optional custom chat name (can be null)
     * @return The created chat
     */
    private Chat createNewDirectChat(User user1, User user2, String chatName) {
        // Create new chat
        Chat chat = new Chat();
        chat.setIsGroup(false);
        // Use custom chat name if provided, otherwise null for direct chats
        chat.setChatName(chatName);
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

    /**
     * Find or create a group chat with specific members
     * This ensures uniqueness - only one group chat exists for a specific set of users
     * 
     * @param request The group chat request containing member IDs and optional chat name
     * @return Optional containing GroupChatResponseDTO if successful
     */
    public Optional<GroupChatResponseDTO> findOrCreateGroupChat(GroupChatRequestDTO request) {
        try {
            // Validate request
            if (request.getMemberIds() == null || request.getMemberIds().isEmpty()) {
                return Optional.empty();
            }

            // Validate minimum members (at least 3 for a group)
            if (request.getMemberIds().size() < 3) {
                return Optional.empty();
            }

            // Remove duplicates and sort member IDs for consistency
            List<Integer> uniqueMemberIds = request.getMemberIds().stream()
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            // Validate that all users exist
            List<User> members = new ArrayList<>();
            for (Integer userId : uniqueMemberIds) {
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isEmpty()) {
                    return Optional.empty(); // User not found
                }
                members.add(userOpt.get());
            }

            // Check if group chat with exact same members already exists
            Optional<Chat> existingChat = chatRepository.findGroupChatWithExactMembers(
                    uniqueMemberIds,
                    uniqueMemberIds.size());

            Chat chat;
            boolean isNewlyCreated;

            if (existingChat.isPresent()) {
                // Group chat already exists
                chat = existingChat.get();
                isNewlyCreated = false;
            } else {
                // Create new group chat
                chat = createNewGroupChat(members, request.getChatName(), request.getChatAvatarUrl());
                isNewlyCreated = true;
            }

            // Create response DTO
            GroupChatResponseDTO responseDTO = new GroupChatResponseDTO();
            responseDTO.setChatId(chat.getId());
            responseDTO.setChatName(chat.getChatName());
            responseDTO.setChatAvatarUrl(chat.getChatAvatarUrl());
            responseDTO.setIsGroup(chat.getIsGroup());
            responseDTO.setMemberCount(uniqueMemberIds.size());
            responseDTO.setMemberIds(uniqueMemberIds);
            responseDTO.setCreatedAt(chat.getCreatedAt());
            responseDTO.setIsNewlyCreated(isNewlyCreated);

            return Optional.of(responseDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Create a new group chat
     * 
     * @param members List of users in the group
     * @param chatName Name of the group (optional)
     * @param chatAvatarUrl Avatar URL of the group (optional)
     * @return The created chat
     */
    private Chat createNewGroupChat(List<User> members, String chatName, String chatAvatarUrl) {
        // Create new group chat
        Chat chat = new Chat();
        chat.setIsGroup(true);
        chat.setChatName(chatName);
        chat.setChatAvatarUrl(chatAvatarUrl);
        chat.setCreatedAt(Instant.now());

        // Save chat
        chat = chatRepository.save(chat);

        // Create chat members
        // First member is admin, others are regular members
        for (int i = 0; i < members.size(); i++) {
            String role = (i == 0) ? "admin" : "member";
            createChatMember(chat, members.get(i), role);
        }

        return chat;
    }

    /**
     * Get all group chats that a user is a member of
     * 
     * @param userId User ID
     * @return List of GroupChatListResponseDTO
     */
    public List<GroupChatListResponseDTO> getGroupChatsByUserId(Integer userId) {
        try {
            // Validate user exists
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return new ArrayList<>();
            }

            List<Chat> groupChats = chatRepository.findGroupChatsByUserId(userId);
            List<GroupChatListResponseDTO> responseDTOs = new ArrayList<>();

            for (Chat chat : groupChats) {
                // Get all members for this chat
                List<ChatMember> members = chatMemberRepository.findByChatId(chat.getId());
                
                // Extract member IDs
                List<Integer> memberIds = members.stream()
                        .map(cm -> cm.getUser().getId())
                        .sorted()
                        .collect(Collectors.toList());

                // Create response DTO
                GroupChatListResponseDTO dto = new GroupChatListResponseDTO();
                dto.setChatId(chat.getId());
                dto.setChatName(chat.getChatName());
                dto.setChatAvatarUrl(chat.getChatAvatarUrl());
                dto.setMemberCount(memberIds.size());
                dto.setMemberIds(memberIds);
                dto.setCreatedAt(chat.getCreatedAt());

                responseDTOs.add(dto);
            }

            return responseDTOs;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Get all direct chats for a user
     * 
     * @param userId User ID
     * @return List of DirectChatListResponseDTO
     */
    public List<DirectChatListResponseDTO> getDirectChatsByUserId(Integer userId) {
        try {
            // Validate user exists
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return new ArrayList<>();
            }

            // Get all direct chats (non-group chats) for this user
            List<Chat> directChats = chatRepository.findDirectChatsByUserId(userId);
            List<DirectChatListResponseDTO> responseDTOs = new ArrayList<>();

            for (Chat chat : directChats) {
                // Get all members for this chat
                List<ChatMember> members = chatMemberRepository.findByChatId(chat.getId());
                
                // Find the other user (not the current user)
                User otherUser = null;
                for (ChatMember member : members) {
                    if (!member.getUser().getId().equals(userId)) {
                        otherUser = member.getUser();
                        break;
                    }
                }

                if (otherUser != null) {
                    // Calculate online status
                    boolean isOnline = false;
                    if (otherUser.getLastSeenAt() != null) {
                        Instant now = Instant.now();
                        long diffMinutes = (now.toEpochMilli() - otherUser.getLastSeenAt().toEpochMilli()) / (1000 * 60);
                        isOnline = diffMinutes < 3; // Online if last seen within 3 minutes
                    }

                    // Create response DTO
                    DirectChatListResponseDTO dto = DirectChatListResponseDTO.builder()
                            .chatId(chat.getId())
                            .chatName(chat.getChatName()) // This will be "MarketChat X Y" or null
                            .otherUserId(otherUser.getId())
                            .otherUserName(otherUser.getFullName())
                            .otherUserAvatar(otherUser.getAvatarUrl())
                            .isOnline(isOnline)
                            .createdAt(chat.getCreatedAt())
                            .build();

                    responseDTOs.add(dto);
                }
            }

            return responseDTOs;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Leave a group chat
     * 
     * @param chatId Chat ID
     * @param userId User ID who wants to leave
     * @return true if successfully left, false otherwise
     */
    public boolean leaveGroupChat(Integer chatId, Integer userId) {
        try {
            // Validate chat exists and is a group
            Optional<Chat> chatOpt = chatRepository.findById(chatId);
            if (chatOpt.isEmpty()) {
                System.err.println("Chat not found: " + chatId);
                return false;
            }

            Chat chat = chatOpt.get();
            if (!chat.getIsGroup()) {
                System.err.println("Cannot leave a direct chat: " + chatId);
                return false;
            }

            // Validate user exists
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                System.err.println("User not found: " + userId);
                return false;
            }

            // Check if user is a member of the chat
            ChatMemberId chatMemberId = new ChatMemberId();
            chatMemberId.setChatId(chatId);
            chatMemberId.setUserId(userId);

            Optional<ChatMember> chatMemberOpt = chatMemberRepository.findById(chatMemberId);
            if (chatMemberOpt.isEmpty()) {
                System.err.println("User is not a member of this chat: userId=" + userId + ", chatId=" + chatId);
                return false;
            }

            // Remove the user from the chat
            chatMemberRepository.deleteById(chatMemberId);
            System.out.println("User " + userId + " successfully left chat " + chatId);

            // Check if the group is now empty
            List<ChatMember> remainingMembers = chatMemberRepository.findByChatId(chatId);
            if (remainingMembers.isEmpty()) {
                // If no members left, optionally delete the chat
                System.out.println("No members left in chat " + chatId + ". Consider deleting the chat.");
                // Uncomment the following line if you want to auto-delete empty chats
                // chatRepository.deleteById(chatId);
            }

            return true;

        } catch (Exception e) {
            System.err.println("Error leaving group chat: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}