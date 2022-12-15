package com.university.libsys.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue
    private Long postID;
    private String postText;
    private String postPhotoPath;
}
