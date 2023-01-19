package com.ekomora.springjwt.controllers;

import com.ekomora.springjwt.models.Post;
import com.ekomora.springjwt.models.PostLikes;
import com.ekomora.springjwt.repository.PostRepository;
import com.ekomora.springjwt.repository.PostLikesRepository;
import com.ekomora.springjwt.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class PostLikesController {
    private final PostLikesRepository userPostLikesRepository;

    final
    PostRepository postRepository;

    final
    UserRepository userRepository;

    public PostLikesController(PostLikesRepository userPostLikesRepository, PostRepository postRepository, UserRepository userRepository) {
        this.userPostLikesRepository = userPostLikesRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/posts/likes")
    public List<PostLikes> getLikes() {
        return userPostLikesRepository.findAll();
    }

    @GetMapping("/posts/likes/{userId}")
    public List<PostLikes> getLikesByUserId(@PathVariable Long userId) {
        return userPostLikesRepository.findByUserId(userId);
    }

    @GetMapping("/posts/likes/{postId}/{userId}")
    public boolean getLikeStatus(@PathVariable Long postId, @PathVariable Long userId) {
        Optional<PostLikes> userPostLikeData =
                userPostLikesRepository.findByPostIdAndUserId(postId, userId);

        if (userPostLikeData.isPresent()) {
            return true;
        }
        return false;
    }

    @PostMapping("/posts/likes/{postId}/{userId}")
    public ResponseEntity<PostLikes> likePost(@PathVariable Long postId,
                                              @PathVariable Long userId) {
        try {
            PostLikes _postLikes = userPostLikesRepository
                    .save(new PostLikes(
                            postId,
                            userId,
                            true)
                    );

            Optional<Post> postData = postRepository.findById(postId);
            if (postData.isPresent()) {
                Post _post = postData.get();
                _post.setLikes(_post.getLikes() + 1);
                postRepository.save(_post);
            }

            return new ResponseEntity<>(_postLikes, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/posts/likes/{postId}/{userId}")
    public ResponseEntity<?> dislikePost(@PathVariable Long postId,
                                         @PathVariable Long userId) {
        try {
            userPostLikesRepository.deleteByPostIdAndUserId(postId, userId);
            Optional<Post> postData = postRepository.findById(postId);
            if (postData.isPresent()) {
                Post _post = postData.get();
                _post.setLikes(_post.getLikes() - 1);
                if (_post.getLikes() < 0) {
                    _post.setLikes(0);
                }
                postRepository.save(_post);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
