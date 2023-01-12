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

    @PostMapping("/user-post-likes/like")
    public ResponseEntity<UserPostLike> likePost(@RequestBody UserPostLike userPostLike) {
        try {
            UserPostLike _userPostLike = userPostLikeRepository
                    .save(new UserPostLike(
                            userPostLike.getPostId(),
                            userPostLike.getUserId(),
                            true)
                    );
            return new ResponseEntity<>(_userPostLike, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/user-post-likes/{postId}/{userId}")
    public ResponseEntity<?> dislikePost(@PathVariable Long postId,
                                         @PathVariable Long userId) {
        return userPostLikeRepository.findByPostIdAndUserId(postId, userId)
                .map(like -> {
                    userPostLikeRepository.delete(like);
                    return ResponseEntity.ok().build();
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Like not found with post id "
                                + postId +
                                " and UserId " + userId));
    }

    @GetMapping("/user-post-likes/{postId}/{userId}")
    public boolean getLikeStatus(@PathVariable Long postId, @PathVariable Long userId) {
        Optional<UserPostLike> userPostLikeData = userPostLikeRepository.findByPostIdAndUserId(postId, userId);

        if (userPostLikeData.isPresent()) {
            return true;
        }
        return false;
    }

//    @PutMapping("/user-post-likes/{id}")
//    public ResponseEntity<UserPostLike> dislikePost(@PathVariable("id") long id,
//                                                    @RequestBody UserPostLike userPostLike) {
//        Optional<UserPostLike> userPostLikeData = userPostLikeRepository.findById(id);
//
//        if (userPostLikeData.isPresent()) {
//            UserPostLike _userPostLike = userPostLikeData.get();
//            _userPostLike.setPostId(userPostLike.getPostId());
//            _userPostLike.setUserId(userPostLike.getUserId());
//            _userPostLike.setLiked(userPostLike.isLiked());
//            return new ResponseEntity<>(userPostLikeRepository.save(_userPostLike), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
