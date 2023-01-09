package com.university.libsys.web.controllers.management;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.services.Post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post_management")
public class PostManagerController {

    private final PostService postService;

    @Autowired
    public PostManagerController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) throws PostNotFoundException {
        return postService.getPostById(id);
    }

    @PatchMapping("/{id}")
    public Post updatePost(@PathVariable Long id) throws PostNotFoundException {
        return postService.updatePost(new Post()); // todo: implement this...
    }

    @DeleteMapping("/{id}")
    public Post deletePost(@PathVariable Long id) throws PostNotFoundException {
        final Post postToDelete = postService.getPostById(id);
        return postService.deletePost(postToDelete);
    }
}
