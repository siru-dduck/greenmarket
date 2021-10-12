package siru.fileservice.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import siru.fileservice.configuration.properties.FileResourceProp;

/**
 * MVC 설정
 * @author siru
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

    private final FileResourceProp fileResourceProp;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/images/product/**")
                .addResourceLocations("file:" + fileResourceProp.getSavePath());
    }

}
