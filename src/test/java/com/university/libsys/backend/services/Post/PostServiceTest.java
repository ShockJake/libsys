package com.university.libsys.backend.services.Post;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.repositories.LikedPostRepository;
import com.university.libsys.backend.repositories.PostsRepository;
import com.university.libsys.backend.services.File.FileService;
import com.university.libsys.backend.services.Message.MessageService;
import com.university.libsys.backend.services.User.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PostServiceTest {

    private final LikedPostRepository likedPostRepository = Mockito.mock(LikedPostRepository.class);
    private final PostsRepository postsRepository = Mockito.mock(PostsRepository.class);
    private final MessageService messageService = Mockito.mock(MessageService.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final FileService fileService = Mockito.mock(FileService.class);
    private final PostService postService = new PostServiceImpl(likedPostRepository, postsRepository,
            messageService, userService, fileService);

    @BeforeAll
    static void beforeAll() {

    }

    @Test
    void shouldGetPostByID() {

    }

    private Post getTestPost() {
        return new Post(1L, 1L, "text", "header", "./photo", 123L, false);
    }
}