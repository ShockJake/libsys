package com.university.libsys.backend.services.File;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${STORAGE_FOLDER}")
    private String storageLocation;

    @Override
    public void save(@NotNull String name, @NotNull InputStream inputStream) {
        try {
            Path filePath = Path.of(storageLocation).resolve(name).normalize();
            Files.copy(inputStream, filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
