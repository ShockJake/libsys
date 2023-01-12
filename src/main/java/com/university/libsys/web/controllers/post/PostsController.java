package com.university.libsys.web.controllers.post;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.File.FileService;
import com.university.libsys.backend.services.Message.MessageService;
import com.university.libsys.backend.services.Post.PostService;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.web.util.DateUtil;
import com.university.libsys.web.util.MessageUtil;
import com.university.libsys.web.util.ModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostsController {

    private final PostService postService;
    private final UserService userService;
    private final FileService fileService;
    private final MessageService messageService;
    private final Logger log = LoggerFactory.getLogger(PostsController.class);

    @Autowired
    public PostsController(PostService postService, UserService userService, FileService fileService, MessageService messageService) {
        this.postService = postService;
        this.userService = userService;
        this.fileService = fileService;
        this.messageService = messageService;
    }

    @GetMapping("")
    public String allPostsPage(Model model) {
        final List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "pages/posts/allPostsPageTemplate";
    }

    @GetMapping("/{id}")
    public String postPage(@PathVariable Long id, Model model) throws PostNotFoundException {
        final Post post = postService.getPostById(id);
        model.addAttribute("postHeader", post.getPostHeader());
        model.addAttribute("postText", post.getPostText());
        model.addAttribute("postPhotoPath", post.getPostPhotoPath());
        model.addAttribute("postCreationDate", DateUtil.format(post.getTimestamp()));
        return "pages/posts/postTemplate";
    }

    @GetMapping("/user/{id}")
    public String userPostsPage(@PathVariable Long id, Model model) {
        try {
            final User user = userService.getUserById(id);
            model.addAttribute("userLogin", user.getLogin());
            final List<Post> posts = postService.getPostsByUserId(id);
            model.addAttribute("posts", posts);
        } catch (PostNotFoundException e) {
            model.addAttribute("noPostsAvailable", "This user has no posts");
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            model.addAttribute("errorCode", HttpStatus.NOT_FOUND.value());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/customErrorPage";
        }
        return "pages/posts/postsOfUserTemplate";
    }

    @ModelAttribute
    public Post getEmptyPost(Authentication authentication) throws UserNotFoundException {
        final Post post = new Post();
        post.setPostID(null);
        post.setPostPhotoPath(null);
        post.setWriterID(userService.getUserByLogin(authentication.getName()).getUserID());
        return post;
    }

    @RequestMapping("/create_post")
    public String createPostPage() {
        return "pages/create_post";
    }

    @PostMapping("/create_post")
    public String createPost(@Valid Post post, Errors errors, Model model, @RequestParam("postPhotoPath") MultipartFile postPhoto) {
        if (errors.hasErrors()) {
            final String errorMessages = getAllErrors(errors);
            log.error(errorMessages);
            model.addAttribute("error", errorMessages);
            return "pages/create_post";
        }
        try {
            log.info(String.format("Received file: %s", postPhoto.getOriginalFilename()));
            final Post savedPost = postService.saveNewPost(post, postPhoto);
            messageService.saveNewMessage(MessageUtil.getCreatedPostMessage(savedPost.getWriterID(), savedPost.getPostHeader()));

            final List<String> messages = List.of(String.format("Post ID: %s", savedPost.getPostID()),
                    String.format("Author: %s", userService.getUserById(savedPost.getWriterID()).getName()));
            ModelUtil.fillInfoModelWithArguments(model, String.format("Created post %s", savedPost.getPostHeader()), messages);

            return "infoPage";
        } catch (ValidationException | UserNotFoundException | IOException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "pages/create_post";
        }
    }

    @GetMapping("/photos/{photoFilename}")
    public ResponseEntity<Resource> getPostPhoto(@PathVariable String photoFilename) {
        final String dispositionHeaderValue = String.format("attachment; filename=\"%s\"", photoFilename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, dispositionHeaderValue)
                .body(fileService.getPhoto(photoFilename));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String sendToCustomErrorPage(MethodArgumentTypeMismatchException ignoredE, Model model) {
        model.addAttribute("errorCode", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("errorMessage", "You provided data with an error...");
        return "pages/customErrorPage";
    }

    private String getAllErrors(Errors errors) {
        final StringBuilder builder = new StringBuilder();
        errors.getAllErrors().forEach(objectError -> {
            builder.append(objectError.getDefaultMessage());
            builder.append('\n');
        });
        return builder.toString();
    }
}
