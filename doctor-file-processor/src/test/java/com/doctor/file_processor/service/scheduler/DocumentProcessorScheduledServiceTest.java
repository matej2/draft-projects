package com.doctor.file_processor.service.scheduler;

import com.doctor.file_processor.service.FileProcessorService;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentProcessorScheduledServiceTest {
    @Mock
    private FileProcessorService fileProcessorService;
    @Mock
    private Validator validator;
    @Mock
    private File resource;
    @Mock
    private ConstraintViolationImpl<Object> constraintViolation;

    @InjectMocks
    private DocumentProcessorScheduledService documentProcessorScheduledService;

    @Test
    void testGetDoctorFilesForInvalidDocument() throws IOException {
        Set<File> resourceList = Set.of(resource);

        when(fileProcessorService.getResources()).thenReturn(resourceList);
        when(fileProcessorService.getResourceBody(eq(resource))).thenReturn("{\"id\":1,\"department\":\"\",\"patients\":[{\"id\":5,\"firstName\":\"\",\"lastName\":\"test2\",\"diseases\":[\"test\"]}]}");
        when(validator.validate(any())).thenReturn(Set.of(constraintViolation));

        documentProcessorScheduledService.getDoctorFiles();

        verify(fileProcessorService).processInvalidDocument(any(), Set.of());
    }

    @Test
    void testGetDoctorFilesForValidDocument() throws IOException {
        Set<File> resourceList = Set.of(resource);


        when(fileProcessorService.getResources()).thenReturn(resourceList);
        when(fileProcessorService.getResourceBody(eq(resource))).thenReturn("{\"id\":1,\"department\":\"\",\"patients\":[{\"id\":5,\"firstName\":\"\",\"lastName\":\"test2\",\"diseases\":[\"test\"]}]}");
        when(validator.validate(any())).thenReturn(Set.of());

        documentProcessorScheduledService.getDoctorFiles();

        verify(fileProcessorService).processValidDocument(any());
    }
}