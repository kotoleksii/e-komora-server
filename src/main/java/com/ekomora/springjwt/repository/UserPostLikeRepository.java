package com.ekomora.springjwt.repository;

import com.ekomora.springjwt.models.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {
    Optional<UserPostLike> findByPostIdAndUserId(Long postId, Long userId);
}
