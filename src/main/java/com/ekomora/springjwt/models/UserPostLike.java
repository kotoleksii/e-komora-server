package com.ekomora.springjwt.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_post_likes")
public class UserPostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "post_id")
    private long postId;

    @Column(name = "user_id")
    private long userId;

    private boolean liked;

    public UserPostLike(long postId, long userId, boolean liked) {
        this.postId = postId;
        this.userId = userId;
        this.liked = liked;
    }
}
