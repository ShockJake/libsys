package com.university.libsys.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @GetMapping("/auth")
    public Boolean checkAuthentication() {
        return true;
    }
}
