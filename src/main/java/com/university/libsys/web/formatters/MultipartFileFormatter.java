package com.university.libsys.web.formatters;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;

@Component
public class MultipartFileFormatter implements Formatter<MultipartFile> {

    private final Logger log = LoggerFactory.getLogger(MultipartFileFormatter.class);

    @Override
    public @NotNull MultipartFile parse(@NotNull String text, @NotNull Locale locale) throws ParseException {
        try {
            log.debug(String.format("Looking for a file: %s", text));

            Path path = Paths.get("/resources/static/photo/posts/" + text);
            String contentType = "text/plain";
            byte[] content = Files.readAllBytes(path);

            return new MockMultipartFile(text, text, contentType, content);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        throw new IllegalStateException();
    }

    @Override
    public @NotNull String print(MultipartFile object, @NotNull Locale locale) {
        log.debug(String.format("Getting filename from file: %s", object.getOriginalFilename()));
        return Objects.requireNonNull(object.getOriginalFilename());
    }
}
