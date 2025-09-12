package qdt.hcmute.vn.dqtbook_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qdt.hcmute.vn.dqtbook_backend.model.Friend;
import qdt.hcmute.vn.dqtbook_backend.model.FriendId;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {}


