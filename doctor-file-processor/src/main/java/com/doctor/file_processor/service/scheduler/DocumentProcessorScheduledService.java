package com.doctor.file_processor.service.scheduler;

import com.doctor.file_processor.domain.dto.ApartmentDto;
import com.doctor.file_processor.service.DocumentValidator;
import com.doctor.file_processor.service.FileProcessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Component
@Slf4j
public class DocumentProcessorScheduledService extends DocumentValidator<ApartmentDto> {

    private final FileProcessorService fileProcessorService;

    public DocumentProcessorScheduledService(FileProcessorService fileProcessorService, Validator validator, ObjectMapper objectMapper) {
        super(objectMapper, validator);
        this.fileProcessorService = fileProcessorService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void getDoctorFiles() throws IOException {
        Set<File> doctorInputJson = fileProcessorService.getResources();
        String resourceBody;

        for (File resource: doctorInputJson) {
            log.info("Processing resource: {}", resource.toString());
            resourceBody = fileProcessorService.getResourceBody(resource);

            Set<ConstraintViolation<ApartmentDto>> violations = validateDocument(resourceBody, ApartmentDto.class);

            if (violations.isEmpty()) {
                fileProcessorService.processValidDocument(resource);
            } else {
                fileProcessorService.processInvalidDocument(resource, violations);
            }
        }
    }
}