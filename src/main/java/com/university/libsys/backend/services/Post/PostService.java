package com.university.libsys.backend.services.Post;

import com.university.libsys.backend.entities.LikedPosts;
import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import org.jetbrains.annotations.NotNull;

import javax.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface PostService {

    Post getPostById(@NotNull Long id) throws PostNotFoundException;

    List<Post> getPostsByUserId(@NotNull Long id) throws PostNotFoundException, UserNotFoundException;

    Post saveNewPost(@NotNull Post post, InputStream inputStream) throws UserNotFoundException, IOException;

    Post updatePost(@NotNull Post post) throws PostNotFoundException;

    Post deletePost(@NotNull Post post) throws PostNotFoundException, UserNotFoundException;

    LikedPosts likePost(@NotNull Long userID, @NotNull Long postID);

    LikedPosts unlikePost(@NotNull Long userID, @NotNull Long postID);

    List<Post> getAllPosts(String login) throws UserNotFoundException;

    List<Post> getAllPostsOrderedByTime();

    List<Post> getAllPostsOrderedByTime(String login) throws UserNotFoundException;

    List<Post> getLikedPosts(@NotNull Long userID);

    void validatePost(@NotNull Post post) throws ValidationException;
}
