package siru.fileservice.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 파일 자원 관련 설정
 * @author siru
 */
@ConfigurationProperties("file-resource")
@Component
@Getter @Setter
public class FileResourceProp {

    // 파일 저장 위치
    private String savePath;

}
