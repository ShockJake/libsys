package com.university.libsys.backend.services.Post;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.repositories.PostsRepository;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.utils.ValidationUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    private final PostsRepository postsRepository;
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    public PostServiceImpl(PostsRepository postsRepository, UserService userService) {
        this.postsRepository = postsRepository;
        this.userService = userService;
    }

    @Override
    public Post getPostById(@NotNull Long id) throws PostNotFoundException {
        return postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    @Override
    public List<Post> getPostsByUserId(@NotNull Long id) throws PostNotFoundException, UserNotFoundException {
        final User writer = userService.getUserById(id);
        List<Post> result = postsRepository.findPostsByWriterID(writer.getUserID());
        if (result.size() == 0) {
            throw new PostNotFoundException(id);
        }
        return result;
    }

    @Override
    public Post saveNewPost(@NotNull Post post) {
        return postsRepository.save(post);
    }

    @Override
    public Post updatePost(@NotNull Post postToUpdate) throws PostNotFoundException {
        final Post post = postsRepository.findById(postToUpdate.getPostID())
                .orElseThrow(() -> new PostNotFoundException(postToUpdate.getPostID()));

        log.debug(String.format("Updating post \"%s\"", post.getPostHeader()));
        log.debug(String.format("Old values: %s, %s, %s",
                post.getPostHeader(), post.getPostText(), post.getPostPhotoPath()));
        log.debug(String.format("New values: %s, %s, %s",
                postToUpdate.getPostHeader(), postToUpdate.getPostText(), postToUpdate.getPostPhotoPath()));
        updatePost(postToUpdate, post);
        return postsRepository.save(post);
    }

    @Override
    public Post deletePost(@NotNull Post postToDelete) throws PostNotFoundException {
        final Post post = postsRepository.findById(postToDelete.getPostID())
                .orElseThrow(() -> new PostNotFoundException(postToDelete.getPostID()));
        postsRepository.deleteById(post.getPostID());
        log.debug(String.format("Deleted post - \"%s\"", post.getPostHeader()));
        return post;
    }

    @Override
    public List<Post> getAllPosts() {
        return postsRepository.findAll();
    }

    @Override
    public void validatePost(@NotNull Post post) throws ValidationException {
        final Map<String, String> postFields = new LinkedHashMap<>();
        postFields.put("postText", post.getPostText());
        postFields.put("postHeader", post.getPostHeader());
        postFields.put("postPhotoPath", post.getPostPhotoPath());

        if (post.getWriterID() == null || post.getPostID().equals(0L)) {
            throw new ValidationException("Field (writerID) cannot be null or blank");
        }
        ValidationUtil.validateFields(postFields);
    }

    private void updatePost(Post postToUpdate, Post postToSave) {
        if (!postToSave.getPostHeader().equals(postToUpdate.getPostHeader())) {
            postToSave.setPostHeader(postToUpdate.getPostHeader());
        }
        if (!postToSave.getPostText().equals(postToUpdate.getPostText())) {
            postToSave.setPostText(postToUpdate.getPostText());
        }
        if (!postToSave.getPostPhotoPath().equals(postToUpdate.getPostPhotoPath())) {
            postToSave.setPostPhotoPath(postToUpdate.getPostPhotoPath());
        }
    }
}
