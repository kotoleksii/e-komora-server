package com.ekomora.springjwt.repository;

import com.ekomora.springjwt.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPublished(boolean published);

    List<Post> findByPublishedOrderByIdDesc(boolean published);

    List<Post> findByTitleContaining(String title);

    List<Post> findAllByOrderByIdDesc();
}
