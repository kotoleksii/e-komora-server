package com.ekomora.springjwt.repository;

import com.ekomora.springjwt.models.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {
    Optional<UserPostLike> findByPostIdAndUserId(Long postId, Long userId);
    List<UserPostLike> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserPostLike l WHERE l.postId = :postId AND l.userId = :userId")
    void deleteByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
