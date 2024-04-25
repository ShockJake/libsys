package com.university.libsys.backend.data;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.repositories.PostsRepository;
import com.university.libsys.web.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.university.libsys.web.util.MessageUtil.ADMINISTRATION_ID;

@Component
public class PostsDataLoader implements ApplicationRunner {
    private final PostsRepository postsRepository;
    private final Logger log = LoggerFactory.getLogger(PostsDataLoader.class);


    @Autowired
    public PostsDataLoader(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (postsRepository.count() == 0) {
            log.warn("Inserting test post as there is no posts in Database");
            final Post post = new Post(null, ADMINISTRATION_ID, "Super text, but not so long",
                    "My lovely header", "1.png", new Date().getTime(), false);
            postsRepository.save(post);
        }
    }
}
