package com.university.libsys.backend.services.File;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface FileService {
    void save(@NotNull String name, @NotNull InputStream inputStream);


}
