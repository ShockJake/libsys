package com.university.libsys.backend.services.Post;

import com.university.libsys.backend.entities.LikedPosts;
import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.repositories.LikedPostRepository;
import com.university.libsys.backend.repositories.PostsRepository;
import com.university.libsys.backend.services.File.FileService;
import com.university.libsys.backend.services.Message.MessageService;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.backend.utils.ValidationUtil;
import com.university.libsys.web.util.MessageUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final LikedPostRepository likedPostRepository;
    private final PostsRepository postsRepository;
    private final MessageService messageService;
    private final UserService userService;
    private final FileService fileService;

    private final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    public PostServiceImpl(LikedPostRepository likedPostRepository, PostsRepository postsRepository, MessageService messageService, UserService userService, FileService fileService) {
        this.likedPostRepository = likedPostRepository;
        this.postsRepository = postsRepository;
        this.messageService = messageService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @Override
    public Post getPostById(@NotNull Long id) throws PostNotFoundException {
        return postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    @Override
    public List<Post> getPostsByUserId(@NotNull Long id) throws PostNotFoundException, UserNotFoundException {
        final User writer = userService.getUserById(id);
        List<Post> result = postsRepository.findPostsByWriterID(writer.getUserID());
        if (result.size() == 0)
            throw new PostNotFoundException(id);

        return result;
    }

    @Override
    @Transactional
    public Post saveNewPost(@NotNull Post post, MultipartFile file) throws UserNotFoundException, IOException {
        validatePost(post);
        fileService.save(Objects.requireNonNull(file.getOriginalFilename()), file.getInputStream());
        updateUserNumberOfPosts(true, post.getWriterID());
        post.setTimestamp(new Date().getTime());
        messageService.saveNewMessage(MessageUtil.getCreatedPostMessage(post.getWriterID(), post.getPostHeader()));
        return postsRepository.save(post);
    }

    @Override
    @Transactional
    public Post updatePost(@NotNull Post postToUpdate) throws PostNotFoundException {
        final Post post = postsRepository.findById(postToUpdate.getPostID())
                .orElseThrow(() -> new PostNotFoundException(postToUpdate.getPostID()));

        log.debug(String.format("Updating post \"%s\"", post.getPostHeader()));
        log.debug(String.format("Old values: %s, %s, %s",
                post.getPostHeader(), post.getPostText(), post.getPostPhotoPath()));
        log.debug(String.format("New values: %s, %s, %s",
                postToUpdate.getPostHeader(), postToUpdate.getPostText(), postToUpdate.getPostPhotoPath()));
        validatePost(postToUpdate);
        updatePost(postToUpdate, post);
        return postsRepository.save(post);
    }

    @Override
    @Transactional
    public Post deletePost(@NotNull Post postToDelete) throws PostNotFoundException, UserNotFoundException {
        final Post post = postsRepository.findById(postToDelete.getPostID())
                .orElseThrow(() -> new PostNotFoundException(postToDelete.getPostID()));
        postsRepository.deleteById(post.getPostID());
        fileService.delete(post.getPostPhotoPath());
        updateUserNumberOfPosts(false, post.getWriterID());
        log.debug(String.format("Deleted post - \"%s\"", post.getPostHeader()));
        return post;
    }

    @Override
    @Transactional
    public LikedPosts likePost(@NotNull Long userID, @NotNull Long postID) {
        return likedPostRepository.save(new LikedPosts(null, userID, postID));
    }

    @Override
    @Transactional
    public LikedPosts unlikePost(@NotNull Long userID, @NotNull Long postID) {
        final LikedPosts likedPosts = likedPostRepository.findByLikerIDAndPostID(userID, postID);
        likedPostRepository.deleteByPostIDAndLikerID(postID, userID);
        return likedPosts;
    }

    @Override
    public List<Post> getAllPosts(String login) throws UserNotFoundException {
        final User user = userService.getUserByLogin(login);
        final List<Post> posts = postsRepository.findAll();
        markLikedPosts(posts, user.getUserID());
        return posts;
    }

    @Override
    public List<Post> getAllPostsOrderedByTime(String login) throws UserNotFoundException {
        final User user = userService.getUserByLogin(login);
        final List<Post> posts = postsRepository.findAll().stream()
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .collect(Collectors.toList());
        markLikedPosts(posts, user.getUserID());
        return posts;
    }

    @Override
    public List<Post> getLikedPosts(@NotNull Long userID) {
        final List<LikedPosts> likedPosts = likedPostRepository.findAllByLikerID(userID);
        return postsRepository.findAllById(likedPosts.stream().map(LikedPosts::getPostID).collect(Collectors.toList()));
    }

    @Override
    public void validatePost(@NotNull Post post) throws ValidationException {
        final Map<String, String> postFields = new LinkedHashMap<>();
        postFields.put("postHeader", post.getPostHeader());
        postFields.put("postText", post.getPostText());
        postFields.put("postPhotoPath", post.getPostPhotoPath());

        if (post.getWriterID() == null || post.getWriterID().equals(0L))
            throw new ValidationException("Field (writerID) cannot be null or blank");

        ValidationUtil.validateFields(postFields);
    }

    private void updatePost(Post postToUpdate, Post postToSave) {
        if (!postToSave.getPostHeader().equals(postToUpdate.getPostHeader()))
            postToSave.setPostHeader(postToUpdate.getPostHeader());
        if (!postToSave.getPostText().equals(postToUpdate.getPostText()))
            postToSave.setPostText(postToUpdate.getPostText());
        if (!postToSave.getPostPhotoPath().equals(postToUpdate.getPostPhotoPath()))
            postToSave.setPostPhotoPath(postToUpdate.getPostPhotoPath());
    }

    private void updateUserNumberOfPosts(boolean add, Long userID) throws UserNotFoundException {
        final User user = userService.getUserById(userID);
        if (add) {
            user.setPostsNumber(user.getPostsNumber() + 1);
        } else {
            user.setPostsNumber(user.getPostsNumber() - 1);
        }
        userService.updateUser(userID, user);
    }

    private void markLikedPosts(List<Post> posts, Long id) {
        List<Long> ids = likedPostRepository.findAllByLikerID(id).stream()
                .map(LikedPosts::getPostID)
                .collect(Collectors.toList());
        posts.forEach(post -> {
            if (ids.contains(post.getPostID())) post.setIsLiked(true);
        });
    }
}
