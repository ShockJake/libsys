package com.university.libsys.backend.services.File;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public interface FileService {
    void save(@NotNull String name, @NotNull InputStream inputStream);

    Resource getPhoto(@NotNull String photoName);
}
