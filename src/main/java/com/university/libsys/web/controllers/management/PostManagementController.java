package com.university.libsys.web.controllers.management;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.Message.MessageService;
import com.university.libsys.backend.services.Post.PostService;
import com.university.libsys.web.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post_management")
public class PostManagementController {

    private final PostService postService;
    private final MessageService messageService;

    @Autowired
    public PostManagementController(PostService postService, MessageService messageService) {
        this.postService = postService;
        this.messageService = messageService;
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) throws PostNotFoundException {
        return postService.getPostById(id);
    }

    @PatchMapping(value ="/{id}")
    public Post updatePost(@PathVariable(name = "id") Long ignoredId, @RequestBody Post post) throws PostNotFoundException {
        final Post updatedPost = postService.updatePost(post);
        messageService.saveNewMessage(MessageUtil.getUpdatedPostMessage(updatedPost.getWriterID(), updatedPost.getPostHeader()));
        return updatedPost;
    }

    @DeleteMapping("/{id}")
    public Post deletePost(@PathVariable Long id) throws PostNotFoundException, UserNotFoundException {
        final Post postToDelete = postService.getPostById(id);
        final Post deletedPost = postService.deletePost(postToDelete);
        messageService.saveNewMessage(MessageUtil.getDeletedPostMessage(deletedPost.getWriterID(), deletedPost.getPostHeader()));
        return deletedPost;
    }
}
