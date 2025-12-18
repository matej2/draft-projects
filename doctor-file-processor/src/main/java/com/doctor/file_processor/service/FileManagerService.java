package com.doctor.file_processor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
public class FileManagerService {

    private final ObjectMapper objectMapper;

    FileManagerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private String getBaseName(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }

    private Path buildResultFilePath(File file) {
        String fileNameNoExtension = getBaseName(file.getName());
        String newFile = String.format("%s.%s", fileNameNoExtension, "log");
        return Path.of(file.getParent(), newFile);
    }

    public void writeToFile(Path file, List<List<Object>> result) {
        Path resultFile = buildResultFilePath(file.toFile());
        String resultJson;

        try(FileWriter myWriter = new FileWriter(resultFile.toFile())) {
            resultJson = objectMapper.writeValueAsString(result);
            myWriter.write(resultJson);
        } catch (IOException e) {
            log.warn(e.toString());
        }
    }
}
