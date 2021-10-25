package siru.fileservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import siru.fileservice.configuration.security.jwt.JwtProvider;
import siru.fileservice.domain.file.ImageFile;
import siru.fileservice.domain.user.AuthUserDetail;
import siru.fileservice.dto.response.UploadResponse;
import siru.fileservice.repository.ImageFileRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageFileRepository imageFileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    private String createAccessToken() {
        String jti = UUID.randomUUID().toString();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test@email.com", null, new ArrayList<>());
        AuthUserDetail userDetail = AuthUserDetail.builder()
                .userId(1)
                .email("test@email.com")
                .nickname("test name")
                .profileImageId(null)
                .tokenId(jti)
                .build();
        authentication.setDetails(userDetail);
        return jwtProvider.createAccessToken(authentication);
    }

    @Test
    void 파일업로드_조회_성공() throws Exception {
        // given
        InputStream inputStream = this.getClass().getResourceAsStream("/test/nongdamgom.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "test/nongdamgom.jpeg", "image/jpeg", inputStream);

        // when

        /**
         * 업로드 테스트
         */
        ResultActions uploadResult = requestFileUpload(file, createAccessToken());
        UploadResponse uploadResponse = objectMapper.readValue(uploadResult.andReturn().getResponse().getContentAsString(), UploadResponse.class);

        /**
         * 조회 테스트
         */
        ResultActions getFileResult = requestGetFileResult(uploadResponse.getFileId());

        // then
        ImageFile findFile = imageFileRepository.findById(uploadResponse.getFileId())
                .orElseThrow();
        getFileResult
                .andExpect(status().isSeeOther());
        assertThat(findFile.getId()).isEqualTo(uploadResponse.getFileId());
        assertThat(findFile.getMimeType()).isEqualTo("image/jpeg");
        assertThat(findFile.getUserId()).isEqualTo(1);
    }

    @Test
    void gif_이미지_업로드_실패() throws Exception {
        // given
        InputStream inputStream =this.getClass().getResourceAsStream("/test/nongdamgom.gif");
        MockMultipartFile file = new MockMultipartFile("file", "test/nongdamgom.gif", "image/gif", inputStream);

        // when
        ResultActions uploadResult = requestFileUpload(file, createAccessToken());

        // then
        uploadResult
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void 이미지크기_380x380미만_업로드_실패() throws Exception {
        // given
        InputStream inputStream =this.getClass().getResourceAsStream("/test/small_nongdamgom.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "test/small_nongdamgom.jpeg", "image/jpeg", inputStream);
        // when
        ResultActions uploadResult = requestFileUpload(file, createAccessToken());

        // then
        uploadResult
                .andExpect(status().isForbidden());

    }

    @Test
    void 존재하지않는_이미지조회() throws Exception {
        // given
        long fileId = -1; // 존재하지 않는 파일번호

        // when
        ResultActions getFileResult = requestGetFileResult(fileId);

        // then
        getFileResult
                .andExpect(status().isNotFound());

    }

    @AfterAll
    static void cleanUp() throws IOException {
        // 이미지 파일 디렉토리 정리
        FileUtils.deleteDirectory(new File("src/test/resources/images"));
    }

    private ResultActions requestFileUpload(MockMultipartFile multipartFile, String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return mockMvc.perform(multipart("/files/image/PROFILE_IMAGE")
                        .file(multipartFile)
                        .headers(headers))
                .andDo(print());
    }

    private ResultActions requestGetFileResult(long fileId) throws Exception {
        return mockMvc.perform(get(String.format("/files/%d/image/origin", fileId)))
                .andDo(print());
    }

}