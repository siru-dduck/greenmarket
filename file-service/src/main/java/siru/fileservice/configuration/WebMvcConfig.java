package siru.fileservice.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import siru.fileservice.configuration.properties.FileResourceProp;
import siru.fileservice.controller.argumentResolver.SecurityArgumentResolver;

import java.util.List;

/**
 * MVC 설정
 * @author siru
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileResourceProp fileResourceProp;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SecurityArgumentResolver());
    }

    /**
     * static 파일설정
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 상품이미지 파일경로 및 핸들러 설정
        registry.addResourceHandler("/resources/images/product/**")
                .addResourceLocations("file:" + fileResourceProp.getProductImageSavePath());

        // 유저 프로필이미지 파일경로 및 핸들러 설정
        registry.addResourceHandler("/resources/images/profile/**")
                .addResourceLocations("file:" + fileResourceProp.getProfileImageSavePath());
    }

}
