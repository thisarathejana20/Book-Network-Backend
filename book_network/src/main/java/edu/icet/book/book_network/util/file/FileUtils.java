package edu.icet.book.book_network.util.file;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {
    private FileUtils() {}

    public static byte[] readFileFromLocation(String filePath) {
      if (StringUtils.isBlank(filePath)) return new byte[0];
      try {
          Path path = new File(filePath).toPath();
          return Files.readAllBytes(path);
      } catch (IOException e) {
          log.error("Unable to read file", e);
      }
      return new byte[0];
    }
}
