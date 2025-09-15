package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qdt.hcmute.vn.dqtbook_backend.dto.FriendRequestDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.FriendResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.FriendActionDTO;
import qdt.hcmute.vn.dqtbook_backend.dto.UserResponseDTO;
import qdt.hcmute.vn.dqtbook_backend.model.Friend;
import qdt.hcmute.vn.dqtbook_backend.model.FriendId;
import qdt.hcmute.vn.dqtbook_backend.model.FriendStatus;
import qdt.hcmute.vn.dqtbook_backend.repository.FriendRepository;
import qdt.hcmute.vn.dqtbook_backend.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository, UserService userService) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Optional<List<FriendResponseDTO>> getFriendsByUserId(Integer userId) {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            return Optional.empty(); // User not found
        }
        
        List<Friend> friends = friendRepository.findByUserId1OrUserId2(userId);
        List<FriendResponseDTO> result = friends.stream()
                .map(friend -> convertToResponseDTO(friend, userId))
                .collect(Collectors.toList());
        return Optional.of(result);
    }

    @Transactional
    public Optional<FriendResponseDTO> sendFriendRequest(FriendRequestDTO dto) {
        Integer senderId = dto.getSenderId();
        Integer receiverId = dto.getReceiverId();

        // Basic validation
        if (senderId == null || receiverId == null) {
            return Optional.empty();
        }

        if (senderId.equals(receiverId)) {
            return Optional.empty(); // Cannot send request to yourself
        }

        // Check if users exist
        if (!userRepository.existsById(senderId) || !userRepository.existsById(receiverId)) {
            return Optional.empty();
        }

        // Check if relationship already exists (both directions)
        FriendId checkId1 = new FriendId();
        checkId1.setUserId1(senderId);
        checkId1.setUserId2(receiverId);
        
        FriendId checkId2 = new FriendId();
        checkId2.setUserId1(receiverId);
        checkId2.setUserId2(senderId);
        
        if (friendRepository.existsById(checkId1) || friendRepository.existsById(checkId2)) {
            return Optional.empty(); // Relationship already exists
        }

        // Create friend request: user_id_1 = sender, user_id_2 = receiver
        Friend friend = new Friend();
        FriendId friendId = new FriendId();
        friendId.setUserId1(senderId);  // sender
        friendId.setUserId2(receiverId); // receiver
        friend.setId(friendId);
        friend.setUser1(userRepository.findById(senderId).get());
        friend.setUser2(userRepository.findById(receiverId).get());
        friend.setStatus(FriendStatus.pending);
        friend.setCreatedAt(Instant.now());
        friend.setUpdatedAt(Instant.now());

        Friend savedFriend = friendRepository.save(friend);
        return Optional.of(convertToResponseDTO(savedFriend, senderId));
    }

    @Transactional
    public Optional<FriendResponseDTO> acceptFriendRequest(FriendActionDTO dto) {
        Integer senderId = dto.getSenderId();    // Người gửi lời mời ban đầu
        Integer receiverId = dto.getReceiverId(); // Người nhận lời mời (đang accept)

        if (senderId == null || receiverId == null) {
            return Optional.empty();
        }

        // Find the friend relationship (check both directions)
        FriendId friendId1 = new FriendId();
        friendId1.setUserId1(senderId);    // user_id_1 = sender
        friendId1.setUserId2(receiverId);  // user_id_2 = receiver
        
        FriendId friendId2 = new FriendId();
        friendId2.setUserId1(receiverId);  // user_id_1 = receiver  
        friendId2.setUserId2(senderId);    // user_id_2 = sender

        Optional<Friend> friendOpt = friendRepository.findById(friendId1);
        if (friendOpt.isEmpty()) {
            friendOpt = friendRepository.findById(friendId2);
        }
        
        if (friendOpt.isEmpty() || friendOpt.get().getStatus() != FriendStatus.pending) {
            return Optional.empty();
        }

        Friend friend = friendOpt.get();
        friend.setStatus(FriendStatus.accepted);
        friend.setUpdatedAt(Instant.now());

        Friend savedFriend = friendRepository.save(friend);
        return Optional.of(convertToResponseDTO(savedFriend, receiverId));
    }

    @Transactional
    public Optional<FriendResponseDTO> blockFriend(FriendActionDTO dto) {
        Integer senderId = dto.getSenderId();    // Người gửi lời mời ban đầu
        Integer receiverId = dto.getReceiverId(); // Người nhận lời mời (đang block)

        if (senderId == null || receiverId == null) {
            return Optional.empty();
        }

        // Find the friend relationship (check both directions)
        FriendId friendId1 = new FriendId();
        friendId1.setUserId1(senderId);    // user_id_1 = sender
        friendId1.setUserId2(receiverId);  // user_id_2 = receiver
        
        FriendId friendId2 = new FriendId();
        friendId2.setUserId1(receiverId);  // user_id_1 = receiver  
        friendId2.setUserId2(senderId);    // user_id_2 = sender

        Optional<Friend> friendOpt = friendRepository.findById(friendId1);
        if (friendOpt.isEmpty()) {
            friendOpt = friendRepository.findById(friendId2);
        }
        
        if (friendOpt.isEmpty()) {
            return Optional.empty();
        }

        Friend friend = friendOpt.get();
        friend.setStatus(FriendStatus.blocked);
        friend.setUpdatedAt(Instant.now());

        Friend savedFriend = friendRepository.save(friend);
        return Optional.of(convertToResponseDTO(savedFriend, senderId));
    }

    @Transactional
    public boolean unfriend(FriendActionDTO dto) {
        Integer senderId = dto.getSenderId();    // Người gửi lời mời ban đầu
        Integer receiverId = dto.getReceiverId(); // Người nhận lời mời (đang unfriend)

        if (senderId == null || receiverId == null) {
            return false;
        }

        // Find the friend relationship (check both directions)
        FriendId friendId1 = new FriendId();
        friendId1.setUserId1(senderId);    // user_id_1 = sender
        friendId1.setUserId2(receiverId);  // user_id_2 = receiver
        
        FriendId friendId2 = new FriendId();
        friendId2.setUserId1(receiverId);  // user_id_1 = receiver  
        friendId2.setUserId2(senderId);    // user_id_2 = sender

        if (friendRepository.existsById(friendId1)) {
            friendRepository.deleteById(friendId1);
            return true;
        } else if (friendRepository.existsById(friendId2)) {
            friendRepository.deleteById(friendId2);
            return true;
        }
        
        return false;
    }

    private FriendResponseDTO convertToResponseDTO(Friend friend, Integer currentUserId) {
        FriendResponseDTO dto = new FriendResponseDTO();
        dto.setSenderId(friend.getId().getUserId1());    // user_id_1 = sender
        dto.setReceiverId(friend.getId().getUserId2());  // user_id_2 = receiver
        dto.setStatus(friend.getStatus().name());
        dto.setCreatedAt(friend.getCreatedAt());
        dto.setUpdatedAt(friend.getUpdatedAt());
        
        // Determine which user is the "friend" (not the current user)
        Integer friendId = friend.getId().getUserId1().equals(currentUserId)
            ? friend.getId().getUserId2()
            : friend.getId().getUserId1();
            
        Optional<UserResponseDTO> friendInfo = userService.getUserById(friendId);
        friendInfo.ifPresent(dto::setFriendInfo);
        
        return dto;
    }
}