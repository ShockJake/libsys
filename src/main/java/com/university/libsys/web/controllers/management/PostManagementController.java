package com.university.libsys.web.controllers.management;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.Post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post_management")
public class PostManagementController {

    private final PostService postService;

    @Autowired
    public PostManagementController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) throws PostNotFoundException {
        return postService.getPostById(id);
    }

    @PatchMapping(value = "/{id}")
    public Post updatePost(@PathVariable(name = "id") Long ignoredId, @RequestBody Post post) throws PostNotFoundException {
        return postService.updatePost(post);
    }

    @DeleteMapping("/{id}")
    public Post deletePost(@PathVariable Long id) throws PostNotFoundException, UserNotFoundException {
        return postService.deletePost(id);
    }
}
