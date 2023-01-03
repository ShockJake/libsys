package com.university.libsys.backend.entities;

import com.university.libsys.backend.utils.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long userID;
    @NotBlank(message = "Login cannot be empty")
    @Column(unique = true)
    private String login;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "User role cannot be empty")
    private UserRole userRole;
    private Integer postsNumber;
}
