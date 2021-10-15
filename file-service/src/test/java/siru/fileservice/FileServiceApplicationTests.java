package siru.fileservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void 파일업로드_성공() throws Exception {
        // given
        String path = "src/test/resources/test/nongdamgom.jpeg";
        FileInputStream inputStream = new FileInputStream(new File(path));
        MockMultipartFile file = new MockMultipartFile("file", "test/nongdamgom.jpeg", "image/jpeg", inputStream);

        // when
        mockMvc.perform(multipart("/api/file-service/image/PROFILE_IMAGE")
                        .file(file))
                .andExpect(status().isOk());

        // then
        Assertions.assertNotNull(this.getClass().getResourceAsStream(""));
    }

}
