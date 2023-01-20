package com.university.libsys.backend.repositories;

import com.university.libsys.backend.entities.LikedPosts;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedPostRepository extends JpaRepository<LikedPosts, Long> {

    List<LikedPosts> findAllByLikerID(@NotNull Long id);

    List<LikedPosts> findAllByPostID(@NotNull Long id);

    LikedPosts findByLikerIDAndPostID(@NotNull Long likerID, @NotNull Long postID);

    void deleteByPostIDAndLikerID(@NotNull Long postID, @NotNull Long likerID);
}
