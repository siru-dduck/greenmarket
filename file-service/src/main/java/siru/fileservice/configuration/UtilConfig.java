package siru.fileservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tika.Tika;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }
}