package com.university.libsys.backend.entities;

import com.university.libsys.utils.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String login;
    private String password;
    private String name;
    private UserRole userRole;
}
