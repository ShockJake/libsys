package com.university.libsys.backend.services.Post;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;

public interface PostService {

    Post getPostById(@NotNull Long id) throws PostNotFoundException;

    List<Post> getPostsByUserId(@NotNull Long id) throws PostNotFoundException, UserNotFoundException;

    Post saveNewPost(@NotNull Post post, MultipartFile file) throws UserNotFoundException, IOException;

    Post updatePost(@NotNull Post post) throws PostNotFoundException;

    Post deletePost(@NotNull Post post) throws PostNotFoundException, UserNotFoundException;

    List<Post> getAllPosts();

    void validatePost(@NotNull Post post) throws ValidationException;
}
