package com.ekomora.springjwt.controllers;

import com.ekomora.springjwt.models.Post;
import com.ekomora.springjwt.models.User;
import com.ekomora.springjwt.models.UserPostLike;
import com.ekomora.springjwt.repository.PostRepository;
import com.ekomora.springjwt.repository.UserPostLikeRepository;
import com.ekomora.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserPostLikeController {
    @Autowired
    private UserPostLikeRepository userPostLikeRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/posts/likes")
    public List<UserPostLike> getLikes() {
        return userPostLikeRepository.findAll();
    }

    @GetMapping("/posts/likes/{userId}")
    public List<UserPostLike> getLikesByUserId(@PathVariable Long userId) {
        return userPostLikeRepository.findByUserId(userId);
    }

    @GetMapping("/posts/likes/{postId}/{userId}")
    public boolean getLikeStatus(@PathVariable Long postId, @PathVariable Long userId) {
        Optional<UserPostLike> userPostLikeData = userPostLikeRepository.findByPostIdAndUserId(postId, userId);

        if (userPostLikeData.isPresent()) {
            return true;
        }
        return false;
    }

    @PostMapping("/posts/likes/{postId}/{userId}")
    public ResponseEntity<UserPostLike> likePost(@PathVariable Long postId,
                                                 @PathVariable Long userId) {
        try {
            UserPostLike _userPostLike = userPostLikeRepository
                    .save(new UserPostLike(
                            postId,
                            userId,
                            true)
                    );

            Optional<Post> postData = postRepository.findById(postId);
            Post _post = postData.get();
            _post.setLikes(_post.getLikes() + 1);
            postRepository.save(_post);

            return new ResponseEntity<>(_userPostLike, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/posts/likes/{postId}/{userId}")
    public ResponseEntity<?> dislikePost(@PathVariable Long postId,
                                         @PathVariable Long userId) {
        try {
            userPostLikeRepository.deleteByPostIdAndUserId(postId, userId);
            Optional<Post> postData = postRepository.findById(postId);
            Post _post = postData.get();
            _post.setLikes(_post.getLikes() - 1);
            postRepository.save(_post);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
