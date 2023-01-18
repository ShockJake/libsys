package com.university.libsys.backend.entities;

import com.university.libsys.web.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postID;
    private Long writerID;
    @NotBlank(message = "Post has to have a text")
    @Column(length = 3500)
    private String postText;
    @NotBlank(message = "Post has to have a header")
    private String postHeader;
    @Column(unique = true)
    private String postPhotoPath;
    @NotBlank(message = "Post has to have a date of creation")
    private Long timestamp;

    public String getDisplayDate() {
        return DateUtil.format(timestamp);
    }
}
