package com.university.libsys.web.controllers.post;

import com.university.libsys.backend.entities.LikedPosts;
import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.File.FileService;
import com.university.libsys.backend.services.Post.PostService;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.web.util.DateUtil;
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
    private final Logger log = LoggerFactory.getLogger(PostsController.class);

    @Autowired
    public PostsController(PostService postService, UserService userService, FileService fileService) {
        this.postService = postService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("")
    public String allPostsPage(Model model, Authentication authentication) throws UserNotFoundException {
        final List<Post> posts = postService.getAllPostsOrderedByTime(authentication.getName());
        model.addAttribute("posts", posts);
        return "pages/posts/allPostsPageTemplate";
    }

    @GetMapping("/{id}")
    public String postPage(@PathVariable Long id, Model model) {
        final Post post;
        try {
            post = postService.getPostById(id);
        } catch (PostNotFoundException e) {
            model.addAttribute("errorCode", HttpStatus.NOT_FOUND.value());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/customErrorPage";
        }
        model.addAttribute("postHeader", post.getPostHeader());
        model.addAttribute("postText", post.getPostText());
        model.addAttribute("postPhotoPath", post.getPostPhotoPath());
        model.addAttribute("postCreationDate", DateUtil.format(post.getTimestamp()));
        return "pages/posts/postTemplate";
    }

    @GetMapping("/liked")
    public String likedPostsPage(Model model, Authentication authentication) {
        try {
            final User user = userService.getUserByLogin(authentication.getName());
            model.addAttribute("posts", postService.getLikedPosts(user.getUserID()));
            return "pages/posts/likedPostsTemplate";
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
        }
        throw new IllegalStateException();
    }

    @GetMapping("/user/{login}")
    public String userPostsPage(@PathVariable String login, Model model) {
        try {
            final User user = userService.getUserByLogin(login);
            model.addAttribute("userLogin", user.getLogin());
            final List<Post> posts = postService.getPostsByUserId(user.getUserID());
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
        post.setIsLiked(false);
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
            model.addAttribute("error", getAllErrors(errors));
            return "pages/create_post";
        }
        try {
            final Post savedPost = postService.saveNewPost(post, postPhoto);
            ModelUtil.fillInfoModelWithArguments(model, String.format("Created post %s", savedPost.getPostHeader()),
                    getCreatedPostInfo(savedPost));

            return "infoPage";
        } catch (ValidationException | UserNotFoundException | IOException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "pages/create_post";
        }
    }

    @PostMapping("/like/{id}")
    @ResponseBody
    public LikedPosts likePost(Authentication authentication, @PathVariable Long id) {
        try {
            final User user = userService.getUserByLogin(authentication.getName());
            return postService.likePost(user.getUserID(), id);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
        }
        throw new IllegalStateException();
    }

    @PostMapping("/unlike/{id}")
    @ResponseBody
    public LikedPosts unlikePost(Authentication authentication, @PathVariable Long id) {
        try {
            final User user = userService.getUserByLogin(authentication.getName());
            return postService.unlikePost(user.getUserID(), id);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
        }
        throw new IllegalStateException();
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
        log.error(builder.toString());
        return builder.toString();
    }

    private List<String> getCreatedPostInfo(Post savedPost) throws UserNotFoundException {
        final String postIDMessage = String.format("Post ID: %s", savedPost.getPostID());
        final String authorMessage = String.format("Author: %s", userService.getUserById(savedPost.getWriterID()).getLogin());
        return List.of(postIDMessage, authorMessage);
    }
}
