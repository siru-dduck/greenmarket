package com.hanium.product.common;

import com.hanium.product.dto.ProductImageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtils {
    @Value("${resource.file.path}")
    private String RESOURCE_FILE_PATH;

    public List<ProductImageDto> parseImageInfo(Integer articleId, List<MultipartFile> multipartFileList) throws Exception {
        if(ObjectUtils.isEmpty(multipartFileList)) {
            return null;
        }
        List<ProductImageDto> imageDtoList = new ArrayList<>();
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        ZonedDateTime current = ZonedDateTime.now();
        String fileUrl = "/resources/images/product/" + current.format(dateTimeFormat);
        String path = RESOURCE_FILE_PATH + current.format(dateTimeFormat);
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }

        String newFileName, contentType, originalFileExtension;

        for(MultipartFile multipartFile : multipartFileList){
            if(!multipartFile.isEmpty()){
                contentType = multipartFile.getContentType();
                if(ObjectUtils.isEmpty(contentType)) {
                    continue;
                } else {
                    if(contentType.contains("image/jpeg")){
                       originalFileExtension = ".jpg";
                    } else if(contentType.contains("image/png")){
                        originalFileExtension = ".png";
                    } else if(contentType.contains("image/gif")){
                        originalFileExtension = ".gif";
                    } else{
                        continue;
                    }
                }

                newFileName = Long.toString(System.nanoTime()) + originalFileExtension;
                ProductImageDto productImageDto = ProductImageDto.builder()
                        .articleId(articleId)
                        .fileUrl(fileUrl + "/" + newFileName)
                        .build();
                imageDtoList.add(productImageDto);
                File targetFile = new File(path + "/" + newFileName);
                InputStream fileStream = multipartFile.getInputStream();
                org.apache.commons.io.FileUtils.copyInputStreamToFile(fileStream, targetFile);
            }
        }
        return imageDtoList;
    }
}
