package com.example.file.integration;

import com.example.file.config.IT;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class FileControllerIT {

    private final MockMvc mockMvc;

//    @Test
//    public void testBulkRegistration() throws Exception {
//        Resource resource = new ClassPathResource("static/test-input.xlsx");
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                resource.getFilename(),
//                MediaType.APPLICATION_OCTET_STREAM_VALUE,
//                resource.getInputStream()
//        );
//
//        mockMvc.perform(multipart("/api/v1/files/")
//                        .file(file).with(user(userDetails)))
//                .andExpectAll(
//                        status().isCreated(),
//                        content().string("true")
//                );
//    }
//
//    @Test
//    public void testBulkRegistrationFailure() throws Exception {
//        Resource resource = new ClassPathResource("static/test-corrupted-input.xlsx");
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                resource.getFilename(),
//                MediaType.APPLICATION_OCTET_STREAM_VALUE,
//                resource.getInputStream()
//        );
//
//        mockMvc.perform(multipart("/api/v1/files/")
//                        .file(file).with(user(userDetails)))
//                .andExpectAll(
//                        status().isBadRequest()
//                );
//    }
//
//    @Test
//    public void testFileDownload() throws Exception {
//        mockMvc.perform(post(RECIPIENTS.toString()).with(user(userDetails))
//                        .content(FIRST_RECIPIENT.toJson())
//                        .contentType(APPLICATION_JSON))
//                .andExpectAll(
//                        status().isCreated(),
//                        jsonPath("$.email").value(FIRST_RECIPIENT.email()),
//                        jsonPath("$.phoneNumber").value(FIRST_RECIPIENT.phoneNumber()),
//                        jsonPath("$.telegramId").value(FIRST_RECIPIENT.telegramId())
//                );
//
//        mockMvc.perform(get("/api/v1/files/").with(user(userDetails)))
//                .andExpectAll(
//                        status().isOk(),
//                        content().contentType("application/octet-stream")
//                ); // TODO: assert returned file content with test-output.xlsx
//    }
}
