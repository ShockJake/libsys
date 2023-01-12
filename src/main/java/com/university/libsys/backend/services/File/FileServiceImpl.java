package com.university.libsys.backend.services.File;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
            Files.copy(inputStream, resolvePath(name));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void delete(@NotNull String name) {
        try {
            Files.deleteIfExists(resolvePath(name));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Resource getPhoto(@NotNull String name) {
        try {
            return new UrlResource(resolvePath(name).toUri());
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
        return null; // todo: change to the always retrievable file
    }

    private Path resolvePath(String filename) {
        return Path.of(storageLocation).resolve(filename).normalize();
    }
}
