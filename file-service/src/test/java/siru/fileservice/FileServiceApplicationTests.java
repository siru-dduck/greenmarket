package siru.fileservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import siru.fileservice.controller.response.UploadResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 파일업로드_조회_성공() throws Exception {
        // given
        InputStream inputStream = this.getClass().getResourceAsStream("/test/nongdamgom.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "test/nongdamgom.jpeg", "image/jpeg", inputStream);

        // when, then

        /**
         * 업로드 테스트
         */
        MvcResult result = mockMvc.perform(multipart("/api/file-service/image/PROFILE_IMAGE")
                        .file(file))
                .andExpect(status().isOk())
                .andReturn();
        UploadResponse uploadResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UploadResponse.class);

        /**
         * 조회 테스트
         */
        mockMvc.perform(get(String.format("/api/file-service/image/%d/origin", uploadResponse.getFileId())))
                .andExpect(status().isSeeOther());
    }

    @Test
    void gif_이미지_업로드_실패() throws Exception {
        // given
        InputStream inputStream =this.getClass().getResourceAsStream("/test/nongdamgom.gif");
        MockMultipartFile file = new MockMultipartFile("file", "test/nongdamgom.gif", "image/gif", inputStream);

        // when, then
        mockMvc.perform(multipart("/api/file-service/image/PROFILE_IMAGE")
                        .file(file))
                .andExpect(status().isUnsupportedMediaType());

    }

    @Test
    void 이미지크기_380x380미만_업로드_실패() throws Exception {
        // given
        InputStream inputStream =this.getClass().getResourceAsStream("/test/small_nongdamgom.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "test/small_nongdamgom.jpeg", "image/jpeg", inputStream);

        // when, then
        mockMvc.perform(multipart("/api/file-service/image/PROFILE_IMAGE")
                        .file(file))
                .andExpect(status().isBadRequest());

    }

    @Test
    void 존재하지않는_이미지조회() throws Exception {
        // given
        long fileId = -1; // 존재하지 않는 파일번호
        URI uri = URI.create(String.format("/api/file-service/image/%d/origin", fileId));

        // when, then
        mockMvc.perform(get(uri))
                .andExpect(status().isNotFound());

    }

    @AfterAll
    static void cleanUp() throws IOException {
        // 이미지 파일 디렉토리 정리
        FileUtils.deleteDirectory(new File("src/test/resources/images"));
    }
}
