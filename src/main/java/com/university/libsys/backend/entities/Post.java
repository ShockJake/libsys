package com.university.libsys.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue
    private Long postID;
    private Long writerID;
    @NotBlank(message = "Post has to have a text")
    private String postText;
    @NotBlank(message = "Post has to have a header")
    private String postHeader;
    @NotBlank(message = "Post has to have a unique photo path")
    @Column(unique = true)
    private String postPhotoPath;
}
