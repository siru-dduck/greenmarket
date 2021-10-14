package siru.fileservice.configuration;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 유틸성 빈 설정
 * @author siru
 */
@Configuration
public class UtilConfig {

    @Bean
    public Tika tika() {
        return new Tika();
    }
}