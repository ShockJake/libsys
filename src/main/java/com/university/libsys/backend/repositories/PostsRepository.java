package com.university.libsys.backend.repositories;

import com.university.libsys.backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {

    List<Post> findPostsByWriterID(Long id);
}
