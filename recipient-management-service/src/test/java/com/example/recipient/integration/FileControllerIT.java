package com.example.recipient.integration;

import com.example.recipient.config.ITBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class FileControllerIT extends ITBase {

    private final MockMvc mockMvc;

    @Test
    public void testBulkRegistration() throws Exception {
        Resource resource = new ClassPathResource("static/test-input.xlsx");
        MockMultipartFile file = new MockMultipartFile(
                "file",
                resource.getFilename(),
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                resource.getInputStream()
        );

        mockMvc.perform(multipart("/api/v1/files/")
                        .file(file))
                .andExpectAll(
                        status().isCreated(),
                        content().string("true")
                );
    }

    @Test
    public void testBulkRegistrationFailure() throws Exception {
        Resource resource = new ClassPathResource("static/test-corrupted-input.xlsx");
        MockMultipartFile file = new MockMultipartFile(
                "file",
                resource.getFilename(),
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                resource.getInputStream()
        );

        mockMvc.perform(multipart("/api/v1/files/")
                        .file(file))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    public void testFileDownload() throws Exception {
        mockMvc.perform(get("/api/v1/files/"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType("application/octet-stream")
                ); // TODO: assert returned file content with test-output.xlsx
    }
}
